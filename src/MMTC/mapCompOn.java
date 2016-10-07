/*
 * mapCompOn.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package MMTC;

import java.util.Vector;

/**
 *
 * @author User83
 */
public class mapCompOn {
	static Vector G;

	/** Creates a new instance of mapComp */
	public mapCompOn(Vector map) {
		G = new Vector(map);

	}

	// Vector p is the trajectory on the map
	// and out is the output Vector
	public double comp(Vector p, Vector out, int[][] pr, double[][] di,
			double par, int rp) {

		double c = 0, maximum = Double.MAX_VALUE;
		int s, e, j, test = 0, i, k = 0;
		int sum = 0, count = 0;
		double av;
		Vector temp = new Vector();
		Vector path = new Vector();
		Vector pros = new Vector();

		while (k < p.size() - 1) {
			int r = 0;
			temp.removeAllElements();
			maximum = Double.MAX_VALUE;
			for (i = k + 1; i < p.size() && r < rp; i++) {
				// for(i=k+1;i<p.size();i++) {
				path.removeAllElements();

				findPath(G, ((point) p.elementAt(k)).id,
						((point) p.elementAt(i)).id, pr[k], path,
						((point) p.elementAt(k)).t, ((point) p.elementAt(i)).t);
				// if the shortest path has less points than the original path
				// replace add it to output
				if (path.size() <= (i - k) && path.size() != 0) {
					r++;
					int z = 0, g = 0;
					double dist1 = 0, dist2 = 0, t1, t2;
					// find the time at the nodes
					for (int l = 0; l < path.size(); l++) {
						// if we don't know the time for that node
						if (((point) path.elementAt(l)).t == 0) {
							dist1 = 0;
							dist2 = 0;
							z = 0;
							// left distance
							while (((point) path.elementAt(l - z)).t == 0) {
								dist1 += Math
										.sqrt(Math.pow(
												((point) path.elementAt(l - z)).x
														- ((point) path
																.elementAt(l
																		- z - 1)).x,
												2)
												+ Math.pow(
														((point) path
																.elementAt(l
																		- z)).y
																- ((point) path
																		.elementAt(l
																				- z
																				- 1)).y,
														2));
								z++;
							}
							t1 = ((point) path.elementAt(l - z)).t;
							z = 0;
							// right distance
							while (((point) path.elementAt(l + z)).t == 0) {
								dist2 += Math
										.sqrt(Math.pow(
												((point) path.elementAt(l + z
														+ 1)).x
														- ((point) path
																.elementAt(l
																		+ z)).x,
												2)
												+ Math.pow(
														((point) path
																.elementAt(l
																		+ z + 1)).y
																- ((point) path
																		.elementAt(l
																				+ z)).y,
														2));
								z++;
							}
							t2 = ((point) path.elementAt(l + z)).t;
							((point) path.elementAt(l)).t = (dist1 * t2 + dist2
									* t1)
									/ (dist1 + dist2);
						}
					}
					pros.removeAllElements();
					for (j = k; j <= i; j++)
						pros.addElement(p.elementAt(j));

					mdlpath mp = new mdlpath(MDL(pros, path, pr, k, par), path,
							i);

					temp.addElement(mp);
				}

			}
			count++;
			sum += (i - k + 1);
			if (temp.size() == 0 || temp == null) {

				pros.removeAllElements();
				for (j = k; j < p.size() && j < k + rp - 1; j++)
					pros.addElement(p.elementAt(j));
				out.addAll((Vector) pros.clone());
				if ((k + rp) <= p.size())
					k = k + rp - 1;
				else
					k = p.size() - 1;

			} else {

				mdlpath mp = null;

				maximum = Double.MAX_VALUE;

				for (j = 0; j < temp.size(); j++) {
					if (((mdlpath) temp.elementAt(j)).MDL <= maximum) {

						mp = ((mdlpath) temp.elementAt(j));
						maximum = ((mdlpath) temp.elementAt(j)).MDL;
						k = mp.n;
					}
				}

				if (mp == null || (mp.path) == null) {
					out.removeAllElements();
					out.addAll(((Vector) p.clone()));
					k = p.size() - 1;
				} else
					out.addAll((Vector) (mp.path).clone());

				if (k < p.size() - 1)
					out.removeElementAt(out.size() - 1);

			}

		}
		if (count > 0)
			return sum / count;
		else
			return p.size();
	}

	private static void findPath(Vector G, int s, int e, int[] pred,
			Vector path, double t1, double t2) {
		path.removeAllElements();
		int x = e;
		while (x != s) {
			if (x == e)
				path.add(
						0,
						new point(((edge) G.elementAt(x)).x, ((edge) G
								.elementAt(x)).y, t2,
								((edge) G.elementAt(x)).id));
			else
				path.add(
						0,
						new point(((edge) G.elementAt(x)).x, ((edge) G
								.elementAt(x)).y, 0, ((edge) G.elementAt(x)).id));
			x = pred[x];
		}
		path.add(0, new point(((edge) G.elementAt(x)).x,
				((edge) G.elementAt(x)).y, t1, ((edge) G.elementAt(x)).id));

	}

	private static double MDL(Vector a, Vector b, int[][] dist, int off,
			double p) {
		Dif dif = new Dif(G);
		double l1, l2;
		l1 = (Math.log(dif.diff(a, b, dist, off)) / Math.log(2))
				- Math.log(0.001) / Math.log(2);

		l2 = (Math.log(Math.pow((double) b.size() / (double) a.size(), p)) / Math
				.log(2)) - Math.log(0.001) / Math.log(2);

		if (l1 < 0)
			l1 = 0;

		return l2 + l1;

	}
}
