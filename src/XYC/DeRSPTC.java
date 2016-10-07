package XYC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeRSPTC extends TrajDecompressor {
	private HashMap<Integer, ArrayList<Integer>> rsPattern = new HashMap<Integer, ArrayList<Integer>>();

	public DeRSPTC(String tdcname, String indexFile, String tireFile,
			String contentFile, String patternFile, String outputFile)
			throws IOException {
		super(tdcname, indexFile, tireFile, contentFile, outputFile);
		loadPattern(patternFile);
	}

	private void loadPattern(String filename) throws IOException {
		HuffmanAll hfa = new HuffmanAll(filename, "temp");
		hfa.expand();

		BufferedReader br = new BufferedReader(new FileReader("temp"));
		String line = br.readLine();
		br.close();

		String[] ss = line.split(",");
		ArrayList<Integer> pattern = new ArrayList<Integer>();
		for (int i = 0; i < ss.length - 1; i++) {
			int curInt = Integer.parseInt(ss[i]);
			if (curInt >= 0)
				pattern.add(curInt);
			else {
				rsPattern.put(curInt, pattern);
				pattern = new ArrayList<Integer>();
			}
		}

	}

	@Override
	public void decode(long id) throws IOException {
		ExpandTraj et = super.getPartialExpandTraj(id);

		ArrayList<Integer> recoveredCurTraj = new ArrayList<Integer>();
		List<Integer> curTraj = et.spatialPoints;

		for (int j = 0; j < curTraj.size(); j++) {
			int curPointId = curTraj.get(j);
			if (curPointId >= 0)
				recoveredCurTraj.add(curPointId);
			else {
				ArrayList<Integer> patternPointsId = rsPattern.get(curPointId);
				for (int i = 0; i < patternPointsId.size(); i++)
					recoveredCurTraj.add(patternPointsId.get(i));
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
