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

public class MFPTC extends TrajCompressor {
	/*
	 * This class detect all-pair most frequent path and compress trajectories
	 * accordingly. Compared with RSPTC(fixed length frequent pattern), this
	 * method compute vary length path.
	 */

	private List<Trajectory> trajList = new ArrayList<Trajectory>();
	private List<Trajectory> tctrajList = new ArrayList<Trajectory>();
	private HashMap<Pair<Integer, Integer>, ArrayList<Integer>> MFPattern = new HashMap<Pair<Integer, Integer>, ArrayList<Integer>>();
	public int MAX_MFP_LENGTH = 10;

	public MFPTC(String tcName, List<Trajectory> trajList, List<Long> tid,
			int max_pl) throws IOException {
		super(tcName, tid);
		this.trajList = trajList;
		super.setTrajList(this.trajList);
		MAX_MFP_LENGTH = max_pl;

		HashMap<Pair<Integer, Integer>, HashMap<ArrayList<Integer>, Integer>> pathCounter = new HashMap<Pair<Integer, Integer>, HashMap<ArrayList<Integer>, Integer>>();

		int count = 0;
		// Scan all trajectories to generate MFPattern, pattern length at least
		// 3
		for (Trajectory t : this.trajList) {
			if (count % 500 == 0)
				System.out.println("Processing " + String.valueOf(count)
						+ "th traj...");
			count++;
			for (int startPos = 0; startPos < t.getList().size() - 3; startPos++) {
				// from length 3 to MAX_MFP_LENGTH, count each subpath to hash
				// map
				int endPos = startPos + 2;
				ArrayList<Integer> curPattern;
				for (; endPos < t.getList().size()
						&& endPos - startPos < MAX_MFP_LENGTH; endPos++) {
					curPattern = new ArrayList<Integer>();
					for (int i = startPos; i <= endPos; i++)
						curPattern.add(t.getList().get(i).getPid());

					Pair<Integer, Integer> currentPair = new Pair<Integer, Integer>(
							t.getList().get(startPos).getPid(), t.getList()
									.get(endPos).getPid());
					if (!pathCounter.containsKey(currentPair)) {
						pathCounter.put(currentPair,
								new HashMap<ArrayList<Integer>, Integer>());
						pathCounter.get(currentPair).put(curPattern, 1);
					} else if (!pathCounter.get(currentPair).containsKey(
							curPattern)) {
						pathCounter.get(currentPair).put(curPattern, 1);
					} else {
						int curCount = pathCounter.get(currentPair).get(
								curPattern);
						curCount++;
						pathCounter.get(currentPair).remove(curPattern);
						pathCounter.get(currentPair).put(curPattern, curCount);
					}
				}

			}
		}

		// pick most frequent path for each pair
		for (Entry<Pair<Integer, Integer>, HashMap<ArrayList<Integer>, Integer>> e : pathCounter
				.entrySet()) {
			int maxCount = -1;
			ArrayList<Integer> maxList = null;
			for (Entry<ArrayList<Integer>, Integer> en : e.getValue()
					.entrySet()) {
				int curCount = en.getValue();
				if (curCount > maxCount) {
					maxCount = curCount;
					maxList = en.getKey();
				}
			}
			// System.out.println("Max count" + maxCount);
			if (maxCount > 0) {
				MFPattern.put(e.getKey(), maxList);
			}
		}
		System.out.println("MFPTable size " + MFPattern.size());

	}

	@Override
	public void encode() throws IOException {
		writeMFPTable("MFPTable");
		try {
			super.encodeTime();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (Trajectory t : this.trajList) {
			int N = t.getList().size();
			if (N < 3) {
				tctrajList.add(t);
				continue;
			}

			Trajectory tctraj = new Trajectory();
			tctraj.setId(t.getId());
			/*
			 * TrajPoint firstP = t.getList().get(0);
			 * tctraj.getList().add(firstP);
			 * tctraj.getList().get(0).setNumFollowIgnore(0);
			 */

			int startPos;
			int ignore = 0;
			for (startPos = 0; startPos < N - 3;) {
				// System.out.println("Processing " + startPos + "th point");
				int endPos;
				if (N - startPos > MAX_MFP_LENGTH)
					endPos = startPos + MAX_MFP_LENGTH;
				else
					endPos = N - 1;
				ArrayList<Integer> curPath = new ArrayList<Integer>();
				for (int i = 0; i <= endPos - startPos; i++) {
					curPath.add(t.getList().get(startPos + i).getPid());
				}
				// System.out.println("Current Path" + curPath);
				Pair<Integer, Integer> currentPair;
				boolean flag = true;
				while (endPos - startPos >= 3) {
					currentPair = new Pair<Integer, Integer>(curPath.get(0),
							curPath.get(curPath.size() - 1));
					ArrayList<Integer> curPattern = this.MFPattern
							.get(currentPair);
					if (curPattern != null && curPath != null
							&& curPattern.equals(curPath)) {
						ignore = endPos - startPos - 1;
						// System.out.println("Successfully ignored " +
						// String.valueOf(ignore) + "edges");
						tctraj.getList().add(t.getList().get(startPos));
						tctraj.getList().get(tctraj.getList().size() - 1)
								.setNumFollowIgnore(ignore);
						startPos = endPos;
						flag = false;
						break;
					} else {
						// System.out.println(curPath);
						curPath.remove(curPath.size() - 1);
						// System.out.println(curPath);
						endPos--;
					}
				}
				if (flag) {
					tctraj.getList().add(t.getList().get(startPos));
					tctraj.getList().get(tctraj.getList().size() - 1)
							.setNumFollowIgnore(0);
					startPos++;
				}
			}
			for (; startPos < N; startPos++) {
				tctraj.getList().add(t.getList().get(startPos));
				tctraj.getList().get(tctraj.getList().size() - 1)
						.setNumFollowIgnore(0);
			}
			tctrajList.add(tctraj);
		}
		super.setTctrajList(tctrajList);
		super.outputData("MFPTC", true);
		writeMFPTable("MFPTable");
		/*super.HuffmanComp("MFPTC", "MFPTCTree.Comp", "MFPTCContent.Comp",
				"MFPTCIndex");
		HuffmanAll hf = new HuffmanAll("MFPTable", "MFPTable.Comp");
		hf.compress();*/

	}

	public void writeMFPTable(String filename) throws IOException {
		// System.out.println("Writing MFPTable...");
		BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(filename))));
		// System.out.println("MFPTable size " + MFPattern.size());
		for (Entry<Pair<Integer, Integer>, ArrayList<Integer>> entry : MFPattern
				.entrySet()) {
			String path = "";
			ArrayList<Integer> curPattern = entry.getValue();
			for (int i = 0; i < curPattern.size() - 1; i++)
				path += entry.getValue().get(i) + ",";
			path += entry.getValue().get(curPattern.size() - 1) + "\n";
			bfw.write(entry.getKey().getFirst() + ","
					+ entry.getKey().getSecond() + "," + path);
		}
		bfw.close();
	}
}
