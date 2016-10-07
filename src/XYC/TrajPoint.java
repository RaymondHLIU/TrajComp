package XYC;

public class TrajPoint {

	// private int rid;
	// private String lng;
	// private String lat;
	private int pid;
	private String time;
	// private boolean isIgnore=false;
	private int numFollowIgnore = 0;

	public TrajPoint() {
		;
	}

	public TrajPoint(Pair<String, String> lngLat, int id) {
		// this.lng=lngLat.getFirst();
		// this.lat=lngLat.getSecond();
		this.pid = id;
	}

	public int getNumFollowIgnore() {
		return numFollowIgnore;
	}

	public void setNumFollowIgnore(int numFollowIgnore) {
		this.numFollowIgnore = numFollowIgnore;
	}

	@Override
	public boolean equals(Object that) {
		if (this.pid == ((TrajPoint) that).getPid())
			return true;
		else
			return false;
	}

	/*
	 * public int getRid() { return rid; } public void setRid(int rid) {
	 * this.rid = rid; }
	 */
	/*
	 * public String getLng() { return lng; } public void setLng(String lng) {
	 * this.lng = lng; } public String getLat() { return lat; } public void
	 * setLat(String lat) { this.lat = lat; }
	 */
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	/*
	 * public boolean isIgnore() { return isIgnore; } public void
	 * setIgnore(boolean isIgnore) { this.isIgnore = isIgnore; }
	 */

}
