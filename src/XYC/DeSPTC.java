package XYC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeSPTC// extends TrajDecompressor
{
	private List<ArrayList<Integer>> encodedTrajList;
	private List<ArrayList<Integer>> recoveredTrajList = new ArrayList<ArrayList<Integer>>();
	// private HashMap<Pair<Integer, Integer>, List<Integer>> spMap;
	private Dijkstra dij;

	public DeSPTC(String tdcname, String binaryFile, String encodedFile,
			Dijkstra dij) throws IOException {
		// super(tdcname);
		// super.HuffmanDecomp(binaryFile, encodedFile);
		// super.loadCompressedData(encodedFile);
		// this.spMap=dij.getSpMap();
		this.dij = dij;
		// this.encodedTrajList=super.getEncodedTrajList();
	}

	public void decode() throws IOException {
		for (int i = 0; i < encodedTrajList.size(); i++) {
			ArrayList<Integer> recoveredCurTraj = new ArrayList<Integer>();
			List<Integer> curTraj = encodedTrajList.get(i);

			int firstId = curTraj.get(0);
			recoveredCurTraj.add(firstId);

			for (int j = 1; j < curTraj.size(); j++) {
				// Pair<Integer,Integer> lookupKey=new
				// Pair<Integer,Integer>(firstId,curTraj.get(j));
				// List<Integer> path=spMap.get(lookupKey);
				List<Integer> path = dij.getShortestPath(firstId,
						curTraj.get(j));
				for (int k = 1; k < path.size(); k++)
					recoveredCurTraj.add(path.get(k));
				firstId = path.get(path.size() - 1);
			}

			recoveredTrajList.add(recoveredCurTraj);
		}
		// super.outputRecoveredTraj(recoveredTrajList, "SPTC_De.csv");

	}
}
