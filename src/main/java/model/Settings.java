package model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.EnumMap;

public class Settings {

	private transient String endpoint = "https://www.foxesscloud.com";

	private transient String urnReportQuery = "/op/v0/device/history/query";

	private String apiKey = "";

	private String inverterSerialNumber = "";

	private String exportFilename = "FoxESSData.csv";

	private Boolean append = true;

	private transient LocalDateTime beginDate;

	private transient LocalDateTime endDate;

	private transient byte spanHour;

	private String dateTimeFormat = "dd/MM/yyyy HH:mm";

	private EnumMap<FoxEssVariables, Boolean> variables = new EnumMap<>(FoxEssVariables.class);

	public Settings() {
		setSpanHour((byte) 1);
		setBeginDate(LocalDateTime.now());
		setEndDate();
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

	public String getUrnReportQuery() {
		return urnReportQuery;
	}

	public void setUrnReportQuery(String urnReportQuery) {
		this.urnReportQuery = urnReportQuery;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getInverterSerialNumber() {
		return inverterSerialNumber;
	}

	public void setInverterSerialNumber(String inverterSerialNumber) {
		this.inverterSerialNumber = inverterSerialNumber;
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

	public LocalDateTime getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(LocalDateTime beginDate) {
		this.beginDate = beginDate.truncatedTo(ChronoUnit.HOURS);
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate() {
		this.endDate = beginDate.plusHours(getSpanHour());
	}

	public byte getSpanHour() {
		return spanHour;
	}

	public void setSpanHour(byte spanHour) {
		this.spanHour = spanHour;
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
		return "Settings [endpoint=" + endpoint + ", urnReportQuery=" + urnReportQuery + ", apiKey=" + apiKey
				+ ", inverterSerialNumber=" + inverterSerialNumber + ", exportFilename=" + exportFilename + ", append="
				+ append + ", beginDate=" + beginDate + ", endDate=" + endDate + ", dateTimeFormat=" + dateTimeFormat
				+ ", variables=" + variables + "]";
	}

}
