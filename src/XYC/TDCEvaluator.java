package XYC;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TDCEvaluator {

	private TrajDecompressor[] tdc = new TrajDecompressor[1];
	private List<Long> toDecodeTrajList;

	private List<Long> randomChooseTraj(List<Long> tid, int num) {
		List<Long> resTrajList = new ArrayList<Long>();
		Collections.shuffle(tid);
		for (int i = 0; i < num; i++)
			resTrajList.add(tid.get(i));
		return resTrajList;
	}

	public TDCEvaluator(String dataFile, String mapFile, int randomNum)
			throws IOException {
		DataLoader dl = new DataLoader(dataFile, mapFile, true);

		toDecodeTrajList = randomChooseTraj(dl.getTid(), randomNum);

		System.out.println("FSTC Initialization");
		System.out.println("Start: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		/*
		 * tdc[0] = new DeFSTC("DeFSTC", "FSTCIndex.Comp", "FSTCTree.Comp",
		 * "FSTCContent.Comp", "FSTC_FollowingTable.Comp", "FSTC.Exp");
		 * System.out.println("End: " + new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 * 
		 * System.out.println("FPTC Initialization");
		 * System.out.println("Start: " + new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 * tdc[1] = new DeFPTC("DeFPTC", "FPTCIndex.Comp", "FPTCTree.Comp",
		 * "FPTCContent.Comp", dl.getDij(), "FPTC.Exp");
		 * System.out.println("End: " + new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 * 
		 * System.out.println("RSPTC Initialization");
		 * System.out.println("Start: " + new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 * tdc[2] = new DeRSPTC("DeRSPTC", "RSPTCIndex.Comp", "RSPTCTree.Comp",
		 * "RSPTCContent.Comp", "RSPTCPattern.Comp", "RSPTC.Exp");
		 * System.out.println("End: " + new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 */

		System.out.println("FLTC Initialization");
		System.out.println("Start: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		tdc[0] = new DeFLTC("DeFLTC", "FLTCIndex.Comp", "FLTCTree.Comp",
				"FLTCContent.Comp", "FLTC_FollowingTable.Comp", "FLTC.Exp");
		System.out.println("End: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));

	}

	public void execute() throws IOException {
		System.out.println("decoding...");
		for (TrajDecompressor curTDC : tdc) {
			System.out.println(curTDC.getName());
			System.out.println("Start: "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
							.format(new Date()));
			for (int i = 0; i < toDecodeTrajList.size(); i++)
				curTDC.decode(toDecodeTrajList.get(i));
			System.out.println("End: "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
							.format(new Date()));
			curTDC.closeOutput();
			curTDC = null;
		}
	}

	public static void main(String[] args) throws IOException {
		final String dataFile = "2007-11-06.dat.2";
		final String mapFile = "shanghai.map";
		final int randomNum = 100; // should be less than the total number of
									// trajectories

		TDCEvaluator tdce = new TDCEvaluator(dataFile, mapFile, randomNum);
		tdce.execute();
	}

}
