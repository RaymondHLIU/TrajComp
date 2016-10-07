/*
 * bpstruct.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package MMTC;

/**
 *
 * @author K. Kell
 */
public class bpstruct {
	int i, j;
	boolean c, ignore = false;

	/** Creates a new instance of bpstruct */
	public bpstruct(boolean ch, int a, int b) {
		i = a;
		j = b;
		c = ch;
	}

	public bpstruct(boolean ign) {
		ignore = true;
	}

}
