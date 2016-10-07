package XYC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class FSTC extends TrajCompressor {
	private List<Trajectory> trajList = new ArrayList<Trajectory>();
	private List<Trajectory> tctrajList = new ArrayList<Trajectory>();
	private HashMap<Integer, Integer> mostFrequentFollower = new HashMap<Integer, Integer>();

	public FSTC(String tcName, List<Trajectory> trajList,
			HashMap<Integer, Pair<String, String>> pointIdMap, Dijkstra dij,
			List<Long> tid) throws IOException {
		super(tcName, tid);
		this.trajList = trajList;

		super.setTrajList(this.trajList);

		HashMap<Integer, HashMap<Integer, Integer>> tmpCounter = new HashMap<Integer, HashMap<Integer, Integer>>();
		for (Entry<Integer, Pair<String, String>> e : pointIdMap.entrySet()) {
			tmpCounter.put(e.getKey(), new HashMap<Integer, Integer>());
			// System.out.println(e);
		}
		for (Trajectory t : this.trajList) {
			for (int i = 0; i < t.getList().size() - 1; i++) {
				// System.out.println(t.getList());
				int curValue = t.getList().get(i).getPid();
				int followingId = t.getList().get(i + 1).getPid();
				// System.out.println(String.valueOf(curValue)+tmpCounter.get(curValue));
				if (!pointIdMap.containsKey(curValue))
					System.out.println(String.valueOf(curValue)
							+ " Not in pointIdMap");
				if (!tmpCounter.get(curValue).containsKey(followingId))
					tmpCounter.get(curValue).put(followingId, 1);
				else {
					int curCount = tmpCounter.get(curValue).get(followingId);
					curCount++;
					tmpCounter.get(curValue).remove(followingId);
					tmpCounter.get(curValue).put(followingId, curCount);
				}
			}
		}

		for (Entry<Integer, HashMap<Integer, Integer>> e : tmpCounter
				.entrySet()) {
			int maxCount = -1;
			int maxCountId = -1;
			for (Entry<Integer, Integer> en : e.getValue().entrySet()) {
				int curCount = en.getValue();
				if (curCount > maxCount) {
					maxCount = curCount;
					maxCountId = en.getKey();
				}
			}
			if (maxCountId != -1) {
				mostFrequentFollower.put(e.getKey(), maxCountId);
				// System.out.println(String.valueOf(e.getKey()) + "," +
				// String.valueOf(maxCountId));
			}
		}

	}

	@Override
	public void encode() throws IOException {
		try {
			super.encodeTime();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (Trajectory traj : trajList) {
			Trajectory tctraj = new Trajectory();
			// tctraj.setValid(traj.isValid());
			tctraj.setId(traj.getId());
			// tctraj.setStartTime(traj.getStartTime());
			// tctraj.setEndTime(traj.getEndTime());
			int N = traj.getList().size();
			// System.out.println("Trajectory size: " + String.valueOf(N));
			if (N < 1)
				continue;
			TrajPoint firstP = traj.getList().get(0);
			tctraj.getList().add(firstP);

			int count = 0;
			int ignore = 0;
			for (int i = 1; i < N; i++) {
				TrajPoint curP = traj.getList().get(i);
				if (!mostFrequentFollower.containsKey(firstP.getPid())
						|| mostFrequentFollower.get(firstP.getPid()) != curP
								.getPid()) {
					tctraj.getList().get(tctraj.getList().size() - 1)
							.setNumFollowIgnore(ignore);
					tctraj.getList().add(curP);
					ignore = 0;
				} else
					ignore++;
				firstP = curP;
			}
			// System.out.println("Current trajectory, ignore " +
			// String.valueOf(ignore) + "points.");
			tctraj.getList().get(tctraj.getList().size() - 1)
					.setNumFollowIgnore(ignore);
			tctrajList.add(tctraj);
		}
		super.setTctrajList(tctrajList);
		super.outputData("FSTC", true);
		writeFollowerTable("FSTC_FollowingTable");
		super.HuffmanComp("FSTC", "FSTCTree.Comp", "FSTCContent.Comp",
				"FSTCIndex");
		HuffmanAll hf = new HuffmanAll("FSTC_FollowingTable",
				"FSTC_FollowingTable.Comp");
		hf.compress();
	}

	public void writeFollowerTable(String filename) throws IOException {
		BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(filename))));
		for (Entry<Integer, Integer> entry : mostFrequentFollower.entrySet()) {
			if (entry.getValue() != -1) {
				bfw.write(entry.getKey() + "," + entry.getValue() + ",");
			}
		}
		bfw.close();
	}

	public void decode() {

	}
}