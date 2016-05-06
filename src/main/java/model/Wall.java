package model;

import util.PlainWritable;

/**
 * Represent a wall or barrier
 */
public class Wall{

    private double lenght;
    private double normal;
    private double positionX;
    private double positionY;
    private double width;

    public Wall(double lenght, double normal, double positionX, double positionY, double width) {
        this.lenght = lenght;
        this.normal = normal;
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
    }

    public double getLenght() {
        return lenght;
    }

    public void setLenght(double lenght) {
        this.lenght = lenght;
    }

    public double getNormal() {
        return normal;
    }

    public void setNormal(double normal) {
        this.normal = normal;
    }

    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public double getWidth() {
        return width;
    }
}
