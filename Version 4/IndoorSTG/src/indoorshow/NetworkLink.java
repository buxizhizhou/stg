/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indoorshow;

/**
 *
 * @author kdelab
 */
public class NetworkLink {

    String fromNode;
    String toNode;
    double distance;

    public NetworkLink(String from, String to, double distance) {

        this.fromNode = from;
        this.toNode = to;
        this.distance = distance;

    }

    public String getFromNode() {

        return this.fromNode;

    }

    public String getToNode() {

        return this.toNode;

    }

    public double getDistance() {

        return this.distance;

    }

    // Find the neighour v of w   
    // @name w'name   
    public boolean containsNode(String name) {

        if (fromNode.equals(name) || toNode.equals(name)) {
            return true;
        }
        return false;
    }
}
