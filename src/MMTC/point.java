package MMTC;

/*
 * point.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author User83
 */
public class point {
	int id;
	public double x;
	public double y;
	public double t;

	public point(double a, double b, double c, int pos) {
		x = a;
		y = b;
		t = c;
		id = pos;
	}

	public point(double a, double b, double c) {
		x = a;
		y = b;
		t = c;
	}
}
