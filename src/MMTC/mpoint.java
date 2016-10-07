/*
 * mpoint.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package MMTC;

/**
 *
 * @author katerina
 */
public class mpoint {
	int id1;
	int id2;
	public double x1, y1, x2, y2, xprev, yprev, xlast, ylast;
	public double tprev, tlast;

	public mpoint(int pos1, int pos2, double xa, double ya, double xb,
			double yb, double xp, double yp, double tp, double xl, double yl,
			double tl) {
		x1 = xa;
		y1 = ya;
		x2 = xb;
		y2 = yb;
		xprev = xp;
		yprev = yp;
		tprev = tp;
		xlast = xl;
		ylast = yl;
		tlast = tl;
		id1 = pos1;
		id2 = pos2;
	}

}
