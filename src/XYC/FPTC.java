package XYC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FPTC extends TrajCompressor {
	private List<Trajectory> trajList = new ArrayList<Trajectory>();
	private List<Trajectory> tctrajList = new ArrayList<Trajectory>();
	private HashMap<Pair<Integer, Integer>, Integer> fpMap;

	public FPTC(String tcName, List<Trajectory> trajList, Dijkstra dij,
			List<Long> tid) throws IOException {
		super(tcName, tid);
		this.trajList = trajList;

		super.setTrajList(this.trajList);
		fpMap = dij.getFpMap();

		/*
		 * BufferedWriter bw = new BufferedWriter(new
		 * FileWriter("patternCount.csv"));
		 * for(Entry<Pair<Integer,Integer>,Integer> en:fpMap.entrySet()) {
		 * bw.write
		 * (en.getKey().getFirst()+","+en.getKey().getSecond()+","+en.getValue
		 * ()+"\r\n"); } bw.close();
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

		long pcount = 0;
		long unconnectpcount = 0;
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
			TrajPoint firstP = traj.getList().get(0);
			firstP.setNumFollowIgnore(0);
			TrajPoint secondP = traj.getList().get(1);
			// secondP.setNumFollowIgnore(0);
			tctraj.getList().add(firstP);
			tctraj.getList().add(secondP);

			int ignore = 0;
			for (int i = 2; i < N; i++) {
				TrajPoint curP = traj.getList().get(i);
				// System.out.println(String.valueOf(firstP.getPid()) + " " +
				// String.valueOf(secondP.getPid()));
				Pair<Integer, Integer> curPair = new Pair<Integer, Integer>(
						firstP.getPid(), secondP.getPid());

				// avoid compute points on same path
				pcount++;
				if (!fpMap.containsKey(curPair)) {
					// System.out.println(curPair.getFirst() + ", " +
					// curPair.getSecond());
					unconnectpcount++;
				}
				if (!fpMap.containsKey(curPair)
						|| fpMap.get(curPair) != curP.getPid()) {
					tctraj.getList().get(tctraj.getList().size() - 1)
							.setNumFollowIgnore(ignore);
					tctraj.getList().add(curP);
					ignore = 0;
				} else
					ignore++;
				firstP = secondP;
				secondP = curP;
			}
			tctraj.getList().get(tctraj.getList().size() - 1)
					.setNumFollowIgnore(ignore);
			tctrajList.add(tctraj);
		}
		super.setTctrajList(tctrajList);
		super.outputData("FPTC", true);

		/*super.HuffmanComp("FPTC", "FPTCTree.Comp", "FPTCContent.Comp",
				"FPTCIndex");
		System.out.println("unconnected point "
				+ String.valueOf(unconnectpcount));*/
		// super.HuffmanComp("FPTC", "FPTC.Comp");
	}

	public void decode() {

	}
}
