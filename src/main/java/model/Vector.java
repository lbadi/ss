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

    public Vector sum(Vector vector){
        double moduleX = getModule() * Math.cos(getAngle()) + vector.getModule() * Math.cos(vector.getAngle());
        double moduleY = getModule() * Math.sin(getAngle()) + vector.getModule() * Math.sin(vector.getAngle());
        double newModule = Math.sqrt(Math.pow(moduleX,2) + Math.pow(moduleY,2));
        double newAngle = Math.atan2(moduleY,moduleX);
        setModule(newModule);
        setAngle(newAngle);
        return this;
    }

    public double angleWith(Vector vector){
        return Math.acos(scalarProductWith(vector) / (getModule() * vector.getModule()));
    }

    public double scalarProductWith(Vector vector){
        double x1 = getModuleX();
        double y1 = getModuleY();
        double x2 = vector.getModuleX();
        double y2 = vector.getModuleY();
        return x1*x2 + y1*y2;
    }


    public double getModuleX(){
        return Math.cos(getAngle()) * getModule();
    }
    public double getModuleY(){
        return Math.sin(getAngle()) * getModule();
    }

    public Vector mult(double val){
        setModule(getModule()*val);
        return this;
    }

    public Vector invert(){
        setAngle((getAngle() + Math.PI) % (Math.PI*2));
        return this;
    }
}
