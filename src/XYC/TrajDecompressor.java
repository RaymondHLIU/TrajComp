package XYC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class TrajDecompressor {
	private final String tdcName;
	// private HashMap<Pair<String,String>,Integer> pointIdMap=new
	// HashMap<Pair<String,String>,Integer>();
	private List<ArrayList<Integer>> encodedTrajList;
	private List<ArrayList<String>> recoveredTimeList;
	private String followerLine = null;
	private BufferedWriter outputBw;
	private List<ExpandTraj> uncompressedList;

	private Huffman hfman;

	private HashMap<Long, Pair<Long, Integer>> offsetMap = new HashMap<Long, Pair<Long, Integer>>();

	// public HashMap<Pair<String,String>,Integer> getPointIdMap()
	// {
	// return pointIdMap;
	// }

	public void closeOutput() throws IOException {
		outputBw.close();
	}

	/*public void outputET(ExpandTraj et) throws IOException {
		outputBw.write(et.id + "\r\n");
		int count = et.spatialPoints.size() > et.expandTime.size() ? et.expandTime
				.size() : et.spatialPoints.size();
		for (int i = 0; i < count; i++) {
			outputBw.write(et.spatialPoints.get(i) + "\t"
					+ et.expandTime.get(i) + "\r\n");
		}
	}
*/
	public void outputET(ExpandTraj et) throws IOException {
		outputBw.write(et.id + "\r\n");
		int count = et.spatialPoints.size();
		for (int i = 0; i < count; i++) {
			outputBw.write(et.spatialPoints.get(i)+"\r\n");
		}
	}

	
	public String getName() {
		return this.tdcName;
	}

	public TrajDecompressor(String tdcName, String indexFile, String tireFile,
			String contentFile, String outputFile) throws IOException {
		this.tdcName = tdcName;
		try {
			this.HuffmanDecompIndex(indexFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.outputBw = new BufferedWriter(new FileWriter(outputFile));
		hfman = new Huffman(tireFile, contentFile);
	}

	public ExpandTraj getPartialExpandTraj(Long trajId) throws IOException {
		Pair<Long, Integer> curPair = offsetMap.get(trajId);
		ExpandTraj et = hfman.getPPartialExpTraj(curPair.getFirst(),
				curPair.getSecond());
		et.temporalPoints = getRecoveredTime(et.temporalPoints);
		return et;
	}

	private List<Integer> getRecoveredTime(List<Integer> curEncodedTime)
			throws IOException {
		// System.out.println(curEncodedTime.get(10));
		VByte vb = new VByte();
		ArrayList<Integer> curRecoveredTime = (ArrayList<Integer>) vb
				.expand(curEncodedTime);
		// System.out.println(curRecoveredTime.get(10));
		return curRecoveredTime;
	}

	public List<ArrayList<Integer>> getEncodedTrajList() {
		return encodedTrajList;
	}

	public List<ArrayList<String>> getRecoveredTimeList() {
		return recoveredTimeList;
	}

	public String getFollowerLine() {
		return followerLine;
	}

	/*
	 * private void buildPointIdMap(String content) { String[]
	 * ss=content.split(","); int length=ss.length-1; for(int i=0;i<length;i+=3)
	 * { Pair<String,String> lnglat=new Pair<String,String>(ss[i],ss[i+1]); int
	 * id=Integer.parseInt(ss[i+2]); pointIdMap.put(lnglat, id); } }
	 */

	private List<ArrayList<String>> getRecoveredTimeList(List<String> trajInfo)
			throws IOException {
		List<ArrayList<String>> tmpRecoverdTimeList = new ArrayList<ArrayList<String>>();
		List<ArrayList<Integer>> encodedTimeList = new ArrayList<ArrayList<Integer>>();
		for (int i = 1; i < trajInfo.size(); i += 2) {
			String[] ss = trajInfo.get(i).split(",");
			List<Integer> curEncodedTime = new ArrayList<Integer>();
			for (int j = 0; j < ss.length; j++)
				curEncodedTime.add(Integer.parseInt(ss[j]));
			VByte vb = new VByte();
			ArrayList<Integer> curRecoveredTime = (ArrayList<Integer>) vb
					.expand(curEncodedTime);
			ArrayList<String> curRecoveredStringTime = new ArrayList<String>();
			for (int j = 0; j < curRecoveredTime.size(); j++)
				curRecoveredStringTime.add(String.valueOf(curRecoveredTime
						.get(j)));
			// curRecoveredStringTime.add(Statistic
			// .getStringTime(curRecoveredTime.get(j)));
			tmpRecoverdTimeList.add(curRecoveredStringTime);
		}
		return tmpRecoverdTimeList;
	}

	private List<ArrayList<Integer>> getEncodedTraj(List<String> trajInfo) {
		List<ArrayList<Integer>> tmpEncodedTraj = new ArrayList<ArrayList<Integer>>();
		int length = trajInfo.size();
		if (trajInfo.size() % 2 != 0)
			length--;
		for (int i = 0; i < length; i += 2) {
			String[] ss = trajInfo.get(i).split(",");
			ArrayList<Integer> curEncodedTraj = new ArrayList<Integer>();
			for (int j = 0; j < ss.length; j++)
				curEncodedTraj.add(Integer.parseInt(ss[j]));
			tmpEncodedTraj.add(curEncodedTraj);
		}
		return tmpEncodedTraj;
	}

	public void loadCompressedData(String filename) throws IOException {
		List<String> tmpTrajInfo = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = br.readLine();
		// buildPointIdMap(line);
		// line=br.readLine();
		while (line != null) {
			tmpTrajInfo.add(line);
			line = br.readLine();
		}
		br.close();
		recoveredTimeList = getRecoveredTimeList(tmpTrajInfo);
		encodedTrajList = getEncodedTraj(tmpTrajInfo);
		if (tmpTrajInfo.size() % 2 != 0)
			followerLine = tmpTrajInfo.get(tmpTrajInfo.size() - 1);
	}

	public void outputRecoveredTraj(List<ArrayList<Integer>> recoveredTrajList,
			String filename) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
		for (int i = 0; i < recoveredTrajList.size(); i++) {
			for (int j = 0; j < recoveredTrajList.get(i).size() - 1; j++) {
				// Pair<String,String>
				// pointLngLat=getPointLngLat(recoveredTrajList.get(i).get(j));
				// bw.write(pointLngLat.getFirst()+","+pointLngLat.getSecond()+","+recoveredTimeList.get(i).get(j)+",");
				bw.write(recoveredTrajList.get(i).get(j) + ","
						+ recoveredTimeList.get(i).get(j) + ",");
			}
			// Pair<String,String>
			// lastPointLngLat=getPointLngLat(recoveredTrajList.get(i).get(recoveredTrajList.get(i).size()-1));
			// bw.write(lastPointLngLat.getFirst()+","+lastPointLngLat.getSecond()+","+recoveredTimeList.get(i).get(recoveredTimeList.get(i).size()-1)+"\r\n");
			bw.write(recoveredTrajList.get(i).get(
					recoveredTrajList.get(i).size() - 1)
					+ ","
					+ recoveredTimeList.get(i).get(
							recoveredTimeList.get(i).size() - 1) + "\r\n");
		}
		bw.close();
	}

	/*
	 * public Pair<String,String> getPointLngLat(int pointId) {
	 * for(Entry<Pair<String, String>, Integer> en : pointIdMap.entrySet()) {
	 * if(pointId==en.getValue()) return en.getKey(); } return null; }
	 */

	private void HuffmanDecompIndex(String inFile) throws IOException {
		HuffmanAll huf = new HuffmanAll(inFile, "temp");
		huf.expand();
		BufferedReader br = new BufferedReader(new FileReader("temp"));
		String line = br.readLine();
		while (line != null) {
			String[] ss = line.split(",");
			offsetMap.put(
					Long.parseLong(ss[0]),
					new Pair<Long, Integer>(Long.parseLong(ss[1]), Integer
							.parseInt(ss[2])));
			line = br.readLine();
		}
		br.close();

		// Huffman hufDecomp=new Huffman(inFile,outFile);
		// hufDecomp.expand();
	}

	public abstract void decode(long id) throws IOException;;

	/*
	 * We implement three queries by using binary search
	 */

	public int whereAt(ExpandTraj et, int midTime, int devation)
			throws IOException {
		List<Integer> tpoints = et.temporalPoints;
		int len = tpoints.size();
		int idx = findWhere(tpoints, midTime, devation, 0, len);

		return et.spatialPoints.get(idx);

	}

	public int findWhere(List<Integer> tp, int midTime, int devation,
			int startPos, int endPos) {
		int idx = (endPos + startPos) / 2;

		if ((endPos - startPos <= 1) || tp.get(idx) >= midTime - devation
				&& tp.get(idx) <= midTime + devation)
			return idx;
		if (tp.get(idx) < (midTime - devation)) {
			return findWhere(tp, midTime, devation, idx, endPos);
		} else {
			return findWhere(tp, midTime, devation, startPos, idx);
		}
	}

	public int whenAt(ExpandTraj et, int pid, double devation)
			throws IOException {
		List<Integer> spoints = et.spatialPoints;
		int len = spoints.size();
		int idx = findWhen(spoints, pid, 0, len);

		return et.temporalPoints.get(idx);
	}

	public int findWhen(List<Integer> sp, int pid, double devation, int len) {
		for (int i = 0; i < len; i++)
			if (sp.get(i) == pid)
				return i;
		return -1;
	}

	public void range(List<ExpandTraj> uncompressedList, double upperRight,
			double lowerleft) throws IOException {

	}

}
