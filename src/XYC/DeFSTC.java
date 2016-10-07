package XYC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeFSTC extends TrajDecompressor {
	// private List<ArrayList<Integer>> encodedTrajList;
	private HashMap<Integer, Integer> mostFrequentFollower;

	// private List<ArrayList<Integer>> recoveredTrajList=new
	// ArrayList<ArrayList<Integer>>();

	public DeFSTC(String tdcname, String indexFile, String tireFile,
			String contentFile, String ftFile, String outputFile)
			throws IOException {
		super(tdcname, indexFile, tireFile, contentFile, outputFile);

		// super.HuffmanDecomp(binaryFile, encodedFile);
		// super.loadCompressedData(encodedFile);
		// this.encodedTrajList=super.getEncodedTrajList();
		this.mostFrequentFollower = getMFF(ftFile);

	}

	private HashMap<Integer, Integer> getMFF(String filename)
			throws IOException {
		HuffmanAll hfa = new HuffmanAll(filename, "temp");
		hfa.expand();

		BufferedReader br = new BufferedReader(new FileReader("temp"));
		HashMap<Integer, Integer> tmpMFF = new HashMap<Integer, Integer>();

		String content = br.readLine();
		br.close();
		String[] ss = content.split(",");
		int length = ss.length - 1;
		for (int i = 0; i < length; i += 2) {
			int key = Integer.parseInt(ss[i]);
			int value = Integer.parseInt(ss[i + 1]);
			tmpMFF.put(key, value);
		}

		return tmpMFF;
	}

	@Override
	public void decode(long id) throws IOException {

		ExpandTraj et = super.getPartialExpandTraj(id);

		ArrayList<Integer> recoveredCurTraj = new ArrayList<Integer>();
		List<Integer> curTraj = et.spatialPoints;

		for (int j = 0; j < curTraj.size(); j += 2) {
			int curPointId = curTraj.get(j);
			int ignorePointsNum = curTraj.get(j + 1);
			recoveredCurTraj.add(curPointId);
			for (int k = ignorePointsNum; k > 0; k--) {

				curPointId = mostFrequentFollower.get(curPointId);
				recoveredCurTraj.add(curPointId);
			}
		}
		et.spatialPoints = recoveredCurTraj;
		et.id = id;
		for (int i = 0; i < et.temporalPoints.size(); i++)
			et.expandTime
					.add(Statistic.getStringTime(et.temporalPoints.get(i)));
		super.outputET(et);

		/*
		 * for(int i=0;i<encodedTrajList.size();i++) { ArrayList<Integer>
		 * recoveredCurTraj=new ArrayList<Integer>(); List<Integer>
		 * curTraj=encodedTrajList.get(i);
		 * 
		 * for(int j=0;j<curTraj.size();j+=2) { int curPointId=curTraj.get(j);
		 * int ignorePointsNum=curTraj.get(j+1);
		 * recoveredCurTraj.add(curPointId); for(int k=ignorePointsNum;k>0;k--)
		 * { curPointId=mostFrequentFollower.get(curPointId);
		 * recoveredCurTraj.add(curPointId); } }
		 * recoveredTrajList.add(recoveredCurTraj); }
		 * super.outputRecoveredTraj(recoveredTrajList, "FSTC_De.csv");
		 */

	}

}
