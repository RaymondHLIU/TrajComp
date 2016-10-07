package XYC;

import java.util.ArrayList;
import java.util.List;

public class TempLine {
	private int tid;
	private List<TempPoint> tList = new ArrayList<TempPoint>();

	public int getId() {
		return tid;
	}

	public void setId(int tid) {
		this.tid = tid;
	}

	public List<TempPoint> getList() {
		return tList;
	}

	public void setList(List<TempPoint> tList) {
		this.tList = tList;
	}

	public List<Integer> getTimeList() {
		List<Integer> times = new ArrayList<Integer>();
		for (TempPoint tp : tList) {
			times.add(tp.getTime());
		}
		return times;
	}
}
