package MMTC;

/*
 * MapM.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.util.Vector;

public class MapM {
	private int flag;
	double md, a, ma, nd, na, xlast = 0, ylast = 0, xprev = 0, yprev = 0;
	int look, e;
	int offset = 1;

	public MapM(double m1, double m2, double n1, double n2, double i, int la) {
		md = m1;
		ma = m2;
		nd = n1;
		na = n2;
		a = i;
		look = la;
	}

	// Map-matching method
	// Input
	// traj: the raw trajectory (x,y,t)
	// map: the network map
	// Output
	// mm:the map-matched trajectory (x,y,t)

	public double MM(Vector traj, Vector<edge> map, Vector<edge> mm) {
		int id1 = 0, id2 = 0, pos1 = 0, pos2 = 0, p = 0, k = 0, siz = 0;
		double av;
		Vector B = new Vector();
		Vector test = new Vector();
		Vector score = new Vector();
		Vector out = new Vector();
		double x1n = 0, x2n = 0, y1n = 0, y2n = 0, x1 = 0, x2 = 0, x3 = 0, x4 = 0, y1 = 0, y2 = 0, y3 = 0, y4 = 0, s = 100, sum = 0, sd, sa = 0;
		double t2 = 1, t1 = 1, tlast = 1, tprev = 1, first = 1;
		mm.removeAllElements();
		// first time
		// take the first traj element
		x2 = ((point) traj.elementAt(0)).x;
		y2 = ((point) traj.elementAt(0)).y;
		tlast = ((point) traj.elementAt(0)).t;
		t1 = tlast;
		// offset=traj.size()/100;
		// System.out.println(offset);
		// for each node
		for (int j = 0; j < map.size(); j++) {
			if ((map.elementAt(j)) == null)
				continue;
			else {
				// for each neighbour node
				for (int i = 0; i < map.elementAt(j).nn.size(); i++) {
					x3 = map.elementAt(j).x;
					y3 = map.elementAt(j).y;
					int tpos = map.elementAt(j).nn.elementAt(i).pos;
					x4 = map.elementAt(tpos).x;
					y4 = map.elementAt(tpos).y;
					// find the distance between the point and the edge
					sd = distl(x2, y2, x3, y3, x4, y4, 0, 0);
					// if(((edge)map.elementAt(j)).id>=12358&&((edge)map.elementAt(j)).id<=12444)
					// System.out.println(sd+", "+((edge)map.elementAt(j)).id);
					// if there's a better match
					if (sd < s) {

						this.flag = 0;
						distl(x2, y2, x3, y3, x4, y4, 1, 1);
						pos1 = j;
						pos2 = tpos;
						s = sd;
						first = 0;
						id1 = map.elementAt(j).id;
						id2 = map.elementAt(tpos).id;
						x1n = x3;
						y1n = y3;
						x2n = x4;
						y2n = y4;
					}
				}
			}
		}

		// if nothing is close enough exit
		if (first == 1)
			return 0;

		// add the first edge
		// System.out.println("Adding "+id1+", "+id2);
		mm.addElement(new edge(id1, x1n, y1n, (Vector) map.elementAt(pos1).nn
				.clone()));
		mm.addElement(new edge(id2, x2n, y2n, (Vector) map.elementAt(pos2).nn
				.clone()));
		out.addElement(new mpoint(id1, id2, x1n, y1n, x2n, y2n, this.xlast,
				this.ylast, tlast, 0, 0, 0));
		tprev = tlast;
		this.xprev = xlast;
		this.yprev = ylast;
		pos1 = id1;
		pos2 = id2;

		// >second time
		this.flag = 0;
		x1n = 0;
		x2n = 0;
		y1n = 0;
		y2n = 0;
		// for each point in trajectory
		s = Double.NEGATIVE_INFINITY;
		for (int i = 0, f = 0; i < traj.size(); f += 2) {

			// if it's the first time or the last point's projection is not off
			// the last selected edge
			// or there's no better much, proceed to the next point
			if ((x1n == 0 && x2n == 0 && y1n == 0 && y2n == 0 && i < traj
					.size() - 1) || (this.flag == 0 && i < traj.size() - 1)) {

				x1 = ((point) traj.elementAt(i)).x;
				y1 = ((point) traj.elementAt(i)).y;
				t1 = ((point) traj.elementAt(i)).t;
				x2 = ((point) traj.elementAt(i + 1)).x;
				y2 = ((point) traj.elementAt(i + 1)).y;
				t2 = ((point) traj.elementAt(i + 1)).t;
				// System.out.println("Point "+x2+", "+y2);

				i++;
				first = 1;

			}

			// find all the neighbour nodes
			B.removeAllElements();
			if (k == 0) {
				B = (Vector) mm.elementAt(f).nn.clone();
				B.addAll((Vector) mm.elementAt(f + 1).nn.clone());
				siz = mm.elementAt(f).nn.size();
			} else {
				B.addElement(new ids(mm.elementAt(f + 1).id, mm
						.elementAt(f + 1).id));
				siz = 1;
				B.addAll((Vector) mm.elementAt(f + 1).nn.clone());
			}
			p = 0;
			x1n = 0;
			x2n = 0;
			y1n = 0;
			y2n = 0;

			// for each neighbour node of the last edge
			// System.out.println(B.size());
			for (int j = 0; j < B.size(); j++) {
				if (j >= siz)
					p = 1;
				id2 = map.elementAt(((ids) B.elementAt(j)).pos).id;
				id1 = mm.elementAt(f + p).id;
				x3 = mm.elementAt(f + p).x;
				y3 = mm.elementAt(f + p).y;
				x4 = map.elementAt(((ids) B.elementAt(j)).pos).x;
				y4 = map.elementAt(((ids) B.elementAt(j)).pos).y;
				// calculate the score
				sd = md - a * Math.pow(distl(x2, y2, x3, y3, x4, y4, 0, 0), nd);
				sa = ma * Math.pow(cosl(x1, y1, x2, y2, x3, y3, x4, y4), na);
				test.addElement(new edge(id1, x3, y3, (Vector) map
						.elementAt(id1).nn.clone()));
				test.addElement(new edge(id2, x4, y4, (Vector) map
						.elementAt(id2).nn.clone()));

				// If there is a look ahead policy
				if (look != 0) {
					score.removeAllElements();
					ahead((sa + sd), score, 0, i, traj, map, test);
					double max = Double.NEGATIVE_INFINITY;
					// System.out.println(score.size());
					for (int o = 0; o < score.size(); o++) {
						if (max < ((Double) score.elementAt(o)))
							max = ((Double) score.elementAt(o));
					}
					sum = max;
				} else
					sum = sa + sd;
				// System.out.println("Score of edge "+x3+", "+y3+"-->"+x4+", "+y4+": "+sum);
				// System.out.println("Score of edge "+id1+"-->"+id2+": "+sum);
				test.removeAllElements();
				// System.out.println(sum);
				// if the score for this edge is better or it's the first score
				if (sum > s || first == 1) {
					pos2 = id2;
					pos1 = id1;
					s = sum;
					first = 0;
					x1n = x3;
					y1n = y3;
					x2n = x4;
					y2n = y4;
				}
			}
			// if there was a better score
			if (x1n != 0 && x2n != 0 && y1n != 0 & y2n != 0) {
				// check if the projection of the point is off the last found
				// edge and find xlast,ylast
				this.flag = 0;
				distl(x2, y2, x1n, y1n, x2n, y2n, 1, 1);
				tlast = t2;
				// check the initial direction of the movement
				if (k == 0) {
					if ((pos1 == ((mpoint) out.lastElement()).id1 && pos2 != ((mpoint) out
							.lastElement()).id2)
							|| (pos1 == ((mpoint) out.lastElement()).id2 && pos2 == ((mpoint) out
									.lastElement()).id1)) {
						double pros = ((mpoint) out.lastElement()).x1;
						((mpoint) out.lastElement()).x1 = ((mpoint) out
								.lastElement()).x2;
						((mpoint) out.lastElement()).x2 = pros;
						pros = ((mpoint) out.lastElement()).y1;
						((mpoint) out.lastElement()).y1 = ((mpoint) out
								.lastElement()).y2;
						((mpoint) out.lastElement()).y2 = pros;
						pros = ((mpoint) out.lastElement()).id1;
						((mpoint) out.lastElement()).id1 = ((mpoint) out
								.lastElement()).id2;
						((mpoint) out.lastElement()).id2 = (int) pros;
						Vector t = new Vector();
						t.addElement(mm.elementAt(1).clone());
						t.addElement(mm.elementAt(0).clone());
						mm.removeAllElements();
						mm.addElement((edge) t.elementAt(0));
						mm.addElement((edge) t.elementAt(1));

					}

					k = 1;
				}

				// if the edge found is the same as before
				if ((mm.elementAt(f).x == x1n && mm.elementAt(f).y == y1n
						&& mm.elementAt(f + 1).x == x2n && mm.elementAt(f + 1).y == y2n))
				// ||(((edge)mm.elementAt(f)).x==x2n&&((edge)mm.elementAt(f)).y==y2n&&((edge)mm.elementAt(f+1)).x==x1n&&((edge)mm.elementAt(f+1)).y==y1n))
				{
					// System.out.println("same as before");
					f = f - 2;
					if (this.flag == 0) {
						if (((mpoint) out.lastElement()).xprev == 0
								&& ((mpoint) out.lastElement()).yprev == 0
								&& ((mpoint) out.lastElement()).tprev == 0) {
							((mpoint) out.lastElement()).xprev = xlast;
							((mpoint) out.lastElement()).yprev = ylast;
							((mpoint) out.lastElement()).tprev = tlast;
						}
						tprev = tlast;
						this.xprev = xlast;
						this.yprev = ylast;
					}

				}
				// if there's a new edge to be added
				else {
					// System.out.println("Adding "+pos1+", "+pos2);
					// System.out.println("Adding "+pos1+", "+pos2);
					((mpoint) out.lastElement()).xlast = this.xprev;
					((mpoint) out.lastElement()).ylast = this.yprev;
					((mpoint) out.lastElement()).tlast = tprev;
					mm.addElement(new edge(pos1, x1n, y1n, (Vector) map
							.elementAt(pos1).nn.clone()));
					mm.addElement(new edge(pos2, x2n, y2n, (Vector) map
							.elementAt(pos2).nn.clone()));
					// if point has projection on the last added edge
					if (this.flag == 0) {
						tprev = tlast;
						this.xprev = xlast;
						this.yprev = ylast;
						out.addElement(new mpoint(pos1, pos2, x1n, y1n, x2n,
								y2n, this.xlast, this.ylast, tlast, 0, 0, 0));
					} else {
						tprev = 0;
						this.xprev = 0;
						this.yprev = 0;
						out.addElement(new mpoint(pos1, pos2, x1n, y1n, x2n,
								y2n, 0, 0, 0, 0, 0, 0));
					}

				}

			}

			// if there's no better match
			else {
				this.flag = 0;
				f = f - 2;
				if (i == traj.size() - 1)
					i++;
			}

		}

		// if the last edge has no point projection on it
		if (((mpoint) out.lastElement()).xprev == 0
				&& ((mpoint) out.lastElement()).yprev == 0
				&& ((mpoint) out.lastElement()).tprev == 0) {
			((mpoint) out.lastElement()).xprev = ((mpoint) out.lastElement()).x2;
			((mpoint) out.lastElement()).yprev = ((mpoint) out.lastElement()).y2;
			((mpoint) out.lastElement()).tprev = t2;

		}
		if (((mpoint) out.lastElement()).xlast == 0
				&& ((mpoint) out.lastElement()).ylast == 0
				&& ((mpoint) out.lastElement()).tlast == 0) {
			((mpoint) out.lastElement()).xlast = ((mpoint) out.lastElement()).xprev;
			((mpoint) out.lastElement()).ylast = ((mpoint) out.lastElement()).yprev;
			((mpoint) out.lastElement()).tlast = ((mpoint) out.lastElement()).tprev;
		}
		// if(out.size()>1)
		// format(out,mm);

		return (double) (traj.size()) / (double) (mm.size());

	}

	private double distl(double cx, double cy, double ax, double ay, double bx,
			double by, int check, int check2) {
		double r_numerator = (cx - ax) * (bx - ax) + (cy - ay) * (by - ay);
		double r_denomenator = (bx - ax) * (bx - ax) + (by - ay) * (by - ay);
		double r = r_numerator / r_denomenator;
		if (check2 == 1) {
			this.xlast = ax + r * (bx - ax);
			this.ylast = ay + r * (by - ay);
		}
		if ((cx == ax && cy == ay) || (cx == bx && cy == by))
			return 0;
		double s = ((ay - cy) * (bx - ax) - (ax - cx) * (by - ay))
				/ r_denomenator;
		double distanceLine = Math.abs(s) * Math.sqrt(r_denomenator);
		if ((r >= 0) && (r <= 1)) {
			if (check == 1 && distanceLine < 100)
				this.flag = 0;
			else if (check == 1 && distanceLine >= 100) {
				this.flag = 1;

			}
			return distanceLine;
		} else {
			if (check == 1)
				this.flag = 1;
			double dist1 = (cx - ax) * (cx - ax) + (cy - ay) * (cy - ay);
			double dist2 = (cx - bx) * (cx - bx) + (cy - by) * (cy - by);
			if (dist1 < dist2) {
				if (check2 == 1) {
					this.xlast = ax;
					this.ylast = ay;
				}
				return Math.sqrt(dist1);
			} else {
				if (check2 == 1) {
					this.xlast = bx;
					this.ylast = by;
				}
				return Math.sqrt(dist2);
			}
		}
	}

	private double cosl(double x1, double y1, double x2, double y2, double x3,
			double y3, double x4, double y4) {
		double pt1 = x2 - x1;
		double pt2 = y2 - y1;
		double pt3 = x4 - x3;
		double pt4 = y4 - y3;
		return ((pt1 * pt3) + (pt2 * pt4))
				/ ((Math.sqrt(pt1 * pt1 + pt2 * pt2)) * (Math.sqrt(pt3 * pt3
						+ pt4 * pt4)));
	}

	private void ahead(double score, Vector sc, int i, int pos, Vector traj,
			Vector map, Vector test) {
		Vector tempedg = new Vector();
		double xt1 = 0, yt1 = 0, xt2 = 0, yt2 = 0, x1n = 0, x2n = 0, y1n = 0, y2n = 0, xt3 = 0, xt4 = 0, yt3 = 0, yt4 = 0, s = 0, sum = 0, sd = 0, sa = 0;
		int pos1 = 0, id1 = 0, id2 = 0, pos2 = 0, p = 0, first = 1, k = 0, g = 1;
		if (i < look && (pos + offset) < traj.size()) {
			tempedg.removeAllElements();
			// tempedg.addElement(new
			// ids(((edge)test.elementAt(0)).id,((edge)test.elementAt(0)).id));
			// tempedg.addAll((Vector)((edge)test.elementAt(0)).nn.clone());
			tempedg.addElement(new ids(((edge) test.elementAt(1)).id,
					((edge) test.elementAt(1)).id));
			tempedg.addAll((Vector) ((edge) test.elementAt(1)).nn.clone());
			// System.out.println(((edge)test.elementAt(1)).id);
			xt1 = ((point) traj.elementAt(pos)).x;
			yt1 = ((point) traj.elementAt(pos)).y;
			xt2 = ((point) traj.elementAt(pos + offset)).x;
			yt2 = ((point) traj.elementAt(pos + offset)).y;

			first = 1;

			p = 0;
			x1n = 0;
			x2n = 0;
			y1n = 0;
			y2n = 0;
			// for all the neighbour nodes
			for (int l = 0; l < tempedg.size(); l++) {
				if (l >= 1)
					p = 1;
				id1 = ((edge) test.elementAt(p)).id;
				xt3 = ((edge) test.elementAt(p)).x;
				yt3 = ((edge) test.elementAt(p)).y;
				int tpos = ((ids) tempedg.elementAt(l)).pos;
				id2 = ((edge) map.elementAt(tpos)).id;
				xt4 = ((edge) map.elementAt(tpos)).x;
				yt4 = ((edge) map.elementAt(tpos)).y;
				sd = md
						- a
						* Math.pow(distl(xt2, yt2, xt3, yt3, xt4, yt4, 0, 0),
								nd);
				sa = ma
						* Math.pow(
								cosl(xt1, yt1, xt2, yt2, xt3, yt3, xt4, yt4),
								na);
				test.removeAllElements();
				test.addElement(new edge(id1, xt3, yt3, (Vector) ((edge) map
						.elementAt(id1)).nn.clone()));
				test.addElement(new edge(id2, xt4, yt4, (Vector) ((edge) map
						.elementAt(id2)).nn.clone()));
				ahead(score + sa + sd, sc, i + 1, pos + offset, traj, map, test);
			}
		} else
			sc.addElement(Double.valueOf(score));
	}

	private double avg1(double x1, double y1, double t1, double x2, double y2,
			double t2, double x3, double y3) {
		double d1, d2;
		d1 = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		d2 = Math.sqrt((x1 - x3) * (x1 - x3) + (y1 - y3) * (y1 - y3));
		double t3 = t1 - d2 * (t2 - t1) / d1;
		return t3;

	}

	private double avg2(double x1, double y1, double t1, double x2, double y2,
			double t2, double x3, double y3) {
		double d1, d2;
		d1 = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		d2 = Math.sqrt((x3 - x2) * (x3 - x2) + (y3 - y2) * (y3 - y2));
		if (d1 == 0)
			return t2;
		else {
			double t3 = t2 + d2 * (t2 - t1) / d1;
			return t3;
		}

	}

	// Calculate the time on each network node
	private void format(Vector traj, Vector B) {
		double t3 = 0;
		int k = 1, f, g;
		double d1 = 0, d2 = 0;
		B.removeAllElements();
		if (((mpoint) traj.firstElement()).xprev == ((mpoint) traj
				.firstElement()).x1
				&& ((mpoint) traj.firstElement()).yprev == ((mpoint) traj
						.firstElement()).y1) {
			B.addElement(new point(((mpoint) traj.firstElement()).xprev,
					((mpoint) traj.firstElement()).yprev, ((mpoint) traj
							.firstElement()).tprev, ((mpoint) traj
							.firstElement()).id1));
		} else {
			B.addElement(new point(((mpoint) traj.firstElement()).x1,
					((mpoint) traj.firstElement()).y1, ((mpoint) traj
							.firstElement()).tprev
							- 10
							* ((mpoint) traj.firstElement()).tprev / 100,
					((mpoint) traj.firstElement()).id1));
		}

		for (int j = 0; j < traj.size() - 1; j++) {

			if (((mpoint) traj.elementAt(j)).xlast == ((mpoint) traj
					.elementAt(j)).x2
					&& ((mpoint) traj.elementAt(j)).ylast == ((mpoint) traj
							.elementAt(j)).y2)
				t3 = ((mpoint) traj.elementAt(j)).tlast;
			else {
				if (((mpoint) traj.elementAt(j + 1)).xprev == ((mpoint) traj
						.elementAt(j)).x2
						&& ((mpoint) traj.elementAt(j + 1)).yprev == ((mpoint) traj
								.elementAt(j)).y2)
					t3 = ((mpoint) traj.elementAt(j + 1)).tprev;
				else {
					d1 = dist1(traj, j);
					f = this.e;
					d2 = dist2(traj, j);
					g = this.e;
					t3 = (d1 * ((mpoint) traj.elementAt(g)).tprev + d2
							* ((mpoint) traj.elementAt(f)).tlast)
							/ (d1 + d2);
				}
			}

			if (((point) B.lastElement()).t >= t3) {
				t3 += 1;
			}
			B.addElement(new point(((mpoint) traj.elementAt(j)).x2,
					((mpoint) traj.elementAt(j)).y2, t3, ((mpoint) traj
							.elementAt(j)).id2));

		}
		if (((mpoint) traj.lastElement()).xlast == ((mpoint) traj.lastElement()).x2
				&& ((mpoint) traj.lastElement()).ylast == ((mpoint) traj
						.lastElement()).y2) {
			B.addElement(new point(((mpoint) traj.lastElement()).xlast,
					((mpoint) traj.lastElement()).ylast, ((mpoint) traj
							.lastElement()).tlast,
					((mpoint) traj.lastElement()).id2));
		} else {
			double tend = avg2(((point) B.lastElement()).x,
					((point) B.lastElement()).y, ((point) B.lastElement()).t,
					((mpoint) traj.lastElement()).xlast,
					((mpoint) traj.lastElement()).ylast,
					((mpoint) traj.lastElement()).tlast,
					((mpoint) traj.lastElement()).x2,
					((mpoint) traj.lastElement()).y2);
			if (!Double.isNaN(tend)) {
				B.addElement(new point(((mpoint) traj.lastElement()).x2,
						((mpoint) traj.lastElement()).y2, tend, ((mpoint) traj
								.lastElement()).id2));
			}
		}
	}

	private double dist1(Vector traj, int j) {
		if (((mpoint) traj.elementAt(j)).xlast != 0) {
			this.e = j;

			return Math.sqrt((((mpoint) traj.elementAt(j)).x2 - ((mpoint) traj
					.elementAt(j)).xlast)
					* (((mpoint) traj.elementAt(j)).x2 - ((mpoint) traj
							.elementAt(j)).xlast)
					+ (((mpoint) traj.elementAt(j)).y2 - ((mpoint) traj
							.elementAt(j)).ylast)
					* (((mpoint) traj.elementAt(j)).y2 - ((mpoint) traj
							.elementAt(j)).ylast));
		} else

			return dist1(traj, j - 1)
					+ Math.sqrt((((mpoint) traj.elementAt(j)).x2 - ((mpoint) traj
							.elementAt(j)).x1)
							* (((mpoint) traj.elementAt(j)).x2 - ((mpoint) traj
									.elementAt(j)).x1)
							+ (((mpoint) traj.elementAt(j)).y2 - ((mpoint) traj
									.elementAt(j)).y1)
							* (((mpoint) traj.elementAt(j)).y2 - ((mpoint) traj
									.elementAt(j)).y1));

	}

	private double dist2(Vector traj, int j) {
		if (((mpoint) traj.elementAt(j + 1)).xprev != 0) {
			this.e = j + 1;

			return Math
					.sqrt((((mpoint) traj.elementAt(j + 1)).xprev - ((mpoint) traj
							.elementAt(j)).x2)
							* (((mpoint) traj.elementAt(j + 1)).xprev - ((mpoint) traj
									.elementAt(j)).x2)
							+ (((mpoint) traj.elementAt(j + 1)).yprev - ((mpoint) traj
									.elementAt(j)).y2)
							* (((mpoint) traj.elementAt(j + 1)).yprev - ((mpoint) traj
									.elementAt(j)).y2));
		} else
			return dist2(traj, j + 1)
					+ Math.sqrt((((mpoint) traj.elementAt(j)).x2 - ((mpoint) traj
							.elementAt(j)).x1)
							* (((mpoint) traj.elementAt(j)).x2 - ((mpoint) traj
									.elementAt(j)).x1)
							+ (((mpoint) traj.elementAt(j)).y2 - ((mpoint) traj
									.elementAt(j)).y1)
							* (((mpoint) traj.elementAt(j)).y2 - ((mpoint) traj
									.elementAt(j)).y1));

	}
}
