package RoadNetWork;

public class Road {

	public Road_Part road_start_part;
	public Road_Part road_end_part;

	public void add_Road_parts(Road_Part start, Road_Part end) {
		this.road_start_part = start;
		this.road_end_part = end;
	}

	public int road_id = -1;

	private int fromNodeIndex;

	public int getFromNodeIndex() {
		return fromNodeIndex;
	}

	private int toNodeIndex;

	public int getToNodeIndex() {
		return toNodeIndex;
	}

	public Road(int fromNodeIndex, int toNodeIndex, int edgeid) {
		this.fromNodeIndex = fromNodeIndex;
		this.toNodeIndex = toNodeIndex;
		this.road_id = edgeid;
	}

	/**
	 * Determines the neighbouring node of a supplied node, based on the 2 nodes
	 * connected by this edge.
	 * 
	 * @param nodeIndex
	 *            The index of one of the nodes that this edge joins.
	 * @return The index of the neighbouring node.
	 *
	 */
	public int getNeighbourIndex(int nodeIndex) {
		if (this.fromNodeIndex == nodeIndex) {
			return this.toNodeIndex;
		} else {
			return this.fromNodeIndex;
		}
	}
	/*
	 * @Override public int compareTo(Object arg0) { // TODO Auto-generated
	 * method stub Road r1 = this; // ǿ��ת�� Road r2 = (Road) arg0;
	 * if(r1.road_start.lng-r2.road_start.lng > 0) return 1; else if
	 * (r1.road_start.lng-r2.road_start.lng < 0) return -1; else if
	 * (r1.road_start.lat-r2.road_start.lat > 0) return 1; else if
	 * (r1.road_start.lat-r2.road_start.lat < 0) return -1;
	 * 
	 * if(r1.road_end.lng-r2.road_end.lng > 0) return 1; else if
	 * (r1.road_end.lng-r2.road_end.lng < 0) return -1; else if
	 * (r1.road_end.lat-r2.road_end.lat > 0) return 1; else if
	 * (r1.road_end.lat-r2.road_end.lat < 0) return -1;
	 * 
	 * return 0; }
	 * 
	 * public boolean equals(Road st) { if
	 * (this.road_start.equals(st.road_start) &&
	 * this.road_end.equals(st.road_end)) return true; else return false; }
	 */
}
