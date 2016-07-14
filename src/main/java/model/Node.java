package model;

/**
 * Created by leonelbadi on 14/7/16.
 */
public class Node implements Comparable<Node>{
    Waypoint waypoint;
    double cost;
    Waypoint target;
    Node parent;

    public Node(Waypoint waypoint, double cost, Waypoint target, Node parent){
        this.waypoint = waypoint;
        this.cost = cost;
        this.target = target;
        this.parent = parent;
    }

    @Override
    public int compareTo(Node o) {
        double value = (waypoint.distanceToCenterOf(target) + cost) - (o.getWaypoint().distanceToCenterOf(target) + o.getCost());
        if(value > 0){
            return 1;
        }else if(value == 0){
            return 0;
        }
        else{
            return -1;
        }
    }

    public Waypoint getWaypoint() {
        return waypoint;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return getWaypoint() != null ? getWaypoint().equals(node.getWaypoint()) : node.getWaypoint() == null;

    }

    @Override
    public int hashCode() {
        return getWaypoint() != null ? getWaypoint().hashCode() : 0;
    }

    public Node getParent() {
        return parent;
    }
}
