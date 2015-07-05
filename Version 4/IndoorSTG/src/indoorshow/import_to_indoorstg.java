/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indoorshow;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author hello
 */
public class import_to_indoorstg {
    static String filename="C:\\Users\\hello\\Documents\\NetBeansProjects\\extract\\readdxfmy2\\结果\\一层平面图结果\\stg_rooms.txt";
    static File file=new File(filename);
    //格式：类型，坐标（0，房间左下角坐标，宽度，高度；4，线段起点坐标，线段重点坐标）
    
    public static void save() throws FileNotFoundException, IOException
    {
        int MNUM=50;
        
        int floor=0;
        int type=0;//0是房间，4是边界
        boolean iscontext=false;
        Color fillc=null, borderc=Color.BLACK;
        ArrayList<Graph> tgraphs = new ArrayList<Graph>();
        
        FileReader fr=new FileReader(file);
        BufferedReader bfr= new BufferedReader(fr);
        String sline=null;
        while((sline=bfr.readLine())!=null)
        {
          String[] strs=sline.split(",");
          if(strs[0].compareTo("0")==0)
          {//是房间
            int cx=Integer.parseInt(strs[1])/MNUM;
            int cy=Integer.parseInt(strs[2])/MNUM;
            int cw=Integer.parseInt(strs[3])/MNUM;
            int ch=Integer.parseInt(strs[4])/MNUM;
            Graph tgph=new Graph(floor,0,iscontext,fillc,borderc,cx,cy,cw,ch,"");
            tgraphs.add(tgph);
          }
          else if(strs[0].compareTo("4")==0)
          {//边界
            int qx=Integer.parseInt(strs[1])/MNUM;
            int qy=Integer.parseInt(strs[2])/MNUM;
            int zx=Integer.parseInt(strs[3])/MNUM;
            int zy=Integer.parseInt(strs[4])/MNUM;
            Graph tgph=new Graph(floor,4,iscontext,fillc,borderc,qx,qy,zx,zy,"");
            tgraphs.add(tgph);
          }
        }
        
        /*
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
        */
        
        for (Graph g : tgraphs) {
            g.setCurrent(false);
        }
        
        File f=new File("test");
        try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(tgraphs);
                oos.flush();
                oos.close();
        } catch (Exception e) {
            
        }
        
    }
    
    public static void main(String[] args) throws SQLException, IOException {
        // TODO code application logic here
        save();
    }
    
}
