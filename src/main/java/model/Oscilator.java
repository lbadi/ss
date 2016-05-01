package model;

public class Oscilator {

    private double mass;
    private double k;
    private double gamma;
    private double acceleration;
    private double position;
    private double speed;

    private static final double[] factorial = {1,1,2,6,24,120};
    private static final double[] alpha = {3.0/20,251.0/360,1,11.0/18,1.0/6,1.0/60};
    private static final int VARIABLE_LENGTH = 6;
    private double[] r = new double[VARIABLE_LENGTH];

    public Oscilator(double mass, double k, double gamma, double initialPosition){
        this.mass = mass;
        this.k = k;
        this.gamma = gamma;
        this.position = initialPosition;
        this.speed = - gamma / (mass/2);
        this.acceleration = (-k*position - gamma * speed) / mass;
        initR();
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
        acceleration = getAcceleration(position,speed,gamma,k,mass);
    }

    private double getAcceleration(double position, double speed, double gamma, double k, double mass) {
        return acceleration = (-k * position - gamma * speed) / mass;
    }

    private void initR() {
        r[0] = position;
        r[1] = speed;
        r[2] = (- k * r[0] - gamma * r[1]) / mass;
        r[3] = (- k * r[1] - gamma * r[2]) / mass;
        r[4] = (- k * r[2] - gamma * r[3]) / mass;
        r[5] = (- k * r[3] - gamma * r[4]) / mass;
    }

    public void makeGearStep(double t) {
        /**
         * Predict
         */
        double[] predictR = new double[VARIABLE_LENGTH];
        predictR[5] = r[5];
        predictR[4] = r[4] + (r[5] * t);
        predictR[3] = r[3] + (r[4] * t) + (r[5] * Math.pow(t,2) / factorial[2]);
        predictR[2] = r[2] + (r[3] * t) + (r[4] * Math.pow(t,2) / factorial[2]) + (r[5] * Math.pow(t,3) / factorial[3]);
        predictR[1] = r[1] + (r[2] * t) + (r[3] * Math.pow(t,2) / factorial[2]) + (r[4] * Math.pow(t,3) / factorial[3]) + (r[5] * Math.pow(t,4) / factorial[4]);
        predictR[0] = r[0] + (r[1] * t) + (r[2] * Math.pow(t,2) / factorial[2]) + (r[3] * Math.pow(t,3) / factorial[3]) + (r[4] * Math.pow(t,4) / factorial[4]) + (r[5] * Math.pow(t,5) / factorial[5]);

        /**
         * Evaluate
         */
        double deltaA = getAcceleration(r[0], r[1], gamma, k, mass) - predictR[2];
        double deltaR2 = (deltaA * Math.pow(t, 2)) / factorial[2];

        /**
         * Correct
         */
        for (int i = 0; i < VARIABLE_LENGTH; i++) {
            r[i] = predictR[i] + (alpha[i] * deltaR2 * factorial[i] / Math.pow(t,i));
        }
    }

    public double getR0() {
        return r[0];
    }

    /**
     * Obtiene el error comparándolo con la posición analítica.
     */
    public double getError(double position, double t) {
        return Math.pow(position - getAnalyticPosition(t),2);
    }

    public double getError(double t) {
        return getError(position, t);
    }

}
