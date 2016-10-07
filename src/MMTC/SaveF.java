/*
 * SaveF.java
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class SaveF {
	File trajFile;
	int obj, traj, d, m, ye;

	/** Creates a new instance of SaveF */
	public SaveF(int a, int b, int c, int e, int f, File name) {
		obj = a;
		traj = b;
		d = c;
		m = e;
		ye = f;
		trajFile = name;
	}

	public void save(Vector A) throws IOException {
		if (!A.isEmpty() && A != null) {
			BufferedWriter outFile = new BufferedWriter(
					new FileWriter(trajFile));
			String line;
			for (int i = 0; i < A.size(); i++) {
				line = (obj + ";" + traj + ";" + d + ";" + m + ";" + ye + ";"
						+ ((int) ((point) A.elementAt(i)).t / 3600) + ";"
						+ ((int) ((point) A.elementAt(i)).t % 3600) / 60 + ";"
						+ (int) ((point) A.elementAt(i)).t % 60 + ";0;0;"
						+ ((point) A.elementAt(i)).x + ";"
						+ ((point) A.elementAt(i)).y + "\r\n");

				outFile.write(line);
			}
			outFile.close();
		}
	}
}
