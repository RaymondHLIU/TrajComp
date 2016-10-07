package XYC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeMFPTC extends TrajDecompressor {
	private HashMap<Pair<Integer, Integer>, ArrayList<Integer>> MFPattern = new HashMap<Pair<Integer, Integer>, ArrayList<Integer>>();

	private HashMap<Pair<Integer, Integer>, ArrayList<Integer>> getMFFL(
			String filename) throws IOException {
		HuffmanAll hfa = new HuffmanAll(filename, "temp");
		hfa.expand();

		BufferedReader br = new BufferedReader(new FileReader("temp"));
		HashMap<Pair<Integer, Integer>, ArrayList<Integer>> tempMFP = new HashMap<Pair<Integer, Integer>, ArrayList<Integer>>();

		String content = "";
		while ((content = br.readLine()) != null && content.length() != 0) {
			String[] ss = content.split(",");
			int length = ss.length - 1;

			int key0 = Integer.parseInt(ss[0]);
			int key1 = Integer.parseInt(ss[1]);
			ArrayList<Integer> value = new ArrayList<Integer>();
			for (int i = 2; i < length; i++)
				value.add(Integer.parseInt(ss[i]));
			tempMFP.put(new Pair<Integer, Integer>(key0, key1), value);
		}

		br.close();
		return tempMFP;

	}

	public DeMFPTC(String tdcname, String indexFile, String tireFile,
			String contentFile, String MFPTableName, String outputFile)
			throws IOException {
		super(tdcname, indexFile, tireFile, contentFile, outputFile);
		this.MFPattern = getMFFL(MFPTableName);
	}

	@Override
	public void decode(long id) throws IOException {

		ExpandTraj et = super.getPartialExpandTraj(id);

		ArrayList<Integer> recoveredCurTraj = new ArrayList<Integer>();
		List<Integer> curTraj = et.spatialPoints;

		if (curTraj.size() == 1)
			recoveredCurTraj.add(curTraj.get(0));
		else {
			int firstid = curTraj.get(0);
			recoveredCurTraj.add(firstid);
			for (int i = 2; i < curTraj.size(); i = i + 2) {
				int curid = curTraj.get(i);
				int IgnoreNum = curTraj.get(i - 1);

				if (IgnoreNum > 0) {
					// System.out.println("Recover pair " + firstid + " " +
					// curid + " " + IgnoreNum);
					/*
					 * if(!MFPattern.containsKey(new Pair<Integer,
					 * Integer>(firstid, curid)))
					 * System.out.println("MFPattern do not have pair " +
					 * firstid + " " + curid);
					 */
					ArrayList<Integer> mfp = MFPattern
							.get(new Pair<Integer, Integer>(firstid, curid));
					// System.out.println(mfp);
					for (int j = 1; j < mfp.size(); j++)
						recoveredCurTraj.add(mfp.get(j));
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
