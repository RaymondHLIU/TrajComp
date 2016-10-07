package XYC;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TCEvaluator {

	private List<Trajectory> listTraj;
	private List<Long> tid;
	// private Map map;
	private HashMap<Integer, Pair<String, String>> pointIdMap;
	private TrajCompressor[] tc = new TrajCompressor[4];

	public TCEvaluator(String dataFile, String mapFile) throws IOException {
		DataLoader dl = new DataLoader(dataFile, mapFile, true); // true or
																	// false for
																	// need dij
		pointIdMap = dl.getPointIdMap();
		listTraj = dl.getTraj();
		tid = dl.getTid();

		// tc[0]=new SPTC("SPTC",listTraj,dl.getDij(),tid);

		System.out.println("FSTC Initialization");
		System.out.println("Start: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		tc[0] = new FSTC("FSTC", listTraj, pointIdMap, dl.getDij(), tid);
		System.out.println("End: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));

		System.out.println("FPTC Initialization");
		System.out.println("Start: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		tc[1] = new FPTC("FPTC", listTraj, dl.getDij(), tid);
		System.out.println("End: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));

		System.out.println("RSPTC Initialization");
		System.out.println("Start: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		tc[2] = new RSPTC("RSPTC", listTraj, 3, tid); // the int means pattern
														// length
		System.out.println("End: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));

		System.out.println("FLTC Initialization");
		System.out.println("Start: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		tc[3] = new FLTC("FLTC", listTraj, dl.getMap(), tid);
		System.out.println("End: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));

	}

	public void execute() throws IOException {
		System.out.println("Encoding...");
		for (TrajCompressor curTC : tc) {
			System.out.println(curTC.getTCName());
			System.out.println("Start: "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
							.format(new Date()));
			curTC.encode();
			System.out.println("End: "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
							.format(new Date()));
			curTC = null;
		}
	}

	public void evaluateCompression() {
		File ori = new File("genTraj.csv");
		File sp = new File("SPTC");
		File fp = new File("FPTC");
		File fs = new File("FSTC");
		File hf = new File("Huff.Comp");

		File spHf = new File("SPTC.Comp");
		File fpHf = new File("FPTC.Comp");
		File fsHf = new File("FSTC.Comp");

		System.out.println("Original: " + ori.length());
		System.out.println("SPTC: " + sp.length());
		System.out.println("FPTC: " + fp.length());
		System.out.println("FSTC: " + fs.length());
		System.out.println("SPTC+Huf: " + spHf.length());
		System.out.println("FPTC+Huf: " + fpHf.length());
		System.out.println("FSTC+Huf: " + fsHf.length());
		System.out.println("Huff: " + hf.length());

	}

	public static void main(String[] args) throws IOException {

		final String dataFile = "2007-11-06.dat.2";
		final String mapFile = "shanghai.map";

		TCEvaluator tce = new TCEvaluator(dataFile, mapFile);
		tce.execute();

	}

}
