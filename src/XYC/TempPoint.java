package XYC;

public class TempPoint {
	private int pid;
	private int time;
	private double offset;

	public TempPoint() {
	}

	public TempPoint(int time, float offset) {
		this.time = time;
		this.offset = offset;
	}

	@Override
	public boolean equals(Object that) {
		if (this.time == ((TempPoint) that).getTime())
			return true;
		else
			return false;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public double getOffset() {
		return offset;
	}

	public void setOffset(double offset) {
		this.offset = offset;
	}

}
