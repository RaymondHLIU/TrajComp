package XYC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DataLoader {
	private List<Long> tid = new ArrayList<Long>();
	private List<Trajectory> trajectories = new ArrayList<Trajectory>();
	// private HashMap<Long,Trajectory> trajectories=new
	// HashMap<Long,Trajectory>();
	private List<Pair<Integer, Integer>> map = new ArrayList<Pair<Integer, Integer>>();
	private Dijkstra dij;
	private HashMap<Integer, Pair<String, String>> pointIdMap = new HashMap<Integer, Pair<String, String>>();

	public List<Pair<Integer, Integer>> getMap() {
		return map;
	}

	public List<Trajectory> getTraj() {
		return trajectories;
	}

	public List<Long> getTid() {
		return tid;
	}

	public Dijkstra getDij() {
		return dij;
	}

	public HashMap<Integer, Pair<String, String>> getPointIdMap() {
		return pointIdMap;
	}

	private void LoadTraj(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = br.readLine();
		while (line != null) {
			int count;
			long tid;
			Trajectory traj;
			if (line.startsWith("TID")) {
				traj = new Trajectory();
				String[] sline = line.split(",");
				tid = Long
						.parseLong(sline[0].substring(sline[0].indexOf('-') + 1));
				count = Integer.parseInt(sline[1]);
				for (int i = 0; i < count; i++) {
					String s = br.readLine();
					String[] ss = s.split(",");
					TrajPoint tp = new TrajPoint();
					tp.setPid(Integer.parseInt(ss[0]));
					tp.setTime(ss[1]);
					traj.getList().add(tp);
				}
				trajectories.add(traj);
				this.tid.add(tid);
				// trajectories.put(tid, traj);
			}
			line = br.readLine();
		}
		br.close();
	}

	private void LoadMap(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = br.readLine();
		int pointsCount = Integer.parseInt(line);
		for (int i = 0; i < pointsCount; i++) {
			line = br.readLine();
			String[] sline = line.split(",");
			pointIdMap.put(Integer.parseInt(sline[0]),
					new Pair<String, String>(sline[2], sline[1]));
		}
		line = br.readLine();
		int linkCount = Integer.parseInt(line);
		for (int i = 0; i < linkCount; i++) {
			line = br.readLine();
			String[] sline = line.split(",");
			Pair<Integer, Integer> curLink = new Pair<Integer, Integer>(
					Integer.parseInt(sline[0]), Integer.parseInt(sline[1]));
			map.add(curLink);
		}
		br.close();
	}

	public DataLoader(String trajFile, String mapFile, boolean isNeedDij)
			throws IOException {
		System.out.println("Trajecotry Data Loading");
		System.out.println("Start: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		LoadTraj(trajFile);
		System.out.println("End: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));

		System.out.println("Map Loading");
		System.out.println("Start: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		LoadMap(mapFile);
		System.out.println("End: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		if (isNeedDij)
			this.dij = new Dijkstra(map, pointIdMap);
	}
}
