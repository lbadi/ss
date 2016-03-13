package model;

import util.PlainWritable;

public class Particle implements PlainWritable{

    private static final String separator = " ";

    private double x;
    private double y;
    private double speedX;
    private double speedY;
    private double radius;
    private double color;

    @Override
    public PlainWritable readObject(String plainObject) {
        String[] values = plainObject.split(separator);
        setX(Double.valueOf(values[0]));
        setY(Double.valueOf(values[1]));
        setSpeedX(Double.valueOf(values[2]));
        setSpeedY(Double.valueOf(values[3]));
        return this;
    }

    @Override
    public String writeObject() {
        StringBuilder sb = new StringBuilder();
        sb.append(getX())
                .append(separator)
                .append(getY())
                .append(separator)
                .append(getSpeedX())
                .append(separator)
                .append(getSpeedY());
        return sb.toString();
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public Particle(double x, double y, double speedX, double speedY) {
        this.x = x;
        this.y = y;
        this.speedY = speedY;
        this.speedX = speedX;
    }

    public Particle(double x, double y, double speedX, double speedY, double radius, double color) {
        this.x = x;
        this.y = y;
        this.speedY = speedY;
        this.speedX = speedX;
        this.radius = radius;
        this.color = color;
    }

    /**
     * Formato de entrada del archivo estático.
     * @param radius
     * @param color
     */
    public Particle(double radius, double color) {
        this.radius = radius;
        this.color = color;
    }

    public Particle(){
    }

    @Override
    public String toString() {
        return "Particle{" +
                "x=" + x +
                ", y=" + y +
                ", speedX=" + speedX +
                ", speedY=" + speedY +
                '}';
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getColor() {
        return color;
    }

    public void setColor(double color) {
        this.color = color;
    }

}
