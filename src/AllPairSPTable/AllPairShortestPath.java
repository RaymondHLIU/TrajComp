package AllPairSPTable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import RWFiles.ReadFile;
import RWFiles.WriteFile;

public class AllPairShortestPath {
	private Node[] nodes;

	public Node[] getNodes() {
		return nodes;
	}

	private int noOfNodes;

	public int getNoOfNodes() {
		return noOfNodes;
	}

	private ArrayList<Edge> edges;

	public ArrayList<Edge> getEdges() {
		return edges;
	}

	private int noOfEdges;

	public int getNoOfEdges() {
		return noOfEdges;
	}

	private double[] shortLen;
	private int[] pre;
	private boolean[] visit;
	LinkedList<Node> sequence;

	/**
	 * Constructor that builds the whole graph from an Array of Edges
	 */
	public AllPairShortestPath(int nodeNumber, ArrayList<Edge> edges) {

		// The edges are passed in, so store them
		this.edges = edges;

		// Create all the nodes, ready to be updated with the edges
		this.noOfNodes = nodeNumber;

		System.out.println("All vertices: " + this.noOfNodes);

		this.nodes = new Node[this.noOfNodes];
		for (int n = 0; n < this.noOfNodes; n++) {
			this.nodes[n] = new Node(n);
		}

		// Add all the edges to the nodes. Each edge is added to 2 nodes (the
		// "to" and the "from")
		this.noOfEdges = edges.size();
		for (int edgeToAdd = 0; edgeToAdd < this.noOfEdges; edgeToAdd++) {
			this.nodes[edges.get(edgeToAdd).getFromNodeIndex()].getEdges().add(
					edges.get(edgeToAdd));
			this.nodes[edges.get(edgeToAdd).getToNodeIndex()].getEdges().add(
					edges.get(edgeToAdd));
		}

		System.out.println("All edges loaded.");

	}

