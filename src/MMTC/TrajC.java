package MMTC;

/*
 * TrajC.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author User83
 */
import java.util.Vector;

public class TrajC {
	double thr;

	/** Creates a new instance of TrajC */
	public TrajC(double a) {
		thr = a;
	}

	public void DP(Vector A, int j, int k, Vector B) {
		if (k <= j + 1)
			return;
		double maxd = 0;
		int maxi = j;
		for (int i = j + 1; i < k; i++) {
			double d = distm(A, j, k, i);
			if (d <= maxd)
				continue;
			else {
				maxd = d;
				maxi = i;
			}
		}
		if (maxd > thr) {
			DP(A, j, maxi, B);
			B.add(A.elementAt(maxi));
			DP(A, maxi, k, B);
		}
	}

	private double distm(Vector A, int s, int e, int i) {
		double de = ((point) A.elementAt(e)).t - ((point) A.elementAt(s)).t;
		double di = ((point) A.elementAt(i)).t - ((point) A.elementAt(s)).t;
		if (de == 0)
			de = 1;
		if (di == 0)
			di = 1;
		double x = ((point) A.elementAt(s)).x + (di / de)
				* (((point) A.elementAt(e)).x - ((point) A.elementAt(s)).x);
		double y = ((point) A.elementAt(s)).y + (di / de)
				* (((point) A.elementAt(e)).y - ((point) A.elementAt(s)).y);
		double dx = x - ((point) A.elementAt(i)).x;
		double dy = y - ((point) A.elementAt(i)).y;
		return Math.sqrt(dx * dx + dy * dy);
	}
}
