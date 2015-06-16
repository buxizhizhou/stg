/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indoorshow;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class GenerateData {

    ArrayList<Graph> cir = new ArrayList<Graph>();//RFID图像
    ArrayList<Graph> notcir = new ArrayList<Graph>();//除RFID图像以外的所有图像
    ArrayList<Graph> graphs = new ArrayList<Graph>();//所有的图像
    ArrayList<Graph> start = new ArrayList<Graph>();//入口图像
    ArrayList<Graph> end = new ArrayList<Graph>();//出口图像
    ArrayList<Graph> left = new ArrayList<Graph>();//电梯图像
    ArrayList<Graph> stair = new ArrayList<Graph>();//楼梯图像
    ArrayList<Graph> room = new ArrayList<Graph>();//房间图像
    ArrayList<Graph> corridor = new ArrayList<Graph>();//过道图像
    ArrayList<Person> persons = new ArrayList<Person>();//移动对象
    ArrayList<DestinationId> destinations = new ArrayList<DestinationId>();//所到达过的目的位置
    ArrayList<RFID> rfid = new ArrayList<RFID>();
    double[][] connectgraph = new double[1][1]; //圆点连通，存距离，不连通，为MAX
    int cirNum = 0;//圆点个数
    final int MAX = 9999;
    int objectNum = 0;//移动对象个数
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置时间格式HH:mm:ss，为24小时制，若hh:mm:ss，为12小时制
    private Date date = null;
    private GInfor ginfor[];
    private DefaultTableModel tablemodel;
    private static int number;//记录的行数
    private SaveIdWeight siw[];//当前点的邻近连通点之间的信息
    private double speed = 200.0;//移动对象的移动速度
    private double prob = 0;//保存每次产生的概率值
    private long moveTime = 0;
    private int des_id = 0;//选取的目标位置
    private boolean isArray = true;
    private int maxSpeed;
    private int minSpeed;
    private int dayNum;
    private int floorNum;
    private int maxObject;
    private int minObject;
    private double destObjectProb, primaryProb, secondProb;
    private int primaryStay, secondStay, otherStay, leftStay;
    String startDate, startTime, endTime;
    private int[] mark = new int[1];
    private double[] distance = new double[1];

    GenerateData(ArrayList<Graph> graphs, DefaultTableModel table, int maxSpeed,
            int minSpeed, int dayNum, int floorNum, int maxObject, int minObject,
            double destObjectProb, double primaryProb, double secondProb, int PrimaryStay,
            int SecondStay, int OtherStay, int LeftStay, String startDate, String startTime, String endTime) {
        this.graphs = graphs;
        this.tablemodel = table;
        for (Graph g : graphs) {
            if (g.getStyle() == 2) {
                cir.add(g);
            } else {
                notcir.add(g);
            }
        }
        cirNum = cir.size();
        if (cirNum != 0) {
            connectgraph = new double[cirNum][cirNum];
            mark = new int[cirNum];
            distance = new double[cirNum];

            for (int i = 0; i < cirNum; i++) {
                for (int j = 0; j < cirNum; j++) {
                    connectgraph[i][j] = MAX;
                }
            }
        }

        this.number = 1;
        this.maxSpeed = maxSpeed;
        this.minSpeed = minSpeed;
        this.maxObject = maxObject;
        this.minObject = minObject;
        this.dayNum = dayNum;
        this.floorNum = floorNum;
        this.destObjectProb = destObjectProb;
        this.primaryProb = primaryProb;
        this.secondProb = secondProb;
        this.primaryStay = PrimaryStay;
        this.secondStay = SecondStay;
        this.otherStay = OtherStay;
        this.leftStay = LeftStay;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endTime = endTime;

        this.connectGraph();

        for (int i = 0; i < cirNum; i++) {
            RFID r = new RFID("" + (i + 1), this.connectgraph[i]);
            rfid.add(r);
        }

        for (int i = 0; i < rfid.size(); i++) {
            rfid.get(i).computeShortestPaths(rfid);
        }
    }

    //所有圆点之间的连通性判定，存储数组按圆的内容id为二维码排列。
    private void connectGraph() {

        if (cir.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "cir is Empty!!!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<Graph> startg = new ArrayList<Graph>();
        ArrayList<Graph> endg = new ArrayList<Graph>();
        ArrayList<Graph> lineg = new ArrayList<Graph>();
        ArrayList<Integer> startint = new ArrayList<Integer>();
        ArrayList<Integer> endint = new ArrayList<Integer>();
        ArrayList<Integer> lineint = new ArrayList<Integer>();
        ArrayList<Graph> tempGraph = new ArrayList<Graph>();
        left.clear();
        stair.clear();
        room.clear();
        corridor.clear();

        for (Graph gs : cir) {
            Shape sShape = judgeShape(gs);
            float sX = (float) sShape.getBounds().getCenterX();
            float sY = (float) sShape.getBounds().getCenterY();
            boolean isInOut = false;
            boolean isLeft = false;
            boolean isStair = false;
            boolean isRoom = false;
            boolean isCorridor = false;

            for (Graph ge : cir) {
                Shape eShape = judgeShape(ge);
                float eX = (float) eShape.getBounds().getCenterX();
                float eY = (float) eShape.getBounds().getCenterY();
                Line2D line = new Line2D.Float(sX, sY, eX, eY);

                for (Graph g : graphs) {
                    Shape gShape = judgeShape(g);
                    if (g != gs && g.getFloor() == gs.getFloor() && isIntersect(gShape, sShape, startint)) {
                        startg.add(g);
                    }
                    if (g != ge && g.getFloor() == ge.getFloor() && isIntersect(gShape, eShape, endint)) {
                        endg.add(g);
                    }
                    if (g != gs && g != ge && g.getFloor() == gs.getFloor() && g.getFloor() == ge.getFloor() && isIntersect(gShape, line, lineint)) {
                        lineg.add(g);
                    }
                }

                int num = 0;
                int temp = 0;
                boolean connect1 = false;
                boolean connect3 = false;
                if (!startg.isEmpty() && !endg.isEmpty()) {
                    for (Graph s : startg) {
                        if (!isInOut && gs != ge && s.getStyle() == 4) {//圆点与直线相交，该点为进口或出口
                            tempGraph.add(gs);
                            isInOut = true;
                        }
                        if (!isLeft && gs != ge && s.getStyle() == 1) {//圆点与正方形相交，该点为电梯
                            left.add(gs);
                            if (isInOut) {
                                tempGraph.remove(gs);
                                isInOut = false;
                            }
                            isLeft = true;
                        }
                        if (!isStair && gs != ge && s.getStyle() == 3) {//圆点与圆角矩形相交，该点为电梯
                            stair.add(gs);
                            if (isInOut) {
                                tempGraph.remove(gs);
                                isInOut = false;
                            }
                            isStair = true;
                        }
                        if (!isRoom && gs != ge && s.getStyle() == 0) {//圆点与矩形相交，该点为房间
                            room.add(gs);
                            if (isInOut) {
                                tempGraph.remove(gs);
                                isInOut = false;
                            }
                            isRoom = true;
                        }

                        for (Graph e : endg) {
                            if (s == e) {
                                temp++;
                            } else if (s.getStyle() == 1 && s.getContext().equalsIgnoreCase(e.getContext())) {
                                //判断电梯之间的连通性
                                connect1 = true;
                            } else if (s.getStyle() == 3 && s.getContext().equalsIgnoreCase(e.getContext())) {
                                //判断楼梯之间的连通性
                                connect3 = true;
                            }
                        }
                    }
                } else if (startg.isEmpty()) {
                    if (!isCorridor && gs != ge) {
                        corridor.add(gs);
                        isCorridor = true;
                    }
                }

                if (!lineg.isEmpty()) {
                    for (int m = 0; m < lineg.size(); m++) {
                        Graph l = lineg.get(m);

                        for (int n = 0; n < startg.size(); n++) {
                            Graph s = startg.get(n);
                            //在房间同一边相交或者相交的是线段
                            if (temp != 0) {
                                if (l == s) {
                                    num++;
                                }
                            } else {
                                if (l == s && (lineint.get(m) == startint.get(n) || s.getStyle() == 4)) {
                                    num++;
                                }
                            }
                        }
                        for (int k = 0; k < endg.size(); k++) {
                            Graph e = endg.get(k);
                            //在房间同一边相交或者相交的是线段
                            if (temp != 0) {
                                if (l == e) {
                                    num++;
                                }
                            } else {
                                if ((lineint.get(m) == endint.get(k) || e.getStyle() == 4) && l == e) {
                                    num++;
                                }
                            }
                        }
                    }
                }
                if (gs == ge || (gs.getFloor() == ge.getFloor() && (lineg.isEmpty() || num == lineg.size() + temp)) || connect1 || connect3) {
                    int i = Integer.parseInt(gs.getContext()) - 1;
                    int j = Integer.parseInt(ge.getContext()) - 1;
                    if (connect1) {//电梯之间
                        connectgraph[i][j] = 100 * (int) Math.abs(gs.getFloor() - ge.getFloor());
                    } else if (connect3) {//楼梯之间
                        connectgraph[i][j] = 200 * (int) Math.abs(gs.getFloor() - ge.getFloor());
                    } else if (gs.getFloor() == ge.getFloor()) {//同一层之间
                        connectgraph[i][j] = (int) Math.ceil(Math.sqrt(Math.pow(sX - eX, 2) + Math.pow(sY - eY, 2)));
                    }
                }

                startg.clear();
                endg.clear();
                lineg.clear();
                startint.clear();
                endint.clear();
                lineint.clear();
            }

        }
        inOutGraphs(tempGraph);
    }

    //两图像是否相交，包括线段相交，线段与矩形相交，矩形相交
    private boolean isIntersect(Shape r1, Shape r2, ArrayList<Integer> i) {

        boolean isInsect = false;

        if ((r1 instanceof Line2D) || (r2 instanceof Line2D)) {
            if ((r1 instanceof Line2D) && (r2 instanceof Line2D)) {
                //线段相交
                Line2D line1 = (Line2D) r1;
                Line2D line2 = (Line2D) r2;
                isInsect = line1.intersectsLine(line2);
                if (isInsect) {
                    i.add(0);
                }
            } else {
                Line2D line = null;
                Shape rect = r2;
                if (r2 instanceof Line2D) {
                    line = (Line2D) r2;
                    rect = r1;
                } else if (r1 instanceof Line2D) {
                    line = (Line2D) r1;
                    rect = r2;
                }

                Rectangle r = rect.getBounds();
                Line2D line1 = new Line2D.Float(r.x, r.y, r.x, r.y + r.height);
                Line2D line2 = new Line2D.Float(r.x, r.y, r.x + r.width, r.y);
                Line2D line3 = new Line2D.Float(r.x + r.width, r.y, r.x + r.width, r.y + r.height);
                Line2D line4 = new Line2D.Float(r.x, r.y + r.height, r.x + r.width, r.y + r.height);

                int num = 0;
                if (line1.intersectsLine(line)) {
                    //线段与矩形左竖线相交
                    if (!isInsect) {
                        isInsect = true;
                        num = 1;
                    }
                }
                if (line2.intersectsLine(line)) {
                    //线段与矩形上横线相交
                    if (!isInsect) {
                        isInsect = true;
                        num = 2;
                    } else {
                        num = 0;
                    }
                }
                if (line3.intersectsLine(line)) {
                    //线段与矩形右竖线相交
                    if (!isInsect) {
                        isInsect = true;
                        num = 3;
                    } else {
                        num = 0;
                    }
                }
                if (line4.intersectsLine(line)) {
                    //线段与矩形下横线相交
                    if (!isInsect) {
                        isInsect = true;
                        num = 4;
                    } else {
                        num = 0;
                    }
                }
                if (isInsect) {
                    i.add(num);
                }
            }
        } else {
            //矩形相交
            Shape rect1 = r1;
            Shape rect2 = r2;
            if (r1 instanceof Ellipse2D) {
                rect1 = r2;
                rect2 = r1;
            } else if (r2 instanceof Ellipse2D) {
                rect1 = r1;
                rect2 = r2;
            }

            Rectangle r = rect1.getBounds();
            Line2D line1 = new Line2D.Float(r.x, r.y, r.x, r.y + r.height);
            Line2D line2 = new Line2D.Float(r.x, r.y, r.x + r.width, r.y);
            Line2D line3 = new Line2D.Float(r.x + r.width, r.y, r.x + r.width, r.y + r.height);
            Line2D line4 = new Line2D.Float(r.x, r.y + r.height, r.x + r.width, r.y + r.height);

            if (line1.intersects(rect2.getBounds())) {
                //线段与矩形左竖线相交
                isInsect = true;
                i.add(1);
            } else if (line2.intersects(rect2.getBounds())) {
                //线段与矩形上横线相交
                isInsect = true;
                i.add(2);
            } else if (line3.intersects(rect2.getBounds())) {
                //线段与矩形右竖线相交
                isInsect = true;
                i.add(3);
            } else if (line4.intersects(rect2.getBounds())) {
                //线段与矩形下横线相交
                isInsect = true;
                i.add(4);
            } else {
                isInsect = false;
            }
        }
        return isInsect;
    }

    //判断Graph对象的图像类型
    private Shape judgeShape(Graph graph) {
        Shape shape = null;
        switch (graph.getStyle()) {
            case 0:
            case 1:
                shape = new Rectangle(graph.getX1(), graph.getY1(), graph.getWide(), graph.getHeight());
                break;
            case 2:
                shape = new Ellipse2D.Double(graph.getX1(), graph.getY1(), graph.getWide(), graph.getHeight());
                break;
            case 3:
                shape = new RoundRectangle2D.Double(graph.getX1(), graph.getY1(), graph.getWide(), graph.getHeight(), 5, 15);
                break;
            case 4:
                shape = new Line2D.Float(graph.getX1(), graph.getY1(), graph.getWide(), graph.getHeight());
                break;
            default:
        }
        return shape;
    }

    //判断进出口所在圆点
    private void inOutGraphs(ArrayList<Graph> grs) {
        start.clear();
        end.clear();
        if (grs.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "grs is Empty!!!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        int min = Integer.parseInt(grs.get(0).getContext());
        Graph minGraph = grs.get(0);
        for (Graph g : grs) {
            int num = Integer.parseInt(g.getContext());
            if (min > num) {
                min = num;
                minGraph = g;
            }
        }
        start.add(minGraph);
        for (Graph g : grs) {
            if (g != minGraph) {
                if (g.getFillColor().equals(minGraph.getFillColor())) {
                    start.add(g);
                } else {
                    end.add(g);
                }
            }
        }
        if (end.isEmpty()) {
            end = start;
        }
    }

    private boolean checkConnect(int currentid) {
        boolean isConnect = false;

        RFID.Path path = rfid.get(currentid - 1).getShortestPath().get(des_id - 1);
        if (!path.getPathNode().isEmpty()) {
            isConnect = true;
        }
        return isConnect;
    }

    void generData() {

        if (cir.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "There are no RFIDs!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Random ran = new Random();
            String datestr = this.startDate + " " + this.startTime;

            this.date = sdf.parse(datestr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, ran.nextInt(60));
            this.generPerson(ran);
            int day = 0;
            int priorId = 0;

            if (objectNum == 0 || persons.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(null, "There are no Moving Objects!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (int i = 0; i < objectNum; i++) {
                while (day != this.dayNum) {
                    //早上从入口进入
                    if (start.isEmpty()) {
                        return;
                    }
                    int inId = Integer.parseInt(start.get(ran.nextInt(start.size())).getContext());
                    persons.get(i).setInId(inId);
                    int current = persons.get(i).getInId();

                    while (!this.isClose(day, cal)) {//未到离开时间
                        int connectNum = 0;
                        if (current == persons.get(i).getInId()) {//入口
                            if (isArray) {
                                this.getDestinationId(persons.get(i), current, ran);
                                while (des_id == 0) {
                                    //目的地为0，重选
                                    this.getDestinationId(persons.get(i), current, ran);
                                }

                                DestinationId dest = new DestinationId(persons.get(i).getId(), des_id);
                                destinations.add(dest);
                                isArray = false;
                                persons.get(i).setIsArrive(isArray);
                            }

                            priorId = current;

                            if (this.isClose(day, cal)) {//判断进入一个位置时是否已到离开时间
                                break;
                            }

                            ginfor[i] = new GInfor(current, persons.get(i).getId(), cal.getTime());//进入当前节点的时间
                            speed = ran.nextInt(this.maxSpeed - this.minSpeed) + this.minSpeed;
                            this.getMoveTime(ran, cal, 2 * cir.get(current - 1).getWide(), speed);

                            if (this.isClose(day, cal)) {//判断离开一个位置时是否已到离开时间
                                this.outPut(ginfor[i], cal);
                                ginfor[i].time = cal.getTime();
                                number++;
                                if (!destinations.isEmpty() && !isArray) {
                                    destinations.remove(destinations.size() - 1);
                                }
                                DestinationId cdest = new DestinationId(persons.get(i).getId(), current);
                                destinations.add(cdest);
                                break;
                            }

                            this.outPut(ginfor[i], cal);
                            ginfor[i].time = cal.getTime();
                            number++;

                        }
                        int next_id = 0;
                        prob = ran.nextDouble();
                        if (prob < 0.01) {//小概率情况下，临时改变目的位置
                            this.getDestinationId(persons.get(i), current, ran);
                            while (des_id == 0) {
                                this.getDestinationId(persons.get(i), current, ran);
                            }
                            if (!destinations.isEmpty() && !isArray) {
                                destinations.remove(destinations.size() - 1);
                            }
                            if (!destinations.isEmpty() && current != destinations.get(destinations.size() - 1).getId()) {//当前为空表明入口处就改变目的位置不用设置临时目标位置，
                                //当前位置不为入口，也不为上次目的位置，即临时改变目的位置时，已经过一段距离
                                DestinationId cdest = new DestinationId(persons.get(i).getId(), current);
                                destinations.add(cdest);
                            }

                            DestinationId dest = new DestinationId(persons.get(i).getId(), des_id);
                            destinations.add(dest);
                            isArray = false;
                            persons.get(i).setIsArrive(isArray);
                            priorId = current;
                        }

                        for (int k = 0; k < cirNum; k++) {
                            if (connectgraph[current - 1][k] != 0 && connectgraph[current - 1][k] != MAX) {
                                connectNum++;
                            }
                        }

                        next_id = this.getNextRID(persons.get(i), current, priorId, ran);
                        while (next_id == 0) {
                            next_id = this.getNextRID(persons.get(i), current, priorId, ran);
                        }
                        if (next_id == -1) {
                            javax.swing.JOptionPane.showMessageDialog(null, "There are no connecting paths, two points are not connected!!!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        this.getSpeed(ran);
                        this.getMoveTime(ran, cal, connectgraph[current - 1][next_id - 1] - cir.get(current - 1).getWide() / 2, speed);//到下一节点的进入时间
                        moveTime = (int) (cal.getTime().getTime() - ginfor[i].time.getTime()) / 1000;

                        if (this.isClose(day, cal)) {//判断进入一个位置时是否已到离开时间
                            if (!destinations.isEmpty() && !isArray) {
                                destinations.remove(destinations.size() - 1);
                            }
                            DestinationId cdest = new DestinationId(persons.get(i).getId(), current);
                            destinations.add(cdest);
                            break;
                        }

                        priorId = current;
                        current = next_id;

                        ginfor[i] = new GInfor(current, persons.get(i).getId(), cal.getTime());//进入当前节点的时间                       

                        if (this.leftStay != 0 && !left.isEmpty()) {
                            for (Graph g : left) {//等待电梯的时间
                                int id = Integer.parseInt(g.getContext());
                                if (id == current && g.getFloor() != cir.get(des_id - 1).getFloor()) {
                                    int wait = ran.nextInt(this.leftStay);
                                    cal.add(Calendar.MINUTE, wait);
                                    break;
                                }
                            }
                        }

                        this.getSpeed(ran);
                        this.getMoveTime(ran, cal, cir.get(current - 1).getWide(), speed);
                        boolean isNotOut = true;
                        if (!end.isEmpty()) {
                            for (Graph g : end) {//当前已处于出口处，不能再继续了
                                int outid = Integer.parseInt(g.getContext());
                                if (outid == current) {
                                    isNotOut = false;
                                    break;
                                }
                            }
                        }

                        if (current == des_id) {//到达目的地
                            isArray = true;
                            persons.get(i).setIsArrive(isArray);

                            if (isNotOut) {//不是出口,得到停留时间
                                this.addStayTime(persons.get(i), cal, ran);
                            }
                        }

                        if (this.isClose(day, cal)) {//判断离开一个位置时是否已到离开时间
                            this.outPut(ginfor[i], cal);
                            ginfor[i].time = cal.getTime();
                            number++;
                            if (!destinations.isEmpty() && !isArray) {
                                destinations.remove(destinations.size() - 1);
                                DestinationId cdest = new DestinationId(persons.get(i).getId(), current);
                                destinations.add(cdest);
                            }
                            break;
                        }

                        this.outPut(ginfor[i], cal);
                        ginfor[i].time = cal.getTime();
                        number++;

                        if (current == des_id) {//到达目的地             
                            if (isNotOut) {//不是出口，增加目的位置，及得到停留时间，若是出口，则进行下次循环
                                this.getDestinationId(persons.get(i), current, ran);
                                while (des_id == 0) {
                                    this.getDestinationId(persons.get(i), current, ran);
                                }
                                DestinationId dest = new DestinationId(persons.get(i).getId(), des_id);
                                destinations.add(dest);

                                priorId = current;//到目的后，以当前点为起点
                            }
                        }

                        //如果中途对象通过出口离开的话
                        if (!isNotOut) {
                            int timelength = Integer.parseInt(this.endTime.substring(0, 2))
                                    - Integer.parseInt(this.startTime.substring(0, 2));
                            int time = ran.nextInt(timelength * 60);
                            if (time > 60) {
                                cal.add(Calendar.HOUR, time / 60);
                                cal.add(Calendar.MINUTE, time % 60);
                            } else {
                                cal.add(Calendar.MINUTE, time);
                            }
                            if (!destinations.isEmpty() && !isArray) {
                                destinations.remove(destinations.size() - 1);
                                DestinationId cdest = new DestinationId(persons.get(i).getId(), current);
                                destinations.add(cdest);
                            }
                            inId = Integer.parseInt(start.get(ran.nextInt(start.size())).getContext());
                            persons.get(i).setInId(inId);
                            current = persons.get(i).getInId();
                            this.moveTime = 0;
                        }
                    }

                    //到时间了，必须离开
                    if (current != persons.get(i).getInId()) {
                        if (end.isEmpty()) {
                            javax.swing.JOptionPane.showMessageDialog(null, "There are no exits！！！", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        int out = ran.nextInt(end.size());
                        int outId = Integer.parseInt(end.get(out).getContext());
                        des_id = outId;
                        DestinationId dest = new DestinationId(persons.get(i).getId(), des_id);
                        destinations.add(dest);
                        speed = this.maxSpeed;

                        if (!this.checkConnect(current)) {
                            javax.swing.JOptionPane.showMessageDialog(null, "There are no connecting paths, two points are not connected！！！", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        while (current != des_id) {
                            int next_id = Integer.parseInt(rfid.get(current - 1).getShortestPath().get(des_id - 1).getPathNode().get(1));

                            this.getMoveTime(ran, cal, connectgraph[current - 1][next_id - 1] - cir.get(current - 1).getWide() / 2, speed);//到下一节点的进入时间
                            moveTime = (int) (cal.getTime().getTime() - ginfor[i].time.getTime()) / 1000;

                            current = next_id;
                            ginfor[i] = new GInfor(current, persons.get(i).getId(), cal.getTime());//进入当前节点的时间
                            if (this.leftStay != 0) {
                                for (Graph g : left) {//等待电梯的时间
                                    int id = Integer.parseInt(g.getContext());
                                    if (id == current && g.getFloor() != cir.get(des_id - 1).getFloor()) {
                                        int wait = ran.nextInt(this.leftStay);
                                        cal.add(Calendar.MINUTE, wait);
                                        break;
                                    }
                                }
                            }
                            this.getSpeed(ran);
                            this.getMoveTime(ran, cal, cir.get(current - 1).getWide(), speed);
                            this.outPut(ginfor[i], cal);
                            ginfor[i].time = cal.getTime();
                            number++;
                        }
                    }
                    cal.setTime(date);//新的一天开始
                    day++;
                    cal.add(Calendar.MINUTE, ran.nextInt(60));
                    moveTime = 0;
                }
                this.date = sdf.parse(datestr);
                cal.setTime(date);
                cal.add(Calendar.MINUTE, ran.nextInt(60));
                day = 0;
            }
        } catch (ParseException ex) {
            Logger.getLogger(GenerateData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void getSpeed(Random r) {
        prob = r.nextDouble();
        if (prob < 0.05) {//百分之五的可能，对象移动速度改变
            speed = r.nextInt(this.maxSpeed - this.minSpeed) + this.minSpeed;
        }
    }

    void getMoveTime(Random r, Calendar cal, double length, double speed) {
        prob = r.nextDouble();
        if (prob < 0.05) {// 5%的可能性在该RFID的覆盖位置停留1-5分钟
            cal.add(Calendar.MINUTE, Math.abs(r.nextInt(5)) + 1);
        } else {// 在RFID停留的时间为经过该区域的时间
            double time = length / speed;
            if (time >= 1) {
                cal.add(Calendar.MINUTE, (int) time);
                time = (time - (int) time) * 100;
                cal.add(Calendar.SECOND, (int) (time) * 60 / 100);// 将100进制数转化为60进制数
            } else {
                time = time * 100;
                cal.add(Calendar.SECOND, (int) time * 60 / 100);
            }
        }
    }

    boolean isClose(int day, Calendar cal) {
        try {
            int time = Integer.parseInt(this.startDate.substring(8));
            String t = this.startDate.substring(0, 8) + (time + day);
            Date d = sdf.parse(t + " " + this.endTime);
            if (d.compareTo(cal.getTime()) <= 0) {
                cal.setTime(d);
                t = this.startDate.substring(0, 8) + (time + day + 1);
                date = sdf.parse(t + " " + this.startTime);//第二天早上
                return true;
            } else {
                return false;
            }
        } catch (ParseException ex) {
            Logger.getLogger(GenerateData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    void addStayTime(Person p, Calendar cal, Random r) {
        int time = 0;
        if (p.getType() == 1) {
            if (des_id == p.getFirstid()) {
                if (this.primaryStay != 0) {
                    time = r.nextInt(this.primaryStay);
                }
            } else if (des_id == p.getSecondid()) {
                if (this.secondStay != 0) {
                    time = r.nextInt(this.secondStay);
                }
            } else {
                if (this.otherStay != 0) {
                    time = r.nextInt(this.otherStay);
                }
            }
        } else {
            if (this.otherStay != 0) {
                time = r.nextInt(this.otherStay);
            }
        }
        if (time > 60) {
            cal.add(Calendar.HOUR, time / 60);
            cal.add(Calendar.MINUTE, time % 60);
        } else {
            cal.add(Calendar.MINUTE, time);
        }
        String str = sdf.format(cal.getTime()).substring(0, 10) + " " + this.endTime;
        Date d;
        try {
            d = sdf.parse(str);
            if (d.compareTo(cal.getTime()) < 0) {
                cal.setTime(d);
            }
        } catch (ParseException ex) {
            Logger.getLogger(GenerateData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void generPerson(Random r) {
        this.objectNum = r.nextInt(this.maxObject - this.minObject) + this.minObject;
        while (objectNum == 0) {
            this.objectNum = r.nextInt(this.maxObject - this.minObject) + this.minObject;
        }
        this.ginfor = new GInfor[objectNum];
        generPersonType(objectNum, r);
    }

    void generPersonType(int objectNum, Random r) {
        persons.clear();
        if (start.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Start location is empty!!!", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < objectNum; i++) {
            int in = r.nextInt(start.size());
            int inId = Integer.parseInt(start.get(in).getContext());
            int firstid = 0, secondid = 0;
            if (i < (int) (objectNum * this.destObjectProb)) {
                if (!room.isEmpty()) {//有房间时，移动对象的首要位置、次要位置均为房间
                    int temp = r.nextInt(room.size());
                    firstid = Integer.parseInt(room.get(temp).getContext());
                    while (firstid == 0 || firstid == inId) {
                        temp = r.nextInt(room.size());
                        firstid = Integer.parseInt(room.get(temp).getContext());

                    }
                    secondid = firstid;
                    while (firstid == secondid) {
                        temp = r.nextInt(room.size());
                        secondid = Integer.parseInt(room.get(temp).getContext());
                        while (secondid == 0 || secondid == inId) {
                            temp = r.nextInt(room.size());
                            secondid = Integer.parseInt(cir.get(temp).getContext());
                        }
                    }
                } else if (!corridor.isEmpty()) {//没有有房间时，移动对象的首要位置、次要位置均为过道
                    int temp = r.nextInt(corridor.size());
                    firstid = Integer.parseInt(corridor.get(temp).getContext());
                    while (firstid == 0 || firstid == inId) {
                        temp = r.nextInt(corridor.size());
                        firstid = Integer.parseInt(corridor.get(temp).getContext());

                    }
                    secondid = firstid;
                    while (firstid == secondid) {
                        temp = r.nextInt(corridor.size());
                        secondid = Integer.parseInt(corridor.get(temp).getContext());
                        while (secondid == 0 || secondid == inId) {
                            temp = r.nextInt(corridor.size());
                            secondid = Integer.parseInt(cir.get(temp).getContext());
                        }
                    }
                } else {
                    javax.swing.JOptionPane.showMessageDialog(null, "There is no Location Information！！！", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Person p = new Person((i + 1), firstid, secondid, 1, inId);
                p.setLocInfor(primaryProb, secondProb);
                persons.add(p);
            } else {
                Person p = new Person((i + 1), firstid, secondid, 2, inId);
                persons.add(p);//firstid=0，secondid=0               
            }
        }
        FileWriter fw;
        BufferedWriter bw;
        try {
            fw = new FileWriter("PersonInfor.txt");
            bw = new BufferedWriter(fw);
            bw.write("Object Number:" + persons.size());
            bw.newLine();
            bw.flush();
            if (persons.isEmpty()) {
                return;
            }
            for (int i = 0; i < persons.size(); i++) {//将移动对象信息输出到文本
                try {
                    if (i == 0) {
                        bw.write("ID " + "Type " + "PriLoc " + "SecLoc " + "PriProb" + "SecProb" + "OtherProb");
                        bw.newLine();
                        bw.flush();
                    }
                    if (persons.get(i).getType() == 1) {
                        bw.write(persons.get(i).getId() + " " + persons.get(i).getType() + " "
                                + persons.get(i).getFirstid() + " " + persons.get(i).getSecondid()
                                + " " + persons.get(i).loc[0].getProb() + " " + persons.get(i).loc[1].getProb() + " " + persons.get(i).loc[2].getProb());
                    } else {
                        bw.write(persons.get(i).getId() + " " + persons.get(i).getType() + " "
                                + persons.get(i).getFirstid() + " " + persons.get(i).getSecondid()
                                + " " + 0 + " " + 0 + " " + 0);
                    }
                    bw.newLine();
                    bw.flush();
                } catch (IOException ex) {
                    Logger.getLogger(GenerateData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(GenerateData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int getObjectNumber() {
        return this.objectNum;
    }

    void outPut(GInfor ginf, Calendar cal) {
        @SuppressWarnings("rawtypes")
        Vector<Comparable> newRow = new Vector<Comparable>();

        newRow.add(number);
        newRow.add(ginf.RID);
        newRow.add(ginf.MID);
        newRow.add(sdf.format(ginf.time));
        newRow.add(sdf.format(cal.getTime()));
        newRow.add(moveTime);
        tablemodel.getDataVector().add(newRow);
        tablemodel.fireTableDataChanged();
    }

    int getOtherDestId(Person p, Random r) {
        double temprob = r.nextDouble();
        int tempid = 0;
        if (!room.isEmpty()) {//有房间时，更大的可能目标位置时房间，其次是过道或楼梯或电梯，接着是出口
            if (temprob < 0.05 / 3) {//电梯
                if (!left.isEmpty()) {
                    tempid = Integer.parseInt(left.get(r.nextInt(left.size())).getContext());
                }
            } else if (temprob < 0.05 * 2 / 3) {//楼梯
                if (!stair.isEmpty()) {
                    tempid = Integer.parseInt(stair.get(r.nextInt(stair.size())).getContext());
                }
            } else if (temprob < 0.05 * 3 / 3) {//过道
                if (!corridor.isEmpty()) {
                    tempid = Integer.parseInt(corridor.get(r.nextInt(corridor.size())).getContext());
                }
            } else if (temprob < 0.1) {//出口
                //System.out.println("选择出口");
                if (!end.isEmpty()) {
                    tempid = Integer.parseInt(end.get(r.nextInt(end.size())).getContext());
                }
            } else {//房间
                tempid = Integer.parseInt(room.get(r.nextInt(room.size())).getContext());
            }
        } else if (!corridor.isEmpty()) {
            if (temprob < 0.05 / 2) {//电梯
                if (!left.isEmpty()) {
                    tempid = Integer.parseInt(left.get(r.nextInt(left.size())).getContext());
                }
            } else if (temprob < 0.05 * 2 / 2) {//楼梯
                if (!stair.isEmpty()) {
                    tempid = Integer.parseInt(stair.get(r.nextInt(stair.size())).getContext());
                }
            } else if (temprob < 0.1) {//出口
                if (!end.isEmpty()) {
                    tempid = Integer.parseInt(end.get(r.nextInt(end.size())).getContext());
                }
            } else {//过道
                tempid = Integer.parseInt(corridor.get(r.nextInt(corridor.size())).getContext());
            }
        } else {
        }
        return tempid;
    }

    //得到目的位置
    void getDestinationId(Person p, int currentid, Random r) {
        if (p.getType() == 1) {
            if (currentid == p.getInId()) {//进场
                isArray = false;
                p.setIsArrive(isArray);
                prob = r.nextDouble();
                if (prob < p.loc[0].getProb()) {//首要位置
                    des_id = p.loc[0].getRid();
                } else if (prob < p.loc[0].getProb() + p.loc[1].getProb()) {//次要位置
                    des_id = p.loc[1].getRid();
                } else {//其他位置
                    des_id = this.getOtherDestId(p, r);
                    while (des_id == currentid || des_id == p.loc[0].getRid() || des_id == p.loc[1].getRid()) {
                        des_id = this.getOtherDestId(p, r);
                    }
                }
            } else if (!isArray) {//临时变更目的位置
                des_id = this.getOtherDestId(p, r);
                while (des_id == currentid) {
                    des_id = this.getOtherDestId(p, r);
                }
            } else if (isArray) {//到达目的位置后
                prob = r.nextDouble();
                if (currentid == p.getFirstid()) {//到达的是首要位置
                    if (prob < p.loc[1].getProb() / (1 - p.loc[0].getProb())) {//到达次要位置
                        des_id = p.loc[1].getRid();
                    } else {//其他位置
                        des_id = this.getOtherDestId(p, r);
                        while (des_id == p.loc[0].getRid() || des_id == p.loc[1].getRid()) {
                            des_id = this.getOtherDestId(p, r);
                        }
                    }
                } else if (currentid == p.getSecondid()) {//到达的是次要位置
                    if (prob < p.loc[0].getProb()) {//首要位置
                        des_id = p.loc[0].getRid();
                    } else {//其他位置
                        des_id = this.getOtherDestId(p, r);
                        while (des_id == p.loc[0].getRid() || des_id == p.loc[1].getRid()) {
                            des_id = this.getOtherDestId(p, r);
                        }
                    }
                } else {//到达的是其他位置
                    if (prob < p.loc[0].getProb()) {//首要位置
                        des_id = p.loc[0].getRid();
                    } else if (prob < p.loc[0].getProb() + p.loc[1].getProb()) {//次要位置
                        des_id = p.loc[1].getRid();
                    } else {//其他位置
                        des_id = this.getOtherDestId(p, r);
                        while (des_id == currentid || des_id == p.loc[0].getRid() || des_id == p.loc[1].getRid()) {
                            des_id = this.getOtherDestId(p, r);
                        }
                    }
                }
                isArray = false;
                p.setIsArrive(isArray);
            }
        } else if (p.getType() == 2) {
            des_id = this.getOtherDestId(p, r);
            while (des_id == currentid) {
                des_id = this.getOtherDestId(p, r);
            }
            isArray = false;
            p.setIsArrive(isArray);
        }
    }

    //得到下一个点位置
    int getNextRID(Person p, int currentid, int priorId, Random r) {

        int nextrid = 0;
        int incount = 0;
        int maxprobid = 0;
        RFID.Path shortpath = rfid.get(currentid - 1).getShortestPath().get(des_id - 1);
        int length = shortpath.getPathNode().size();

        if (!this.checkConnect(currentid)) {
            return -1;
        }

        if (cirNum == 0) {
            return -1;
        }

        double pro = 1;

        if (length == 1) {//说明当前位置是目的地
            nextrid = Integer.parseInt(shortpath.getPathNode().get(0));
        } else {
            pro = Math.pow(0.9, 1.0 / (length - 1));//计算最短路径边上设置的概率

            int count = 0;
            int tem_r = Integer.parseInt(shortpath.getPathNode().get(0));//当前点
            if (tem_r != currentid) {
                javax.swing.JOptionPane.showMessageDialog(null, "The connecting path is Wrong！！！", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return nextrid;
            }
            for (int j = 0; j < cirNum; j++) {// 统计与该RFID向连通的边的个数
                if (connectgraph[tem_r - 1][j] > 0 && connectgraph[tem_r - 1][j] < MAX) {
                    count++;
                }
            }
            SaveIdWeight[] temsiw = new SaveIdWeight[count];
            siw = new SaveIdWeight[count];
            int t = 0;
            for (int j = 0; j < cirNum; j++) {// 获取连通ID值和边权值
                if (connectgraph[tem_r - 1][j] > 0 && connectgraph[tem_r - 1][j] < MAX) {
                    temsiw[t] = new SaveIdWeight(j, tem_r, j + 1, (int) connectgraph[tem_r - 1][j]);
                    t++;
                }
            }

            boolean isLeft = false;
            boolean isStair = false;

            if (start.equals(end) || start.isEmpty()) {
                siw = temsiw;
                incount = t;
            } else {
                for (int i = 0; i < t; i++) {//删除连通点中的入口位置
                    boolean s = false;
                    for (Graph g : start) {
                        int in = Integer.parseInt(g.getContext());
                        if (in == temsiw[i].to) {
                            s = true;
                            break;
                        }
                    }
                    if (!s) {
                        siw[incount] = temsiw[i];
                        incount++;
                    }
                }
            }

            if (!stair.isEmpty()) {//判断当前点是否是在楼梯位置
                for (Graph g : stair) {
                    if (isLeft) {
                        break;
                    }
                    int id = Integer.parseInt(g.getContext());
                    if (id == currentid) {
                        isStair = true;
                        isLeft = false;
                        break;
                    }
                }
            }

            if (!left.isEmpty()) {//判断当前点是否是在电梯位置
                for (Graph g : left) {
                    if (isStair) {
                        break;
                    }
                    int id = Integer.parseInt(g.getContext());
                    if (id == currentid) {
                        isStair = false;
                        isLeft = true;
                        break;
                    }
                }
            }

            boolean toLeft = false;//下一步是否到电梯
            boolean toStair = false;//下一步是否到楼梯
            int leftNum = 0;//同层楼连通的电梯个数
            int floor = (int) Math.abs(cir.get(currentid - 1).getFloor() - cir.get(des_id - 1).getFloor());
            if (floor < 3 && floor > 0) {//更可能走楼梯
                if (isStair) {
                    toStair = true;
                } else {//判断该层非楼梯的点 包括电梯位置
                    boolean tem = false;
                    if (!stair.isEmpty()) {//判断当前位置连通点中是否有楼梯位置
                        for (int j = 0; j < incount; j++) {
                            for (Graph g : stair) {
                                int id = Integer.parseInt(g.getContext());
                                if (id == siw[j].to) {//找到楼梯的点
                                    toStair = true;
                                    tem = true;
                                    break;
                                }
                            }
                            if (tem) {
                                break;
                            }
                        }
                    }
                }
            } else if (floor >= 3) {//更可能走电梯
                if (isLeft) {
                    toLeft = true;
                } else {//判断该层非电梯的点 包括楼梯位置
                    boolean tem = false;
                    if (!left.isEmpty()) {//判断当前位置连通点中是否有电梯位置
                        for (int j = 0; j < incount; j++) {
                            for (Graph g : left) {
                                int id = Integer.parseInt(g.getContext());
                                if (id == siw[j].to) {//找到电梯的点
                                    toLeft = true;
                                    tem = true;
                                    break;
                                }
                            }
                            if (tem) {
                                break;
                            }
                        }
                    }
                }
            }

            int pric = -1;
            int minlength = MAX;
            int sameleft = r.nextInt(this.leftStay);//该电梯处等待时间
            int samepric = -1;
            for (int j = 0; j < incount; j++) {//设置当前点与所有连通的点的概率
                if (this.leftStay != 0) {//电梯有等待时间
                    if (toStair) {//走楼梯
                        int markstair = 0;
                        for (Graph g : stair) {
                            int id = Integer.parseInt(g.getContext());
                            if (id == siw[j].to) {//找到楼梯的点
                                markstair = 1;
                                if (isStair) {//当前点为楼梯位置时，直接找到和目的位置同层的楼梯位置
                                    if (g.getFloor() == cir.get(des_id - 1).getFloor()) {
                                        siw[j].setP(pro);
                                        maxprobid = siw[j].to;
                                    } else {
                                        siw[j].setP((1 - pro) / (incount - 1));
                                    }
                                } else {//当前点不为楼梯位置时，找距离最近的点
                                    if (minlength > siw[j].weight) {//距离最近的楼梯的点
                                        minlength = siw[j].weight;
                                        siw[j].setP(pro);
                                        maxprobid = siw[j].to;
                                        if (pric != -1) {//非最近的楼梯的点重新设置
                                            siw[pric].setP((1 - pro) / (incount - 1));
                                        }
                                        pric = j;
                                    }
                                }
                                break;
                            }
                        }
                        if (markstair == 0) {//该点不是楼梯的点
                            siw[j].setP((1 - pro) / (incount - 1));
                        }
                    } else if (toLeft) {//走电梯
                        int marklift = 0;
                        for (Graph g : left) {
                            int id = Integer.parseInt(g.getContext());
                            if (id == siw[j].to) {//找到电梯的点
                                marklift = 1;
                                if (isLeft) {//当前点为电梯位置时，一种为到连通的另外一个电梯位置，一种为找到和目的位置同层的相连的电梯位置
                                    if (g.getFloor() == cir.get(currentid - 1).getFloor()) {//靠近的连通电梯，选择之一上楼
                                        int wait = r.nextInt(this.leftStay);
                                        if (sameleft > wait && priorId != siw[j].to) {//停留时间更短，且之前并没有经过的电梯
                                            siw[j].setP(pro);
                                            maxprobid = siw[j].to;
                                            if (samepric != -1) {
                                                siw[samepric].setP((1 - pro) / (incount - 1));
                                            }
                                            samepric = j;
                                        } else {
                                            siw[j].setP((1 - pro) / (incount - 1));
                                        }
                                    } else {
                                        siw[j].setP((1 - pro) / (incount - 1));
                                    }
                                } else {//当前点不为电梯位置时，找停留时间最短的点
                                    int wait = r.nextInt(this.leftStay);
                                    if (minlength > wait) {//停留时间最短的电梯的点
                                        minlength = wait;
                                        siw[j].setP(pro);
                                        maxprobid = siw[j].to;
                                        if (pric != -1) {//非停留时间最短的点重新设置
                                            siw[pric].setP((1 - pro) / (incount - 1));
                                        }
                                        pric = j;
                                    } else {
                                        siw[j].setP((1 - pro) / (incount - 1));
                                    }
                                }
                                break;
                            }
                        }
                        if (marklift == 0) {//该点不是电梯的点
                            siw[j].setP((1 - pro) / (incount - 1));
                        }
                    } else if (!toStair && !toLeft) {//按最短路径走
                        if (siw[j].from == Integer.parseInt(shortpath.getPathNode().get(0))
                                && siw[j].to == Integer.parseInt(shortpath.getPathNode().get(1))) {
                            siw[j].setP(pro);//在最短路径上时设置为pro
                            maxprobid = siw[j].to;
                        } else {
                            siw[j].setP((1 - pro) / (incount - 1));//不在最短路径上时其余点概率相同
                        }
                    }
                } else {//电梯无等待时间，即来即走
                    if (siw[j].from == Integer.parseInt(shortpath.getPathNode().get(0))
                            && siw[j].to == Integer.parseInt(shortpath.getPathNode().get(1))) {

                        siw[j].setP(pro);//在最短路径上时设置为pro
                        maxprobid = siw[j].to;
                    } else {
                        siw[j].setP((1 - pro) / (incount - 1));//不在最短路径上时其余点概率相同
                    }
                }
            }

            if (toLeft && maxprobid == 0) {//电梯选择同层失败，仍以当前电梯上楼
                for (int j = 0; j < incount; j++) {
                    for (Graph g : left) {
                        int id = Integer.parseInt(g.getContext());
                        if (id == siw[j].to) {
                            if (g.getFloor() == cir.get(des_id - 1).getFloor()) {
                                siw[j].setP(pro);
                                maxprobid = siw[j].to;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (currentid != nextrid) {
            prob = r.nextDouble();
            int t = 0;
            for (int j = 0; j < incount; j++) {
                //在最大概率的位置上
                if (prob >= (1 - pro)) {
                    nextrid = maxprobid;
                    t++;
                    break;
                } else {
                    if (prob < (j - t + 1) * siw[j].p1) {
                        nextrid = siw[j].to;
                        break;
                    }//不在最大概率的位置上时其余点概率相同
                }
            }
        }
        return nextrid;
    }

    ArrayList<Graph> getCircles() {
        return this.cir;
    }

    ArrayList<Graph> getNotCircles() {
        return this.notcir;
    }

    ArrayList<Graph> getStartCircles() {
        return this.start;
    }

    ArrayList<Graph> getEndCircles() {
        return this.end;
    }

    ArrayList<Person> getPersons() {
        return this.persons;
    }

    ArrayList<DestinationId> getDestinations() {
        return this.destinations;
    }
}

class GInfor {

    int RID;//RFID编号
    int MID;//移动对象的编号
    Date time;//对象离开时间

    public GInfor(int rid, int mid, Date time) {
        this.RID = rid;
        this.MID = mid;
        this.time = time;
    }
}

class SaveIdWeight {

    int id;
    int from;//边起点
    int to;//边终点
    double p1;//from --> to 的概率
    int weight;//边的权值(两边之间的距离)

    public SaveIdWeight(int id, int from, int to, int weight) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.weight = weight;
        p1 = 0;
    }

    public void setP(double p1) {//设置边的运动权重
        this.p1 = p1;
    }

    public double getP() {
        return p1;
    }
}

class SaveProb {

    int rid;
    int nextid;
    double prob;

    public SaveProb(int rid, int nextid, double prob) {
        this.rid = rid;
        this.nextid = nextid;
        this.prob = prob;
    }
}

class DestinationId {

    int object;
    int id;

    public DestinationId(int object, int id) {
        this.object = object;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getObject() {
        return object;
    }
}
