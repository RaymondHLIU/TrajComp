package XYC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeFPTC extends TrajDecompressor {
	// private List<ArrayList<Integer>> encodedTrajList;
	// private List<ArrayList<Integer>> recoveredTrajList=new
	// ArrayList<ArrayList<Integer>>();
	private HashMap<Pair<Integer, Integer>, Integer> fpMap;

	public DeFPTC(String tdcname, String indexFile, String tireFile,
			String contentFile, Dijkstra dij, String outputFile)
			throws IOException {
		super(tdcname, indexFile, tireFile, contentFile, outputFile);
		// super.HuffmanDecomp(binaryFile, encodedFile);
		// super.loadCompressedData(encodedFile);
		this.fpMap = dij.getFpMap();

		// this.encodedTrajList=super.getEncodedTrajList();
	}

	@Override
	public void decode(long id) throws IOException {

		ExpandTraj et = super.getPartialExpandTraj(id);
		// System.out.println("Size of spatial points: " +
		// et.spatialPoints.size() + ". Size of temporal points: " +
		// et.temporalPoints.size());

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
				// if(IgnoreNum > 0)
				// System.out.println("Curid " + curid + "IgnoreNum " +
				// IgnoreNum);

				for (int k = IgnoreNum; k > 0; k--) {
					int minId = fpMap.get(new Pair<Integer, Integer>(firstid,
							curid));
					recoveredCurTraj.add(curid);
					firstid = curid;
					curid = minId;
				}
				recoveredCurTraj.add(curid);
				firstid = curid;
			}
		}
		// System.out.println("Size of recovered spatial points: " +
		// recoveredCurTraj.size());

		et.spatialPoints = recoveredCurTraj;
		et.id = id;
		/*for (int i = 0; i < et.temporalPoints.size(); i++)
			et.expandTime
					.add(Statistic.getStringTime(et.temporalPoints.get(i)));*/
		// System.out.println(et.spatialPoints.size() + " " +
		// et.temporalPoints.size() + " " + et.expandTime.size());
		super.outputET(et);

		/*
		 * for(int i=0;i<encodedTrajList.size();i++) { ArrayList<Integer>
		 * recoveredCurTraj=new ArrayList<Integer>(); List<Integer>
		 * curTraj=encodedTrajList.get(i);
		 * 
		 * if(curTraj.size()==2) recoveredCurTraj.add(curTraj.get(0)); else {
		 * int firstid=curTraj.get(0); recoveredCurTraj.add(firstid); for(int
		 * j=2;j<curTraj.size();j+=2) { int curid=curTraj.get(j); int
		 * IgnoreNum=curTraj.get(j+1);
		 * 
		 * for(int k=IgnoreNum;k>0;k--) { int minId=fpMap.get(new
		 * Pair<Integer,Integer>(firstid,curid)); recoveredCurTraj.add(curid);
		 * firstid=curid; curid=minId; } recoveredCurTraj.add(curid);
		 * firstid=curid; } }
		 * 
		 * recoveredTrajList.add(recoveredCurTraj); }
		 */
		// super.outputRecoveredTraj(recoveredTrajList, "FPTC_De.csv");

		/* Query on decompressed trajectory, random choose a point */
		int idx = (int) (Math.random() * et.temporalPoints.size());
		/* When at query */
		int pid = et.spatialPoints.get(idx);
		// super.whenAt(et, pid, 0);
		/* Where at query */
		int time = et.temporalPoints.get(idx);
		// super.whereAt(et, time, 15);
	}
}
