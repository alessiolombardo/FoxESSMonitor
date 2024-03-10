package model;

import java.util.ArrayList;
import java.util.List;

public class RequestData {

	private String sn;

	private List<FoxEssVariables> variables = new ArrayList<FoxEssVariables>();

	private long begin;

	private long end;

	public String sn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public List<FoxEssVariables> getVariables() {
		return variables;
	}

	public void setVariables(List<FoxEssVariables> variables) {
		this.variables = variables;
	}

	public long getBegin() {
		return begin;
	}

	public void setBegin(long begin) {
		this.begin = begin;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "RequestData [sn=" + sn + ", variables=" + variables + ", begin=" + begin + ", end=" + end + "]";
	}

}
