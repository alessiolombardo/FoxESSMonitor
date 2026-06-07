package model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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

	private transient byte spanDay;

	private String dateTimeFormat = "dd/MM/yyyy HH:mm";

	private List<FoxEssVariables> variablesList = new ArrayList<FoxEssVariables>();

	public Settings() {
		setSpanHour((byte) 1);
		setBeginDate(LocalDateTime.now());
		setEndDate(false);
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

	public void setEndDate(boolean isDay) {
		if (isDay) {
			this.endDate = beginDate.plusDays(getSpanDay());
		} else {
			this.endDate = beginDate.plusHours(getSpanHour());
		}
	}

	public byte getSpanDay() {
		return spanDay;
	}

	public void setSpanDay(byte spanDay) {
		this.spanDay = spanDay;
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

	public List<FoxEssVariables> getVariablesList() {
		return variablesList;
	}

	public void setVariablesList(List<FoxEssVariables> variablesList) {
		this.variablesList = variablesList;
	}

	@Override
	public String toString() {
		return "Settings [apiKey=" + apiKey + ", inverterSerialNumber=" + inverterSerialNumber + ", exportFilename="
				+ exportFilename + ", append=" + append + ", dateTimeFormat=" + dateTimeFormat + ", variablesList="
				+ variablesList + "]";
	}

}
