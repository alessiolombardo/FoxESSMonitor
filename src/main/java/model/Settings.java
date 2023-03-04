package model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.EnumMap;

public class Settings {

	private String endpoint = "https://www.foxesscloud.com/c/v0";

	private transient String urnLogin = "/user/login";

	private transient String urnRawData = "/device/history/raw";

	private String username = "";

	private transient String password = "";

	private String md5Password = "";

	private String deviceId = "";

	private String exportFilename = "FoxESSData.csv";

	private Boolean append = true;

	private FoxEssTimeSpan timeSpan = FoxEssTimeSpan.hour;

	private transient LocalDateTime date = LocalDateTime.now();

	private String dateTimeFormat = "dd/MM/yyyy HH:mm";

	private EnumMap<FoxEssVariables, Boolean> variables = new EnumMap<>(FoxEssVariables.class);

	public Settings() {
		for (FoxEssVariables v : Arrays.asList(FoxEssVariables.values())) {
			variables.put(v, false);
		}
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getUrnLogin() {
		return urnLogin;
	}

	public String getUrnRawData() {
		return urnRawData;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) throws NoSuchAlgorithmException {
		this.password = password;
		if (!password.isEmpty()) {
			this.md5Password = new BigInteger(1, MessageDigest.getInstance("MD5").digest(password.getBytes()))
					.toString(16);
		}
	}

	public String getMd5Password() {
		return md5Password;
	}

	public void setMd5Password(String md5Password) {
		this.md5Password = md5Password;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getExportFilename() {
		return exportFilename;
	}

	public void setExportFilename(String exportFilename) {
		this.exportFilename = exportFilename;
	}

	public Boolean getAppend() {
		return append;
	}

	public void setAppend(Boolean append) {
		this.append = append;
	}

	public FoxEssTimeSpan getTimeSpan() {
		return timeSpan;
	}

	public void setTimeSpan(FoxEssTimeSpan timeSpan) {
		this.timeSpan = timeSpan;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

	public EnumMap<FoxEssVariables, Boolean> getVariables() {
		return variables;
	}

	public void setVariables(EnumMap<FoxEssVariables, Boolean> variables) {
		this.variables = variables;
	}

	@Override
	public String toString() {
		return "Settings [endpoint=" + endpoint + ", urnLogin=" + urnLogin + ", urnRawData=" + urnRawData
				+ ", username=" + username + ", password=" + password + ", md5Password=" + md5Password + ", deviceId="
				+ deviceId + ", exportFilename=" + exportFilename + ", append=" + append + ", timeSpan=" + timeSpan
				+ ", date=" + date + ", variables=" + variables + "]";
	}

}
