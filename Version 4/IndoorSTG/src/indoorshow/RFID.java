/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indoorshow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *
 * @author kdelab
 */
public class RFID {

    private String nodeid;
    private ArrayList<NetworkLink> linklist = new ArrayList<NetworkLink>();
    private ArrayList<Path> shortestPath = new ArrayList<Path>();
    private ArrayList<NetworkNode> nodelist = new ArrayList<NetworkNode>();
    private PriorityQueue<NetworkNode> queue;
    private boolean mark[];//标记
    private final double MAX = 9999;
    private Comparator<NetworkNode> comparator = new Comparator<NetworkNode>() {
        @Override
        public int compare(NetworkNode o1, NetworkNode o2) {
            if (o1.getDistance() != o2.getDistance()) {
                return (int) (o1.getDistance() - o2.getDistance());
            } else {
                return o1.getNodeid().compareTo(o2.getNodeid());
            }
        }
    };

    public RFID(String nodeid, double graph[]) {
        this.nodeid = nodeid;
        int number = graph.length;
        queue = new PriorityQueue<NetworkNode>(number, comparator);
        mark = new boolean[number];

        for (int i = 0; i < number; i++) {
            double distance = graph[i];
            mark[i] = false;
            NetworkNode node = new NetworkNode((i + 1) + "", distance);
            nodelist.add(node);
            queue.offer(nodelist.get(i));
            Path path = new Path();
            shortestPath.add(path);
            if (distance > 0 && distance < MAX) {
                NetworkLink linknode = new NetworkLink(nodeid, (i + 1) + "", distance);
                linklist.add(linknode);
            }
        }
    }

    public void computeShortestPaths(ArrayList<RFID> rfid) {
        NetworkNode current = queue.poll();//自身这个点
        int index = Integer.parseInt(current.getNodeid()) - 1;
        if (!current.getNodeid().equalsIgnoreCase(nodeid) || mark[index]) {
            System.out.println("Priority Queue is Unavailable: " + nodeid);
            return;
        }
        mark[index] = true;//标记自身
        this.shortestPath.get(index).getPathNode().add(current.getNodeid());//到自己的最短路径完成
        for (NetworkLink net : linklist) {//更新到邻接点的最短路径
            if (net.getFromNode().equalsIgnoreCase(nodeid)) {
                int linkindex = Integer.parseInt(net.getToNode()) - 1;
                this.shortestPath.get(linkindex).getPathNode().add(net.getFromNode());
            } else {
                System.out.println("Networklink Node is Unavailable: " + net.getToNode());
            }
        }

        while (!queue.isEmpty()) {
            current = queue.poll();
            index = Integer.parseInt(current.getNodeid()) - 1;
            if (!mark[index]) {
                mark[index] = true;
                this.shortestPath.get(index).getPathNode().add(current.getNodeid());
                for (NetworkLink net : rfid.get(index).getLinklist()) {//更新邻接点的距离
                    if (net.getFromNode().equalsIgnoreCase(current.getNodeid())) {
                        int linkindex = Integer.parseInt(net.getToNode()) - 1;
                        if (!mark[linkindex] && nodelist.get(linkindex).getDistance()
                                > nodelist.get(index).getDistance() + net.getDistance()) {
                            queue.remove(nodelist.get(linkindex));//更新优先级队列中点距离
                            nodelist.get(linkindex).setDistance(nodelist.get(index).getDistance() + net.getDistance());
                            queue.offer(nodelist.get(linkindex));

                            this.shortestPath.get(linkindex).getPathNode().clear();
                            this.shortestPath.get(linkindex).getPathNode().addAll(this.shortestPath.get(index).getPathNode());
                        }
                    } else {
                        System.out.println("Networklink Node is Unavailable: " + net.getToNode());
                    }
                }
            }
        }
    }

    public ArrayList<NetworkLink> getLinklist() {
        return linklist;
    }

    public ArrayList<Path> getShortestPath() {
        return this.shortestPath;
    }

    public class Path {

        ArrayList<String> pathnode = new ArrayList<String>();

        public ArrayList<String> getPathNode() {
            return pathnode;
        }

        @Override
        public String toString() {
            String s = "Path{";
            for (int i = 0; i < pathnode.size(); i++) {
                if (i == pathnode.size() - 1) {
                    s += pathnode.get(i) + "}";
                } else {
                    s += pathnode.get(i) + "->";
                }
            }
            return s;
        }
    }

    private class NetworkNode {

        String nodeid;
        double distance;

        public NetworkNode(String nodeid, double distance) {
            this.nodeid = nodeid;
            this.distance = distance;
        }

        public String getNodeid() {
            return nodeid;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }
    }
}
