package XYC;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class TrajCompressor {

	private List<Long> tidList;
	private List<Trajectory> trajList;
	private List<Trajectory> tctrajList;
	private final String tcName;
	private List<ArrayList<Integer>> encodedTrajTimeList = new ArrayList<ArrayList<Integer>>();

	public void encodeTime() throws IOException {
		System.out.println("Encoding Time...");
		for (Trajectory traj : trajList) {
			ArrayList<Integer> preEncodeTimeList = new ArrayList<Integer>();
			for (TrajPoint tp : traj.getList()) {
				String sTime = tp.getTime();
				// System.out.println(sTime);
				// int intTime=Statistic.getIntTime(sTime);
				int intTime = Integer.parseInt(sTime);
				preEncodeTimeList.add(intTime);
				// System.out.print(sTime + " ");
			}
			VByte vb = new VByte();
			ArrayList<Integer> postEncodeTimeList = (ArrayList<Integer>) vb
					.compress(preEncodeTimeList);
			encodedTrajTimeList.add(postEncodeTimeList);
		}
		System.out.println("Encode Time Finish.");

	}

	public List<ArrayList<Integer>> getEncodedTrajTimeList() {
		return encodedTrajTimeList;
	}

	public void setEncodedTrajTimeList(
			List<ArrayList<Integer>> encodedTrajTimeList) {
		this.encodedTrajTimeList = encodedTrajTimeList;
	}

	public void setTrajList(List<Trajectory> trajList) {
		this.trajList = trajList;
	}

	public void setTctrajList(List<Trajectory> tctrajList) {
		this.tctrajList = tctrajList;
	}

	public void HuffmanComp(String inFile, String outHFFile, String outContent,
			String outIndex) throws IOException {
		Huffman hufComp = new Huffman(inFile, outHFFile, outContent, outIndex); // compress
		hufComp.compress(tidList);
	}

	public List<Long> getTidMap() {
		return tidList;
	}

	public TrajCompressor(String tcName, List<Long> tidList) {
		this.tcName = tcName;
		this.tidList = tidList;
	}

	public String getTCName() {
		return tcName;
	}

	public void outputData(String filename, boolean recordIgnore)
			throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

		// System.out.println("Writing output...");
		// System.out.println(tctrajList.size());
		for (int i = 0; i < tctrajList.size(); i++) {
			// System.out.println(tctrajList.get(i).getList().size());
			if (tctrajList.get(i).getList().size() > 0) {
				int j = 0;
				for (; j < tctrajList.get(i).getList().size() - 1; j++) {
					TrajPoint tp = tctrajList.get(i).getList().get(j);
					if (recordIgnore)
						/*
						 * if(tp.getNumFollowIgnore() == 0)
						 * bw.write(tp.getPid()+","); else
						 */
						bw.write(tp.getPid() + "," + tp.getNumFollowIgnore()
								+ ",");
					else
						bw.write(tp.getPid() + ",");
				}
				TrajPoint lasttp = tctrajList.get(i).getList().get(j);
				if (recordIgnore)
					bw.write(lasttp.getPid() + ","
							+ lasttp.getNumFollowIgnore() + "\n");
				else
					bw.write(lasttp.getPid() + "\n");

				for (j = 0; j < encodedTrajTimeList.get(i).size() - 1; j++) {
					bw.write(encodedTrajTimeList.get(i).get(j) + ",");
					// System.out.print(encodedTrajTimeList.get(i).get(j) +
					// " ");
				}
				bw.write(encodedTrajTimeList.get(i).get(
						encodedTrajTimeList.get(i).size() - 1)
						+ "\n");

			}
		}
		bw.close();
	}

	public abstract void encode() throws IOException;

	// public abstract void decode();

	public List<Trajectory> getTrajList() {
		return trajList;
	}

	public List<Trajectory> getTCTrajList() {
		return tctrajList;
	}
}