	public void calculate(DataOutputStream binaryWriter) {
		shortLen = new double[this.noOfNodes];
		pre = new int[this.noOfNodes];
		visit = new boolean[this.noOfNodes];
		sequence = new LinkedList<Node>();
		for (int s = 0; s < this.noOfNodes; s++) {

			System.out.println(s);

			for (int j = 0; j < this.noOfNodes; j++)
				shortLen[j] = Double.MAX_VALUE;
			for (int j = 0; j < this.noOfNodes; j++)
				visit[j] = false;

			Node startNode = this.nodes[s];
			shortLen[s] = 0;
			pre[s] = -1;// not connected
			visit[s] = true;

			sequence.offer(startNode);

			while (!sequence.isEmpty()) {
				Node tmp = sequence.getFirst();
				for (int i = 0; i < tmp.getEdges().size(); i++) {
					Edge currentEdge = tmp.getEdges().get(i);
					if (shortLen[currentEdge.getNeighbourIndex(tmp.node_id)] > shortLen[tmp.node_id]
							+ currentEdge.getLength()) {
						shortLen[currentEdge.getNeighbourIndex(tmp.node_id)] = shortLen[tmp.node_id]
								+ currentEdge.getLength();
						pre[currentEdge.getNeighbourIndex(tmp.node_id)] = currentEdge.edge_id;
						if (!visit[currentEdge.getNeighbourIndex(tmp.node_id)]) {
							visit[currentEdge.getNeighbourIndex(tmp.node_id)] = true;
							sequence.offer(this.nodes[currentEdge
									.getNeighbourIndex(tmp.node_id)]);
						}
					}
				}
				visit[tmp.node_id] = false;
				sequence.poll();
			}

			try {

				for (int t = 0; t < this.noOfNodes; t++) {
					if (shortLen[t] == Double.MAX_VALUE) {
						binaryWriter.writeInt(-1);
					} else {
						binaryWriter.writeInt(pre[t]);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private Graph graph;

	public void restoreGraph(int node_size, String SPTable) {
		this.graph = new Graph(node_size, SPTable);
	}

	public LinkedList<Integer> getPath(int pre, int scc) {
		LinkedList<Integer> result = new LinkedList<Integer>();

		if (this.getEdges().get(pre).getToNodeIndex() == this.getEdges()
				.get(scc).getToNodeIndex()
				|| this.getEdges().get(pre).getToNodeIndex() == this.getEdges()
						.get(scc).getFromNodeIndex()
				|| this.getEdges().get(pre).getFromNodeIndex() == this
						.getEdges().get(scc).getToNodeIndex()
				|| this.getEdges().get(pre).getFromNodeIndex() == this
						.getEdges().get(scc).getFromNodeIndex())
			return result;

		int start_id = this.getEdges().get(pre).getToNodeIndex();
		int end_id = this.getEdges().get(scc).getFromNodeIndex();

		while (start_id != end_id && graph.table[start_id][end_id] != -1) {
			result.addFirst(graph.table[start_id][end_id]);
			end_id = this.edges.get(graph.table[start_id][end_id])
					.getNeighbourIndex(end_id);
		}
		if (result.size() > 0 && result.getFirst() == (pre))
			result.removeFirst();
		if (result.size() > 0 && result.getLast() == (scc))
			result.removeLast();
		return result;
	}

	public LinkedList<Integer> getPath_Beijing(int pre, int scc) {
		LinkedList<Integer> result = new LinkedList<Integer>();

		if (this.getEdges().get(pre - 1).getToNodeIndex() == this.getEdges()
				.get(scc - 1).getToNodeIndex()
				|| this.getEdges().get(pre - 1).getToNodeIndex() == this
						.getEdges().get(scc - 1).getFromNodeIndex()
				|| this.getEdges().get(pre - 1).getFromNodeIndex() == this
						.getEdges().get(scc - 1).getToNodeIndex()
				|| this.getEdges().get(pre - 1).getFromNodeIndex() == this
						.getEdges().get(scc - 1).getFromNodeIndex())
			return result;

		int start_id = this.getEdges().get(pre - 1).getToNodeIndex();
		int end_id = this.getEdges().get(scc - 1).getFromNodeIndex();

		if (start_id >= this.graph.start && start_id < this.graph.end) {
			while (start_id != end_id
					&& graph.table[start_id - this.graph.start][end_id] != -1) {
				result.addFirst(graph.table[start_id - this.graph.start][end_id]);
				end_id = this.edges.get(
						graph.table[start_id - this.graph.start][end_id] - 1)
						.getNeighbourIndex(end_id);
			}
			if (result.size() > 0 && result.getFirst() == (pre))
				result.removeFirst();
			if (result.size() > 0 && result.getLast() == (scc))
				result.removeLast();
			return result;
		} else
			return result;

	}

	public boolean getPre(int pre, int scc, int mid) {
		int start_id = this.getEdges().get(pre - 1).getToNodeIndex();
		int end_id = this.getEdges().get(scc - 1).getFromNodeIndex();
		if (start_id >= this.graph.start && start_id < this.graph.end) {
			if (graph.table[start_id - this.graph.start][end_id] == mid)
				return true;
		}
		end_id = this.getEdges().get(scc - 1).getToNodeIndex();
		if (start_id >= this.graph.start && start_id < this.graph.end) {
			if (graph.table[start_id - this.graph.start][end_id] == mid)
				return true;
		}

		start_id = this.getEdges().get(pre - 1).getFromNodeIndex();
		end_id = this.getEdges().get(scc - 1).getFromNodeIndex();
		if (start_id >= this.graph.start && start_id < this.graph.end) {
			if (graph.table[start_id - this.graph.start][end_id] == mid)
				return true;
		}
		end_id = this.getEdges().get(scc - 1).getToNodeIndex();
		if (start_id >= this.graph.start && start_id < this.graph.end) {
			if (graph.table[start_id - this.graph.start][end_id] == mid)
				return true;
		}
		return false;
	}

	public static void main(String[] args) {

		ArrayList<Edge> edges = new ArrayList<Edge>();
		int node_size = 0;
		try {
			BufferedReader br_connect = new BufferedReader(new FileReader(
					"/mnt/EBS/data/trajectory/BJMap/edges.txt"));
			BufferedReader br_nodes = new BufferedReader(new FileReader(
					"/mnt/EBS/data/trajectory/BJMap/vertices.txt"));
			BufferedReader br_length = new BufferedReader(new FileReader(
					"/mnt/EBS/data/trajectory/BJMap/geos_length.txt"));
			String line;
			while ((line = br_connect.readLine()) != null) {
				String[] edge_split = line.split(" ");
				edges.add(new Edge(Integer.parseInt(edge_split[1]) - 1, Integer
						.parseInt(edge_split[2]) - 1, Double
						.parseDouble(br_length.readLine()), Integer
						.parseInt(edge_split[0])));
			}
			br_connect.close();
			br_length.close();
			while ((line = br_nodes.readLine()) != null) {
				node_size++;
			}
			br_nodes.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		AllPairShortestPath ASP = new AllPairShortestPath(node_size, edges);
		/*
		 * ASP.Reform_trajectory("MapMatched_Beijing", "Cleaned_Beijing");
		 * ASP.SPCompelement_decomposed(node_size,
		 * "/mnt/EBS/data/trajectory/SPTable.txt",
		 * "/mnt/EBS/data/trajectory/Cleaned_Beijing",
		 * "/mnt/EBS/data/trajectory/SPLinkedFile_Beijing");
		 * ASP.get_Statistics(node_size, "/mnt/EBS/data/trajectory/SPTable.txt",
		 * "Cleaned_Beijing", "result.txt");
		 */

		
		 ASP.SPCompression_decomposed(node_size,
		 "/mnt/EBS/data/trajectory/SPTable.txt",
		 "/mnt/EBS/data/trajectory/SPLinkedFile_Beijing",
		 "sp_compressed.txt"); ASP.convertBinaryToString("sp_compressed.txt",
		 "sp_compressed_s.txt");
		 

		ASP.queryPath(node_size, "/mnt/EBS/data/trajectory/SPTable.txt",
				"/mnt/EBS/data/trajectory/SPCompressed_query",
				"/mnt/EBS/data/trajectory/SPCompressed_exp");

	}

	public void convertBinaryToString(String input, String output) {
		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(input)));
			WriteFile wf = new WriteFile(output);
			int size = in.readInt();
			wf.write("" + size);

			for (int i = 0; i < size; i++) {
				int in_size = in.readInt();
				wf.writeNo("" + in_size);
				for (int j = 0; j < in_size; j++) {
					int ele = in.readInt();
					wf.writeNo(" " + ele);
				}
				wf.write("");
			}

			in.close();
			wf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SPCompression_decomposed(int node_size, String SPTable,
			String inputDir, String outputFile) {
		long simpTime = 0;
		long loadTime = 0;
		long start_time = 0, end_time = 0;

		Map<String, LinkedList<Integer>> file_records = new HashMap<String, LinkedList<Integer>>();
		File file = new File(inputDir);
		File[] inner_files = file.listFiles();
		System.out.println(inputDir + " has " + inner_files.length + "files");
		try {
			for (int j = 0; j < inner_files.length; j++) {
				File linked_file = inner_files[j];
				LinkedList<Integer> records = new LinkedList<Integer>();
				DataInputStream in = new DataInputStream(
						new BufferedInputStream(
								new FileInputStream(linked_file)));
				int size = in.readInt();
				for (int i = 0; i < size; i++) {
					records.add(in.readInt());
				}
				in.close();
				file_records.put(linked_file.getName(), records);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("All trajectories loaded.");
		System.out.println("Start Simplification: "
				+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
						.format(new Date()));

		try {
			int offset = 0;
			this.graph = new Graph(node_size);
			DataInputStream in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(SPTable)));
			do {
				int count = 0;
				start_time = System.currentTimeMillis();
				this.graph.buildGraph(in, offset);
				end_time = System.currentTimeMillis();
				loadTime += end_time - start_time;
				System.out.println("graph loaded");
				offset = this.graph.end;

				start_time = System.currentTimeMillis();

				for (String traj : file_records.keySet()) {
					if (count++ % 100 == 0)
						System.out.println(count);
					LinkedList<Integer> source = file_records.get(traj);
					if (source.size() < 2)
						continue;

					int preEdge = source.get(0);
					for (int i = 2; i < source.size(); ++i) {
						if (!this.getPre(preEdge, source.get(i),
								source.get(i - 1))) {
							preEdge = source.get(i - 1);
						} else {
							source.remove(i - 1);
							i--;
						}
					}
				}

				end_time = System.currentTimeMillis();
				simpTime += (end_time - start_time);

				System.out.println("finished one round");
				System.out.println(start_time + " " + end_time + " " + simpTime
						+ " " + loadTime + " " + count);
			} while (!this.graph.finished);
			System.out.println("End Simplification: "
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
							.format(new Date()));
			in.close();

			int copy = 1;

			DataOutputStream objectOutputStream = new DataOutputStream(
					new FileOutputStream(outputFile));
			objectOutputStream.writeInt(file_records.size() * copy);
			for (String traj : file_records.keySet()) {
				LinkedList<Integer> records = file_records.get(traj);
				for (int i = 0; i < copy; i++) {
					objectOutputStream.writeInt(records.size());
					for (int t = 0; t < records.size(); t++)
						objectOutputStream.writeInt(records.get(t));
				}
			}
			objectOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// time += System.currentTimeMillis();

		System.out.println((simpTime * 1.0) / 1000);
	}

	/*
	 * Use same logic of SPComplement_decomposed.
	 */
	public void queryPath(int node_size, String SPTable, String inputDir,
			String outputDir) {
		System.out.println("Begin query path...");
		long time = 0;
		long start_time = 0, end_time = 0;

		Map<String, Double> rate = new HashMap<String, Double>();
		ReadFile rf = new ReadFile("File_statistics.txt");
		String line;
		while ((line = rf.readline()) != null) {
			String[] re = line.split("\t");
			double crit = Double.parseDouble(re[1]);
			if (crit < 7)
				rate.put(re[0], crit);
		}

		LinkedList<HashSet<Integer>> all_records = new LinkedList<HashSet<Integer>>();

		Map<String, LinkedList<Integer>> file_records = new HashMap<String, LinkedList<Integer>>();
		File file = new File(inputDir);
		File[] inner_files = file.listFiles();
		System.out.println("# of files " + inner_files.length);

		int count = 0;

		try {
			for (int j = 0; j < inner_files.length; j++) {
				File linked_file = inner_files[j];
				/*
				 * if (!rate.keySet().contains(linked_file.getName())) continue;
				 */
				LinkedList<Integer> records = new LinkedList<Integer>();
				BufferedReader in = new BufferedReader(new FileReader(
						linked_file));

				String[] sline = in.readLine().split(" ");
				for (int k = 0; k < sline.length; k++)
					records.add(Integer.parseInt(sline[k]));
				in.close();
				System.out.println(records.size());

				// if(records.size()<500)
				// continue;

				HashSet<Integer> records_set = new HashSet<Integer>(records);
				boolean none = true;
				for (HashSet<Integer> pre_traj : all_records) {
					if (this.join(pre_traj, records_set) > 0.2) {
						none = false;
						break;
					}
				}
				if (none) {
					all_records.add(records_set);
					file_records.put(linked_file.getName(), records);
					// System.out.println(file_records.size());

					count += records.size();
				}
			}
			System.out.println("selection finished");
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(count);
		System.out.println(file_records.size());

		try {
			int offset = 0;
			this.graph = new Graph(node_size);
			DataInputStream in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(SPTable)));
			do {
				this.graph.buildGraph(in, offset);
				System.out.println("graph loaded");
				offset = this.graph.end;
				for (String traj : file_records.keySet()) {
					start_time = System.currentTimeMillis();
					LinkedList<Integer> records = file_records.get(traj);
					System.out.println(start_time + " " + traj + " "
							+ records.size());
					for (int i = records.size() - 1; i > 0; i--) {
						records.addAll(i, this.getPath_Beijing(
								records.get(i - 1), records.get(i)));
					}
					end_time = System.currentTimeMillis();
					System.out.println(end_time);
					time += end_time - start_time;
				}
				System.out.println("finished one round " + time);
			} while (!this.graph.finished);
			in.close();
			System.out.println("Recover three file: " + time);

			for (String traj : file_records.keySet()) {
				start_time = System.currentTimeMillis();
				DataOutputStream objectOutputStream = new DataOutputStream(
						new FileOutputStream(outputDir + "/" + traj));
				LinkedList<Integer> records = file_records.get(traj);
				objectOutputStream.writeInt(records.size());
				for (int t = 0; t < records.size(); t++)
					objectOutputStream.writeInt(records.get(t));
				objectOutputStream.close();
				end_time = System.currentTimeMillis();
				time += end_time - start_time;
			}
			System.out.println("Overall time: " + time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LinkedList<Integer> getProcessedSequence(LinkedList<Integer> sequence) {
		LinkedList<Integer> result = new LinkedList<Integer>();
		int i = 0;
		while (i < sequence.size() && sequence.get(i) == 0) {
			i++;
		}
		for (; i < sequence.size(); ++i) {
			if (sequence.get(i) != 0) {
				if (result.size() == 0
						|| result.get(result.size() - 1) - sequence.get(i) != 0) {
					result.add(sequence.get(i));
				}
			}
		}
		return result;
	}

	public void Reform_trajectory(String inputDir, String outputDir) {

		File file = new File(inputDir);
		File[] inner_files = file.listFiles();
		try {
			for (int j = 0; j < inner_files.length; j++) {
				File unlinked_file = inner_files[j];
				LinkedList<Integer> records = new LinkedList<Integer>();
				BufferedReader br = new BufferedReader(new FileReader(
						unlinked_file));
				String line;
				while ((line = br.readLine()) != null) {
					records.add(Integer.parseInt(line.split("\t")[0]));
				}
				br.close();

				records = this.getProcessedSequence(records);

				DataOutputStream objectOutputStream = new DataOutputStream(
						new FileOutputStream(outputDir + "/"
								+ unlinked_file.getName()));
				objectOutputStream.writeInt(records.size());
				for (int t = 0; t < records.size(); t++)
					objectOutputStream.writeInt(records.get(t));
				objectOutputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void REFORM_TRAJECTORY(String inputDir, String outputDir) {
		File file = new File(inputDir);
		File[] inner_files = file.listFiles();
		try {
			for (int j = 0; j < inner_files.length; j++) {
				File unlinked_file = inner_files[j];
				LinkedList<Integer> records = new LinkedList<Integer>();
				LinkedList<LinkedList<Integer>> all_records = new LinkedList<LinkedList<Integer>>();
				BufferedReader br = new BufferedReader(new FileReader(
						unlinked_file));
				String line;
				int cur_edge_id, pre_edge_id = 0;
				while ((line = br.readLine()) != null) {
					cur_edge_id = Integer.parseInt(line.split("\t")[0]);
					if (cur_edge_id == 0) {
						if (records.size() > 200) {
							all_records.add(records);
							records = new LinkedList<Integer>();
							;
						}
						pre_edge_id = 0;
						continue;
					} else if (pre_edge_id != 0) {
						if (pre_edge_id == cur_edge_id)
							continue;
					}
					records.add(cur_edge_id);
					pre_edge_id = cur_edge_id;
				}
				br.close();

				for (int i = 0; i < all_records.size(); i++) {
					records = all_records.get(i);
					DataOutputStream objectOutputStream = new DataOutputStream(
							new FileOutputStream(outputDir + "/" + "20160718_"
									+ i + "_" + unlinked_file.getName()));
					objectOutputStream.writeInt(records.size());
					for (int t = 0; t < records.size(); t++)
						objectOutputStream.writeInt(records.get(t));
					objectOutputStream.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private double join(HashSet<Integer> hs1, HashSet<Integer> hs2) {
		int count = 0;
		for (Integer ele : hs1)
			if (hs2.contains(ele))
				count++;
		return 2.0 * count / (hs1.size() + hs2.size());
	}

	public void get_Statistics(int node_size, String SPTable, String inputDir,
			String outputFile) {
		Map<String, LinkedList<Integer>> file_records = new HashMap<String, LinkedList<Integer>>();
		Map<String, Double> rate = new HashMap<String, Double>();
		File file = new File(inputDir);
		File[] inner_files = file.listFiles();
		try {
			for (int j = 0; j < inner_files.length; j++) {
				File linked_file = inner_files[j];
				LinkedList<Integer> records = new LinkedList<Integer>();
				DataInputStream in = new DataInputStream(
						new BufferedInputStream(
								new FileInputStream(linked_file)));
				int size = in.readInt();
				for (int i = 0; i < size; i++) {
					records.add(in.readInt());
				}
				in.close();
				file_records.put(linked_file.getName(), records);
			}

			int offset = 0;
			this.graph = new Graph(node_size);
			DataInputStream in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(SPTable)));
			do {
				this.graph.buildGraph(in, offset);
				System.out.println("graph loaded");
				offset = this.graph.end;
				for (String traj : file_records.keySet()) {
					LinkedList<Integer> records = file_records.get(traj);
					for (int i = records.size() - 1; i > 0; i--) {
						records.addAll(i, this.getPath_Beijing(
								records.get(i - 1), records.get(i)));
					}
				}
				System.out.println("finished one round");
			} while (!this.graph.finished);
			for (String traj : file_records.keySet())
				rate.put(traj, file_records.get(traj).size() + 0.0);
			in.close();

			offset = 0;
			in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(SPTable)));
			do {
				this.graph.buildGraph(in, offset);
				System.out.println("graph loaded");
				offset = this.graph.end;
				for (String traj : file_records.keySet()) {
					LinkedList<Integer> source = file_records.get(traj);
					if (source.size() < 2)
						continue;

					int preEdge = source.get(0);
					for (int i = 2; i < source.size(); ++i) {
						if (!this.getPre(preEdge, source.get(i),
								source.get(i - 1))) {
							preEdge = source.get(i - 1);
						} else {
							source.remove(i - 1);
							i--;
						}
					}
				}
				System.out.println("finished one round");
			} while (!this.graph.finished);
			for (String traj : file_records.keySet()) {
				if (rate.containsKey(traj))
					rate.put(traj, rate.get(traj)
							/ file_records.get(traj).size());
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		WriteFile wf = new WriteFile(outputFile);
		for (String traj : rate.keySet())
			wf.write(traj + "\t" + rate.get(traj));
		wf.close();
	}

	public void SPCompelement_decomposed(int node_size, String SPTable,
			String inputDir, String outputDir) {

		Map<String, Double> rate = new HashMap<String, Double>();
		ReadFile rf = new ReadFile("File_statistics.txt");
		String line;
		while ((line = rf.readline()) != null) {
			String[] re = line.split("\t");
			double crit = Double.parseDouble(re[1]);
			if (crit < 7)
				rate.put(re[0], crit);
		}

		LinkedList<HashSet<Integer>> all_records = new LinkedList<HashSet<Integer>>();

		Map<String, LinkedList<Integer>> file_records = new HashMap<String, LinkedList<Integer>>();
		File file = new File(inputDir);
		File[] inner_files = file.listFiles();

		int count = 0;

		try {
			for (int j = 0; j < inner_files.length; j++) {
				File linked_file = inner_files[j];
				if (!rate.keySet().contains(linked_file.getName()))
					continue;
				LinkedList<Integer> records = new LinkedList<Integer>();
				DataInputStream in = new DataInputStream(
						new BufferedInputStream(
								new FileInputStream(linked_file)));
				int size = in.readInt();
				for (int i = 0; i < size; i++) {
					records.add(in.readInt());
				}
				in.close();

				// if(records.size()<500)
				// continue;

				HashSet<Integer> records_set = new HashSet<Integer>(records);
				boolean none = true;
				for (HashSet<Integer> pre_traj : all_records) {
					if (this.join(pre_traj, records_set) > 0.2) {
						none = false;
						break;
					}
				}
				if (none) {
					all_records.add(records_set);
					file_records.put(linked_file.getName(), records);
					// System.out.println(file_records.size());

					count += records.size();
				}
			}
			System.out.println("selection finished");
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(count);
		System.out.println(file_records.size());

		try {
			int offset = 0;
			this.graph = new Graph(node_size);
			DataInputStream in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(SPTable)));
			do {
				this.graph.buildGraph(in, offset);
				System.out.println("graph loaded");
				offset = this.graph.end;
				for (String traj : file_records.keySet()) {
					LinkedList<Integer> records = file_records.get(traj);
					for (int i = records.size() - 1; i > 0; i--) {
						records.addAll(i, this.getPath_Beijing(
								records.get(i - 1), records.get(i)));
					}
				}
				System.out.println("finished one round");
			} while (!this.graph.finished);
			in.close();

			for (String traj : file_records.keySet()) {
				DataOutputStream objectOutputStream = new DataOutputStream(
						new FileOutputStream(outputDir + "/" + traj));
				LinkedList<Integer> records = file_records.get(traj);
				objectOutputStream.writeInt(records.size());
				for (int t = 0; t < records.size(); t++)
					objectOutputStream.writeInt(records.get(t));
				objectOutputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void SPCompelement(String inputDir, String outputDir) {

		File file = new File(inputDir);
		File[] inner_files = file.listFiles();
		try {
			for (int j = 0; j < inner_files.length; j++) {
				File unlinked_file = inner_files[j];

				System.out.println(unlinked_file.getName());

				try {
					LinkedList<Integer> records = new LinkedList<Integer>();
					BufferedReader br = new BufferedReader(new FileReader(
							unlinked_file));
					String line;
					int pre_edge_id = -1, cur_edge_id;
					while ((line = br.readLine()) != null) {
						cur_edge_id = Integer.parseInt(line.split(",")[1]);
						if (cur_edge_id == -1) {
							continue;
						} else if (pre_edge_id != -1) {
							if (pre_edge_id == cur_edge_id)
								continue;
							LinkedList<Integer> res = this.getPath(pre_edge_id,
									cur_edge_id);
							for (int i = 0; i < res.size(); i++)
								records.add(res.get(i));
						}
						records.add(cur_edge_id);
						pre_edge_id = cur_edge_id;
					}
					br.close();

					DataOutputStream objectOutputStream = new DataOutputStream(
							new FileOutputStream(outputDir + "/"
									+ unlinked_file.getName()));
					objectOutputStream.writeInt(records.size());
					for (int t = 0; t < records.size(); t++)
						objectOutputStream.writeInt(records.get(t));
					objectOutputStream.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
