/*
 * Dif.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package MMTC;

/**
 *
 * @author User83
 */
import java.util.Vector;

public class Dif {
	double div = 310;
	Vector G;

	/** Creates a new instance of Dif */
	public Dif(Vector map) {
		G = new Vector(map);

	}

	public double diff(Vector a, Vector b, int[][] dist, int off) {

		if (a == null || b == null || a.isEmpty() || b.isEmpty())
			return -1;
		double k = 0, l = 0;
		int i = 1;
		if (a.size() == b.size())
			return dif1(a, b, 0, a.size(), dist, off);
		else {
			if (a.size() > b.size()) {
				for (i = 0; i <= a.size() - b.size(); i++) {
					l = dif1(a, b, i, b.size() + i, dist, off);
					k += l;
				}
			} else {
				if (a.size() < b.size()) {
					for (i = 0; i <= b.size() - a.size(); i++) {
						l = dif2(a, b, i, a.size() + i, dist, off);
						k += l;
					}
				}
			}

			return k / i;
		}
	}

	private double dif1(Vector a, Vector b, int start, int end, int[][] pr,
			int off) {
		Vector path = new Vector();

		int i, j;
		double k = 0, l = 0;
		int size = end - start;

		double temp, test = 0;

		for (i = start, j = 0; i < size + start - 1; i++, j++) {

			findPath(G, ((point) a.elementAt(i)).id,
					((point) b.elementAt(j)).id, pr[i + off], path,
					((point) a.elementAt(i)).t, ((point) b.elementAt(j)).t);
			temp = len(path);

			k += (2 / Math.PI) * Math.atan(temp / div);

			temp = Math.abs(Math.abs(((point) a.elementAt(i + 1)).t
					- ((point) a.elementAt(i)).t)
					- Math.abs(((point) b.elementAt(j + 1)).t
							- ((point) b.elementAt(j)).t));

			l += temp
					/ Math.max(
							Math.abs(((point) a.elementAt(i + 1)).t
									- ((point) a.elementAt(i)).t),
							Math.abs(((point) b.elementAt(j + 1)).t
									- ((point) b.elementAt(j)).t));

		}

		findPath(G, ((point) a.elementAt(i)).id, ((point) b.elementAt(j)).id,
				pr[i + off], path, ((point) a.elementAt(i)).t,
				((point) b.elementAt(j)).t);
		temp = len(path);

		k += (2 / Math.PI) * Math.atan(temp / div);

		k = k / size;

		l = l / (size - 1);

		// return l*0.5+k*0.5;
		return k;

	}

	private double dif2(Vector a, Vector b, int start, int end, int[][] pr,
			int off) {
		Vector path = new Vector();
		int i, j;
		double k = 0, l = 0;
		int size = end - start;

		double temp, test = 0;

		for (i = 0, j = start; j < size + start - 1; i++, j++) {

			findPath(G, ((point) a.elementAt(i)).id,
					((point) b.elementAt(j)).id, pr[i + off], path,
					((point) a.elementAt(i)).t, ((point) b.elementAt(j)).t);
			temp = len(path);

			k += (2 / Math.PI) * Math.atan(temp / div);

			temp = Math.abs(Math.abs(((point) a.elementAt(i + 1)).t
					- ((point) a.elementAt(i)).t)
					- Math.abs(((point) b.elementAt(j + 1)).t
							- ((point) b.elementAt(j)).t));

			l += temp
					/ Math.max(
							Math.abs(((point) a.elementAt(i + 1)).t
									- ((point) a.elementAt(i)).t),
							Math.abs(((point) b.elementAt(j + 1)).t
									- ((point) b.elementAt(j)).t));

		}

		findPath(G, ((point) a.elementAt(i)).id, ((point) b.elementAt(j)).id,
				pr[i + off], path, ((point) a.elementAt(i)).t,
				((point) b.elementAt(j)).t);
		temp = len(path);

		k += (2 / Math.PI) * Math.atan(temp / div);

		k = k / size;

		l = l / (size - 1);

		// return l*0.5+k*0.5;
		return k;

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

	private double len(Vector path) {
		double sum = 0;
		for (int i = 0; i < path.size() - 1; i++) {
			sum += Math.sqrt(Math.pow(((point) path.elementAt(i + 1)).x
					- ((point) path.elementAt(i)).x, 2)
					+ Math.pow(((point) path.elementAt(i + 1)).y
							- ((point) path.elementAt(i)).y, 2));
		}
		return sum;
	}

}
