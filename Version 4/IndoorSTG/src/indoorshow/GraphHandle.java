/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indoorshow;

//import indoor.Line;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JFileChooser;

/**
 *
 * @author Administrator
 */
public class GraphHandle {

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new GraphHandle(new IndoorShowFrame(), new String[]{""});
                //new test().setVisible(true);
                //envirSet.setVisible(true);
                //indoor.setVisible(true);
            }
        });
    }
    static ArrayList<Graph> graphs = new ArrayList<Graph>();
    static ArrayList<Graph> cir = new ArrayList<Graph>();
    static ArrayList<Graph> notcir = new ArrayList<Graph>();
    static Graph graph = null, selected = null;
    static Color fillc, borderc;
    static boolean isfill, isborder;
    static int maxR = 35;
    static int mx, my, tx, ty;
    static EnvirSet envirSet = null;
    static JFileChooser jfc = new JFileChooser();
    static Stroke bstroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
    static Stroke selectStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{5, 2}, 1);

    public GraphHandle(final IndoorShowFrame indoor, String[] context) {

        envirSet = new EnvirSet(indoor);
        graphs.clear();
        cir.clear();
        notcir.clear();
        if (context.length != 0 && !context[0].equalsIgnoreCase("")) {
            envirSet.getFloorBox().setModel(new javax.swing.DefaultComboBoxModel(context));
        }
        envirSet.floor = envirSet.getFloorBox().getSelectedIndex();
        envirSet.setVisible(true);
    }
    static Comparator<Graph> comparator = new Comparator<Graph>() {
        @Override
        public int compare(Graph g1, Graph g2) {
            //先排楼层
            if (g1.getFloor() != g2.getFloor()) {
                return g1.getFloor() - g2.getFloor();
            } else {
                //再排y
                if (g1.getY1() != g2.getY1()) {
                    return g1.getY1() - g2.getY1();
                } else {
                    //y相同则按x排序
                    return g1.getX1() - g2.getX1();
                }
            }
        }
    };

    static void hPressed(Point p) {
        checkSelect(p);
        if (envirSet.method == 0) {
            mx = p.x;
            my = p.y;
        } else if (envirSet.method == 2) {
            mx = p.x;
            my = p.y;
        } else if (envirSet.method == 1) {
            if (selected != null) {
                graphs.remove(selected);
                if (selected.getStyle() == 2) {
                    cir.remove(selected);
                } else {
                    notcir.remove(selected);
                }
                envirSet.repaint();
            }
        }
    }

    static void hShape() {
        int cx, cy, cw, ch, min;
        int type = envirSet.element;
        int floor = envirSet.floor;
        boolean iscontext = envirSet.iscontext;
        isfill = envirSet.isfill;
        isborder = envirSet.isborder;

        cx = Math.min(mx, tx) + 3;//起点x坐标
        cy = Math.min(my, ty) + 3;//起点y坐标
        cw = Math.abs(mx - tx);//两点坐标的x距离
        ch = Math.abs(my - ty);//两点坐标的y距离
        min = Math.min(cw, ch);

        if (!isfill) {
            fillc = null;
        } else {
            fillc = envirSet.fillcolor;
        }

        if (!isborder) {
            borderc = null;
        } else {
            borderc = envirSet.bordercolor;
        }

        switch (type) {
            case 0:
                graph = new Graph(floor, type, iscontext, fillc, borderc, cx, cy, cw, ch, "");
                break;
            case 1:
                graph = new Graph(floor, type, iscontext, fillc, borderc, cx, cy, min, min, "");
                break;
            case 2:
                if (min > 35) {
                    min = 35;
                }
                graph = new Graph(floor, type, iscontext, fillc, borderc, cx, cy, min, min, "");
                break;
            case 3:
                graph = new Graph(floor, type, iscontext, fillc, borderc, cx, cy, cw, ch, "");
                break;
            case 4:
                graph = new Graph(floor, type, iscontext, fillc, borderc, mx, my, tx, ty, "");
                break;
            default:
        }
        envirSet.repaint();
    }

    static void hDragged(Point p) {
        if (envirSet.method == 2) {
            if (selected != null) {
                moveto(selected, p);
                envirSet.repaint();
            }
        } else if (envirSet.method == 1) {
            checkSelect(p);
            if (selected != null) {
                graphs.remove(selected);
                if (selected.getFloor() == 2) {
                    cir.remove(selected);
                } else {
                    notcir.remove(selected);
                }
                envirSet.repaint();
            }
        } else if (envirSet.method == 0) {
            tx = p.x;
            ty = p.y;
            hShape();
        }
    }

    static void hReleassed(Point p) {
        if (envirSet.method == 0) {
            if (graph != null) {
                if (graph.getStyle() != 4 && graph.getStyle() != 2) {
                    int min = Math.min(graph.getWide(), graph.getHeight());
                    maxR = Math.min(maxR, min - 10);
                }
                for (Graph g : graphs) {
                    g.setCurrent(false);
                    if (g.getStyle() == 2) {
                        g.setWide(35);
                        g.setHeight(35);
                    }
                }
                graphs.add(graph);
                Collections.sort(graphs, comparator);
                if (graph.getStyle() == 2) {
                    cir.add(graph);
                    Collections.sort(cir, comparator);
                } else {
                    notcir.add(graph);
                    Collections.sort(notcir, comparator);
                }
                selected = graph;
                graph = null;
            }
            cPressed();
            envirSet.repaint();
        }
    }

    static void moveto(Graph select, Point p) {
        if (select.getFloor() != envirSet.floor) {
            return;
        }
        Shape shape = judgeShape(select);
        if (shape instanceof Rectangle) {
            Rectangle r = (Rectangle) shape;
            r.setLocation(r.x + p.x - mx, r.y + p.y - my);
            select.setX1(r.x);
            select.setY1(r.y);
            mx = p.x;
            my = p.y;
        } else if (shape instanceof Ellipse2D) {
            Ellipse2D e = (Ellipse2D) shape;
            e.setFrame(e.getX() + p.x - mx, e.getY() + p.y - my, e.getWidth(), e.getHeight());
            select.setX1((int) e.getX());
            select.setY1((int) e.getY());
            mx = p.x;
            my = p.y;
        } else if (shape instanceof Line2D) {
            Line2D l = (Line2D) shape;
            l.setLine(l.getX1() + p.x - mx, l.getY1() + p.y - my, l.getX2() + p.x - mx, l.getY2() + p.y - my);
            select.setX1((int) l.getX1());
            select.setY1((int) l.getY1());
            select.setWide((int) l.getX2());
            select.setHeight((int) l.getY2());
            mx = p.x;
            my = p.y;
        } else if (shape instanceof RoundRectangle2D) {
            RoundRectangle2D re = (RoundRectangle2D) shape;
            re.setFrame(re.getX() + p.x - mx, re.getY() + p.y - my, re.getWidth(), re.getHeight());
            select.setX1((int) re.getX());
            select.setY1((int) re.getY());
            mx = p.x;
            my = p.y;
        }
    }

    static void drawAll(Graphics2D g) {
        Shape shape = null;
        if (graph != null) {
            graph.setCurrent(true);
            graphs.add(graph);
            if (graph.getStyle() == 2) {
                cir.add(graph);
            } else {
                notcir.add(graph);
            }
        }
        trimGraphs();

        for (Graph graphic : notcir) {
            if (graphic.getFloor() == envirSet.floor) {
                shape = judgeShape(graphic);
                if (graphic.getFillColor() != null) {
                    g.setColor(graphic.getFillColor());
                    g.fill(shape);
                }
                if (graphic.getBordercolor() != null) {
                    g.setColor(graphic.getBordercolor());
                    g.setStroke(bstroke);
                    g.draw(shape);
                }

                g.setColor(Color.black);
                if (graphic.getStyle() != 4) {
                    g.setFont(new Font("Times New Roman", Font.PLAIN, 16));
                    if (graphic.getStyle() != 0 || (graphic.getStyle() == 0 && graphic.isIsContext())) {
                        g.drawString(graphic.getContext(), graphic.getX1() + graphic.getWide() / 2 - graphic.getContext().length() * g.getFont().getSize() / 4, graphic.getY1() + graphic.getHeight() / 2 + g.getFont().getSize() / 3);
                    }
                }

                if (graphic.isCurrent()) {
                    g.setColor(Color.GREEN);
                    g.setStroke(selectStroke);
                    g.draw(shape.getBounds());
                }
            }
        }

        for (Graph graphic : cir) {
            if (graphic.getFloor() == envirSet.floor) {
                shape = judgeShape(graphic);
                if (graphic.getFillColor() != null) {
                    g.setColor(graphic.getFillColor());
                    g.fill(shape);
                }
                if (graphic.getBordercolor() != null) {
                    g.setColor(graphic.getBordercolor());
                    g.setStroke(bstroke);
                    g.draw(shape);
                }

                g.setColor(Color.black);
                g.setFont(new Font("Times New Roman", Font.BOLD, 2 * graphic.getWide() / 3));
                g.drawString(graphic.getContext(), graphic.getX1() + graphic.getWide() / 2 - graphic.getContext().length() * g.getFont().getSize() / 4, graphic.getY1() + graphic.getHeight() / 2 + g.getFont().getSize() / 3);

                if (graphic.isCurrent()) {
                    g.setColor(Color.GREEN);
                    g.setStroke(selectStroke);
                    g.draw(shape.getBounds());
                }
            }
        }

        if (graph != null) {
            graphs.remove(graph);
            if (graph.getStyle() == 2) {
                cir.remove(graph);
            } else {
                notcir.remove(graph);
            }
        }
        envirSet.graphs = graphs;
        envirSet.cir = cir;
        envirSet.notcir = notcir;
    }

    static void checkSelect(Point p) {
        selected = null;
        Shape shape = null;
        for (int i = graphs.size(); i > 0; i--) {
            Graph temGraphic = graphs.get(i - 1);
            shape = judgeShape(temGraphic);
            if (temGraphic.getFloor() == envirSet.floor && shape.getBounds().contains(p)) {
                selected = temGraphic;
                break;
            }
        }
        if (selected == null) {
        } else {
            for (Graph g : graphs) {
                if (selected != g) {
                    g.setCurrent(false);
                } else {
                    g.setCurrent(true);
                }
            }
        }
        envirSet.repaint();
    }

    static Shape judgeShape(Graph graph) {
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

    static void clear() {
        graphs.clear();
        cir.clear();
        notcir.clear();
        envirSet.repaint();
    }

    static void save() {
        for (Graph g : graphs) {
            g.setCurrent(false);
        }
        int x = jfc.showSaveDialog(envirSet);
        if (x == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(graphs);
                oos.flush();
                oos.close();
            } catch (Exception e) {
            }
        }
    }

    static void load() {
        int x = jfc.showOpenDialog(envirSet);
        if (x == JFileChooser.APPROVE_OPTION) {
            File f = jfc.getSelectedFile();
            try {
                graphs.clear();
                notcir.clear();
                cir.clear();
                ObjectInputStream oos = new ObjectInputStream(new FileInputStream(f));
                Object r = oos.readObject();
                if (r != null) {
                    graphs = (ArrayList<Graph>) r;
                    coordChange();
                    int floornum = 1;
                    for (Graph g : graphs) {
                        g.setIsContext(envirSet.iscontext);
                        if ((floornum - 1) <= g.getFloor()) {
                            floornum = g.getFloor() + 1;
                        }
                        if (g.getStyle() == 2) {
                            cir.add(g);
                        } else {
                            notcir.add(g);
                        }
                    }
                    String context[] = new String[floornum];
                    for (int i = 0; i < floornum; i++) {
                        context[i] = "Floor " + (i + 1);
                    }
                    envirSet.getFloorBox().setModel(new javax.swing.DefaultComboBoxModel(context));
                    envirSet.floor = envirSet.getFloorBox().getSelectedIndex();
                    envirSet.repaint();
                }
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void coordChange() {
        int addx = 0, addy = 0;

        int x = envirSet.getDrawPanel().getX();
        int y = envirSet.getDrawPanel().getY();
        int w = envirSet.getDrawPanel().getWidth();
        int h = envirSet.getDrawPanel().getHeight();

        if (graphs.isEmpty()) {
            return;
        }
        int minX = graphs.get(0).getX1();
        int maxX = graphs.get(0).getX1() + graphs.get(0).getWide();
        int minY = graphs.get(0).getY1();
        int maxY = graphs.get(0).getY1() + graphs.get(0).getHeight();

        int floorNum = 0;
        if (!graphs.isEmpty()) {
            for (Graph g : graphs) {
                if (g.getFloor() >= (floorNum - 1)) {
                    floorNum = g.getFloor() + 1;
                }
            }
        }

        for (int i = 0; i < floorNum; i++) {
            for (Graph g : graphs) {
                if (g.getFloor() == i) {
                    if (minX >= g.getX1()) {
                        minX = g.getX1();
                    }
                    if (minY >= g.getY1()) {
                        minY = g.getY1();
                    }
                    if (g.getStyle() != 4) {
                        if (maxX <= g.getX1() + g.getWide()) {
                            maxX = g.getX1() + g.getWide();
                        }
                        if (maxY <= g.getY1() + g.getHeight()) {
                            maxY = g.getY1() + g.getHeight();
                        }
                    } else {
                        if (maxX <= g.getWide()) {
                            maxX = g.getWide();
                        }
                        if (maxY <= g.getHeight()) {
                            maxY = g.getHeight();
                        }
                    }
                }
            }

            int length = maxX - minX;
            int height = maxY - minY;
            if (w - length >= 0) {
                addx = (w - length) / 2;
            }
            if (h - height >= 0) {
                addy = (h - height + 60) / 2;
            }
            for (Graph graph : graphs) {
                if (graph.getFloor() == i) {
                    int x1 = graph.getX1();
                    int y1 = graph.getY1();
                    graph.setX1(x1 + x + addx - minX);
                    graph.setY1(y1 + y + addy - minY);
                    if (graph.getStyle() == 4) {
                        int x2 = graph.getWide();
                        int y2 = graph.getHeight();
                        graph.setWide(x2 + x + addx - minX);
                        graph.setHeight(y2 + y + addy - minY);
                    }
                }
            }
        }
    }

    static void cPressed() {
        if (selected != null) {
            fillc = envirSet.fillcolor;
            borderc = envirSet.bordercolor;
            isfill = envirSet.isfill;
            isborder = envirSet.isborder;
            selected.setIsContext(envirSet.iscontext);
            if (isfill) {
                selected.setFillColor(fillc);
            } else {
                selected.setFillColor(null);
            }
            if (isborder) {
                selected.setBordercolor(borderc);
            } else {
                selected.setBordercolor(null);
            }
            envirSet.repaint();
        }
    }

    static void trimGraphs() {
        int rectNum = 1, squNum = 1, cirTotalNum = 1, cirFloNum = 0, roundrectNum = 1;
        int floNum = 0;
        if (graphs.isEmpty()) {
            return;
        }
        int temp = 0;
        for (Graph g : graphs) {
            if (g.getStyle() == 2) {
                g.setContext("" + cirTotalNum);
                cirTotalNum++;
                Shape cirsh = judgeShape(g);
                boolean t = g.isCurrent();
                g.setCurrent(false);

                //处理圆心位置，使其在矩形外一点
                for (Graph grect : graphs) {
                    if (grect.getFloor() == g.getFloor() && grect.getStyle() != 4 && grect.getStyle() != 2) {
                        Shape rectsh = judgeShape(grect);
                        if (rectsh.contains(cirsh.getBounds().getCenterX(), cirsh.getBounds().getCenterY())) {
                            Rectangle r = rectsh.getBounds();
                            Line2D line1 = new Line2D.Float(r.x, r.y, r.x, r.y + r.height);
                            Line2D line2 = new Line2D.Float(r.x, r.y, r.x + r.width, r.y);
                            Line2D line3 = new Line2D.Float(r.x + r.width, r.y, r.x + r.width, r.y + r.height);
                            Line2D line4 = new Line2D.Float(r.x, r.y + r.height, r.x + r.width, r.y + r.height);

                            if (line1.intersects(cirsh.getBounds())) {
                                //线段与矩形左竖线相交
                                if (!g.isCurrent()) {
                                    g.setX1(r.x - 2 - g.getWide() / 2);
                                } else {
                                    g.setX1(r.x - g.getWide() / 2);
                                }
                            } else if (line2.intersects(cirsh.getBounds())) {
                                //线段与矩形上横线相交
                                if (!g.isCurrent()) {
                                    g.setY1(r.y - 2 - g.getHeight() / 2);
                                } else {
                                    g.setY1(r.y - g.getHeight() / 2);
                                }
                            } else if (line4.intersects(cirsh.getBounds())) {
                                //线段与矩形下横线相交
                                if (!g.isCurrent()) {
                                    g.setY1(r.y + r.height + 2 - g.getHeight() / 2);
                                } else {
                                    g.setY1(r.y + r.height - g.getHeight() / 2);
                                }
                            } else if (line3.intersects(cirsh.getBounds())) {
                                //线段与矩形右竖线相交
                                if (!g.isCurrent()) {
                                    g.setX1(r.x + r.width + 2 - g.getWide() / 2);
                                } else {
                                    g.setX1(r.x + r.width - g.getWide() / 2);
                                }
                            }
                            g.setCurrent(true);
                        }
                    }
                }
                g.setCurrent(t);
            }

            if (g.getFloor() == envirSet.floor && g.getStyle() == 4) {
                int difX = Math.abs(g.getX1() - g.getWide());
                int difY = Math.abs(g.getY1() - g.getHeight());
                if (difX < 10) {
                    g.setWide(g.getX1());
                }
                if (difY < 10) {
                    g.setHeight(g.getY1());
                }
            }

            if (g.getFloor() == envirSet.floor) {
                g.setCurrent(false);
                floNum++;
                if (g.getStyle() == 2) {
                    cirFloNum++;
                } else if (g.getStyle() == 1) {
                    temp++;
                }
            }
        }

        int size = 0;
        ArrayList<Graph> trimGraphs = new ArrayList<Graph>();
        Graph minGraph = graphs.get(0), tempGraph;

        int minY = -1;
        while (floNum > cirFloNum) {
            for (Graph gra : graphs) {
                if (gra.getFloor() == envirSet.floor && gra.getStyle() != 2 && !gra.isCurrent()) {
                    minGraph = gra;
                    break;
                }
            }

            for (int i = 0; i < graphs.size(); i++) {
                tempGraph = graphs.get(i);
                if (tempGraph.getFloor() == envirSet.floor && tempGraph.getStyle() != 2 && !tempGraph.isCurrent() && minGraph.getY1() >= tempGraph.getY1()) {
                    minGraph = tempGraph;
                }
            }
            trimGraphs.add(minGraph);
            for (int i = 0; i < graphs.size(); i++) {
                tempGraph = graphs.get(i);
                int diff = Math.abs(minGraph.getY1() - tempGraph.getY1());
                if (tempGraph != minGraph && tempGraph.getFloor() == envirSet.floor && tempGraph.getStyle() != 2 && !tempGraph.isCurrent() && diff < 5) {
                    trimGraphs.add(tempGraph);
                }
            }

            size = trimGraphs.size();
            if (trimGraphs.isEmpty()) {
                continue;
            }
            minGraph = trimGraphs.get(0);
            for (int i = 0; i < trimGraphs.size(); i++) {
                tempGraph = trimGraphs.get(i);
                if (minGraph.getX1() > tempGraph.getX1()) {
                    minGraph = tempGraph;
                }
            }

            //不同y值之间的图像处理
            xyAlign(minGraph);

            floNum -= size;
            int secX = 0;
            Graph gra = null;
            while (gra != minGraph) {
                gra = minGraph;
                if (minGraph.getStyle() == 0) {
                    String str = (minGraph.getFloor() + 1) + "-" + (minGraph.getFloor() + 1);
                    if (rectNum < 10) {
                        str += "0" + rectNum;
                    } else {
                        str += rectNum;
                    }
                    rectNum++;
                    minGraph.setContext(str);
                } else if (minGraph.getStyle() == 1) {
                    minGraph.setContext("Lift" + squNum);
                    squNum++;
                } else if (minGraph.getStyle() == 3) {
                    minGraph.setContext("Stair" + roundrectNum);
                    roundrectNum++;
                }
                minGraph.setCurrent(true);
                if (minGraph.getStyle() == 0 || minGraph.getStyle() == 1 || minGraph.getStyle() == 3) {
                    secX = minGraph.getX1() + minGraph.getWide();
                } else if (minGraph.getStyle() == 4) {
                    secX = minGraph.getWide();
                }
                for (Graph gr : trimGraphs) {
                    if (gr.isCurrent()) {
                        continue;
                    }
                    int trimdif = Math.abs(secX - gr.getX1());
                    if (trimdif < 5) {
                        gr.setX1(secX);
                        if (minGraph.getStyle() == 0 || minGraph.getStyle() == 1 || minGraph.getStyle() == 3) {
                            gr.setY1(minGraph.getY1());
                        } else if (minGraph.getStyle() == 4) {
                            gr.setY1(minGraph.getHeight());
                        }
                        gr.setCurrent(true);
                        if (minGraph.getStyle() == 0 && gr.getStyle() == 0 || minGraph.getStyle() == 1 && gr.getStyle() == 1
                                || minGraph.getStyle() == 3 && gr.getStyle() == 3) {
                            gr.setHeight(minGraph.getHeight());
                        }

                        minGraph = gr;
                        break;
                    }
                }
                size--;
            }
            floNum += size;
            trimGraphs.clear();
        }

        for (Graph g : graphs) {
            if (selected != g) {
                g.setCurrent(false);
            } else {
                g.setCurrent(true);
            }
        }
    }

    static void xyAlign(Graph gr) {
        int mX = gr.getX1();
        int mY = gr.getY1();
        int mW = gr.getWide();
        int mH = gr.getHeight();
        int style = gr.getStyle();
        boolean isClear = false;

        for (Graph g : graphs) {
            if (g.isCurrent()) {
                int x = g.getX1();
                int y = g.getY1();
                int w = g.getWide();
                int h = g.getHeight();
                int gsty = g.getStyle();

                if (style == 4) {
                    if (gsty == 4) {
                        if ((x - 5 < mX && mX < w + 5) && (y - 5 < mY && mY < h + 5) || (x - 5 < mW && mW < w + 5) && (y - 5 < mH && mH < h + 5)) {
                            isClear = true;
                        }
                    } else if (gsty == 0 || gsty == 1 || gsty == 3) {
                        if ((x - 5 < mX && mX < x + w + 5) && (y - 5 < mY && mY < y + h + 5) || (x - 5 < mW && mW < x + w + 5) && (y - 5 < mH && mH < y + h + 5)) {
                            isClear = true;
                        }
                    }
                } else if (style == 0 || style == 1 || style == 3) {
                    if (gsty == 4) {
                        if ((x - 5 < mX && mX < w + 5) && (y - 5 < mY && mY < h + 5) || (x - 5 < mX && mX < w + 5) && (y - 5 < mY + mH && mY + mH < h + 5)
                                || (x - 5 < mX + mW && mX + mW < w + 5) && (y - 5 < mY && mY < h + 5)
                                || (x - 5 < mX + mW && mX + mW < w + 5) && (y - 5 < mY + mH && mY + mH < h + 5)) {
                            isClear = true;
                        }
                    } else if (gsty == 0 || gsty == 1 || gsty == 3) {
                        if ((x - 5 < mX && mX < x + w + 5) && (y - 5 < mY && mY < y + h + 5) || (x - 5 < mX && mX < x + w + 5) && (y - 5 < mY + mH && mY + mH < y + h + 5)
                                || (x - 5 < mX + mW && mX + mW < x + w + 5) && (y - 5 < mY && mY < y + h + 5)
                                || (x - 5 < mX + mW && mX + mW < x + w + 5) && (y - 5 < mY + mH && mY + mH < y + h + 5)) {
                            isClear = true;
                        }
                    }
                }

                if (isClear) {
                    if (Math.abs(x - mX) < 10) {
                        gr.setX1(x);
                    }
                    if (Math.abs(y - mY) < 10) {
                        gr.setY1(y);
                    }

                    if (g.getStyle() == 4) {
                        //正向
                        if (Math.abs(w - mX) < 10) {
                            gr.setX1(w);
                        }
                        if (Math.abs(h - mY) < 10) {
                            gr.setY1(h);
                        }

                        //逆向
                        if (gr.getStyle() == 4) {
                            if (Math.abs(x - mW) < 10) {
                                gr.setWide(x);
                            }
                            if (Math.abs(y - mH) < 10) {
                                gr.setHeight(y);
                            }
                            if (Math.abs(w - mW) < 10) {
                                gr.setWide(w);
                            }
                            if (Math.abs(h - mH) < 10) {
                                gr.setHeight(h);
                            }
                        } else if (gr.getStyle() == 0 || gr.getStyle() == 1) {
                            if (Math.abs(x - mX - mW) < 10) {
                                gr.setX1(x - mW);
                            }
                            if (Math.abs(y - mY - mH) < 10) {
                                gr.setY1(y - mH);
                            }

                            if (Math.abs(w - mX - mW) < 10) {
                                gr.setX1(w - mW);
                            }
                            if (Math.abs(h - mY - mH) < 10) {
                                gr.setY1(h - mH);
                            }
                        }
                    } else if (g.getStyle() == 0 || g.getStyle() == 1) {
                        if (Math.abs(x + w - mX) < 5) {
                            gr.setX1(x + w);
                        }
                        if (Math.abs(y + h - mY) < 5) {
                            gr.setY1(y + h);
                        }
                        if (gr.getStyle() == 0 || gr.getStyle() == 1) {
                            if (Math.abs(x - mX - mW) < 10) {
                                gr.setX1(x - mW);
                            }
                            if (Math.abs(y - mY - mH) < 10) {
                                gr.setY1(y - mH);
                            }

                            if (Math.abs(x + w - mX - mW) < 10) {
                                gr.setX1(x + w - mW);
                            }
                            if (Math.abs(y + h - mY - mH) < 10) {
                                gr.setY1(y + h - mH);
                            }
                        } else if (gr.getStyle() == 4) {
                            if (Math.abs(x - mW) < 10) {
                                gr.setWide(x);
                            }
                            if (Math.abs(y - mH) < 10) {
                                gr.setHeight(y);
                            }
                            if (Math.abs(x + w - mW) < 10) {
                                gr.setWide(x + w);
                            }
                            if (Math.abs(y + h - mH) < 10) {
                                gr.setHeight(y + h);
                            }
                        }
                    }
                }
            }
        }
    }
}
