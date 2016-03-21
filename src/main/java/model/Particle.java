package model;

import util.PlainWritable;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Particle implements PlainWritable {

    private static final String separator = " ";

    private double x;
    private double y;
    private double speedX;
    private double speedY;
    private double radius;
    private double color;

    private Set<Particle> neighbours = new HashSet<>();

    private static AtomicLong counter = new AtomicLong();
    private long id;

    @Override
    public PlainWritable readObject(String plainObject) {
        Scanner scanner = new Scanner(plainObject);
        setX(Double.valueOf(scanner.next()));
        setY(Double.valueOf(scanner.next()));
        if(this.getX() > 100000) {
            System.out.println(this.getX());
            System.out.println(this.getId());

        }
        if(this.getY() > 100000) {
            System.out.println(this.getY());
            System.out.println(this.getId());

        }
        if(scanner.hasNext()) {
            setSpeedX(Double.valueOf(scanner.next()));
            setSpeedY(Double.valueOf(scanner.next()));
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

    /**
     * Formato de entrada del archivo estático.
     * @param radius
     * @param color
     */
    public Particle(double radius, double color) {
        id = counter.incrementAndGet();
        this.radius = radius;
        this.color = color;
    }

    public long getId() {
        return id;
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
        double distanceX  = particle.getX() - getX();
        double distanceY = particle.getY() - getY();
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

}
