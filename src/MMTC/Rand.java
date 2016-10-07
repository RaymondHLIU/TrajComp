/*
 * Rand.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package MMTC;

import java.util.Random;
import java.util.Vector;

/**
 *
 * @author User83
 */
public class Rand {
	Vector map;

	/** Creates a new instance of Rand */
	public Rand(Vector matrix) {
		map = new Vector(matrix);
	}

	public void random(Vector out, int maxsize, double pn, Vector pros, int ti) {
		Random generator = new Random();

		Vector cos = new Vector();
		int i;
		int r = generator.nextInt(map.size());
		while (map.elementAt(r) == null)
			r = generator.nextInt(map.size());
		pros.addElement(new point(((edge) map.elementAt(r)).x, ((edge) map
				.elementAt(r)).y, 1, ((edge) map.elementAt(r)).id));

		int s = generator.nextInt(((edge) map.elementAt(r)).nn.size());
		int k = ((edge) map.elementAt(r)).nn.elementAt(s).id;
		pros.addElement(new point(((edge) map.elementAt(k)).x, ((edge) map
				.elementAt(k)).y, 0, ((edge) map.elementAt(k)).id));
		for (i = 2; i < maxsize; i++) {
			cos.removeAllElements();
			for (int j = 0; j < ((edge) map.elementAt(k)).nn.size(); j++) {

				// if(cosl(((point)pros.elementAt(i-2)).x,((point)pros.elementAt(i-2)).y,((point)pros.elementAt(i-1)).x,((point)pros.elementAt(i-1)).y,((point)pros.elementAt(i-1)).x,((point)pros.elementAt(i-1)).y,((edge)map.elementAt(((ids)((edge)map.elementAt(k)).nn.elementAt(j)).id)).x,((edge)map.elementAt(((ids)((edge)map.elementAt(k)).nn.elementAt(j)).id)).y)+1-0.1>0)
				// {
				// for(int
				// z=0;z<(1/(cosl(((point)pros.elementAt(i-2)).x,((point)pros.elementAt(i-2)).y,((point)pros.elementAt(i-1)).x,((point)pros.elementAt(i-1)).y,((point)pros.elementAt(i-1)).x,((point)pros.elementAt(i-1)).y,((edge)map.elementAt(((ids)((edge)map.elementAt(k)).nn.elementAt(j)).id)).x,((edge)map.elementAt(((ids)((edge)map.elementAt(k)).nn.elementAt(j)).id)).y)+1))*100;z++)
				for (int z = 0; z < (cosl(((point) pros.elementAt(i - 2)).x,
						((point) pros.elementAt(i - 2)).y,
						((point) pros.elementAt(i - 1)).x,
						((point) pros.elementAt(i - 1)).y,
						((point) pros.elementAt(i - 1)).x,
						((point) pros.elementAt(i - 1)).y,
						((edge) map.elementAt(((edge) map.elementAt(k)).nn
								.elementAt(j).id)).x,
						((edge) map.elementAt(((edge) map.elementAt(k)).nn
								.elementAt(j).id)).y) + 1) * 100; z++) {
					cos.addElement(Integer.valueOf(((edge) map.elementAt(k)).nn
							.elementAt(j).id));
				}
				// }
			}

			if (cos.size() > 1) {
				s = generator.nextInt(cos.size());
				k = (Integer) cos.elementAt(s);

			} else {
				k = ((edge) map.elementAt(k)).nn.elementAt(0).id;
			}

			pros.addElement(new point(((edge) map.elementAt(k)).x, ((edge) map
					.elementAt(k)).y, 0, ((edge) map.elementAt(k)).id));
		}

		((point) pros.lastElement()).t = ti * (pn - 1) + 1;
		// System.out.println("last: "+(ti*(pn-1)+1));
		int z = 0, g = 0;
		double dist1 = 0, dist2 = 0, t1, t2;
		// find the time at the nodes
		for (int l = 0; l < pros.size(); l++) {
			// if we don't know the time for that node
			if (((point) pros.elementAt(l)).t == 0) {
				dist1 = 0;
				dist2 = 0;
				z = 0;
				// left distance
				while (((point) pros.elementAt(l - z)).t == 0) {
					dist1 += Math
							.sqrt(Math.pow(((point) pros.elementAt(l - z)).x
									- ((point) pros.elementAt(l - z - 1)).x, 2)
									+ Math.pow(
											((point) pros.elementAt(l - z)).y
													- ((point) pros.elementAt(l
															- z - 1)).y, 2));
					z++;
				}
				t1 = ((point) pros.elementAt(l - z)).t;
				z = 0;
				// right distance
				while (((point) pros.elementAt(l + z)).t == 0) {
					dist2 += Math.sqrt(Math.pow(
							((point) pros.elementAt(l + z + 1)).x
									- ((point) pros.elementAt(l + z)).x, 2)
							+ Math.pow(((point) pros.elementAt(l + z + 1)).y
									- ((point) pros.elementAt(l + z)).y, 2));
					z++;
				}
				t2 = ((point) pros.elementAt(l + z)).t;
				((point) pros.elementAt(l)).t = (dist1 * t2 + dist2 * t1)
						/ (dist1 + dist2);
				// System.out.println("Speed: "+((dist1+dist2)/(t2-t1)));
			}
		}

		out.addElement(pros.firstElement());
		int j = ti + 1;
		double x, y;
		for (i = 0; i < pros.size() - 1; i++) {
			double time = ((point) pros.elementAt(i + 1)).t
					- ((point) pros.elementAt(i)).t;
			double distx = ((point) pros.elementAt(i + 1)).x
					- ((point) pros.elementAt(i)).x;
			double speedx = distx / time;

			double disty = ((point) pros.elementAt(i + 1)).y
					- ((point) pros.elementAt(i)).y;
			double speedy = disty / time;
			// System.out.println(Math.sqrt(Math.pow(speedx,2)+Math.pow(speedy,2)));
			// System.out.println("between "+((point)pros.elementAt(i)).x+","+((point)pros.elementAt(i)).y+" and "+((point)pros.elementAt(i+1)).x+","+((point)pros.elementAt(i+1)).y);
			while (j < ((point) pros.elementAt(i + 1)).t) {
				x = ((point) pros.elementAt(i)).x + speedx
						* (j - ((point) pros.elementAt(i)).t);

				y = ((point) pros.elementAt(i)).y + speedy
						* (j - ((point) pros.elementAt(i)).t);
				// System.out.println(j);
				out.addElement(new point(x, y, j));

				j += ti;
			}

		}
		out.addElement(pros.lastElement());
		// System.out.println(out.size()+","+pros.size());
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

}
