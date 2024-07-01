package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.security.MessageDigest;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import model.FoxEssVariables;
import model.RequestData;
import model.ResponseData;
import model.Settings;
import view.Gui;

public class Controller extends Application {

	private HttpsURLConnection conn;

	private Settings settings = new Settings();

	private Gui gui;

	private Scene scene;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("FoxESS Monitor");
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image(Controller.class.getResourceAsStream("/icon.png")));

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Controller.class.getResource("/Gui.fxml"));
			BorderPane primaryPanel = (BorderPane) loader.load();

			gui = loader.getController(); // NOTE: "Controller" is the JavaFX GUI Controller, not this class)
			gui.setController(this);
			gui.controlsInit();

			scene = new Scene(primaryPanel);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Settings loadSettings() {

		try {
			FileReader fileReader = new FileReader("settings.json");
			settings = new Gson().fromJson(fileReader, Settings.class);
			fileReader.close();
			gui.setStatus(null, "Load settings", false);
		} catch (JsonSyntaxException | JsonIOException e) {
			gui.setStatus(e, "Invalid settings file", true);
		} catch (FileNotFoundException e) {
			gui.setStatus(null, "Load default settings", false);
		} catch (IOException e) {
			gui.setStatus(e, "Load settings: I/O Error", true);
		}

		return settings;

	}

	public void saveSettings() {

		try {
			FileWriter fileWriter = new FileWriter("settings.json");
			new GsonBuilder().setPrettyPrinting().create().toJson(settings, fileWriter);
			fileWriter.flush();
			fileWriter.close();
			gui.setStatus(null, "Save settings", false);
		} catch (IOException e) {
			gui.setStatus(e, "Save settings: I/O Error", true);
		}

	}

	public void extractData(Boolean isMultipleDayExtraction, Boolean export) {

		try {
			sslTrustInit();
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			gui.setStatus(e, "SSL Error", true);
		}

		Map<OffsetDateTime, ArrayList<String>> dataMap = new TreeMap<OffsetDateTime, ArrayList<String>>();

		if (!isMultipleDayExtraction) {
			try {
				ResponseData responseData = requestRawData(settings.getBeginDate(), settings.getEndDate());
				elaborateResponse(dataMap, responseData, true);
			} catch (IOException | NoSuchAlgorithmException e) {
				gui.setStatus(e, "Request data error (from " + settings.getBeginDate() + " to " + settings.getEndDate(),
						true);
			} finally {
				conn.disconnect();
			}
		} else {

			for (LocalDateTime beginDate = settings.getBeginDate(); beginDate
					.compareTo(settings.getEndDate()) != 0; beginDate = beginDate.plusDays(1)) {

				try {
					ResponseData responseData = requestRawData(beginDate, beginDate.plusDays(1));
					int error = elaborateResponse(dataMap, responseData, false);
					if (error > 0) { // Try again if fail
						responseData = requestRawData(beginDate, beginDate.plusDays(1));
						elaborateResponse(dataMap, responseData, true);
					}
				} catch (IOException | NoSuchAlgorithmException e) {
					gui.setStatus(e, "Request data error (from " + beginDate + " to " + beginDate.plusDays(1), true);
				} finally {
					conn.disconnect();
				}
			}

		}

		gui.updateMonitowRecord(dataMap);

		if (export) {
			exportData(dataMap);
		}

	}

	private void sslTrustInit() throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager trm = new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		};
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[] { trm }, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	private ResponseData requestRawData(LocalDateTime beginDate, LocalDateTime endDate)
			throws IOException, NoSuchAlgorithmException {

		conn = (HttpsURLConnection) new URL(settings.getEndpoint() + settings.getUrnReportQuery()).openConnection();
		conn.setDoOutput(true);

		String timestamp = "" + Instant.now().toEpochMilli();
		String signatureTemp = settings.getUrnReportQuery() + "\\r\\n" + settings.getApiKey() + "\\r\\n" + timestamp;
		String signature = new BigInteger(1, MessageDigest.getInstance("MD5").digest(signatureTemp.getBytes()))
				.toString(16);

		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("token", settings.getApiKey());
		conn.setRequestProperty("signature", signature);
		conn.setRequestProperty("timestamp", timestamp);
		conn.setRequestProperty("lang", "en");

		RequestData d = new RequestData();
		d.setSn(settings.getInverterSerialNumber());
		for (Map.Entry<FoxEssVariables, Boolean> entry : settings.getVariables().entrySet()) {
			if (entry.getValue().equals(true)) {
				d.getVariables().add(entry.getKey());
			}
		}

		d.setBegin(beginDate.toEpochSecond(OffsetDateTime.now().getOffset()) * 1000);
		d.setEnd(endDate.toEpochSecond(OffsetDateTime.now().getOffset()) * 1000);

		conn.getOutputStream().write(new Gson().toJson(d).getBytes());

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
		String output = br.readLine();
		ResponseData responseData = new Gson().fromJson(output, ResponseData.class);

		return responseData;

	}

	public int elaborateResponse(Map<OffsetDateTime, ArrayList<String>> dataMap, ResponseData responseData,
			Boolean showAlert) {

		if (responseData.getErrno() == 40256) {
			gui.setStatus(null, "Error " + responseData.getErrno()
					+ ": The request header parameters are missing. Please check whether the request headers are consistent with the document requirements.",
					showAlert);
		} else if (responseData.getErrno() == 40257) {
			gui.setStatus(null, "Error " + responseData.getErrno()
					+ ": The request body parameters are invalid. Please check whether the request body is consistent with the document requirements.",
					showAlert);
		} else if (responseData.getErrno() == 40400) {
			gui.setStatus(null,
					"Error " + responseData.getErrno()
							+ ": The number of requests is too frequent. Please reduce the frequency of access.",
					showAlert);
		} else if (responseData.getErrno() > 0) {
			gui.setStatus(null, "Error " + responseData.getErrno(), showAlert);
		}

		if (responseData.getErrno() > 0) {
			return responseData.getErrno();
		}

		for (ResponseData.Result.Datas datas : responseData.getResult().get(0).getDatas()) {
			for (ResponseData.Result.Datas.Data data : datas.getData()) {
				OffsetDateTime offsetDatetime = OffsetDateTime.parse(data.getTime(),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss zZ"));
				dataMap.putIfAbsent(offsetDatetime, new ArrayList<String>());
				dataMap.get(offsetDatetime).add(data.getValue());
			}
		}

		gui.setStatus(null, "Data extraction completed successfully [from " + settings.getBeginDate() + " to "
				+ settings.getEndDate() + "]", false);

		return 0;

	}

	private void exportData(Map<OffsetDateTime, ArrayList<String>> dataMap) {

		try {

			String lastFileHeader = "";
			if (new File(settings.getExportFilename()).exists()) {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(settings.getExportFilename()));
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					if (line.startsWith("Timestamp")) {
						lastFileHeader = line;
					}
				}
				bufferedReader.close();
			}

			FileWriter fileWriter = new FileWriter(settings.getExportFilename(), settings.getAppend());
			StringBuilder header = new StringBuilder("Timestamp");
			for (Map.Entry<FoxEssVariables, Boolean> entry : settings.getVariables().entrySet()) {
				if (entry.getValue().equals(true)) {
					header.append(";" + entry.getKey());
				}
			}
			if (new File(settings.getExportFilename()).length() == 0 || !lastFileHeader.equals(header.toString())) {
				fileWriter.write(header.toString() + "\n");
				fileWriter.flush();
			}
			for (Map.Entry<OffsetDateTime, ArrayList<String>> entry : dataMap.entrySet()) {
				fileWriter.write(entry.getKey().format(DateTimeFormatter.ofPattern(settings.getDateTimeFormat())));
				for (String variable : entry.getValue()) {
					fileWriter.write(
							";" + variable.replace(".", "" + DecimalFormatSymbols.getInstance().getDecimalSeparator()));
				}
				fileWriter.write("\n");
				fileWriter.flush();
			}
			fileWriter.close();

		} catch (IOException e) {
			gui.setStatus(e, "Error to save CSV file", true);
		}

	}

}
