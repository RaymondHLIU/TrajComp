package XYC;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class QueryUncompress {
	private List<Trajectory> trajList = new ArrayList<Trajectory>();
	private List<Long> tid = new ArrayList<Long>();
	private List<Long> toDecodeTrajList;
	private int randomNum;

	private List<Long> randomChooseTraj(List<Long> tid, int num) {
		List<Long> resTrajList = new ArrayList<Long>();
		Collections.shuffle(tid);
		for (int i = 0; i < num; i++)
			resTrajList.add(tid.get(i));
		return resTrajList;
	}

	public QueryUncompress(String dataFile, String mapFile, int randomNum)
			throws IOException {
		BJDataLoader dl = new BJDataLoader(dataFile, mapFile, true);
		this.trajList = dl.getTraj();
		;
		this.tid = dl.getTid();
		this.randomNum = randomNum;
		toDecodeTrajList = randomChooseTraj(dl.getTid(), randomNum);

	}

	public void execute() throws IOException {
		BufferedWriter outputBw = new BufferedWriter(new FileWriter(
				"QueryUncompress.exp"));
		System.out.println("Start: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		double maxTime = 0;
		double minTime = 100;
		double avgTime = 0;
		for (int i = 0; i < randomNum; i++) {
			long startTime = System.currentTimeMillis();
			ExpandTraj et = new ExpandTraj();
			int idx = (int) (Math.random() * trajList.size());
			Trajectory traj = trajList.get(idx);
			for (int j = 0; j < traj.getList().size(); j++) {
				et.spatialPoints.add(traj.getList().get(j).getPid());
				et.temporalPoints.add(Integer.parseInt(traj.getList().get(j)
						.getTime()));
				et.expandTime.add(Statistic.getStringTime(et.temporalPoints
						.get(j)));
				// System.out.println(traj.getList().get(j).getPid() + " " +
				// traj.getList().get(j).getTime() + " " +
				// Statistic.getStringTime(et.temporalPoints.get(i)));
			}

			outputBw.write(et.id + "\r\n");
			int count = traj.getList().size();
			System.out.println(count);
			for (int k = 0; k < count; k++) {
				outputBw.write(et.spatialPoints.get(k) + "\t"
						+ et.expandTime.get(k) + "\r\n");
			}
			/* Query on decompressed trajectory, random choose a point */
			int qidx = (int) (Math.random() * et.temporalPoints.size());
			// System.out.println(qidx);
			/* When at query */
			System.out.println("When at query...");
			int pid = et.spatialPoints.get(qidx);
			// int ret = whenAt(et, pid, 0);
			// System.out.println(ret);
			/* Where at query */
			System.out.println("Where at query...");
			int time = et.temporalPoints.get(qidx);
			int ret = whereAt(et, time, 0);
			System.out.println(ret);
			long endTime = System.currentTimeMillis();
			long t = endTime - startTime;
			if (t > maxTime)
				maxTime = t;
			if (t < minTime && t > 0.5)
				minTime = t;
			avgTime += t;
		}
		avgTime = 1.0 * avgTime / toDecodeTrajList.size();
		System.out.println("Overall Time: "
				+ (1.0 * avgTime * toDecodeTrajList.size() / 1000) + "s");
		System.out.println("Avg Time: " + (1.0 * avgTime / 1000) + "s");
		System.out.println("Max Time: " + (1.0 * maxTime / 1000) + "s");
		System.out.println("Min Time: " + (1.0 * minTime / 1000) + "s");
		System.out.println("End: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		outputBw.close();

	}

	/*
	 * We implement three queries by using binary search
	 */

	public int whereAt(ExpandTraj et, int midTime, int devation)
			throws IOException {
		List<Integer> tpoints = et.temporalPoints;
		int len = tpoints.size();
		int idx = findWhere(tpoints, midTime, devation, 0, len);

		return et.spatialPoints.get(idx);

	}

	public int findWhere(List<Integer> tp, int midTime, int devation,
			int startPos, int endPos) {
		int idx = (endPos + startPos) / 2;

		if ((endPos - startPos <= 1) || tp.get(idx) >= midTime - devation
				&& tp.get(idx) <= midTime + devation)
			return idx;
		if (tp.get(idx) < (midTime - devation)) {
			return findWhere(tp, midTime, devation, idx, endPos);
		} else {
			return findWhere(tp, midTime, devation, startPos, idx);
		}
	}

	public int whenAt(ExpandTraj et, int pid, double devation)
			throws IOException {
		List<Integer> spoints = et.spatialPoints;
		int len = spoints.size();
		int idx = findWhen(spoints, pid, 0, len);

		return et.temporalPoints.get(idx);
	}

	public int findWhen(List<Integer> sp, int pid, double devation, int len) {
		for (int i = 0; i < len; i++)
			if (sp.get(i) == pid)
				return i;
		return -1;
	}

	public static void main(String[] args) throws IOException {

		final String dataDir = "data/SPLinkedFile_Beijing";
		final String mapDir = "data/BJMap";
		final int randomNum = 100; // should be less than the total number of
									// trajectories

		QueryUncompress tdce = new QueryUncompress(dataDir, mapDir, randomNum);
		tdce.execute();
	}

}
