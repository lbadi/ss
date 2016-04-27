package model;

/**
 * Created by nicolas on 27/04/16.
 */
public class Oscilator {
    private static double mass;
    private static double k;
    private static double gamma;
    private double acceleration;
    private double position;
    private double speed;

    public Oscilator(double mass, double k, double gamma, double initialPosition){
        this.mass = mass;
        this.k = k;
        this.gamma = gamma;
        this.position = initialPosition;
        this.speed = - gamma / (mass/2);
        this.acceleration = (-k*position - gamma * speed) / mass;
    }

    public static double getMass() {
        return mass;
    }

    public static double getK() {
        return k;
    }

    public static double getGamma() {
        return gamma;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAnaliticPosition(double t){
        return Math.exp(-(gamma/(2*mass))*t) * Math.cos(Math.sqrt((k/mass)-(Math.pow(gamma, 2)/(4*Math.pow(mass, 2))) )*t);
    }

    public void makeEulerStep(double t){
        position = position + t * speed + Math.pow(t,2) *  acceleration / 2;
        speed = speed + t * acceleration;
        updateAcceleration();
    }

    public void makeVerletStep(double t, double previousPosition){
        position = 2 * position - previousPosition + Math.pow(t,2) * acceleration;
        speed = (position - previousPosition) / (2*t);
        updateAcceleration();
    }

    public void makeBeemanStep(double t, double previousAcceleration){
        position = position + speed * t + (2/3.0) * acceleration * Math.pow(t,2) - (1/6.0) * previousAcceleration * Math.pow(t,2);
        speed = (speed - (k*t*position) / (3*mass) + (5/6.0)*acceleration*t - (1/6.0)*previousAcceleration*t) / (1 + (gamma*t)/(3*mass));
        updateAcceleration();
    }

    private void updateAcceleration(){
        acceleration = (-k*position - gamma * speed) / mass;
    }


}
