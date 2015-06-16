/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indoorshow;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JFileChooser;

/**
 *
 * @author User
 */
public class testGraph {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int floor=0;
        int type=0;//0是房间，4是边界
        boolean iscontext=false;
        Color fillc=null, borderc=Color.BLACK;
        int cx=100;
        int cy=100;
        int cw=500;
        int ch=400;
        Graph tgraph=new Graph(floor, type, iscontext, fillc, borderc, cx, cy, cw, ch, "");
        Graph t2graph=new Graph(floor,4,iscontext,fillc,borderc,600,100,200,0,"");
        Graph t3graph=new Graph(floor,4,iscontext,fillc,borderc,0,0,50,0,"");//type为4的时候，即线段，依次为x1,y1,x2,y2
        Graph t4graph=new Graph(floor,4,iscontext,fillc,borderc,50,0,50,50,"");
        Graph t5graph=new Graph(floor,4,iscontext,fillc,borderc,50,50,0,50,"");
        Graph t6graph=new Graph(floor,4,iscontext,fillc,borderc,0,50,0,0,"");
        ArrayList<Graph> tgraphs = new ArrayList<Graph>();
        tgraphs.add(tgraph);
        tgraphs.add(t2graph);
        tgraphs.add(t3graph);
        tgraphs.add(t4graph);
        tgraphs.add(t5graph);
        tgraphs.add(t6graph);
        //static void save() {
        for (Graph g : tgraphs) {
            g.setCurrent(false);
        }
        //int x = jfc.showSaveDialog(envirSet);
        //if (x == JFileChooser.APPROVE_OPTION) {
            //File f = jfc.getSelectedFile();
            File f=new File("test");
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(tgraphs);
                oos.flush();
                oos.close();
            } catch (Exception e) {
            }
        //}
    }
}
