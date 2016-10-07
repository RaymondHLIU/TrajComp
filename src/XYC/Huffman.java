package XYC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class Huffman {

	// alphabet size of extended ASCII
	private static final int R = 256;
	private BinaryIn bIn;
	private BinaryOut bOutHFTree;
	private BinaryOut bOutContent;
	private BufferedWriter bw;
	private String indexFile;

	private RandomAccessFile raf;

	private HashMap<Long, Pair<Long, Integer>> offsetMap = new HashMap<Long, Pair<Long, Integer>>();
	// private long tableOffSet;

	private Node expandRoot = null;

	public Huffman(String inFile, String outHFFile, String outContent,
			String outIndex) throws IOException {
		indexFile = outIndex;
		try {
			bIn = new BinaryIn(new FileInputStream(new File(inFile)));
			bOutHFTree = new BinaryOut(
					new FileOutputStream(new File(outHFFile)));
			bOutContent = new BinaryOut(new FileOutputStream(new File(
					outContent)));
			bw = new BufferedWriter(new FileWriter(outIndex));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Huffman(String tireFile, String contentFile) throws IOException // for
																			// expand
																			// reading
																			// tire
	{
		try {
			bIn = new BinaryIn(new FileInputStream(new File(tireFile)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// need to create a flow to contentFile
		this.expandRoot = readTrie();
		raf = new RandomAccessFile(contentFile, "r");
	}

	// Huffman trie node
	private static class Node implements Comparable<Node> {
		private final char ch;
		private final int freq;
		private final Node left, right;

		Node(char ch, int freq, Node left, Node right) {
			this.ch = ch;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}

		// is the node a leaf node?
		private boolean isLeaf() {
			assert (left == null && right == null)
					|| (left != null && right != null);
			return (left == null && right == null);
		}

		// compare, based on frequency
		@Override
		public int compareTo(Node that) {
			return this.freq - that.freq;
		}
	}

	// compress bytes from standard input and write to standard output
	public void compress(List<Long> tid) throws IOException {
		// read the input
		String s = bIn.readString();
		char[] input = s.toCharArray();

		// tabulate frequency counts
		int[] freq = new int[R];
		for (int i = 0; i < input.length; i++)
			freq[input[i]]++;

		// build Huffman trie
		Node root = buildTrie(freq);

		// build code table
		String[] st = new String[R];
		buildCode(st, root, "");

		// print trie for decoder
		writeTrie(root);

		// print number of bytes in original uncompressed message
		bOutHFTree.write(input.length);

		int tidPos = 1;
		long bitOffset = 0;
		int returnTimes = 0;
		int length = 0;

		offsetMap.put(tid.get(0), new Pair<Long, Integer>(bitOffset, -1));

		// use Huffman code to encode input
		for (int i = 0; i < input.length; i++) {

			String code = st[input[i]];
			for (int j = 0; j < code.length(); j++) {
				bitOffset++;
				length++;
				if (code.charAt(j) == '0') {
					bOutContent.write(false);
				} else if (code.charAt(j) == '1') {
					bOutContent.write(true);
				} else
					throw new IllegalStateException("Illegal state");
			}

			if (input[i] == '\n') {
				if (returnTimes == 1) {
					returnTimes = 0;
					if (tidPos < tid.size()) {
						offsetMap.get(tid.get(tidPos - 1)).setSecond(length);
						offsetMap.put(tid.get(tidPos), new Pair<Long, Integer>(
								bitOffset, -1));
						tidPos++;
						length = 0;
					} else {
						// tableOffSet=bitOffset;
						offsetMap.get(tid.get(tidPos - 1)).setSecond(length);
					}
				} else
					returnTimes++;
			}
		}

		for (Entry<Long, Pair<Long, Integer>> en : offsetMap.entrySet()) {
			bw.write(en.getKey() + "," + en.getValue().getFirst() + ","
					+ en.getValue().getSecond() + "\n");
		}
		// bw.write(tableOffSet+"\n");

		// close output stream
		bOutHFTree.close();
		bOutContent.close();
		bw.close();

		HuffmanAll hfa = new HuffmanAll(indexFile, indexFile + ".Comp");
		hfa.compress();
	}

	// build the Huffman trie given frequencies
	private static Node buildTrie(int[] freq) {

		// initialze priority queue with singleton trees
		MinPQ<Node> pq = new MinPQ<Node>();
		for (char i = 0; i < R; i++)
			if (freq[i] > 0)
				pq.insert(new Node(i, freq[i], null, null));

		// merge two smallest trees
		while (pq.size() > 1) {
			Node left = pq.delMin();
			Node right = pq.delMin();
			Node parent = new Node('\0', left.freq + right.freq, left, right);
			pq.insert(parent);
		}
		return pq.delMin();
	}

	// write bitstring-encoded trie to standard output
	private void writeTrie(Node x) {
		if (x.isLeaf()) {
			bOutHFTree.write(true);
			bOutHFTree.write(x.ch, 8);
			return;
		}
		bOutHFTree.write(false);
		writeTrie(x.left);
		writeTrie(x.right);
	}

	// make a lookup table from symbols and their encodings
	private static void buildCode(String[] st, Node x, String s) {
		if (!x.isLeaf()) {
			buildCode(st, x.left, s + '0');
			buildCode(st, x.right, s + '1');
		} else {
			st[x.ch] = s;
		}
	}

	public void expandTrie() {
		Node root = readTrie();
		expandRoot = root;
		int length = bIn.readInt();
	}

	public ExpandTraj getPPartialExpTraj(Long offset, int length)
			throws IOException {
		ExpandTraj et = new ExpandTraj();
		// read file and recover according to the tire

		long offSetInBytes = offset / 8;
		int bitOS = (int) (offset - (offSetInBytes * 8));
		int lengthInBytes = length / 8 + 2;

		byte[] content = new byte[lengthInBytes];

		raf.seek(offSetInBytes);
		raf.read(content, 0, lengthInBytes);

		String curString = new String();

		int curByte = (content[0]);
		int pos = 0;
		int count = 0;
		for (int i = 0; i < bitOS; i++) {
			count++;
			curByte = curByte << 1;
		}
		Node x = expandRoot;

		while (length > 0) {
			if (count == 8) {
				count = 0;
				pos++;

				curByte = (content[pos]);
			}

			boolean curBit = ((curByte & 128) == 128);

			curByte = curByte << 1;
			count++;

			if (curBit)
				x = x.right;
			else
				x = x.left;

			if (x.isLeaf()) {
				curString += x.ch;
				x = expandRoot;
			}

			length--;
		}
		String[] ss = curString.split("\n");
		String[] spaS = ss[0].split(",");
		String[] temS = ss[1].split(",");

		for (int i = 0; i < spaS.length; i++)
			et.spatialPoints.add(Integer.parseInt(spaS[i]));
		for (int i = 0; i < temS.length; i++)
			et.temporalPoints.add(Integer.parseInt(temS[i]));
		return et;
	}

	/*
	 * // expand Huffman-encoded input from standard input and write to standard
	 * output public void expand() {
	 * 
	 * // read in Huffman trie from input stream Node root = readTrie();
	 * 
	 * // number of bytes to write int length = bIn.readInt();
	 * 
	 * // decode using the Huffman trie for (int i = 0; i < length; i++) { Node
	 * x = root; while (!x.isLeaf()) { boolean bit = bIn.readBoolean(); if (bit)
	 * x = x.right; else x = x.left; } bOut.write(x.ch, 8); } bOut.close(); }
	 */

	private Node readTrie() {
		boolean isLeaf = bIn.readBoolean();
		if (isLeaf) {
			return new Node(bIn.readChar(), -1, null, null);
		} else {
			return new Node('\0', -1, readTrie(), readTrie());
		}
	}

}