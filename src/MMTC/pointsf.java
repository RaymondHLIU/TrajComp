/*
 * pointsf.java
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class pointsf {
	File trajFile = null;
	int obj, traj, d, m, ye;

	/** Creates a new instance of pointsf */
	public pointsf(int a, int b, int c, int e, int f, File name) {
		obj = a;
		traj = b;
		d = c;
		m = e;
		ye = f;
		trajFile = name;
	}

	public void addPoints(Vector A) throws IOException {
		int objid, trajid, day, month, year;
		double x, y;
		long tid = 0;
		if (trajFile.exists()) {
			BufferedReader inFile = new BufferedReader(new FileReader(trajFile));
			String line = inFile.readLine();
			// for(int i=0;i<6;i++){
			while (line != null) {
				java.util.StringTokenizer st = new java.util.StringTokenizer(
						line, " ;:/\t");
				objid = Integer.parseInt(st.nextToken());
				trajid = Integer.parseInt(st.nextToken());
				day = Integer.parseInt(st.nextToken());
				month = Integer.parseInt(st.nextToken());
				year = Integer.parseInt(st.nextToken());
				tid = 3600 * Long.parseLong(st.nextToken());
				tid += 60 * Long.parseLong(st.nextToken());
				tid += Long.parseLong(st.nextToken());
				st.nextToken();
				st.nextToken();
				x = Double.parseDouble(st.nextToken());
				y = Double.parseDouble(st.nextToken());
				if (objid == obj && trajid == traj && day == d && month == m
						&& year == ye) {
					A.addElement(new point(x, y, tid));
					tid = 0;
				}

				line = inFile.readLine();
			}
			inFile.close();
		}
	}
}
