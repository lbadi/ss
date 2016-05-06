package model;

import util.PlainWritable;
import util.RandomUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Particle implements PlainWritable {

    private static final String separator = " ";
    public static final double DEFAULT_SPEED = 0.1;

    private double x;
    private double y;
    private double radius;
    private double color;

    //Speed without direction (mod)
    private double speed;
    //Direction of the particle
    private double angle;
    //Mass of the particle
    private double mass;

    private Vector acceleration = new Vector(0,0);

    private Set<Particle> neighbours = new HashSet<>();

    private static AtomicLong counter = new AtomicLong();
    private long id;

    private double angularMoment;

    private boolean markToBeRemove = false;

    @Override
    public PlainWritable readObject(String plainObject) {
        Scanner scanner = new Scanner(plainObject);
        setX(Double.valueOf(scanner.next()));
        setY(Double.valueOf(scanner.next()));
        if(scanner.hasNext()) {
//            setSpeedX(Double.valueOf(scanner.next()));
//            setSpeedY(Double.valueOf(scanner.next()));
        }
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
        return Math.cos(getAngle()) * getSpeed();
    }


    public double getSpeedY() {
        return Math.sin(getAngle()) * getSpeed();
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Particle() {
        id = counter.incrementAndGet();
        this.radius = Brownian.BROWNIAN_R1;
        this.mass = Brownian.BROWNIAN_M1;
        setSpeed(RandomUtils.between(Brownian.BROWNIAN_V_MIN, Brownian.BROWNIAN_V_MAX), RandomUtils.between(Brownian.BROWNIAN_V_MIN, Brownian.BROWNIAN_V_MAX));
    }

    public Particle(double x, double y) {
        this();
        setX(x);
        setY(y);
    }

    public Particle(double x, double y, double radius) {
        this();
        setX(x);
        setY(y);
        setRadius(radius);
    }
    public Particle(double mass){
        this();
        this.setMass(mass);
    }

    public Particle(double radius, double color, double angle, double speed){
        id = counter.incrementAndGet();
        this.radius = radius;
        this.color = color;
        this.angle = angle;
        this.speed = speed;
    }

    public long getId() {
        return id;
    }

//    @Override
//    public String toString() {
//        return "Particle{" +
//                "x=" + x +
//                ", y=" + y +
//                ", speedX=" + speedX +
//                ", speedY=" + speedY +
//                '}';
//    }

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

    public void addNeighbour(Particle particle){
        neighbours.add(particle);
        particle.neighbours.add(this);
    }

    public Set<Particle> getNeighbours() {
        return neighbours;
    }

    public boolean isNeighbour(Particle particle,Double interactionRadius, double l){
        if( distanceTo(particle, l) < interactionRadius ){
            return true;
        }
        return false;
    }

    public double distanceTo(Particle particle, double l){
        double distanceX  = Math.abs(particle.getX() - getX());
        double distanceY = Math.abs(particle.getY() - getY());
        if(l - distanceX < distanceX){
            distanceX = l-distanceX;
        }
        if(l - distanceY < distanceY){
            distanceY = l-distanceY;
        }
        double distance = Math.sqrt(Math.pow(distanceX,2) + Math.pow(distanceY,2)) - particle.getRadius() - getRadius();
        return distance;
    }
    public double distanceTo(Particle particle){
        double distanceX  = Math.abs(particle.getX() - getX());
        double distanceY = Math.abs(particle.getY() - getY());
        double distance = Math.sqrt(Math.pow(distanceX,2) + Math.pow(distanceY,2)) - particle.getRadius() - getRadius();
        return distance;
    }

    public double distanceToCenterOf(Particle particle){
        double distanceX  = Math.abs(particle.getX() - getX());
        double distanceY = Math.abs(particle.getY() - getY());
        double distance = Math.sqrt(Math.pow(distanceX,2) + Math.pow(distanceY,2));
        return distance;
    }

    //Todo ARREGLAR POR QUE ESTA CALCULANDO MAL LA TANGENTE(Por ahi el angulo lo hace bien pero siempre en el mismo sentido)
    public double tangencialWith(Particle particle){
        return (angleWith(particle) + Math.PI/2) % (Math.PI * 2);
    }

    public double angleWith(Particle particle){
        double angle = Math.atan2(particle.getY() - this.getY(), particle.getX() - this.getX());
        if(angle<0){
            angle += 2 * Math.PI;
        }
        return angle;
    }

    public void inverseDirection(){
        setAngle((getAngle() + Math.PI) % (Math.PI * 2));
    }

    public double calculatePromAngle(){
//        //⟨sin(θ(t))⟩r
//        double sinAngle = Math.sin(angle);
//        //⟨cos(θ(t))⟩r
//        double cosAngle = Math.cos(angle);
//        int i = 1;
//        for(Particle particle : neighbours){
//            sinAngle += Math.sin(particle.getAngle());
//            cosAngle += Math.cos(particle.getAngle());
//            i++;
//        }
//        double promSin = sinAngle/i;
//        double promCos = cosAngle/i;
        //arctg[⟨sin(θ(t))⟩r/⟨cos(θ(t))⟩r]
//        if(angle > Math.PI/2 || angle < Math.PI/2){
//            return Math.atan(promSin/promCos) + Math.PI;
//        }
//        if(promSin/promCos)
//            return Math.atan(promSin/promCos) < 0 ? Math.atan(promSin/promCos) + Math.PI : Math.atan(promSin/promCos) ;

        double sumAngle = angle;
        for(Particle particle : neighbours){
            double avg = (sumAngle + particle.getAngle())/2;
            if(Math.abs(sumAngle - particle.getAngle()) > Math.PI){
                avg += Math.PI;
                avg = avg % (2 * Math.PI);
            }
            sumAngle = avg;
        }
        return sumAngle;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean overlap(Particle particle) {
        return !(Math.pow(particle.getX() - getX(),2) + Math.pow(particle.getY() - getY(),2) > Math.pow(particle.getRadius() + getRadius(),2));
    }

    public void move(double t){
        setX(getX() + getSpeedX() * t);
        setY(getY() + getSpeedY() * t);
    }


    public void setSpeed(double vx, double vy){
        setSpeed(Math.sqrt(Math.pow(vx,2) + Math.pow(vy,2)));
        setAngle(Math.atan2(vy,vx));
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAngularMoment(Particle particle){
        double distanceToPoint = distanceTo(particle);
        double angularMoment = getMass() * getTangencialSpeedWith(particle) * distanceToPoint;
        return angularMoment;
    }


    public void setAngularMoment(double angularMoment) {
        this.angularMoment = angularMoment;
    }

    public void markToBeRemove() {
        markToBeRemove = true;
    }

    public boolean isMarkToBeRemove(){
        return markToBeRemove;
    }

    public void makeEulerStep(double t, double acceleration, double accelerationAngle){
        double speedX = getSpeedX();
        double speedY  = getSpeedY();
        x = x + t * speedX + Math.pow(t,2) *  (acceleration * Math.cos(accelerationAngle)) / 2;
        y = y + t * speedY + Math.pow(t,2) *  (acceleration * Math.sin(accelerationAngle)) / 2;
        speedX =  speedX + t * ((acceleration * Math.cos(accelerationAngle)));
        speedY = speedY + t * ((acceleration * Math.sin(accelerationAngle)));
        speed = Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2));
        angle = Math.atan2(speedY, speedX);
        this.acceleration.setModule(acceleration);
        this.acceleration.setAngle(accelerationAngle);
    }

    public void makeBeemanStep(double t, Vector acceleration, Particle sun){
        Vector previousAcceleration = this.acceleration;
        this.acceleration = acceleration;
        double speedX = getSpeedX();
        double speedY  = getSpeedY();
        x = x + speedX  * t + ((2.0/3) * acceleration.getModule() * Math.cos(acceleration.getAngle()) * Math.pow(t,2)) - ((1.0/6) * previousAcceleration.getModule() * Math.cos(previousAcceleration.getAngle()) * Math.pow(t,2));
        y = y + speedY  * t + ((2.0/3) * acceleration.getModule() * Math.sin(acceleration.getAngle()) * Math.pow(t,2)) - ((1.0/6) * previousAcceleration.getModule() * Math.sin(previousAcceleration.getAngle()) * Math.pow(t,2));
        Vector nextAcceleration = new Vector(getSolarGravityForce(sun), angleWith(sun));
        speedX = speedX + (1.0/3) * nextAcceleration.getModule() * Math.cos(nextAcceleration.getAngle()) * t + (5.0/6) * acceleration.getModule() * Math.cos(acceleration.getAngle()) * t - (1.0/6) * previousAcceleration.getModule() * Math.cos(previousAcceleration.getAngle()) * t;
        speedY = speedY + (1.0/3) * nextAcceleration.getModule() * Math.sin(nextAcceleration.getAngle()) * t + (5.0/6) * acceleration.getModule() * Math.sin(acceleration.getAngle()) * t - (1.0/6) * previousAcceleration.getModule() * Math.sin(previousAcceleration.getAngle()) * t;
        setSpeed(Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2)));
        setAngle(Math.atan2(speedY, speedX));
    }

    private double getSolarGravityForce(Particle sun){
        return (SolarSystem.getConstGravity() * SolarSystem.getSunMass() * mass) / distanceToCenterOf(sun);
    }

    public double getTangencialSpeedWith(Particle particle){
        double tangencialAngle = tangencialWith(particle);
        double difAngle = Math.abs(tangencialAngle - getAngle());
        return Math.cos(difAngle) * getSpeed();
    }

    public double getNormalSpeedWith(Particle particle){
        double tangencialSpeed = getTangencialSpeedWith(particle);
        double normalSpeed = Math.sqrt(Math.pow(getSpeed(),2) - Math.pow(tangencialSpeed,2));
        return normalSpeed;
    }

    public double getPosition(){
        return Math.sqrt(Math.pow(getX(),2) + Math.pow(getY(),2));
    }

    public double getOverlap(Particle particle){
        return this.getRadius() + particle.getRadius() - Math.abs(getPosition() - particle.getPosition());
    }


    public double getNormalVersorWith(Particle particle){
        double xNormal = (particle.getX() - getX()) / Math.abs(getPosition() - particle.getPosition());
        double yNormal = (particle.getY() - getY()) / Math.abs(getPosition() - particle.getPosition());
        return Math.sqrt(Math.pow(xNormal,2) + Math.pow(yNormal,2));
    }


}
