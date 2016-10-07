/*
 * NoiseAdd.java
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
import java.util.Random;
import java.util.Vector;

public class NoiseAdd {
	Vector t;

	/** Creates a new instance of NoiseAdd */
	public NoiseAdd(Vector k) {
		t = new Vector(k);
	}

	public void add(Vector out) {
		Random generatorx = new Random();
		Random generatory = new Random();
		for (int i = 0; i < t.size(); i++) {
			out.addElement(new point(((point) t.elementAt(i)).x
					+ generatorx.nextGaussian() * 2, ((point) t.elementAt(i)).y
					+ generatory.nextGaussian() * 2, ((point) t.elementAt(i)).t));
		}
	}
}
