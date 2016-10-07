package XYC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeFLTC extends TrajDecompressor {
	// private List<ArrayList<Integer>> encodedTrajList;
	private HashMap<Pair<Integer, Integer>, Integer> mostFrequentFollowingLink;

	// private List<ArrayList<Integer>> recoveredTrajList=new
	// ArrayList<ArrayList<Integer>>();

	public DeFLTC(String tdcname, String indexFile, String tireFile,
			String contentFile, String ftFile, String outputFile)
			throws IOException {
		super(tdcname, indexFile, tireFile, contentFile, outputFile);

		// super.HuffmanDecomp(binaryFile, encodedFile);
		// super.loadCompressedData(encodedFile);
		// this.encodedTrajList=super.getEncodedTrajList();
		this.mostFrequentFollowingLink = getMFFL(ftFile);

	}

	private HashMap<Pair<Integer, Integer>, Integer> getMFFL(String filename)
			throws IOException {
		HuffmanAll hfa = new HuffmanAll(filename, "temp");
		hfa.expand();

		BufferedReader br = new BufferedReader(new FileReader("temp"));
		HashMap<Pair<Integer, Integer>, Integer> tmpMFF = new HashMap<Pair<Integer, Integer>, Integer>();

		String content = br.readLine();
		br.close();
		String[] ss = content.split(",");
		int length = ss.length - 1;
		for (int i = 0; i < length; i += 3) {
			int key0 = Integer.parseInt(ss[i]);
			int key1 = Integer.parseInt(ss[i + 1]);
			int value = Integer.parseInt(ss[i + 2]);
			tmpMFF.put(new Pair<Integer, Integer>(key0, key1), value);
		}
		return tmpMFF;
	}

	@Override
	public void decode(long id) throws IOException {
		ExpandTraj et = super.getPartialExpandTraj(id);

		ArrayList<Integer> recoveredCurTraj = new ArrayList<Integer>();
		List<Integer> curTraj = et.spatialPoints;

		if (curTraj.size() == 2)
			recoveredCurTraj.add(curTraj.get(0));
		else {
			int firstid = curTraj.get(0);
			recoveredCurTraj.add(firstid);
			for (int j = 2; j < curTraj.size(); j += 2) {
				int curid = curTraj.get(j);
				int IgnoreNum = curTraj.get(j + 1);

				for (int k = IgnoreNum; k > 0; k--) {
					int minId = mostFrequentFollowingLink
							.get(new Pair<Integer, Integer>(firstid, curid));
					recoveredCurTraj.add(curid);
					firstid = curid;
					curid = minId;
				}
				recoveredCurTraj.add(curid);
				firstid = curid;
			}
		}

		et.spatialPoints = recoveredCurTraj;
		et.id = id;
		for (int i = 0; i < et.temporalPoints.size(); i++)
			et.expandTime
					.add(Statistic.getStringTime(et.temporalPoints.get(i)));
		// System.out.println(et.spatialPoints.size() + " " +
		// et.temporalPoints.size() + " " + et.expandTime.size());
		super.outputET(et);

		/* Query on decompressed trajectory, random choose a point */
		int idx = (int) (Math.random() * et.temporalPoints.size());
		/* When at query */
		int pid = et.spatialPoints.get(idx);
		super.whenAt(et, pid, 0);
		/* Where at query */
		int time = et.temporalPoints.get(idx);
		// super.whereAt(et, time, 15);

	}

}
