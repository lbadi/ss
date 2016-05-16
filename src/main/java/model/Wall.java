package model;

import util.PlainWritable;

/**
 * Represent a wall or barrier
 */
public class Wall{

    private double x1;
    private double x2;

    private double y1;
    private double y2;

    boolean open = false;

    /**
     * Considerar que depende el orden de los puntos, la direccion del vector normal cambia.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public Wall(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public double pDistance(double x, double y) {

        double A = x - x1;
        double B = y - y1;
        double C = x2 - x1;
        double D = y2 - y1;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = -1;
        if (len_sq != 0) //in case of 0 length line
            param = dot / len_sq;

        double xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        }
        else if (param > 1) {
            xx = x2;
            yy = y2;
        }
        else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        double dx = x - xx;
        double dy = y - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getNormalAngle(Particle particle){
        double tangAngle = getTangencialAngle(particle);
        return tangAngle - Math.PI/2;
    }

    public double getTangencialAngle(Particle particle){
        double mod = Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
        double x = x1-x2;
        double y = y1-y2;
        double tangAngle = Math.atan2(y,x);
        return tangAngle;
    }
    public Vector getTangencialVector(Particle particle){
        double tangencialAngle = getTangencialAngle(particle);
        return new Vector(1,tangencialAngle);
    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }

    public double getY1() {
        return y1;
    }

    public double getY2() {
        return y2;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
