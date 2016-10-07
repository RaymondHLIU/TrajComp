package XYC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RSPTC extends TrajCompressor {
	private List<Trajectory> trajList = new ArrayList<Trajectory>();
	private List<Trajectory> tctrajList = new ArrayList<Trajectory>();
	private HashMap<ArrayList<Integer>, Integer> rsPattern = new HashMap<ArrayList<Integer>, Integer>();
	private int patternLength;

	public RSPTC(String tcName, List<Trajectory> trajList, int pl,
			List<Long> tid) throws IOException {
		super(tcName, tid);
		this.trajList = trajList;
		super.setTrajList(this.trajList);
		patternLength = pl;

		HashMap<ArrayList<Integer>, Integer> patternCounter = new HashMap<ArrayList<Integer>, Integer>();
		for (Trajectory traj : this.trajList) {
			for (int startPos = 0; startPos <= traj.getList().size()
					- patternLength; startPos += patternLength) {
				ArrayList<Integer> curPattern = new ArrayList<Integer>();
				for (int i = startPos; i < startPos + patternLength; i++)
					curPattern.add(traj.getList().get(i).getPid());
				if (patternCounter.containsKey(curPattern)) {
					int curCount = patternCounter.get(curPattern);
					curCount++;
					patternCounter.remove(curPattern);
					patternCounter.put(curPattern, curCount);
				} else {
					patternCounter.put(curPattern, 1);
				}
			}
		}

		ArrayList<Entry<ArrayList<Integer>, Integer>> patternList = new ArrayList<Entry<ArrayList<Integer>, Integer>>(
				patternCounter.entrySet());
		Collections.sort(patternList,
				new Comparator<Map.Entry<ArrayList<Integer>, Integer>>() {
					@Override
					public int compare(
							Map.Entry<ArrayList<Integer>, Integer> o1,
							Map.Entry<ArrayList<Integer>, Integer> o2) {
						return (o2.getValue() - o1.getValue());
					}
				});

		int patternCode = -1;
		for (int i = 0; i < patternList.size(); i++) {
			if (patternList.get(i).getValue() > 1) {
				rsPattern.put(patternList.get(i).getKey(), patternCode);
				patternCode--;
			} else
				break;
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
			int startPos = 0;
			for (; startPos <= traj.getList().size() - patternLength; startPos += patternLength) {
				ArrayList<Integer> curList = new ArrayList<Integer>();
				ArrayList<TrajPoint> curTPList = new ArrayList<TrajPoint>();
				for (int i = startPos; i < startPos + patternLength; i++) {
					curList.add(traj.getList().get(i).getPid());
					curTPList.add(traj.getList().get(i));
				}
				if (rsPattern.containsKey(curList)) {
					TrajPoint tp = new TrajPoint();
					tp.setPid(rsPattern.get(curList));
					tctraj.getList().add(tp);
				} else {
					for (int i = 0; i < curTPList.size(); i++)
						tctraj.getList().add(curTPList.get(i));
				}
			}
			if (startPos != traj.getList().size()) {
				for (int i = startPos; i < traj.getList().size(); i++)
					tctraj.getList().add(traj.getList().get(i));
			}
			tctrajList.add(tctraj);
		}
		super.setTctrajList(tctrajList);
		super.outputData("RSPTC", false);
		writeRSPattern("RSPTCPattern");
		/*super.HuffmanComp("RSPTC", "RSPTCTree.Comp", "RSPTCContent.Comp",
				"RSPTCIndex");
		HuffmanAll hf = new HuffmanAll("RSPTCPattern", "RSPTCPattern.Comp");
		hf.compress();*/

		// super.HuffmanComp("RSPTC"+patternLength,
		// "RSPTC"+patternLength+".Comp");
	}

	private void writeRSPattern(String filename) throws IOException {
		BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File(filename))));
		for (Entry<ArrayList<Integer>, Integer> entry : rsPattern.entrySet()) {
			for (int i = 0; i < patternLength; i++)
				bfw.write(entry.getKey().get(i) + ",");
			bfw.write(entry.getValue() + ",");
		}
		bfw.close();
	}
}
