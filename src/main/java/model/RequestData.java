package model;

import java.util.ArrayList;
import java.util.List;

public class RequestData {

	public class DateTime {
		private String year;
		private String month;
		private String day;
		private String hour;

		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}

		public String getMonth() {
			return month;
		}

		public void setMonth(String month) {
			this.month = month;
		}

		public String getDay() {
			return day;
		}

		public void setDay(String day) {
			this.day = day;
		}

		public String getHour() {
			return hour;
		}

		public void setHour(String hour) {
			this.hour = hour;
		}

		@Override
		public String toString() {
			return "DateTime [year=" + year + ", month=" + month + ", day=" + day + ", hour=" + hour + "]";
		}

	}

	private String deviceId;

	private List<FoxEssVariables> variables = new ArrayList<FoxEssVariables>();

	private String timespan;

	private DateTime beginDate = new DateTime();

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public List<FoxEssVariables> getVariables() {
		return variables;
	}

	public void setVariables(List<FoxEssVariables> variables) {
		this.variables = variables;
	}

	public String getTimespan() {
		return timespan;
	}

	public void setTimespan(String timespan) {
		this.timespan = timespan;
	}

	public DateTime getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(DateTime beginDate) {
		this.beginDate = beginDate;
	}

	@Override
	public String toString() {
		return "RequestData [deviceId=" + deviceId + ", variables=" + variables + ", timespan=" + timespan
				+ ", beginDate=" + beginDate + "]";
	}

}
