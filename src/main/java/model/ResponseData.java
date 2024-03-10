package model;

import java.util.ArrayList;
import java.util.List;

public class ResponseData {

	public class Result {

		public class Datas {

			public class Data {

				private String time;

				private String value;

				public String getTime() {
					return time;
				}

				public void setTime(String time) {
					this.time = time;
				}

				public String getValue() {
					return value;
				}

				public void setValue(String value) {
					this.value = value;
				}

				@Override
				public String toString() {
					return "Data [time=" + time + ", value=" + value + "]";
				}

			}

			private FoxEssVariables variable;

			private String unit;

			private String name;

			private List<Data> data = new ArrayList<Data>();

			public FoxEssVariables getVariable() {
				return variable;
			}

			public void setVariable(FoxEssVariables variable) {
				this.variable = variable;
			}

			public String getUnit() {
				return unit;
			}

			public void setUnit(String unit) {
				this.unit = unit;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public List<Data> getData() {
				return data;
			}

			public void setData(List<Data> data) {
				this.data = data;
			}

			@Override
			public String toString() {
				return "Datas [variable=" + variable + ", unit=" + unit + ", name=" + name + ", data=" + data + "]";
			}

		}

		private List<Datas> datas = new ArrayList<Datas>();

		private String deviceSN;

		public List<Datas> getDatas() {
			return datas;
		}

		public void setDatas(List<Datas> datas) {
			this.datas = datas;
		}

		public String getDeviceSN() {
			return deviceSN;
		}

		public void setDeviceSN(String deviceSN) {
			this.deviceSN = deviceSN;
		}

		@Override
		public String toString() {
			return "Result [datas=" + datas + ", deviceSN=" + deviceSN + "]";
		}

	}

	private int errno;

	private String msg;

	private List<Result> result = new ArrayList<Result>();

	public int getErrno() {
		return errno;
	}

	public void setErrno(int errno) {
		this.errno = errno;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<Result> getResult() {
		return result;
	}

	public void setResult(List<Result> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ResponseData [errno=" + errno + ", msg=" + msg + ", result=" + result + "]";
	}

}
