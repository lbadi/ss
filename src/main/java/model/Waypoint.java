package model;

import java.util.ArrayList;
import java.util.List;


public class Waypoint extends Particle{

    private List<Waypoint> neighbours = new ArrayList<>();
    private double x;
    private double y;

    public Waypoint(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void addNeighbour(Waypoint neighbour){
        if(neighbour.equals(this)){
            return;
        }
        neighbours.add(neighbour);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public List<Waypoint> getWaypointsNeighbours() {
        return neighbours;
    }


}
