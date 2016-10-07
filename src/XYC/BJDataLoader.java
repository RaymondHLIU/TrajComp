package XYC;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BJDataLoader {
	private List<Long> tid = new ArrayList<Long>();
	private List<Trajectory> trajectories = new ArrayList<Trajectory>();
	private List<TempLine> times = new ArrayList<TempLine>();
	// private HashMap<Long,Trajectory> trajectories=new
	// HashMap<Long,Trajectory>();
	private List<Pair<Integer, Integer>> map = new ArrayList<Pair<Integer, Integer>>();
	private Dijkstra dij;
	private spDijkstra spdij;
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

	public spDijkstra getspDij() {
		return spdij;
	}

	public HashMap<Integer, Pair<String, String>> getPointIdMap() {
		return pointIdMap;
	}

	private void LoadTraj(String dirName) throws IOException {
		File folder = new File(dirName + "/20130603");
		File[] listOfFiles = folder.listFiles();
		BufferedReader br;

		long pcount = 0;
		for (File file : listOfFiles) {
			String[] path = file.toString().split("/");
			// String raw_id = path[path.length - 1];
			String raw_id = file.toString();

			br = new BufferedReader(new FileReader(raw_id));
			Trajectory traj = new Trajectory();
			String car_plate = path[path.length - 1];
			long tid = Long.parseLong(path[path.length - 2]
					+ String.valueOf((int) car_plate.charAt(1))
					+ String.valueOf((int) car_plate.charAt(2))
					+ car_plate.substring(3, 7));
			// System.out.println(tid);

			String line = br.readLine();
			while (line != null) {
				TrajPoint tp = new TrajPoint();
				tp.setPid(Integer.parseInt(line));
				traj.getList().add(tp);

				String[] sline = line.split(" ");
				/*
				 * if(car_plate.substring(3,7).equals("1521")){
				 * System.out.println(line); System.out.println(sline.length);
				 * System
				 * .out.println(String.valueOf(Integer.parseInt(sline[1]))); }
				 */

				line = br.readLine();
			}
			// System.out.println("Current trajectory size: " +
			// traj.getList().size());
			trajectories.add(traj);
			this.tid.add(tid);
			br.close();
		}
		System.out.println("All points:" + String.valueOf(pcount));

	}

	private void LoadCleanTraj(String dirName) throws IOException {
		File folder = new File(dirName);
		File[] listOfFiles = folder.listFiles();
		BufferedReader br;

		long pcount = 0;
		for (File file : listOfFiles) {
			if (pcount++ % 1000 == 0)
				System.out.println("Loading " + pcount + "th trajectory...");

			String[] path = file.toString().split("/");
			// String raw_id = path[path.length - 1];
			String raw_id = file.toString();

			br = new BufferedReader(new FileReader(raw_id));
			Trajectory traj = new Trajectory();
			String car_plate = path[path.length - 1];
			int oldTime = 10000;
			// System.out.println(car_plate);
			/*
			 * long tid = Long.parseLong(car_plate.substring(0, 8) +
			 * String.valueOf((int) car_plate.charAt(9)) + String.valueOf((int)
			 * car_plate.charAt(10)) + car_plate.substring(11, 14));
			 */
			long tid = Long.parseLong(car_plate.substring(0, 8)
					+ String.valueOf((int) car_plate.charAt(9))
					+ String.valueOf((int) car_plate.charAt(10))
					+ car_plate.substring(11, 14));
			// System.out.println(tid);
			/*
			 * if (!car_plate.substring(10, 11).equals("_")) tid =
			 * Long.parseLong(car_plate.substring(0, 8) + car_plate.substring(9,
			 * 11) + car_plate.substring(15, 19)); else tid =
			 * Long.parseLong(car_plate.substring(0, 8) + car_plate.substring(9,
			 * 10) + car_plate.substring(14, 18));
			 */

			String line = br.readLine();
			while (line != null) {
				TrajPoint tp = new TrajPoint();
				tp.setPid(Integer.parseInt(line));
				traj.getList().add(tp);
				tp.setTime(String.valueOf(oldTime));
				oldTime = ComputeTime(oldTime);
				line = br.readLine();
			}
			// System.out.println("Current trajectory size: " +
			// traj.getList().size());
			trajectories.add(traj);
			this.tid.add(tid);
			br.close();
		}
		System.out.println("All points:" + String.valueOf(pcount));

	}

	private void LoadByteTraj(String map, String dirName) throws IOException {

		/* load map file and construct a translator of edge and vertex */
		HashMap<Integer, Integer> translator = new HashMap<Integer, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(map));
		String line = br.readLine();
		while (line != null) {
			String[] sline = line.split(" ");
			translator.put(Integer.parseInt(sline[0]),
					Integer.parseInt(sline[1]));
			line = br.readLine();
		}
		br.close();

		/* load binary files and translate edges to vertex */
		File folder = new File(dirName);
		File[] listOfFiles = folder.listFiles();
		
		System.out.println(dirName + " has " + listOfFiles.length + "files");

		DataInputStream in;
		

		for (File file : listOfFiles) {
			String[] path = file.toString().split("/");
			String raw_id = file.toString();
			String fname = path[path.length - 1].substring(0, 1) + "B"
					+ path[path.length - 1].substring(4);
			// System.out.println(fname);

			in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(raw_id)));
			// BufferedWriter bw = new BufferedWriter(new FileWriter(
			// "data/SPLinked_Beijing_Text_Vertex/" + fname));

			// BufferedWriter bw = new BufferedWriter(new FileWriter(
			// "data/SPLinked_Beijing_Time/" + fname));

			Trajectory traj = new Trajectory();
			// String car_plate = "20160603_1_"+ fname;
			String car_plate = fname;
			int oldTime = 10000;
			long tid;
			// System.out.println(fname);
			// if(Character.isDigit(car_plate.charAt(10)))
			/*
			 * if (!car_plate.substring(10, 11).equals("_")) tid =
			 * Long.parseLong(car_plate.substring(0, 8) + car_plate.substring(9,
			 * 11) + car_plate.substring(15, 19)); else tid =
			 * Long.parseLong(car_plate.substring(0, 8) + car_plate.substring(9,
			 * 10) + car_plate.substring(14, 18));
			 */
			tid = Long.parseLong(car_plate.substring(0, 1)
					+ String.valueOf((int) car_plate.charAt(1))
					+ String.valueOf((int) car_plate.charAt(2))
					+ car_plate.substring(3, 7));
			// System.out.println(tid);

			int pcount = in.readInt();
			int pid;
			int vid;
			while (pcount > 0) {
				pid = in.readInt();
				vid = translator.get(pid);
				TrajPoint tp = new TrajPoint();
				tp.setPid(vid);
				// System.out.println(vid);
				tp.setTime(String.valueOf(oldTime));
				traj.getList().add(tp);
				oldTime = ComputeTime(oldTime);
				pcount--;
				// System.out.println(oldTime);
				// bw.write(String.valueOf(oldTime) + "\n");
			}
			trajectories.add(traj);
			this.tid.add(tid);
			in.close();
			// bw.close();
		}
	}

	public int ComputeTime(int lastTime) {
		double d = Math.random();
		int interval = (int) d * 6 - 4;
		int newTime = lastTime + 12 + interval;
		return newTime;
	}

	private void LoadTime(String timeDir) throws IOException {
		// TODO
	}

	private void LoadByteTime(String fileName) throws IOException {

		TempLine time;
		DataInputStream in = new DataInputStream(new BufferedInputStream(
				new FileInputStream(fileName)));
		int numTimes = in.readInt();
		int numTimePoint;
		while (numTimes > 0) {
			numTimePoint = in.readInt();
			time = new TempLine();
			for (int i = 0; i < numTimePoint; i++) {
				int t = in.readInt();
				double distance = in.readDouble();
				TempPoint tp = new TempPoint();
				tp.setTime(t);
				tp.setOffset(distance);
				// System.out.println(String.valueOf(t) + "," +
				// String.valueOf(distance));
				time.getList().add(tp);
			}
			times.add(time);
			numTimes--;
		}
		in.close();

	}

	private void LoadMap(String dirName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(dirName
				+ "/vertices.txt"));
		int pointsCount = 0;
		String line = br.readLine();
		while (line != null) {
			pointsCount++;
			String[] sline = line.split(" ");
			pointIdMap.put(Integer.parseInt(sline[0]),
					new Pair<String, String>(sline[2], sline[1]));
			line = br.readLine();

		}
		br.close();
		System.out.println("Vertices loaded.");

		br = new BufferedReader(new FileReader(dirName + "/edges.txt"));
		int linkCount = 0;
		line = br.readLine();
		while (line != null) {
			linkCount++;
			String[] sline = line.split(" ");
			Pair<Integer, Integer> curLink = new Pair<Integer, Integer>(
					Integer.parseInt(sline[1]), Integer.parseInt(sline[2]));
			map.add(curLink);
			line = br.readLine();
			if (linkCount % 100000 == 0) {
				System.out.println(linkCount + "edges loaded.");
				System.out.println(sline[1] + " " + sline[2]);
			}
		}
		br.close();
		System.out.println("Edges loaded.");
	}

	public BJDataLoader(String trajDir, String mapDir, boolean isNeedDij)
			throws IOException {
		System.out.println("Trajecotry Data Loading");
		/*
		 * System.out.println("Start: " + new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 * LoadTraj(trajDir); System.out.println("End: " + new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 */

		/*
		 * System.out.println("Temporal Data Loading");
		 * System.out.println("Start: " + new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 * LoadByteTime("data/BJTemporal/temporal"); System.out.println("End: "
		 * + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new
		 * Date()));
		 */

		System.out.println("Binary Trajecotry Data Loading");
		System.out.println("Start: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		LoadByteTraj(mapDir + "/edges.txt", trajDir);
		//LoadCleanTraj(trajDir);
		System.out.println("End: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));

		System.out.println("Map Loading");
		System.out.println("Start: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		LoadMap(mapDir);
		System.out.println("End: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		if (isNeedDij)
			this.dij = new Dijkstra(map, pointIdMap);
		// this.spdij=new spDijkstra(map,pointIdMap);
	}
}
