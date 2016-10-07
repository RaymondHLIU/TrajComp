package XYC;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BJTDCEvaluator {
	private TrajDecompressor[] tdc = new TrajDecompressor[1];
	private List<Long> toDecodeTrajList;

	private List<Long> randomChooseTraj(List<Long> tid, int num) {
		List<Long> resTrajList = new ArrayList<Long>();
		Collections.shuffle(tid);
		for (int i = 0; i < num; i++)
			resTrajList.add(tid.get(i));
		return resTrajList;
	}

	public BJTDCEvaluator(String dataFile, String mapFile, int randomNum)
			throws IOException {
		BJDataLoader dl = new BJDataLoader(dataFile, mapFile, true);
		// System.out.println(dl.getTid());
		toDecodeTrajList = randomChooseTraj(dl.getTid(), randomNum);

		/*
		 * System.out.println("FPTC Initialization");
		 * System.out.println("Start: " + new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 * tdc[0] = new DeFPTC("DeFPTC", "FPTCIndex.Comp", "FPTCTree.Comp",
		 * "FPTCContent.Comp", dl.getDij(), "FPTC.Exp");
		 * System.out.println("End: " + new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 */

		/*
		 * System.out.println("RSPTC Initialization");
		 * System.out.println("Start: " + new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 * tdc[0] = new DeRSPTC("DeRSPTC", "RSPTCIndex.Comp", "RSPTCTree.Comp",
		 * "RSPTCContent.Comp", "RSPTCPattern.Comp", "RSPTC.Exp");
		 * System.out.println("End: " + new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 */

		/*
		 * System.out.println("FLTC Initialization");
		 * System.out.println("Start: " + new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 * tdc[0] = new DeFLTC("DeFLTC", "FLTCIndex.Comp", "FLTCTree.Comp",
		 * "FLTCContent.Comp", "FLTC_FollowingTable.Comp", "FLTC.Exp");
		 * System.out.println("End: " + new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 */

		System.out.println("MFPTC Initialization");
		System.out.println("Start: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		tdc[0] = new DeMFPTC("MFPTC", "MFPTCIndex.Comp", "MFPTCTree.Comp",
				"MFPTCContent.Comp", "MFPTable.Comp", "MFPTC.Exp");
		System.out.println("End: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));

	}

	public void execute() throws IOException {
		System.out.println("decoding...");
		for (TrajDecompressor curTDC : tdc) {
			double maxTime = 0;
			double minTime = 100;
			double avgTime = 0;
			// System.out.println(curTDC.getName());
			System.out.println("Start: "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
							.format(new Date()));
			for (int i = 0; i < toDecodeTrajList.size(); i++) {
				// System.out.println(toDecodeTrajList.get(i));
				long startTime = System.currentTimeMillis();
				curTDC.decode(toDecodeTrajList.get(i));
				long endTime = System.currentTimeMillis();
				long t = endTime - startTime;
				if (t > maxTime)
					maxTime = t;
				if (t < minTime && t > 0.5)
					minTime = t;
				avgTime += t;
			}
			avgTime = 1.0 * avgTime / toDecodeTrajList.size();
			System.out.println("Overall Time: "
					+ (1.0 * avgTime * toDecodeTrajList.size() / 1000) + "s");
			System.out.println("Avg Time: " + (1.0 * avgTime / 1000) + "s");
			System.out.println("Max Time: " + (1.0 * maxTime / 1000) + "s");
			System.out.println("Min Time: " + (1.0 * minTime / 1000) + "s");
			System.out.println("End: "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
							.format(new Date()));
			curTDC.closeOutput();
			curTDC = null;
		}
	}

	public static void main(String[] args) throws IOException {

		final String dataDir = "data/SPLinkedFile_Beijing";
		/* This dir for plain reader */
		// final String dataDir = "data/SPLinked_Beijing_Text_vertex";
		final String mapDir = "data/BJMap";
		final int randomNum = 1; // should be less than the total number of
									// trajectories

		BJTDCEvaluator tdce = new BJTDCEvaluator(dataDir, mapDir, randomNum);
		tdce.execute();
	}

}
