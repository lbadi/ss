package model;

public class Oscilator {

    private double mass;
    private double k;
    private double gamma;
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

    public double getMass() {
        return mass;
    }

    public double getK() {
        return k;
    }

    public double getGamma() {
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

    public double getAnalyticPosition(double t){
        return Math.exp(-(gamma/(2*mass))*t) * Math.cos(Math.sqrt((k/mass)-(Math.pow(gamma, 2)/(4*Math.pow(mass, 2))) )*t);
    }

    public void makeEulerStep(double t){
        position = position + t * speed + Math.pow(t,2) *  acceleration / 2;
        speed = speed + t * acceleration;
        updateAcceleration();
    }

    public void makeVelocityVerletStep(double t){
        position = position + (t * speed) + (Math.pow(t, 2) * acceleration);
        speed = (speed + (t / 2) * (acceleration  - (k * position / mass))) / (1 + ((gamma * t) / (2 * mass)));
        updateAcceleration();
    }

    public void makeBeemanStep(double t, double previousAcceleration){
        position = position + (speed * t) + ((2.0/3) * acceleration * Math.pow(t,2)) - ((1.0/6) * previousAcceleration * Math.pow(t,2));
        speed = (speed - ((k*position*t)/(3*mass)) + ((5.0/6)*acceleration*t) - ((1.0/6)*previousAcceleration*t)) / (1 + ((gamma*t) / (3*mass)));
        updateAcceleration();
    }

    private void updateAcceleration(){
        acceleration = (-k * position - gamma * speed) / mass;
    }

}
