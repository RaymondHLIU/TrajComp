package XYC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SPTC extends TrajCompressor {
	// private HashMap<Pair<Integer, Integer>, List<Integer>> spMap;
	private List<Trajectory> trajList = new ArrayList<Trajectory>();
	private List<Trajectory> tctrajList = new ArrayList<Trajectory>();
	private spDijkstra dij;

	public SPTC(String tcName, List<Trajectory> trajList, spDijkstra dij,
			List<Long> tid) throws IOException {
		super(tcName, tid);
		this.trajList = trajList;

		super.setTrajList(this.trajList);
		// this.spMap=dij.getSpMap();
		this.dij = dij;
	}

	@Override
	public void encode() throws IOException {
		try {
			super.encodeTime();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int count = 0;
		for (Trajectory traj : trajList) {
			count++;
			if (count % 100 == 0)
				System.out.println(count + "\t" + trajList.size());
			Trajectory tctraj = new Trajectory();
			// tctraj.setStartTime(traj.getStartTime());
			// tctraj.setEndTime(traj.getEndTime());
			// tctraj.setValid(traj.isValid());
			tctraj.setId(traj.getId());
			int N = traj.getList().size();
			if (N < 2)// || !checkConnectivity(traj))
				continue;
			tctraj.getList().add(traj.getList().get(0));

			Trajectory subtraj = new Trajectory();
			subtraj.getList().add(traj.getList().get(0));
			subtraj.getList().add(traj.getList().get(1));
			for (int i = 1; i < N - 1; i++) {
				subtraj.getList().add(traj.getList().get(i + 1));
				int M = subtraj.getList().size();
				// List<Integer> tempSubTraj = spMap.get(new
				// Pair(subtraj.getList().get(0).getPid(),subtraj.getList().get(M-1).getPid()));
				List<Integer> tempSubTraj = dij.getShortestPath(subtraj
						.getList().get(0).getPid(), subtraj.getList()
						.get(M - 1).getPid());
				if (!tempSubTraj.equals(subtraj.getIdList())) {
					tctraj.getList().add(traj.getList().get(i));
					subtraj = new Trajectory();
					subtraj.getList().add(traj.getList().get(i));
					subtraj.getList().add(traj.getList().get(i + 1));
				}
			}
			tctraj.getList().add(traj.getList().get(N - 1));
			tctrajList.add(tctraj);
		}
		super.setTctrajList(tctrajList);
		super.outputData("SPTC", false);

		super.HuffmanComp("SPTC", "SPTCTree.Comp", "SPTCContent.Comp",
				"SPTCIndex");
		// super.HuffmanComp("SPTC", "SPTC.Comp");
	}

	public void decode() {

	}
}
