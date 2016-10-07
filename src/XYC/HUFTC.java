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

public class HUFTC extends TrajCompressor {
	private List<Trajectory> trajList = new ArrayList<Trajectory>();
	private List<Trajectory> tctrajList = new ArrayList<Trajectory>();
	private HashMap<Pair<Integer, Integer>, Integer> mostFrequentFollowingLink = new HashMap<Pair<Integer, Integer>, Integer>();

	public HUFTC(String tcName, List<Trajectory> trajList,
			List<Pair<Integer, Integer>> map, List<Long> tid)
			throws IOException {
		super(tcName, tid);
		this.trajList = trajList;

		super.setTrajList(this.trajList);

		HashMap<Pair<Integer, Integer>, HashMap<Integer, Integer>> tmpCounter = new HashMap<Pair<Integer, Integer>, HashMap<Integer, Integer>>();

		for (Pair<Integer, Integer> e : map) {
			tmpCounter.put(e, new HashMap<Integer, Integer>());
			// System.out.println(e);
		}

		int connected = 0;
		int unconnect = 0;
		for (Trajectory t : this.trajList) {
			int firstId;
			if (t.getList().size() > 0)
				firstId = t.getList().get(0).getPid();
			else
				firstId = 0;
			for (int i = 1; i < t.getList().size() - 1; i++) {
				int curId = t.getList().get(i).getPid();
				int followingId = t.getList().get(i + 1).getPid();
				Pair<Integer, Integer> curPair = new Pair<Integer, Integer>(
						firstId, curId);
				// System.out.println(tmpCounter.containsKey(curPair));
				// System.out.println(curPair);

				if (!tmpCounter.containsKey(curPair)) {
					unconnect++;
					tmpCounter.put(curPair, new HashMap<Integer, Integer>());
				}
				connected++;

				if (!tmpCounter.get(curPair).containsKey(followingId))
					tmpCounter.get(curPair).put(followingId, 1);
				else {
					int curCount = tmpCounter.get(curPair).get(followingId);
					curCount++;
					tmpCounter.get(curPair).remove(followingId);
					tmpCounter.get(curPair).put(followingId, curCount);
				}
				firstId = curId;
			}
		}
		System.out.println(connected);
		System.out.println(unconnect);

		for (Entry<Pair<Integer, Integer>, HashMap<Integer, Integer>> e : tmpCounter
				.entrySet()) {
			// System.out.println(e.getKey().getFirst() + "," +
			// e.getKey().getSecond() + ":");
			int maxCount = -1;
			int maxCountId = -1;
			// System.out.println(e.getValue());
			for (Entry<Integer, Integer> en : e.getValue().entrySet()) {
				int curCount = en.getValue();
				// System.out.println(curCount);
				if (curCount > maxCount) {
					maxCount = curCount;
					maxCountId = en.getKey();
				}
			}
			if (maxCountId != -1) {
				mostFrequentFollowingLink.put(e.getKey(), maxCountId);
				// System.out.println(maxCountId);
			}
		}

		/*
		 * for(Trajectory t: this.trajList) { int count = 0; for(int
		 * i=0;i<t.getList().size()-1;i++){
		 * if(mostFrequentFollowingLink.containsKey(new Pair(t.getList().get(i),
		 * t.getList().get(i+1)))) System.out.println("edge recorded"); } }
		 */
	}

	@Override
	public void encode() throws IOException {
		try {
			super.encodeTime();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		long point = 0;
		long unconnectPoint = 0;
		for (Trajectory traj : trajList) {
			Trajectory tctraj = new Trajectory();
			// tctraj.setValid(traj.isValid());
			tctraj.setId(traj.getId());
			// tctraj.setStartTime(traj.getStartTime());
			// tctraj.setEndTime(traj.getEndTime());
			int N = traj.getList().size();
			if (N < 3) {
				tctrajList.add(traj);
				continue;
			}
			
			for (int i = 0; i < N; i++)
				tctraj.getList().add(traj.getList().get(i));

			tctrajList.add(tctraj);
		}

		System.out.println("All points:" + String.valueOf(point));
		System.out.println("Unconnected points:"
				+ String.valueOf(unconnectPoint));
		super.setTctrajList(tctrajList);
		super.outputData("HUFTC", true);
		writeFollowerTable("HUFTC_FollowingTable");
		super.HuffmanComp("HUFTC", "HUFTCTree.Comp", "HUFTCContent.Comp",
				"HUFTCIndex");
		HuffmanAll hf = new HuffmanAll("HUFTC_FollowingTable",
				"HUFTC_FollowingTable.Comp");
		hf.compress();
	}

	public void writeFollowerTable(String filename) throws IOException {
		BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(filename))));
		for (Entry<Pair<Integer, Integer>, Integer> entry : mostFrequentFollowingLink
				.entrySet()) {
			if (entry.getValue() != -1) {
				bfw.write(entry.getKey().getFirst() + ","
						+ entry.getKey().getSecond() + "," + entry.getValue()
						+ ",");
			}
		}
		bfw.close();
	}

	public void decode() {

	}

}
