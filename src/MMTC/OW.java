/*
 * OW.java
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
public class OW {
	double thr;

	/** Creates a new instance of OW */
	public OW(double a) {
		thr = a;
	}

	public double DP(Vector A, Vector B) {
		int j = 2, i = 0, flag = 0;
		int count = 0, sum = 0;
		double av = A.size();
		// B.add(A.elementAt(i));
		while (j < A.size()) {
			flag = 0;
			for (int k = i + 1; k < j; k++) {
				if (distm(A, i, j, k) > thr) {
					count++;
					sum += (j - i + 1);
					B.add(A.elementAt(k));
					i = k;
					j = k + 2;
					flag = 1;
					break;
				}

			}
			if (flag == 0)
				j++;

		}
		// B.add(A.elementAt(j-1));
		if (count > 0)
			return sum / count;
		else
			return av;

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
