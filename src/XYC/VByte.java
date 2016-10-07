package XYC;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VByte {
	private List<Integer> listInts = new ArrayList<Integer>();
	private int buffer;
	private int N;
	private List<Integer> codedInts = new ArrayList<Integer>();
	private static final int binSize = 7;
	private List<Integer> compInts = new ArrayList<Integer>();
	private List<Integer> expInts = new ArrayList<Integer>();

	private void clearBuffer() {
		if (N == 0)
			return;
		if (N > 0)
			buffer <<= (8 - N);
		codedInts.add(buffer);
		N = 0;
		buffer = 0;
	}

	private void writeBit(boolean bit) {
		// add bit to buffer
		buffer <<= 1;
		if (bit)
			buffer |= 1;

		// if buffer is full (8 bits), write out as a single byte
		N++;
		if (N == 8)
			clearBuffer();
	}

	public void readInInts(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = br.readLine();
		br.close();
		String[] sInts = line.split(",");
		for (int i = 0; i < sInts.length; i++)
			listInts.add(Integer.parseInt(sInts[i]));
	}

	public void readInComp(String filename) throws IOException {
		FileInputStream fis = new FileInputStream(filename);
		BufferedInputStream in = new BufferedInputStream(fis);
		int b = in.read();
		while (b != -1) {
			compInts.add(b);
			b = in.read();

		}
		in.close();

	}

	public List<Integer> expand(List<Integer> inInt) throws IOException {
		compInts = inInt;
		expand();
		return expInts;
	}

	public List<Integer> compress(List<Integer> inInt) throws IOException {
		listInts = inInt;
		compress();
		return codedInts;
	}

	public void expand() {
		List<Integer> tmpExpInts = new ArrayList<Integer>();
		List<Integer> groupInts = new ArrayList<Integer>();
		for (int i = 0; i < compInts.size(); i++) {
			boolean done = false;
			int curInt = compInts.get(i);
			if (curInt % 2 == 0)
				done = true;
			curInt = curInt >> 1;
			groupInts.add(curInt);
			if (done) {
				if (groupInts.size() > 0) {
					int expInt = groupInts.get(0);
					for (int j = 1; j < groupInts.size(); j++) {
						expInt = expInt << 7;
						expInt = expInt | groupInts.get(j);
					}
					tmpExpInts.add(expInt);
					groupInts = new ArrayList<Integer>();

				}
			}
		}
		if (tmpExpInts.size() > 0) {
			expInts.add(tmpExpInts.get(0));
			for (int i = 1; i < tmpExpInts.size(); i++)
				expInts.add(tmpExpInts.get(i) + expInts.get(expInts.size() - 1));
		}
	}

	public void writeExp(String filename) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
		bw.write(expInts.get(0).toString());
		for (int i = 1; i < expInts.size(); i++)
			bw.write("," + expInts.get(i));
		bw.close();
	}

	public void writeComp(String filename) throws IOException {
		FileOutputStream fos = new FileOutputStream(filename);
		BufferedOutputStream osw = new BufferedOutputStream(fos);
		for (int i = 0; i < codedInts.size(); i++)
			osw.write(codedInts.get(i));
		osw.close();
	}

	public void compress() throws IOException {
		List<Integer> distListInts = new ArrayList<Integer>();
		if (listInts.size() > 0) {
			distListInts.add(listInts.get(0));
			for (int i = 1; i < listInts.size(); i++) {
				distListInts.add(listInts.get(i) - listInts.get(i - 1));
			}
		}

		for (int i = 0; i < distListInts.size(); i++) {
			int curInt = distListInts.get(i);
			String bCode = Integer.toBinaryString(curInt);
			char[] bCodeChar = bCode.toCharArray();
			boolean first = true;
			int preFillLength = binSize - bCodeChar.length % binSize;
			int pos = 0;
			int boundary = 0;
			while (true) {
				if (first) {
					for (int j = 0; j < preFillLength; j++)
						writeBit(false);
					boundary = binSize - preFillLength;
					first = false;
				} else
					boundary += binSize;
				for (; pos < boundary; pos++) {
					if (bCodeChar[pos] == '0')
						writeBit(false);
					else
						writeBit(true);
				}
				if (pos == bCodeChar.length) {
					writeBit(false);
					break;
				} else
					writeBit(true);
			}
		}
	}

	public static void main(String[] args) throws IOException {

		generateTest("test.txt", 100, 86400);

		VByte vb = new VByte();
		// compress: text -> binary
		vb.readInInts("test.txt");
		vb.compress();
		vb.writeComp("comp.txt");
		// expand: binary -> text
		vb.readInComp("comp.txt");
		vb.expand();
		vb.writeExp("exp.txt");

	}

	private static void generateTest(String filename, int randomRange,
			int upperBound) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
		Random r = new Random();
		int curInt = r.nextInt(randomRange);
		bw.write(Integer.toString(curInt));
		while (curInt < upperBound) {
			curInt += r.nextInt(randomRange);
			bw.write("," + curInt);
		}
		bw.close();
	}
}
