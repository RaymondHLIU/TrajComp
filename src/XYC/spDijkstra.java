package XYC;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class spDijkstra {
	private HashMap<Pair<Integer, Integer>, Integer> fpMap = new HashMap<Pair<Integer, Integer>, Integer>();
	private HashMap<Pair<Integer, Integer>, List<Integer>> spMap = new HashMap<Pair<Integer, Integer>, List<Integer>>();
	private List<Vertex> vertices;
	private HashMap<Integer, Pair<String, String>> pointIdMap;

	public HashMap<Pair<Integer, Integer>, List<Integer>> getSpMap() {
		return spMap;
	}

	public HashMap<Pair<Integer, Integer>, Integer> getFpMap() {
		return fpMap;
	}

	private void computePaths(Vertex source) {
		source.minDistance = 0;
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.poll();

			// Visit each edge exiting u
			for (Edge e : u.adjacencies) {
				Vertex v = e.target;
				int weight = e.weight;
				int distanceThroughU = u.minDistance + weight;
				if (distanceThroughU < v.minDistance) {
					vertexQueue.remove(v);
					v.minDistance = distanceThroughU;
					v.previous = u;
					vertexQueue.add(v);
				}
			}
		}
	}

	private List<Integer> getAdjacencies(int pid) {
		List<Integer> adjIds = new ArrayList<Integer>();
		int pos = lookupVertex(pid, vertices);
		for (Edge e : vertices.get(pos).adjacencies)
			adjIds.add(e.target.pid);
		return adjIds;
	}

	public List<Integer> getShortestPath(int sourcePid, int targetPid) {
		List<Integer> pathIds = new ArrayList<Integer>();
		int sourcePos = lookupVertex(sourcePid, vertices);
		int targetPos = lookupVertex(targetPid, vertices);
		Vertex source = vertices.get(sourcePos);
		Vertex target = vertices.get(targetPos);
		computePaths(source);

		List<Vertex> path = getShortestPathTo(target);
		for (Vertex v : path)
			pathIds.add(v.pid);
		resetDis(vertices);
		return pathIds;
	}

	private List<Vertex> getShortestPathTo(Vertex target) {
		List<Vertex> path = new ArrayList<Vertex>();
		if (target.previous == null)
			return null;
		for (Vertex vertex = target; vertex != null; vertex = vertex.previous)
			path.add(vertex);
		Collections.reverse(path);
		return path;
	}

	private int lookupVertex(int id, List<Vertex> vertices) {
		for (int i = 0; i < vertices.size(); i++)
			if (vertices.get(i).pid == id)
				return i;
		return -1;
	}

	private List<Vertex> generateVertex(List<Pair<Integer, Integer>> map) {
		List<Vertex> vertices = new ArrayList<Vertex>();

		int maxPointId = 0;
		for (Pair<Integer, Integer> link : map) {
			int firstId = link.getFirst();
			int secondId = link.getSecond();
			if (firstId > maxPointId)
				maxPointId = firstId;
			if (secondId > maxPointId)
				maxPointId = secondId;
		}

		for (int i = 0; i <= maxPointId; i++) {
			Vertex v = new Vertex(i);
			vertices.add(v);
		}

		for (Vertex v : vertices) {
			for (Pair<Integer, Integer> rs : map) {
				int sp = rs.getFirst();
				int ep = rs.getSecond();
				if (v.pid == sp) {
					v.adjacencies.add(new Edge(vertices.get(ep)));
				}
				if (v.pid == ep) {
					v.adjacencies.add(new Edge(vertices.get(sp)));
				}
			}
		}

		return vertices;
	}

	public spDijkstra(List<Pair<Integer, Integer>> map,
			HashMap<Integer, Pair<String, String>> pointIdMap)
			throws IOException {
		this.pointIdMap = pointIdMap;
		vertices = generateVertex(map);
		System.out.println("Vertices Generation Done");

		System.out.println("Build SP HashMap");
		System.out.println("Start: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		// spMap
		for (int i = 0; i < vertices.size(); i++) {
			if (i % 10 == 0)
				System.out.println(i + "\t" + vertices.size());
			Vertex source = vertices.get(i);
			computePaths(source);

			for (Vertex target : vertices) {
				List<Vertex> path = getShortestPathTo(target);

				if (path == null)
					continue;
				Pair<Integer, Integer> stPair = new Pair<Integer, Integer>(
						source.pid, target.pid);
				spMap.put(stPair, new ArrayList<Integer>());
				for (Vertex v : path)
					spMap.get(stPair).add(v.pid);
			}

			resetDis(vertices);
		}
		System.out.println("SPMap Done");
		System.out.println("End: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));

		// fpMap
		/*
		 * System.out.println("Build FPTC HashMap");
		 * System.out.println("Start: "+new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		 * //BufferedWriter bw = new BufferedWriter(new FileWriter("dij"));
		 * BufferedReader br = new BufferedReader(new FileReader("dij")); String
		 * line; for(Pair<Integer,Integer> link: map) { int npoint =
		 * getSecNextPoint(link.getFirst(),link.getSecond(),map);
		 * fpMap.put(link, npoint); bw.write(String.valueOf(link.getFirst()) +
		 * " " + String.valueOf(link.getSecond()) + " " + String.valueOf(npoint)
		 * + "\n");
		 * 
		 * line = br.readLine(); String[] sline = line.split(" ");
		 * fpMap.put(link, Integer.valueOf(Integer.parseInt(sline[2]))); }
		 * //bw.close(); br.close(); System.out.println("Size:" + fpMap.size());
		 * System.out.println("End: "+new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
		 */

	}

	private int getSecNextPoint(int firstId, int secondId,
			List<Pair<Integer, Integer>> map) {
		int bestNextId = -1;
		double minimalCos = Double.MAX_VALUE;
		List<Integer> candidatePoint = new ArrayList<Integer>();
		for (Pair<Integer, Integer> link : map) {
			if (link.getFirst() == secondId && link.getSecond() != firstId)
				candidatePoint.add(link.getSecond());
		}
		double firstPLng = Double.parseDouble(pointIdMap.get(firstId)
				.getFirst());
		double firstPLat = Double.parseDouble(pointIdMap.get(firstId)
				.getSecond());
		double secondPLng = Double.parseDouble(pointIdMap.get(secondId)
				.getFirst());
		double secondPLat = Double.parseDouble(pointIdMap.get(secondId)
				.getSecond());
		for (int i = 0; i < candidatePoint.size(); i++) {
			double thirdPLng = Double.parseDouble(pointIdMap.get(
					candidatePoint.get(i)).getFirst());
			double thirdPLat = Double.parseDouble(pointIdMap.get(
					candidatePoint.get(i)).getSecond());
			double cosValue = Statistic.getCosValue(firstPLng, firstPLat,
					secondPLng, secondPLat, thirdPLng, thirdPLat);
			if (cosValue < minimalCos) {
				minimalCos = cosValue;
				bestNextId = candidatePoint.get(i);
			}
		}
		return bestNextId;
	}

	private void resetDis(List<Vertex> vertices) {
		for (Vertex v : vertices) {
			v.minDistance = Integer.MAX_VALUE;
			v.previous = null;
		}
	}

}
