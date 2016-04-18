package model;

public class ResultContainer {
    double t;
    long collisionQuantity;
    double promSpeedX;
    double promSpeedY;

    public ResultContainer(double totalTime, long quantityOfCollisionInInterval, double promSpeedX, double promSpeedY) {
        this.t = totalTime;
        collisionQuantity = quantityOfCollisionInInterval;
        this.promSpeedX = promSpeedX;
        this.promSpeedY = promSpeedY;
    }

    public double getT() {
        return t;
    }

    public double getPromFrecuency(){
        return collisionQuantity/t;
    }

    public double getPromSpeedX() {
        return promSpeedX;
    }

    public double getPromSpeedY() {
        return promSpeedY;
    }
}