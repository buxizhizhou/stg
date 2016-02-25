/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 从数据库中，将数据导入到IndoorSTG
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.spatial.geometry.*;
import oracle.spatial.util.*;
import oracle.sql.STRUCT;

/**
 *
 * @author hello
 */
public class import_to_stg_fromDB {
    static int MNUM=100;//缩放比例
    static int YLEN=565;//显示板的竖直高度

    //格式：类型，坐标（0，房间左下角坐标，宽度，高度；4，线段起点坐标，线段重点坐标）
    
    public static void connect_database() throws InstantiationException, IllegalAccessException, SQLException{
           //建立数据库连接   
           String Driver="oracle.jdbc.driver.OracleDriver";    //连接数据库的方法    
           String URL="jdbc:oracle:thin:@127.0.0.1:1521:cad";    //indoor为数据库的SID    
           String Username="cadadmin";    //用户名               
           String Password="cad";    //密码    
           String tableName=null;
           try {
                   Class.forName(Driver).newInstance();    //加载数据库驱动
                   Connection con=DriverManager.getConnection(URL,Username,Password);  
                   if(!con.isClosed())
                       System.out.println("Succeeded connecting to the Database!");
                   //Statement stmt=con.createStatement();
                   
                   ArrayList<Graph> tgraphs=new ArrayList();//所有图元集合，最后通过save函数保存
                   tableName="room2";//房间表
                   String sql_rm="select * from "+tableName;
                   System.out.println("Executing query:'"+sql_rm+"'");
                   Statement stmt=con.createStatement();
                   ResultSet rms=stmt.executeQuery(sql_rm);
                   while(rms.next())
                   {
                       //得到每个房间的坐标
                       int rm_id=Integer.parseInt(rms.getString(1));
                       STRUCT rmObject=(STRUCT)rms.getObject(2);
                       JGeometry rm_geom=JGeometry.load(rmObject);
                       double zbords[]=rm_geom.getOrdinatesArray();//得到房间多边形的坐标
                       int ordinatesSize=(rm_geom.getNumPoints()+1)*2;//多边形点的个数，以求得坐标个数
                       int zxjX=100000000,zxjY=100000000;//房间左下角的坐标，即最小的x、y值
                       int maxX=-100000000,maxY=-10000000;//最大x、y值
                       for(int i=0;i<ordinatesSize-2;++i)
                       {
                           int tempInt=(int)zbords[i];
                           if(i%2==0)
                           {
                               zxjX=zxjX<tempInt?zxjX:tempInt;
                               maxX=maxX>tempInt?maxX:tempInt;
                           }                           
                           else
                           {
                               zxjY=zxjY<tempInt?zxjY:tempInt;
                               maxY=maxY>tempInt?maxY:tempInt;
                           }
                       }
                       maxX=cast(maxX);
                       zxjX=cast(zxjX);
                       maxY=cast(maxY);
                       zxjY=cast(zxjY);
                       int wide=maxX-zxjX;//宽度
                       int high=maxY-zxjY;//高度
                       
                       boolean iscontext=false;
                       Color fillc=null, borderc=Color.BLACK;
                       int floor=0;
                       int type=0;//0是房间，4是边界
                       Graph tgph=new Graph(floor,0,iscontext,fillc,borderc,zxjX,YLEN-zxjY-high,wide,high,"");//坐标变换，注意这里别错了。
                       tgraphs.add(tgph);
                   }
                   stmt.close();
                   
                   tableName="lines";//边界线表
                   String sql_lns="select * from "+tableName;
                   System.out.println("Executing query:'"+sql_lns+"'");
                   Statement stmt2=con.createStatement();
                   ResultSet rms2=stmt2.executeQuery(sql_lns);
                   while(rms2.next())
                   {
                       //得到每个边界线的坐标
                       int rm_id=Integer.parseInt(rms2.getString(1));
                       STRUCT rmObject=(STRUCT)rms2.getObject(2);
                       JGeometry rm_geom=JGeometry.load(rmObject);
                       double zbords[]=rm_geom.getOrdinatesArray();//得到线的坐标
                       
                       int qx=(int)zbords[0];
                       int qy=(int)zbords[1];
                       int zx=(int)zbords[2];
                       int zy=(int)zbords[3];
                       
                       qx=cast(qx);
                       qy=cast(qy);
                       zx=cast(zx);
                       zy=cast(zy);
                       
                       int YLEN=565;//显示板的竖直高度
                       boolean iscontext=false;
                       Color fillc=null, borderc=Color.BLACK;
                       int floor=0;
                       int type=0;//0是房间，4是边界
                       Graph tgph=new Graph(floor,4,iscontext,fillc,borderc,qx,YLEN-qy,zx,YLEN-zy,"");
                       tgraphs.add(tgph);
                   }
                   stmt2.close();
                   
                   con.close();  
                   
                   save(tgraphs);
               } catch (ClassNotFoundException ex) {
                   Logger.getLogger(import_to_stg_fromDB.class.getName()).log(Level.SEVERE, null, ex);
               }
    }
    
    public static int cast(int x)
    {
        return x/MNUM;
    }
    
    public static void save(ArrayList<Graph> tgraphs)
    {//将tgraphs中的图元保存到文件中
        //ArrayList<Graph> tgraphs = new ArrayList<Graph>();
        for (Graph g : tgraphs) {
            g.setCurrent(false);
        }
        
        File f=new File("test-yuan");
        try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(tgraphs);
                oos.flush();
                oos.close();
        } catch (Exception e) {
            System.out.println("File error!");
        }
    }
    
    public static void main(String[] args) throws SQLException, IOException, InstantiationException, IllegalAccessException {
        // TODO code application logic here
        connect_database();
    }
    
}
