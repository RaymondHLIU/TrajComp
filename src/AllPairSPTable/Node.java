package AllPairSPTable;

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

	public Node(int nodeid) {
		this.node_id = nodeid;
	}

	private ArrayList<Edge> edges = new ArrayList<Edge>();

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}

}