package FrequentFollower;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import RWFiles.WriteFile;
import RoadNetWork.RoadNetWork;

public class StatisticFreqentRoadSegment {

	// double direction
	public Map<Integer, Map<Integer, Integer>> roadid_follower_roadid_statistic = new HashMap<Integer, Map<Integer, Integer>>();
	public Map<Integer, Integer> roadid_follower = new HashMap<Integer, Integer>();
	public long count = 0;

	public long getCount() {
		return count;
	}

	public LinkedList<Integer> FrequentFollowerCompression(
			LinkedList<Integer> road_id_list) {
		if (road_id_list.size() < 4)
			return road_id_list;
		LinkedList<Integer> buf = new LinkedList<Integer>();
		LinkedList<Integer> result = new LinkedList<Integer>();
		int pre_road_id = -1, cur_road_id;
		boolean in_follower = false;
		for (int i = 0; i < road_id_list.size(); i++) {
			cur_road_id = road_id_list.get(i);
			if (!roadid_follower.containsKey(pre_road_id))
				count++;
			if (pre_road_id != -1 && roadid_follower.containsKey(pre_road_id)) {
				if ((roadid_follower.get(pre_road_id) - cur_road_id) == 0) {
					if (!in_follower) {
						in_follower = true;
						buf.clear();
					}
					pre_road_id = cur_road_id;
					buf.add(pre_road_id);
				} else {
					if (in_follower) {
						in_follower = false;
						if (buf.size() < 3) {
							result.addAll(buf);
						} else {
							for (int t = 0; t < buf.size(); t++) {
								result.add(-1);
							}
							result.add(pre_road_id);
						}
					}
					pre_road_id = cur_road_id;
					result.add(pre_road_id);
				}
			} else {
				pre_road_id = cur_road_id;
				result.add(pre_road_id);
			}
		}
		return result;
	}

	// Map<Intersection, Integer> trained_model = new HashMap<Intersection,
	// Integer>();

	public StatisticFreqentRoadSegment(RoadNetWork rnw,
			String sp_linked_filepath) {

		File file = new File(sp_linked_filepath);
		File[] inner_files = file.listFiles();
		try {
			for (int j = 0; j < inner_files.length; j++) {
				LinkedList<Integer> records = new LinkedList<Integer>();
				File linked_file = inner_files[j];
				DataInputStream objectInputStream = new DataInputStream(
						new FileInputStream(linked_file));
				int entry = objectInputStream.readInt();
				for (int i = 0; i < entry; i++)
					records.add(objectInputStream.readInt());
				objectInputStream.close();
				if (records.size() < 2)
					continue;
				int current_roadid, pre_roadid = records.get(0);
				for (int i = 0; i < records.size(); i++) {
					current_roadid = records.get(i);

					if (rnw.nodes.get(rnw.roads.get(pre_roadid)
							.getToNodeIndex()).road_ids
							.contains(current_roadid)) {
						if (roadid_follower_roadid_statistic.keySet().contains(
								pre_roadid)) {
							if (roadid_follower_roadid_statistic
									.get(pre_roadid).keySet()
									.contains(current_roadid)) {
								int old_value = roadid_follower_roadid_statistic
										.get(pre_roadid).get(current_roadid);
								roadid_follower_roadid_statistic
										.get(pre_roadid).put(current_roadid,
												old_value + 1);
							} else {
								roadid_follower_roadid_statistic
										.get(pre_roadid).put(current_roadid, 1);
							}

						} else {
							roadid_follower_roadid_statistic.put(pre_roadid,
									new HashMap<Integer, Integer>());
							roadid_follower_roadid_statistic.get(pre_roadid)
									.put(current_roadid, 1);
						}
					}
					pre_roadid = current_roadid;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Integer pre : roadid_follower_roadid_statistic.keySet()) {
			int max_suc = -1;
			int max = 0;
			for (Integer suc : roadid_follower_roadid_statistic.get(pre)
					.keySet()) {
				if (roadid_follower_roadid_statistic.get(pre).get(suc) > max) {
					max_suc = suc;
					max = roadid_follower_roadid_statistic.get(pre).get(suc);
				}
			}
			roadid_follower.put(pre, max_suc);
		}
	}

	public static void main(String[] args) {
		final String dataDir = "data/SPLinkedFile_Beijing";// "data/BJTraj";
		// final String dataDir="data/SPLinked_Beijing_Text_Vertex";
		final String mapDir = "data/BJMap";

		LinkedList<LinkedList<Integer>> all = new LinkedList<LinkedList<Integer>>();
		LinkedList<Integer> records, ffe_records;

		RoadNetWork rnw = new RoadNetWork(mapDir);
		StatisticFreqentRoadSegment ffe = new StatisticFreqentRoadSegment(rnw,
				dataDir);

		File file = new File(dataDir);
		File[] inner_files = file.listFiles();
		System.out.println(inner_files.length);

		try {
			// DataOutputStream dos = new DataOutputStream(new
			// FileOutputStream("result_FF.txt"));
			WriteFile dos = new WriteFile("result.txt");
			for (int j = 0; j < inner_files.length; j++) {
				File linked_file = inner_files[j];

				records = new LinkedList<Integer>();
				DataInputStream objectInputStream = new DataInputStream(
						new FileInputStream(linked_file));
				int entry = objectInputStream.readInt();
				for (int i = 0; i < entry; i++)
					records.add(objectInputStream.readInt());
				objectInputStream.close();

				all.add(records);

			}

			System.out.println("All trajectory loaded.");
			int count = 0;

			long mili = 0;
			mili -= System.currentTimeMillis();

			for (int i = 0; i < all.size(); i++) {
				count += all.get(i).size();
				// frequent_follower_compression
				ffe_records = ffe.FrequentFollowerCompression(all.get(i));
				String res = String.valueOf(ffe_records.get(0));
				for (int j = 1; j < ffe_records.size(); j++) {
					res = res + "," + String.valueOf(ffe_records.get(j));
				}
				if (i % 100 == 0)
					System.out.println(String.valueOf(i / 100)
							+ " traj compressed");
				res = res + "\n";
				dos.write(res);

			}
			dos.close();

			mili += System.currentTimeMillis();
			System.out.println(mili);

			System.out.println(count / 5);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("no following edge count"
				+ String.valueOf(ffe.getCount()));

	}

}
