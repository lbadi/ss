package model;

import util.PlainWritable;
import util.RandomUtils;

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

    private Set<Particle> neighbours = new HashSet<>();

    private static AtomicLong counter = new AtomicLong();
    private long id;

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

    private double distanceTo(Particle particle, double l){
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Particle particle = (Particle) o;

        if (Double.compare(particle.getId(), getId()) != 0) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getX());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getY());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getRadius());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
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

    public double getSpeed() {
        return speed;
    }

    public boolean overlap(Particle particle) {
        return !(Math.pow(particle.getX() - getX(),2) + Math.pow(particle.getY() - getY(),2) > Math.pow(particle.getRadius() + getRadius(),2));
    }

    public boolean overlap(Wall wall) {
        //Distance of a point(particle) o a line(wall)
        double distancePointToRect = Math.abs(wall.getA() * getX() + wall.getB() * getY() + wall.getC())
                / Math.sqrt(Math.pow(wall.getA(),2) + Math.pow(wall.getB(),2));
        return getRadius() >= distancePointToRect;
    }

    public void move(double t){
        setX(getX() + getSpeedX() * t);
        setY(getY() + getSpeedY() * t);
    }

    public double getMass() {
        return mass;
    }

    public void setSpeed(double vx, double vy){
        setSpeed(Math.sqrt(Math.pow(vx,2) + Math.pow(vy,2)));
        setAngle(Math.atan2(vy,vx));
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }
}
