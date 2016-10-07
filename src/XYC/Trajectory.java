package XYC;

import java.util.ArrayList;
import java.util.List;

public class Trajectory {

	private int tid;
	private List<TrajPoint> tList = new ArrayList<TrajPoint>();

	// private String startTime;
	// private String endTime;
	// private boolean valid=false;

	/*
	 * public boolean isValid() { return valid; } public void setValid(boolean
	 * valid) { this.valid = valid; } public String getStartTime() { return
	 * startTime; } public void setStartTime(String startTime) { this.startTime
	 * = startTime; } public String getEndTime() { return endTime; } public void
	 * setEndTime(String endTime) { this.endTime = endTime; }
	 */
	public int getId() {
		return tid;
	}

	public void setId(int tid) {
		this.tid = tid;
	}

	public List<TrajPoint> getList() {
		return tList;
	}

	public void setList(List<TrajPoint> tList) {
		this.tList = tList;
	}

	public List<Integer> getIdList() {
		List<Integer> ids = new ArrayList<Integer>();
		for (TrajPoint tp : tList) {
			ids.add(tp.getPid());
		}
		return ids;
	}
}
