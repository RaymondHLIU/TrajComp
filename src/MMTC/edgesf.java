/*
 * edgesf.java
 *
 * Created on 26  2007, 10:27 
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

public class edgesf {
	File nodeFile;

	/** Creates a new instance of edgesf */
	public edgesf(File name) {
		nodeFile = name;
	}

	public void addEdges(Vector A) throws IOException {
		int nod_id, num = 0;
		// A.setSize(31771);
		// A.ensureCapacity(20000);

		double x, y;

		/*
		 * DataInputStream din = null; try { FileInputStream fin = new
		 * FileInputStream(nodeFile); din = new DataInputStream(fin); while
		 * (true) { byte l=din.readByte(); //System.out.print(l+","); byte
		 * b[]=new byte[128]; din.read(b,0,l); long idt = din.readLong(); int xt
		 * = din.readInt(); int yt = din.readInt();
		 * //System.out.println(idt+","+xt+","+yt); A.setElementAt(new
		 * edge((int)idt,(double)xt,(double)yt),(int)idt); } } catch
		 * (EOFException ex) {
		 * 
		 * din.close(); } catch (IOException ex) { }
		 */
		if (nodeFile.exists()) {
			BufferedReader inFile = new BufferedReader(new FileReader(nodeFile));
			// DataOutputStream out=new DataOutputStream(new
			// FileOutputStream("c:\\data\\athens.node"));
			String line = inFile.readLine();
			// line=inFile.readLine();
			while (line != null) {
				num++;
				java.util.StringTokenizer st = new java.util.StringTokenizer(
						line, " n;:/,\t");
				nod_id = Integer.parseInt(st.nextToken());
				x = Double.parseDouble(st.nextToken());
				y = Double.parseDouble(st.nextToken());
				// if(y<1000)
				// y=Double.parseDouble(st.nextToken());
				// st.nextToken();
				// System.out.println(nod_id+","+x+","+y);
				// A.setElementAt(new edge(nod_id,x,y),nod_id);
				A.addElement(new edge(nod_id, x, y));
				line = inFile.readLine();
			}
			System.out.println("Number of nodes: " + num);
		}
		/*
		 * 
		 * 
		 * if (nodeFile.exists()) { BufferedReader inFile = new BufferedReader(
		 * new FileReader(nodeFile)); DataOutputStream out=new
		 * DataOutputStream(new FileOutputStream("c:\\data\\athens.node"));
		 * String line = inFile.readLine(); while (line!= null) {
		 * java.util.StringTokenizer st = new
		 * java.util.StringTokenizer(line,";:/");
		 * nod_id=Integer.parseInt(st.nextToken());
		 * x=Double.parseDouble(st.nextToken());
		 * y=Double.parseDouble(st.nextToken()); st.nextToken();
		 * A.setElementAt(new edge(nod_id,x,y),nod_id); line =
		 * inFile.readLine();
		 * 
		 * 
		 * String name=String.valueOf(nod_id); long id=nod_id; int x1=(int)x;
		 * int y1=(int)y; //System.out.println(x1+","+y1);
		 * 
		 * try { byte l = (byte)name.length(); out.writeByte(l); if (l > 0)
		 * out.write(name.getBytes()); out.writeLong(id); out.writeInt(x1);
		 * out.writeInt(y1);
		 * 
		 * } catch (IOException ex) {
		 * 
		 * } } inFile.close(); out.close(); }
		 */
	}

	public void addN(Vector A) throws IOException {
		int edge_id, id1, id2, num = 0;

		/*
		 * DataInputStream din = null; try { FileInputStream fin = new
		 * FileInputStream(nodeFile); din = new DataInputStream(fin); while
		 * (true) { long idt1 = din.readLong(); long idt2 = din.readLong(); byte
		 * l=din.readByte(); //System.out.print(l+","); byte b[]=new byte[128];
		 * din.read(b,0,l); long idt = din.readLong(); int edgeclass =
		 * din.readInt(); //int yt = din.readInt();
		 * //System.out.println(idt+","+xt+","+yt);
		 * ((edge)A.elementAt((int)idt1)).addn((int)idt2,(int)idt2);
		 * ((edge)A.elementAt((int)idt2)).addn((int)idt1,(int)idt1); } } catch
		 * (EOFException ex) { din.close(); } catch (IOException ex) { }
		 */
		if (nodeFile.exists()) {
			BufferedReader inFile = new BufferedReader(new FileReader(nodeFile));
			// DataOutputStream out=new DataOutputStream(new
			// FileOutputStream("c:\\data\\athens.edge"));
			String line = inFile.readLine();
			// line=inFile.readLine();
			while (line != null) {
				num++;
				java.util.StringTokenizer st = new java.util.StringTokenizer(
						line, " n;:/,\t");
				edge_id = Integer.parseInt(st.nextToken());
				id1 = Integer.parseInt(st.nextToken());
				id2 = Integer.parseInt(st.nextToken());
				// st.nextToken();
				// int flag=0;
				// for(int z=0;z<((edge)A.elementAt(id1)).nn.size();z++)
				// if(((ids)((edge)A.elementAt(id1)).nn.elementAt(z)).id==id2)
				// flag=1;
				// if(flag==0){
				((edge) A.elementAt(id1)).addn(id2, id2);
				// ((edge)A.elementAt(id2)).addn(id1,id1);
				// }
				// else
				// num--;
				line = inFile.readLine();
			}
			System.out.println("Number of edges: " + (num));
		}
		/*
		 * if (nodeFile.exists()) { BufferedReader inFile = new BufferedReader(
		 * new FileReader(nodeFile)); DataOutputStream out=new
		 * DataOutputStream(new FileOutputStream("c:\\data\\athens.edge"));
		 * String line = inFile.readLine(); while (line!= null) {
		 * java.util.StringTokenizer st = new
		 * java.util.StringTokenizer(line,";:/");
		 * edge_id=Integer.parseInt(st.nextToken());
		 * id1=Integer.parseInt(st.nextToken());
		 * id2=Integer.parseInt(st.nextToken()); st.nextToken();
		 * ((edge)A.elementAt(id1)).addn(id2,id2);
		 * ((edge)A.elementAt(id2)).addn(id1,id1); line = inFile.readLine();
		 * 
		 * 
		 * String name=String.valueOf(edge_id); long nodeId1=id1; long
		 * nodeId2=id2; long id=edge_id; int edgeClass=5;
		 * 
		 * try { out.writeLong(nodeId1); out.writeLong(nodeId2); byte l =
		 * (byte)name.length(); out.writeByte(l); if (l > 0)
		 * out.write(name.getBytes()); out.writeLong(id);
		 * out.writeInt(edgeClass);
		 * 
		 * } catch (IOException ex) {
		 * 
		 * }
		 * 
		 * } inFile.close(); out.close(); }
		 */
	}
}
