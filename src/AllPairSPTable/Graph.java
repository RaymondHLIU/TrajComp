package AllPairSPTable;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

public class Graph {

	int nodeSize = 0;
	int[] table[];

	// private int avail_node_size = 42000;
	private int avail_node_size = 16800;
	public boolean finished = false;
	public int start;
	public int end;

	public Graph(int nodesize, String filename) {
		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(filename)));
			this.nodeSize = nodesize;
			table = new int[this.nodeSize][];
			for (int i = 0; i < this.nodeSize; i++)
				table[i] = new int[this.nodeSize];
			for (int i = 0; i < this.nodeSize; i++)
				for (int j = 0; j < this.nodeSize; j++)
					table[i][j] = in.readInt();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Graph(int nodesize) {
		this.nodeSize = nodesize;
		table = new int[avail_node_size][];
		for (int i = 0; i < avail_node_size; i++)
			table[i] = new int[this.nodeSize];
	}

	public void buildGraph(DataInputStream filename_in, int offset) {
		start = offset;
		end = (offset + this.avail_node_size) >= nodeSize ? nodeSize
				: (offset + this.avail_node_size);
		this.finished = (offset + this.avail_node_size) >= nodeSize ? true
				: false;
		try {
			for (int i = start; i < end; i++) {
				// if(i/1000 ==0)
				// System.out.println(String.valueOf(i) +
				// " SP entry loaded...");
				for (int j = 0; j < this.nodeSize; j++)
					table[i - start][j] = filename_in.readInt();
			}
			System.out.println("data stored");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
