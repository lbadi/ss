package model;

import util.PlainWritable;

/**
 * Represent a wall or barrier
 */
public class Wall implements PlainWritable {

    //A * x + B*y + C = 0
    private double a;
    private double b;
    private double c;

    //The lenght of a barrier
    private double lenght;

    @Override
    public PlainWritable readObject(String plainObject) {
        return null;
    }

    public Wall(double a, double b, double c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public String writeObject() {
        return null;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }
}
