package MMTC;

/*
 * ids.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author User83
 */
public class ids {
	public int id, pos;

	public ids(int a, int b) {
		id = a;
		pos = b;
	}

	@Override
	public ids clone() {
		ids temp;
		temp = new ids(id, pos);

		temp.id = this.id;
		temp.pos = this.pos;

		return temp;
	}
}
