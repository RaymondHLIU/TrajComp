package RoadNetWork;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a node in a graph.
 *
 * @author Stewart Wright
 * 
 */
public class Node implements Serializable {

	public int node_id = -1;
	public double lng;
	public double lat;
	public ArrayList<Road_Part> part_list = new ArrayList<Road_Part>();
	public ArrayList<Integer> road_ids = new ArrayList<Integer>();

	public Node(String lng, String lat, int node_id) {
		this.lng = Double.parseDouble(lng);
		this.lat = Double.parseDouble(lat);
		this.node_id = node_id;
	}

	public Node(Node point) {
		this.lng = point.lng;
		this.lat = point.lat;
		this.node_id = point.node_id;
		this.part_list = new ArrayList<Road_Part>(point.part_list);
	}

	@Override
	public int hashCode() {
		return new Double(this.lng * this.lat).hashCode();
	}

	public boolean equals(Node st) {
		if (this.lng == st.lng && this.lat == st.lat)
			return true;
		else
			return false;
	}

}