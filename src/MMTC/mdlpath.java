/*
 * mdlpath.java
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
public class mdlpath {
	double MDL;
	Vector path;
	int n;

	/** Creates a new instance of mdlpath */
	public mdlpath(double a, Vector p, int i) {
		MDL = a;
		path = new Vector(p);
		n = i;
	}

}
