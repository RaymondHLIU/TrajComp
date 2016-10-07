/*
 * app.java
 *
 * Created on 26  2007, 10:48 
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;

public class app extends javax.swing.JFrame {

	Vector traj = new Vector();
	Vector trajN = new Vector();
	Vector comptraj = new Vector();
	Vector map = new Vector();
	Vector newtraj1 = new Vector();
	Vector newtraj2 = new Vector();
	Vector newtraj3 = new Vector();
	Vector<edge> matrix = new Vector<edge>();
	Vector maxc = new Vector();
	Vector initial = new Vector();
	// Vector dist=new Vector();
	// static int [][] dist;
	int maxsize = 150;
	double thr, md, ma, nd, na, a;
	int look, obj, trajid, day, month, year, id;
	String name = "150";

	/**
	 * Creates a new instance of app
	 */
	public app() {
		thr = 100;
		md = 10;
		a = 0.17;
		nd = 1.4;
		ma = 10;
		na = 3;
		look = 5;
		obj = 500;
		trajid = 0;
		day = 22;
		month = 2;
		year = 2001;
		id = 1;
		initComponents();
		setSize(560, 500);
		setLocation(200, 150);

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">
	private void initComponents() {
		jScrollPane1 = new javax.swing.JScrollPane();
		jTextArea1 = new javax.swing.JTextArea();
		jButton1 = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		menuBar = new javax.swing.JMenuBar();
		fileMenu = new javax.swing.JMenu();
		openMenuItem1 = new javax.swing.JMenuItem();
		openMenuItem2 = new javax.swing.JMenuItem();
		openMenuItem3 = new javax.swing.JMenuItem();
		saveAsMenuItem = new javax.swing.JMenuItem();
		exitMenuItem = new javax.swing.JMenuItem();
		editMenu = new javax.swing.JMenu();
		setParam = new javax.swing.JMenuItem();
		setParam2 = new javax.swing.JMenuItem();
		setParam3 = new javax.swing.JMenuItem();
		helpMenu = new javax.swing.JMenu();
		aboutMenuItem = new javax.swing.JMenuItem();

		getContentPane().setLayout(null);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Trajectory Compression under Network Constraints");
		setResizable(false);

		jTextArea1.setColumns(35);
		jTextArea1.setEditable(false);
		jTextArea1.setRows(5);
		jScrollPane1.setViewportView(jTextArea1);

		getContentPane().add(jScrollPane1);
		jScrollPane1.setBounds(10, 110, 535, 325);

		jButton1.setText("Run!");
		jButton1.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		getContentPane().add(jButton1);
		jButton1.setBounds(380, 20, 69, 45);

		jLabel1.setText("Results");
		getContentPane().add(jLabel1);
		jLabel1.setBounds(20, 90, 60, 16);

		jLabel2.setText("Trajectory File: <None>");
		getContentPane().add(jLabel2);
		jLabel2.setBounds(20, 10, 280, 16);

		jLabel3.setText("Map Nodes File: <None>");
		getContentPane().add(jLabel3);
		jLabel3.setBounds(20, 30, 280, 16);

		jLabel4.setText("Map Edges File: <None>");
		getContentPane().add(jLabel4);
		jLabel4.setBounds(20, 50, 280, 16);

		fileMenu.setText("File");
		openMenuItem1.setText("Open Trajectory");
		openMenuItem1.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openMenuItem1ActionPerformed(evt);
			}
		});

		fileMenu.add(openMenuItem1);

		openMenuItem2.setText("Open Map Nodes");
		openMenuItem2.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openMenuItem2ActionPerformed(evt);
			}
		});

		fileMenu.add(openMenuItem2);

		openMenuItem3.setText("Open Map Edges");
		openMenuItem3.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openMenuItem3ActionPerformed(evt);
			}
		});

		fileMenu.add(openMenuItem3);

		saveAsMenuItem.setText("Save As...");
		saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveAsMenuItemActionPerformed(evt);
			}
		});

		fileMenu.add(saveAsMenuItem);

		exitMenuItem.setText("Exit");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});

		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);

		editMenu.setText("Edit");
		setParam.setText("Set Map Matching Parameters");
		setParam.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setParamActionPerformed(evt);
			}
		});

		editMenu.add(setParam);

		setParam2.setText("Set Trajectory Parameters");
		setParam2.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setParam2ActionPerformed(evt);
			}
		});

		editMenu.add(setParam2);

		setParam3.setText("Set Compression Parameters");
		setParam3.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setParam3ActionPerformed(evt);
			}
		});

		editMenu.add(setParam3);

		menuBar.add(editMenu);

		helpMenu.setText("Help");
		aboutMenuItem.setText("About");
		aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aboutMenuItemActionPerformed(evt);
			}
		});

		helpMenu.add(aboutMenuItem);

		menuBar.add(helpMenu);

		setJMenuBar(menuBar);

		pack();
	}// </editor-fold>

	private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		javax.swing.JOptionPane j = null;
		JOptionPane
				.showMessageDialog(
						this,
						"Created by User83\n\nFor any comments/bugs/suggestions contact me at\n  user83 (at) tellas (dot) gr\n\n  v0.9",
						"About", JOptionPane.PLAIN_MESSAGE);
	}

	private void setParamActionPerformed(java.awt.event.ActionEvent evt) {
		String s;
		double temp;
		int t;
		javax.swing.JOptionPane j = null;

		s = (String) JOptionPane.showInputDialog(this, "ma", "Parameters",
				JOptionPane.PLAIN_MESSAGE, null, null, Double.toString(ma));
		if ((temp = Double.parseDouble(s)) > 0) {
			ma = temp;
		}
		s = (String) JOptionPane.showInputDialog(this, "md", "Parameters",
				JOptionPane.PLAIN_MESSAGE, null, null, Double.toString(md));
		if ((temp = Double.parseDouble(s)) > 0) {
			md = temp;
		}
		s = (String) JOptionPane.showInputDialog(this, "na", "Parameters",
				JOptionPane.PLAIN_MESSAGE, null, null, Double.toString(na));
		if ((temp = Double.parseDouble(s)) > 0) {
			na = temp;
		}
		s = (String) JOptionPane.showInputDialog(this, "nd", "Parameters",
				JOptionPane.PLAIN_MESSAGE, null, null, Double.toString(nd));
		if ((temp = Double.parseDouble(s)) > 0) {
			nd = temp;
		}
		s = (String) JOptionPane.showInputDialog(this, "a", "Parameters",
				JOptionPane.PLAIN_MESSAGE, null, null, Double.toString(a));
		if ((temp = Double.parseDouble(s)) > 0) {
			a = temp;
		}
		s = (String) JOptionPane.showInputDialog(this, "look ahead",
				"Parameters", JOptionPane.PLAIN_MESSAGE, null, null,
				Integer.toString(look));
		if ((t = Integer.parseInt(s)) > 0) {
			look = t;
		}

	}

	private void setParam2ActionPerformed(java.awt.event.ActionEvent evt) {
		String s;

		int t;
		javax.swing.JOptionPane j = null;
		s = (String) JOptionPane.showInputDialog(this, "object id",
				"Parameters", JOptionPane.PLAIN_MESSAGE, null, null,
				Integer.toString(obj));
		if ((t = Integer.parseInt(s)) > 0) {
			obj = t;
		}
		s = (String) JOptionPane.showInputDialog(this, "trajectory id",
				"Parameters", JOptionPane.PLAIN_MESSAGE, null, null,
				Integer.toString(trajid));
		if ((t = Integer.parseInt(s)) > 0) {
			trajid = t;
		}
		s = (String) JOptionPane.showInputDialog(this, "day", "Parameters",
				JOptionPane.PLAIN_MESSAGE, null, null, Integer.toString(day));
		if ((t = Integer.parseInt(s)) > 0) {
			day = t;
		}
		s = (String) JOptionPane.showInputDialog(this, "month", "Parameters",
				JOptionPane.PLAIN_MESSAGE, null, null, Integer.toString(month));
		if ((t = Integer.parseInt(s)) > 0) {
			month = t;
		}
		s = (String) JOptionPane.showInputDialog(this, "year", "Parameters",
				JOptionPane.PLAIN_MESSAGE, null, null, Integer.toString(year));
		if ((t = Integer.parseInt(s)) > 0) {
			year = t;
		}
	}

	private void setParam3ActionPerformed(java.awt.event.ActionEvent evt) {
		String s;
		double temp;

		javax.swing.JOptionPane j = null;
		s = (String) JOptionPane.showInputDialog(this, "thr", "Parameters",
				JOptionPane.PLAIN_MESSAGE, null, null, Double.toString(thr));
		if ((temp = Double.parseDouble(s)) > 0) {
			thr = temp;
		}

	}

	private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		javax.swing.JFileChooser jFileChooser4 = new javax.swing.JFileChooser();
		int returnVal = jFileChooser4.showSaveDialog(this);
		if (returnVal == javax.swing.JFileChooser.CANCEL_OPTION) {
			return;
		}
		SaveF d = new SaveF(obj, trajid, day, month, year,
				jFileChooser4.getSelectedFile());
		if (!newtraj2.isEmpty()) {
			try {
				d.save(newtraj2);
				jTextArea1.append("\nOutput saved\n");
			} catch (IOException e) {
			}
		} else {
			jTextArea1.append("\nNothing to save yet...\n");
		}
	}

	private void openMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
		javax.swing.JFileChooser jFileChooser3 = new javax.swing.JFileChooser();
		int returnVal = jFileChooser3.showOpenDialog(this);
		if (returnVal == javax.swing.JFileChooser.CANCEL_OPTION) {
			return;
		}
		edgesf c = new edgesf(jFileChooser3.getSelectedFile());
		if (!map.isEmpty()) {
			try {
				c.addN(map);
			} catch (IOException e) {
			}
			if (!map.isEmpty()) {
				jLabel4.setText("Map Edges File: "
						+ jFileChooser3.getSelectedFile().getName());
			}
		} else {
			jTextArea1.append("\nFirst choose Node File\n");
		}
	}

	private void openMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
		javax.swing.JFileChooser jFileChooser2 = new javax.swing.JFileChooser();
		int returnVal = jFileChooser2.showOpenDialog(this);
		if (returnVal == javax.swing.JFileChooser.CANCEL_OPTION) {
			return;
		}
		edgesf b = new edgesf(jFileChooser2.getSelectedFile());
		try {
			map.removeAllElements();
			b.addEdges(map);
		} catch (IOException e) {
		}
		if (!map.isEmpty()) {
			jLabel3.setText("Map Nodes File: "
					+ jFileChooser2.getSelectedFile().getName());
		} else {
			jTextArea1.append("\nError reading Node File\n");
		}
	}

	private void openMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
		javax.swing.JFileChooser jFileChooser1 = new javax.swing.JFileChooser();
		int returnVal = jFileChooser1.showOpenDialog(this);
		if (returnVal == javax.swing.JFileChooser.CANCEL_OPTION) {
			return;
		}
		pointsf a = new pointsf(obj, trajid, day, month, year,
				jFileChooser1.getSelectedFile());
		try {
			traj.removeAllElements();
			a.addPoints(traj);
		} catch (IOException e) {
		}
		if (!traj.isEmpty()) {
			jLabel2.setText("Trajectory File: "
					+ jFileChooser1.getSelectedFile().getName());
		} else {
			jTextArea1.append("\nError reading Trajectory File\n");
		}
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		if (map.isEmpty()) {
			jTextArea1.append("\nPlease load all the files\n");
		} else {
			jTextArea1.setText("");
			long mmt;

			// File output = new File("d:\\outputW.txt");
			try {
				// BufferedWriter outFile = new BufferedWriter(new
				// FileWriter(output));

				int ne = 40;
				int np = 160;

				Vector[] in = new Vector[100];
				Vector[] out = new Vector[100];
				Vector[] outon = new Vector[100];
				Vector[] out2 = new Vector[100];
				for (int i = 0; i < 100; i++) {
					out[i] = new Vector();
					in[i] = new Vector();
					outon[i] = new Vector();
					out2[i] = new Vector();
				}
				System.out.print("Creating Trajectories... ");

				int max = new File("./input/").listFiles().length;
				File[] list = new File("./input/").listFiles();
				for (File f : list) {
					// System.out.println(f.toString());
					// for (int n = 0; n < max; n++) {
					// System.out.println(n);
					// outFile.write("\r\nTraj:" + n + "\t");

					int objid = 0, tra = 0, d = 0, mo = 0, ye = 0, end = 0;
					double x = 0, y = 0, compr1 = 0, compr2 = 0;
					long tid = 1;
					File trajFile = new File(f.toString());
					if (trajFile.exists()) {

						BufferedReader inFile = new BufferedReader(
								new FileReader(trajFile));

						String line = inFile.readLine();
						line = inFile.readLine();
						trajN.removeAllElements();
						while (line != null) {
							// outFile.write("\r\nTraj:"+end+"\t");
							java.util.StringTokenizer st = new java.util.StringTokenizer(
									line, " ;:/,\t");

							// if(line!= "")
							// {
							// objid = Integer.parseInt(st.nextToken());
							// trajid = Integer.parseInt(st.nextToken());
							x = Double.parseDouble(st.nextToken());
							y = Double.parseDouble(st.nextToken());
							// year=Integer.parseInt(st.nextToken());
							// month=Integer.parseInt(st.nextToken());
							// day=Integer.parseInt(st.nextToken());
							// tid=3600*Integer.parseInt(st.nextToken());
							// tid+=60*Integer.parseInt(st.nextToken());
							// tid+=Integer.parseInt(st.nextToken());
							// line = inFile.readLine();
							// }
							obj = objid;
							tra = trajid;
							ye = year;
							mo = month;
							d = day;
							// traj.addElement(new point(x,y,tid));
							// System.out.println(x+", "+y);

							// while(objid==obj&&trajid==tra&&d==day&&mo==month&&ye==year&&line!=null)
							// {
							// st = new
							// java.util.StringTokenizer(line," ;:/,\t");
							tid += 10;
							trajN.addElement(new point(x, y, tid));
							// objid = Integer.parseInt(st.nextToken());
							// trajid = Integer.parseInt(st.nextToken());
							// x=Double.parseDouble(st.nextToken());
							// y=Double.parseDouble(st.nextToken());

							// year=Integer.parseInt(st.nextToken());
							// month=Integer.parseInt(st.nextToken());
							// day=Integer.parseInt(st.nextToken());
							// tid=3600*Integer.parseInt(st.nextToken());
							// tid+=60*Integer.parseInt(st.nextToken());
							// tid+=Integer.parseInt(st.nextToken());

							line = inFile.readLine();
							// System.out.println(x+", "+y);
							// }

							// if(end<3400)
							// {
							// end++;
							// continue;
							// }
							// ////////////////////////////////////////////////////////
							// Rand r = new Rand(map);
							// traj.removeAllElements();
							// initial.removeAllElements();
							// r.random(traj, ne, np, initial, 15);

							// NoiseAdd noise = new NoiseAdd(traj);
							// trajN.removeAllElements();
							// noise.add(trajN);

							// int end=0;

							// while(end<100){
							// ////////////////////////////////////////////////////////

							// System.out.println(end);

							/*
							 * MapM mm=new MapM(md,ma,nd,na,a,look);
							 * initial.removeAllElements();
							 * mm.MM(trajN,map,initial);
							 * if(initial.size()<3||initial.size()>40){ //
							 * end++; continue; }
							 */
						}
						long startTime2;
						long startTime3;
						long endTime2;
						long endTime3;

						// int i;
						String str;

						// ////////////////////////////////

						/*
						 * final int[][] pr = new
						 * int[initial.size()][map.size()];//Vector with the
						 * shortest paths final double[][] di = new
						 * double[initial.size()][map.size()];//Vector with the
						 * shortest paths' distances
						 * 
						 * for (i = 0; i < initial.size(); i++) {
						 * 
						 * int count = 0; DataInputStream din = null; File
						 * nodeFile = new File("C:\\aspl.bin");
						 * 
						 * FileInputStream fin = new FileInputStream(nodeFile);
						 * din = new DataInputStream(new
						 * BufferedInputStream(fin));
						 * 
						 * for (int k = 0; k < ((point)
						 * initial.elementAt(i)).id; k++) { if (map.elementAt(k)
						 * == null) { count++; } }
						 * 
						 * din.skipBytes((((point) initial.elementAt(i)).id -
						 * count) * 20 * map.size());
						 * 
						 * 
						 * 
						 * for (int j = 0; j < map.size(); j++) {
						 * 
						 * int it = din.readInt();
						 * 
						 * int jt = din.readInt(); int pt = din.readInt();
						 * double dt = din.readDouble(); pr[i][j] = pt; di[i][j]
						 * = dt;
						 * 
						 * }
						 * 
						 * 
						 * fin.close();
						 * 
						 * }
						 */

						long startTime1 = System.currentTimeMillis();
						MapM mm0 = new MapM(md, ma, nd, na, a, look);
						matrix.removeAllElements();
						double wp1 = mm0.MM(trajN, map, matrix);
						// if (matrix.size() < 3||matrix.size()>40) {
						// continue;
						// }
						long endTime1 = System.currentTimeMillis();

						// ////////////////

						if (!matrix.isEmpty() && matrix != null) {
							File oFile = new File(f.toString().replace(
									"c:\\input\\", "c:\\output\\"));
							BufferedWriter outFile = new BufferedWriter(
									new FileWriter(oFile));
							// String line;
							for (int i = 0; i < matrix.size(); i += 2) {
								/*
								 * line=(obj+";"+traj+";"+d+";"+m+";"+ye+";"
								 * +((int
								 * )((point)A.elementAt(i)).t/3600)+";"+((int
								 * )((point
								 * )A.elementAt(i)).t%3600)/60+";"+(int)(
								 * (point)A.elementAt(i)).t%60+";0;0;"
								 * +((point)A.elementAt(i)).x+";"
								 * +((point)A.elementAt(i)).y +"\r\n");
								 */
								// line=(((edge)matrix.elementAt(i)).x+","+((edge)matrix.elementAt(i)).y+"\r\n");
								line = (matrix.elementAt(i).id + " ");
								outFile.write(line);
							}
							line = (matrix.elementAt(matrix.size() - 1).id + "\r\n");
							outFile.write(line);
							outFile.close();
						}

						// ///////////////////

						/*
						 * DataOutputStream dout = null; FileOutputStream fout =
						 * new FileOutputStream("c:\\data\\test.txt", true);
						 * dout = new DataOutputStream(fout);
						 * 
						 * for(int i=0;i<matrix.size();i+=2){ //ids asd=new
						 * ids((
						 * (edge)matrix.elementAt(i+1)).id,((edge)matrix.elementAt
						 * (i+1)).id);
						 * //System.out.println("Node: "+((edge)matrix
						 * .elementAt(i)).id); //int flag=0; //for(int
						 * j=0;j<((edge
						 * )map.elementAt(((edge)matrix.elementAt(i))
						 * .id)).nn.size();j++){ //
						 * System.out.println("Checking if "
						 * +((ids)((edge)map.elementAt
						 * (((edge)matrix.elementAt(i)
						 * ).id)).nn.elementAt(j)).id+
						 * " = "+((edge)matrix.elementAt(i+1)).id); //
						 * if(((ids)(
						 * (edge)map.elementAt(((edge)matrix.elementAt(
						 * i)).id)).nn
						 * .elementAt(j)).id==((edge)matrix.elementAt(i+1)).id){
						 * // flag=1; // } // }
						 * //if(!((edge)map.elementAt(((edge
						 * )matrix.elementAt(i)).id)).nn.contains(asd)){
						 * //if(flag==0){ // System.out.println("skata"); //
						 * return; // } dout.writeInt(obj); dout.writeInt(tra);
						 * dout.writeInt(((edge)matrix.elementAt(i)).id);
						 * dout.writeLong((long)((edge)matrix.elementAt(i)).x);
						 * dout.writeLong((long)((edge)matrix.elementAt(i)).y);
						 * } dout.close();
						 */
						/*
						 * 
						 * in[end].addAll((Vector)matrix.clone());
						 * 
						 * mmt = endTime1 - startTime1;
						 * 
						 * final int [][] prc= new int
						 * [matrix.size()][map.size()];//Vector with the
						 * shortest paths final double [][] dic= new double
						 * [matrix.size()][map.size()];//Vector with the
						 * shortest paths' distances
						 * for(i=0;i<matrix.size();i++)
						 * prc[i]=dijkstra(map,((point)matrix.elementAt(i)).id,
						 * dic[i],false);
						 * 
						 * 
						 * double wp = 0; int rp = 15;
						 * ///////////////////////////////////////////
						 * 
						 * //outFile.write("MMTC-App\t");
						 * 
						 * //for (double par = 0.005; par < 1.05; par *= 2) {
						 * 
						 * startTime1 = System.currentTimeMillis(); mapComp mc =
						 * new mapComp(map);
						 * 
						 * comptraj.removeAllElements();
						 * //maxc.removeAllElements(); double par=2;
						 * mc.comp(matrix, comptraj, prc, dic, par); endTime1 =
						 * System.currentTimeMillis(); if(comptraj.size()<2){ //
						 * end++; in[end].removeAllElements(); continue; }
						 * compr1+= (((double) (matrix.size() -
						 * comptraj.size())) / (double) (matrix.size())); /*Dif
						 * dif1=new Dif(map); Dif dif2 = new Dif(map, 32998.75);
						 * double compr1 = (((double) (initial.size() -
						 * comptraj.size())) / (double) (initial.size())); if
						 * (compr1 < 0) { compr1 = 0; } if (comptraj.size() < 3)
						 * { continue; }
						 * 
						 * 
						 * 
						 * str = "\t" + dif2.diff(initial, comptraj, pr, 0) +
						 * "\t" + compr1 + "\t" + mmt + "\t" + (endTime1 -
						 * startTime1) + "\t"; //else
						 * 
						 * outFile.write(str);
						 * 
						 * out[end].addAll((Vector)comptraj.clone());
						 * 
						 * mapCompOn mc2 = new mapCompOn(map);
						 * comptraj.removeAllElements(); mc2.comp(matrix,
						 * comptraj, prc, dic, par, rp);
						 * outon[end].addAll((Vector)comptraj.clone()); compr2+=
						 * (((double) (matrix.size() - comptraj.size())) /
						 * (double) (matrix.size())); //} /*for (double par = 1;
						 * par <= 8; par *= 2) {
						 * 
						 * startTime1 = System.currentTimeMillis(); mapComp mc =
						 * new mapComp(map); comptraj.removeAllElements();
						 * maxc.removeAllElements();
						 * 
						 * mc.comp(matrix, comptraj, prc, dic, par, rp);
						 * endTime1 = System.currentTimeMillis();
						 * 
						 * Dif dif2 = new Dif(map, 32998.75);
						 * 
						 * 
						 * 
						 * double compr1 = (((double) (initial.size() -
						 * comptraj.size())) / (double) (initial.size())); if
						 * (compr1 < 0) { compr1 = 0; } if (comptraj.size() < 3)
						 * { continue; }
						 * 
						 * str = "\t" + dif2.diff(initial, comptraj, pr, 0) +
						 * "\t" + compr1 + "\t" + mmt + "\t" + (endTime1 -
						 * startTime1) + "\t";
						 * 
						 * outFile.write(str); }
						 * 
						 * outFile.write("\tMM+TC+MM"); for (i = 1; i < 10; i++)
						 * { if (i == 1) { thr = 0.001; } else if (i == 2) { thr
						 * = 0.005; } else if (i == 3) { thr = 0.01; } else if
						 * (i == 4) { thr = 0.02; } else if (i == 5) { thr =
						 * 0.04; } else if (i == 6) { thr = 0.05; } else if (i
						 * == 7) { thr = 0.06; } else if (i == 8) { thr = 0.08;
						 * } else if (i == 9) { thr = 0.1; } startTime1 =
						 * System.currentTimeMillis(); MapM mm3 = new MapM(md,
						 * ma, nd, na, a, look); matrix.removeAllElements();
						 * double wp3 = mm3.MM(trajN, map, matrix); endTime1 =
						 * System.currentTimeMillis(); if (matrix.size() < 3) {
						 * continue; } mmt = endTime1 - startTime1; startTime1 =
						 * System.currentTimeMillis();
						 * comptraj.removeAllElements(); TrajC ct = new
						 * TrajC(thr * l); comptraj.addElement((point)
						 * matrix.firstElement()); ct.DP(matrix, 0,
						 * matrix.size() - 1, comptraj);
						 * comptraj.addElement((point) matrix.lastElement());
						 * endTime1 = System.currentTimeMillis(); if
						 * (comptraj.size() < 2) { continue; } startTime2 =
						 * System.currentTimeMillis(); MapM mm2 = new MapM(md,
						 * ma, nd, na, a, look); newtraj2.removeAllElements();
						 * double wp5 = mm2.MM(comptraj, map, newtraj2);
						 * endTime2 = System.currentTimeMillis(); Dif dif4 = new
						 * Dif(map, 32998.75); double compr = (((double)
						 * (initial.size() - newtraj2.size())) / (double)
						 * (initial.size())); if (compr < 0) { compr = 0; } if
						 * (newtraj2.size() < 3) { continue; }
						 * 
						 * str = "\t" + dif4.diff(initial, newtraj2, pr, 0) +
						 * "\t" + compr + "\t" + mmt + "\t" + (endTime1 -
						 * startTime1) + "\t" + (endTime2 - startTime2);
						 * 
						 * jTextArea1.append(str); outFile.write(str); }
						 * 
						 * ///////////////////////////////////////////
						 * 
						 * outFile.write("\tTC+MM");
						 * 
						 * 
						 * 
						 * 
						 * 
						 * thr = 0.09;
						 * 
						 * startTime1 = System.currentTimeMillis();
						 * comptraj.removeAllElements(); TrajC ct3 = new
						 * TrajC(thr * l); comptraj.addElement((point)
						 * trajN.firstElement()); ct3.DP(trajN, 0, trajN.size()
						 * - 1, comptraj); comptraj.addElement((point)
						 * trajN.lastElement());
						 * 
						 * endTime1 = System.currentTimeMillis();
						 * 
						 * if (comptraj.size() < 2) {
						 * 
						 * continue; }
						 * 
						 * startTime2 = System.currentTimeMillis(); MapM mm23 =
						 * new MapM(md, ma, nd, na, a, look);
						 * newtraj3.removeAllElements(); double wp7 =
						 * mm23.MM(comptraj, map, newtraj3);
						 * 
						 * 
						 * out2[n].addAll((Vector)newtraj3.clone());
						 * 
						 * endTime2 = System.currentTimeMillis();
						 * 
						 * 
						 * 
						 * Dif dif5 = new Dif(map, 32998.75); double compr =
						 * (((double) (initial.size() - newtraj3.size())) /
						 * (double) (initial.size())); if (compr < 0) { compr =
						 * 0; } if (newtraj3.size() < 3) {
						 * //System.out.println("ok"); continue; }
						 * 
						 * str = "\t" + dif5.diff(initial, newtraj3, pr, 0) +
						 * "\t" + compr + "\t\t" + (endTime1 - startTime1) +
						 * "\t" + (endTime2 - startTime2);
						 * 
						 * jTextArea1.append(str); outFile.write(str); }
						 * ///////////////////////////////////////////
						 * 
						 * // outFile.flush(); //
						 * System.out.println(in[n].size()
						 * +", "+out[n].size()+", "+outon[n].size()); end++;
						 */
					}
				}
				System.out.println("Done!");
				// outFile.close();
				/*
				 * System.out.print("Calculating NN... ");
				 * /////////////////////////////////////////// double[][]
				 * inarr=new double[100][100]; double[][] outarr=new
				 * double[100][100]; double[][] outonarr=new double[100][100];
				 * 
				 * 
				 * for(int n=0;n<100;n++){
				 * 
				 * final int [][] pr= new int
				 * [in[n].size()][map.size()];//Vector with the shortest paths
				 * final double [][] di= new double
				 * [in[n].size()][map.size()];//Vector with the shortest paths'
				 * distances for(int i=0;i<in[n].size();i++)
				 * pr[i]=dijkstra(map,((point)in[n].elementAt(i)).id,
				 * di[i],true); Dif dif = new Dif(map); for(int m=0;m<100;m++){
				 * if(m!=n) inarr[n][m]=dif.diff(in[n], in[m], pr, 0); else
				 * inarr[n][m]=0; }
				 * 
				 * }
				 * 
				 * for(int n=0;n<100;n++){
				 * 
				 * final int [][] pr= new int
				 * [out[n].size()][map.size()];//Vector with the shortest paths
				 * final double [][] di= new double
				 * [out[n].size()][map.size()];//Vector with the shortest paths'
				 * distances for(int i=0;i<out[n].size();i++)
				 * pr[i]=dijkstra(map,((point)out[n].elementAt(i)).id,
				 * di[i],true); Dif dif = new Dif(map); for(int m=0;m<100;m++){
				 * if(m!=n) outarr[n][m]=dif.diff(out[n], out[m], pr, 0); else
				 * outarr[n][m]=0; }
				 * 
				 * }
				 * 
				 * 
				 * for(int n=0;n<100;n++){
				 * 
				 * final int [][] pr= new int
				 * [outon[n].size()][map.size()];//Vector with the shortest
				 * paths final double [][] di= new double
				 * [outon[n].size()][map.size()];//Vector with the shortest
				 * paths' distances for(int i=0;i<outon[n].size();i++)
				 * pr[i]=dijkstra(map,((point)outon[n].elementAt(i)).id,
				 * di[i],true); Dif dif = new Dif(map); for(int m=0;m<100;m++){
				 * if(m!=n) outonarr[n][m]=dif.diff(outon[n], outon[m], pr, 0);
				 * else outonarr[n][m]=0; }
				 * 
				 * } //System.out.println("In"); double counter15=0; double
				 * counter25=0; double counter110=0; double counter210=0; double
				 * counter115=0; double counter215=0;
				 * 
				 * int [] ids1=new int[15]; int [] ids2=new int[15]; int []
				 * ids3=new int[15];
				 * 
				 * for(int i=0;i<100;i++){ for(int k=0;k<15;k++) { double
				 * min1=Double.MAX_VALUE; double min2=Double.MAX_VALUE; double
				 * min3=Double.MAX_VALUE;
				 * 
				 * int id1=-1; int id2=-1; int id3=-1;
				 * 
				 * for(int j=0;j<100;j++) if(i!=j){ if(min1>inarr[i][j]){ id1=j;
				 * min1=inarr[i][j]; } if(min2>outarr[i][j]){ id2=j;
				 * min2=outarr[i][j]; } if(min3>outonarr[i][j]){ id3=j;
				 * min3=outonarr[i][j]; }
				 * 
				 * } inarr[i][id1]=Double.MAX_VALUE;
				 * outarr[i][id2]=Double.MAX_VALUE;
				 * outonarr[i][id3]=Double.MAX_VALUE;
				 * 
				 * ids1[k]=id1; ids2[k]=id2; ids3[k]=id3;
				 * 
				 * } for(int k=0;k<5;k++){ for(int l=0;l<5;l++){
				 * if(ids1[k]==ids2[l]) counter15++; if(ids1[k]==ids3[l])
				 * counter25++;
				 * 
				 * } }
				 * 
				 * for(int k=0;k<10;k++){ for(int l=0;l<10;l++){
				 * if(ids1[k]==ids2[l]) counter110++; if(ids1[k]==ids3[l])
				 * counter210++;
				 * 
				 * } }
				 * 
				 * for(int k=0;k<15;k++){ for(int l=0;l<15;l++){
				 * if(ids1[k]==ids2[l]) counter115++; if(ids1[k]==ids3[l])
				 * counter215++;
				 * 
				 * } }
				 * 
				 * } System.out.println("Done!");
				 * System.out.println("Success offline for 5: "
				 * +(counter15/5)+"% Compression: "+(compr1/100));
				 * System.out.println
				 * ("Success online for 5: "+(counter25/5)+"% Compression: "
				 * +(compr2/100));
				 * System.out.println("Success offline for 10: "+
				 * (counter110/10)+"% Compression: "+(compr1/100));
				 * System.out.println
				 * ("Success online for 10: "+(counter210/10)+"% Compression: "
				 * +(compr2/100));
				 * System.out.println("Success offline for 15: "+
				 * (counter115/15)+"% Compression: "+(compr1/100));
				 * System.out.println
				 * ("Success online for 15: "+(counter215/15)+"% Compression: "
				 * +(compr2/100));
				 * 
				 * 
				 * 
				 * 
				 * /*System.out.println("Out"); for(int i=99;i<100;i++){ for(int
				 * k=0;k<5;k++) { double min=Double.MAX_VALUE; int id=-1;
				 * for(int j=0;j<100;j++) if(i!=j){ if(min>outarr[i][j]) id=j; }
				 * outarr[i][id]=Double.MAX_VALUE; System.out.println(id); } }
				 * System.out.println("Outon"); for(int i=99;i<100;i++){ for(int
				 * k=0;k<5;k++) { double min=Double.MAX_VALUE; int id=-1;
				 * for(int j=0;j<100;j++) if(i!=j){ if(min>outonarr[i][j]) id=j;
				 * } outonarr[i][id]=Double.MAX_VALUE; System.out.println(id); }
				 * }
				 */
				/*
				 * File output1 = new File("c:\\in.txt"); File output2 = new
				 * File("c:\\out.txt"); File output3 = new
				 * File("c:\\outon.txt");
				 * 
				 * BufferedWriter outFile1 = new BufferedWriter(new
				 * FileWriter(output1)); BufferedWriter outFile2 = new
				 * BufferedWriter(new FileWriter(output2)); BufferedWriter
				 * outFile3 = new BufferedWriter(new FileWriter(output3));
				 * 
				 * for(int i=0;i<100;i++){ for(int j=0;j<100;j++){
				 * outFile1.write(inarr[i][j]+"\t");
				 * outFile2.write(outarr[i][j]+"\t");
				 * outFile3.write(outonarr[i][j]+"\t"); }
				 * outFile1.write("\r\n"); outFile2.write("\r\n");
				 * outFile3.write("\r\n"); } outFile1.close(); outFile2.close();
				 * outFile3.close();
				 */

				// /////////////////////////////////////////

			} catch (IOException e) {
			}
			jTextArea1.append("Done!");

		}
	}

	/*
	 * private static int[] dijkstra(Vector G, int s, double[] dist, boolean
	 * count) { //final int [] dist = new int [G.size()]; // shortest known
	 * distance from "s" //pred = new int [G.size()]; // preceeding node in path
	 * final int[] pred = new int[G.size()]; final boolean[] visited = new
	 * boolean[G.size()]; // all false initially
	 * //System.out.println(G.size()+" "); for (int i = 0; i < dist.length; i++)
	 * { dist[i] = Double.MAX_VALUE; } dist[s] = 0;
	 * 
	 * for (int i = 0; i < dist.length; i++) { final int next = minVertex(dist,
	 * visited); //System.out.println(next+" "); if (next == -1) { continue; }
	 * visited[next] = true;
	 * 
	 * // The shortest path to next is dist[next] and via pred[next].
	 * //System.out.println(next); Vector n = (Vector) ((edge)
	 * G.elementAt(next)).nn.clone(); for (int j = 0; j < n.size(); j++) { final
	 * int v = ((ids) n.elementAt(j)).id; final double d; if (count == true) { d
	 * = dist[next] + Math.sqrt(Math.pow(((edge) G.elementAt(next)).x - ((edge)
	 * G.elementAt(v)).x, 2) + Math.pow(((edge) G.elementAt(next)).y - ((edge)
	 * G.elementAt(v)).y, 2)); } else { d = dist[next] + 1; } if (dist[v] > d) {
	 * dist[v] = d; pred[v] = next; } } } return pred; }
	 * 
	 * private static int minVertex(double[] dist, boolean[] v) { double x =
	 * Double.MAX_VALUE; int y = -1; // graph not connected, or no unvisited
	 * vertices for (int i = 0; i < dist.length; i++) { if (!v[i] && dist[i] <
	 * x) { y = i; x = dist[i]; } } return y; }
	 */

	private int[] dijkstra(Vector G, int s, double[] dist, boolean count) {

		int[] pred = new int[G.size()]; // preceeding node in path
		Vector a = new Vector();
		boolean[] visited = new boolean[G.size()]; // all false initially
		// System.out.println(G.size()+" ");
		for (int i = 0; i < dist.length; i++) {
			dist[i] = Double.MAX_VALUE;
			pred[i] = -1;
		}
		heapEntry h = new heapEntry();
		// if(fin==false){
		// int pos=mapos(G,s);
		int pos = s;
		dist[pos] = 0;
		h.dist = 0;
		h.pos = pos;
		a.addElement(h);

		// }
		// else{
		// dist[new_mapos(G,s)] = 0;
		// a.addElement(new Integer(new_mapos(G,s)));
		// destination=new_mapos(G,dest);
		// }

		for (int i = 0; i < dist.length; i++) {
			int next = minVertex(a, visited);

			if (next == -1) {
				continue;
			}
			visited[next] = true;

			for (int j = 0; j < ((edge) G.elementAt(next)).nn.size(); j++) {
				int v;

				v = ((edge) G.elementAt(next)).nn.elementAt(j).pos;

				if (v == -1)
					continue;
				double w;
				if (count == true) {
					w = dist[next]
							+ Math.sqrt(Math.pow(((edge) G.elementAt(next)).x
									- ((edge) G.elementAt(v)).x, 2)
									+ Math.pow(((edge) G.elementAt(next)).y
											- ((edge) G.elementAt(v)).y, 2));
				} else {
					w = dist[next] + 1;
				}
				double d;
				d = dist[next] + w;

				if (dist[v] > d) {
					dist[v] = d;
					pred[v] = next;
					if (visited[v] == false) {
						heapEntry new_h = new heapEntry();
						new_h.pos = v;
						new_h.dist = d;
						addEl(a, new_h);
					}
					// pred[v] = next;
				}
			}
		}
		return pred;
	}

	private static int minVertex(Vector a, boolean[] visited) {
		int y = -1, i = a.size() - 1;
		if (a.size() == 0)
			return y;
		else {
			while (visited[((heapEntry) a.elementAt(i)).pos] == true) {
				a.removeElementAt(i);
				i--;
				if (i < 0)
					return y;
			}
			y = ((heapEntry) a.elementAt(i)).pos;
			a.removeElementAt(i);

			return y;

		}

	}

	private void addEl(Vector a, heapEntry h) {
		int first = 0;
		int last = a.size() - 1;
		int med = 0;
		if (a.size() > 0) {
			if (h.dist >= ((heapEntry) a.firstElement()).dist)
				a.insertElementAt(h, 0);
			else if (h.dist < ((heapEntry) a.lastElement()).dist)
				a.addElement(h);
			else {
				med = (first + last) / 2;
				do {

					if (h.dist > ((heapEntry) a.elementAt(med)).dist)
						last = med;
					else
						first = med + 1;
					med = (first + last) / 2;
				} while (first < last);

				a.insertElementAt(h, med);
			}
		} else
			a.addElement(h);

	}

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(0);
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new app().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify
	private javax.swing.JMenuItem aboutMenuItem;
	private javax.swing.JMenu editMenu;
	private javax.swing.JMenuItem exitMenuItem;
	private javax.swing.JMenu fileMenu;
	private javax.swing.JMenu helpMenu;
	private javax.swing.JButton jButton1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextArea jTextArea1;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenuItem openMenuItem1;
	private javax.swing.JMenuItem openMenuItem2;
	private javax.swing.JMenuItem openMenuItem3;
	private javax.swing.JMenuItem saveAsMenuItem;
	private javax.swing.JMenuItem setParam;
	private javax.swing.JMenuItem setParam2;
	private javax.swing.JMenuItem setParam3;
	// End of variables declaration
}