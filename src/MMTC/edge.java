package MMTC;

/*
 * edge.java
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

public class edge {
	Vector<ids> nn = new Vector(); // adjacent edges
	public int id;
	public double x;
	public double y;

	public edge(int i, double a, double b) {
		id = i;
		x = a;
		y = b;

	}

	public edge(int i, double a, double b, Vector temp) {
		nn = new Vector(temp);
		id = i;
		x = a;
		y = b;

	}

	public void addn(int id, int pos) {
		nn.addElement(new ids(id, pos));
	}

	@Override
	public edge clone() {
		Vector<ids> temp2 = new Vector();
		for (int i = 0; i < temp2.size(); i++) {
			int k = temp2.get(i).id;
			int l = temp2.get(i).pos;
			temp2.add(new ids(k, l));
		}
		edge temp = new edge(id, x, y, temp2);

		// temp.x=this.x;
		// temp.y=this.y;
		// temp.id=this.id;
		// temp.nn=this.nn.clone();
		return temp;
	}

}
