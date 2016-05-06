package model;

/**
 * Created by nicolas on 01/05/16.
 */
public class Vector {

    private double module;
    private double angle;

    public Vector(double module, double angle) {
        this.angle = angle;
        this.module = module;
    }

    public double getModule() {
        return module;
    }

    public void setModule(double module) {
        this.module = module;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
