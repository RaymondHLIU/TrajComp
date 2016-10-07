package RoadNetWork;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class RoadNetWork {

	public Map<Integer, Road> roads = new HashMap<Integer, Road>();
	public Map<Integer, Node> nodes = new HashMap<Integer, Node>();

	public RoadNetWork(String RoadNetWorkInfoPath) {

		// point_list with road_part
		try {
			BufferedReader br_edges = new BufferedReader(new FileReader(
					RoadNetWorkInfoPath + "/edges.txt"));
			BufferedReader br_geos = new BufferedReader(new FileReader(
					RoadNetWorkInfoPath + "/geos.txt"));
			String line;
			while ((line = br_edges.readLine()) != null) {
				String[] edge_nodes_split = line.split(" ");
				int start_node_id = Integer.parseInt(edge_nodes_split[1]);
				int end_node_id = Integer.parseInt(edge_nodes_split[2]);
				int road_id = Integer.parseInt(edge_nodes_split[0]);
				// add road
				Road add_road = new Road(start_node_id, end_node_id, road_id);
				roads.put(road_id, add_road);

				line = br_geos.readLine();
				String[] edge_segments_split = line.split(" ");
				int geos_size = edge_segments_split.length;
				Road_Part start = new Road_Part(new Point(
						edge_segments_split[1], edge_segments_split[2]),
						new Point(edge_segments_split[3],
								edge_segments_split[4]));
				Road_Part end = new Road_Part(new Point(
						edge_segments_split[geos_size - 2],
						edge_segments_split[geos_size - 1]), new Point(
						edge_segments_split[geos_size - 4],
						edge_segments_split[geos_size - 3]));
				// add road parts
				add_road.add_Road_parts(start, end);

				// add nodes
				if (nodes.keySet().contains(start_node_id)) {
					nodes.get(start_node_id).part_list.add(start);
					if (!nodes.get(start_node_id).road_ids.contains(road_id))
						nodes.get(start_node_id).road_ids.add(road_id);
				} else {
					Node add_node = new Node(edge_segments_split[2],
							edge_segments_split[1], start_node_id);
					nodes.put(start_node_id, add_node);
					add_node.part_list.add(start);
					add_node.road_ids.add(road_id);
				}
				if (nodes.keySet().contains(end_node_id)) {
					nodes.get(end_node_id).part_list.add(end);
					if (!nodes.get(end_node_id).road_ids.contains(road_id))
						nodes.get(end_node_id).road_ids.add(road_id);
				} else {
					Node add_node = new Node(
							edge_segments_split[geos_size - 1],
							edge_segments_split[geos_size - 2], end_node_id);
					nodes.put(end_node_id, add_node);
					add_node.part_list.add(end);
					add_node.road_ids.add(road_id);
				}
			}
			br_geos.close();
			br_edges.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		RoadNetWork ccw = new RoadNetWork("RoadNetworkInfo_Path_Beijing");
		int count6 = 0, count7 = 0, count8 = 0, count_o = 0;
		for (Integer nodeid : ccw.nodes.keySet()) {
			if (ccw.nodes.get(nodeid).part_list.size() <= 3)
				count6++;
			// else if(ccw.nodes.get(nodeid).part_list.size()==6)
			// count7++;
			// else if(ccw.nodes.get(nodeid).part_list.size()<=8)
			// count8++;
			// else
			// count_o++;
		}

		System.out.println(count6);
		// System.out.println(count7);
		// System.out.println(count8);
		// System.out.println(count_o);
	}
}
