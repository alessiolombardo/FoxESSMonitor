package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormatSymbols;
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
import model.ResponseToken;
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

	public void extractData(Boolean export) {
		try {

			sslTrustInit();

			String token = login();
			if (token.isEmpty()) {
				return;
			}

			ResponseData responseData = requestRawData(token);

			conn.disconnect();

			Map<OffsetDateTime, ArrayList<String>> dataMap = elaborateResponse(responseData);
			if (dataMap == null) {
				return;
			}

			gui.updateMonitowRecord(dataMap);

			if (export) {
				exportData(dataMap);
			}

		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			gui.setStatus(e, "SSL Error", true);
		} catch (IOException e) {
			gui.setStatus(e, "Request data error", true);
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

	private String login() throws IOException {

		conn = (HttpsURLConnection) new URL(settings.getEndpoint() + settings.getUrnLogin()).openConnection();
		conn.setDoOutput(true);

		String input = "user=" + settings.getUsername() + "&password=" + settings.getMd5Password();
		conn.getOutputStream().write(input.getBytes());

		String output = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
		ResponseToken responseToken = new Gson().fromJson(output, ResponseToken.class);

		if (responseToken.getErrno() == 0) {
			return responseToken.getResult().getToken();
		} else if (responseToken.getErrno() == 41807) {
			gui.setStatus(null, "Error " + responseToken.getErrno() + ": Bad username or password", true);
		} else if (responseToken.getErrno() == 40401) {
			gui.setStatus(null, "Error " + responseToken.getErrno() + ": Too many requests", true);
		} else {
			gui.setStatus(null, "Error " + responseToken.getErrno(), true);
		}
		return "";

	}

	private ResponseData requestRawData(String token) throws IOException {

		if (token.isEmpty()) {
			return new ResponseData();
		}

		conn = (HttpsURLConnection) new URL(settings.getEndpoint() + settings.getUrnRawData()).openConnection();
		conn.setDoOutput(true);
		conn.setRequestProperty("token", token);

		RequestData d = new RequestData();
		d.setDeviceId(settings.getDeviceId());
		for (Map.Entry<FoxEssVariables, Boolean> entry : settings.getVariables().entrySet()) {
			if (entry.getValue().equals(true)) {
				d.getVariables().add(entry.getKey());
			}
		}
		d.setTimespan(settings.getTimeSpan().name());
		d.getBeginDate().setYear("" + settings.getDate().getYear());
		d.getBeginDate().setMonth("" + settings.getDate().getMonthValue());
		d.getBeginDate().setDay("" + settings.getDate().getDayOfMonth());
		d.getBeginDate().setHour("" + settings.getDate().getHour());

		conn.getOutputStream().write(new Gson().toJson(d).getBytes());

		String output = new BufferedReader(new InputStreamReader((conn.getInputStream()))).readLine();
		ResponseData responseData = new Gson().fromJson(output, ResponseData.class);

		return responseData;

	}

	public Map<OffsetDateTime, ArrayList<String>> elaborateResponse(ResponseData responseData) {

		if (responseData.getErrno() == 40261) {
			gui.setStatus(null, "Error " + responseData.getErrno() + ": Invalid Device ID", true);
		} else if (responseData.getErrno() == 41929) {
			gui.setStatus(null, "Error " + responseData.getErrno() + ": Device ID not exist", true);
		} else if (responseData.getErrno() > 0) {
			gui.setStatus(null, "Error " + responseData.getErrno(), true);
		}

		if (responseData.getErrno() > 0) {
			return null;
		}

		Map<OffsetDateTime, ArrayList<String>> dataMap = new TreeMap<OffsetDateTime, ArrayList<String>>();
		for (ResponseData.Result result : responseData.getResult()) {
			for (ResponseData.Result.Data data : result.getData()) {
				OffsetDateTime offsetDatetime = OffsetDateTime.parse(data.getTime(),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss zZ"));
				dataMap.putIfAbsent(offsetDatetime, new ArrayList<String>());
				dataMap.get(offsetDatetime).add(data.getValue());
			}
		}

		gui.setStatus(null, "Data extraction completed successfully", false);

		return dataMap;

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
