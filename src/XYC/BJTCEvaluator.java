package XYC;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BJTCEvaluator {

	private List<Trajectory> listTraj;
	private List<Long> tid;
	// private Map map;
	private HashMap<Integer, Pair<String, String>> pointIdMap;
	private TrajCompressor[] tc = new TrajCompressor[1];

	public BJTCEvaluator(String dataFile, String mapFile) throws IOException {
		BJDataLoader dl = new BJDataLoader(dataFile, mapFile, true); // true or
																		// false
																		// for
																		// need
																		// dij
		pointIdMap = dl.getPointIdMap();
		listTraj = dl.getTraj();
		tid = dl.getTid();
		System.out.println("Loading statistics:");
		// System.out.println(listTraj.size());
		// System.out.println(listTraj);
		// System.out.println(tid);
		// System.out.println(pointIdMap);
		// System.out.println(dl.getDij());
		/*
		 * for(Trajectory t: this.listTraj){
		 * System.out.println(t.getList().size()); }
		 */

		/*
		 * System.out.println("FSTC Initialization");
		 * System.out.println("Start: " + new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 * tc[0] = new FSTC("FSTC", listTraj, pointIdMap, dl.getDij(), tid);
		 * System.out.println("End: " + new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 */

		
		 /*System.out.println("FPTC Initialization");
		 System.out.println("Start: " + new
		 SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 tc[0] = new FPTC("FPTC", listTraj, dl.getDij(), tid);
		 System.out.println("End: " + new
		 SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 

		
		System.out.println("RSPTC Initialization");
		System.out.println("Start: " + new
				SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		tc[1] = new RSPTC("RSPTC", listTraj, 6, tid); // the int means pattern length 
		System.out.println("End: " + new
		 	SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 

		
		System.out.println("FLTC Initialization");
		System.out.println("Start: " + new
				SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		tc[2] = new FLTC("FLTC", listTraj, dl.getMap(), tid);
		System.out.println("End: " + new
				SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));*/
		 

		System.out.println("MFPTC Initialization");
		System.out.println("Start: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		tc[0] = new MFPTC("MFPTC", listTraj, tid, 10);
		System.out.println("End: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));
		 
		 /*System.out.println("HUFTC Initialization");
		 System.out.println("Start: " + new
		 SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));
		 tc[3] = new HUFTC("HUFTC", listTraj, dl.getMap(), tid);
		 System.out.println("End: " + new
		 SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS") .format(new Date()));*/

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
		System.out.println("Trajecotry Compression Done.");
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

		// final String dataDir="data/BJTraj";//"data/BJTraj";
		/* This dir for binary reader */
		final String dataDir = "data/SPLinkedFile_Beijing";
		/* This fir for hao linked traj */
		//final String dataDir = "data/hao_beijing";
		/* This dir for plain reader */
		// final String dataDir = "data/SPLinked_Beijing_Text_Vertex";
		final String mapDir = "data/BJMap";

		BJTCEvaluator tce = new BJTCEvaluator(dataDir, mapDir);
		tce.execute();

	}
}
