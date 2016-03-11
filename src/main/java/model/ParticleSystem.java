package model;

import util.PlainWritable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonelbadi on 11/3/16.
 */
public class ParticleSystem implements PlainWritable{

    long n;
    long l;

    List<Particle> particles = new ArrayList<>();


    @Override
    public PlainWritable readObject(String plainObject) {
        return null;
    }

    @Override
    public String writeObject() {
        StringBuilder sb = new StringBuilder();
        sb.append(getN())
                .append("\n")
                .append(getL());
        particles.stream().forEach(particle -> sb.append("\n").append(particle.getRadius() + " " + particle.getColor()));
        return sb.toString();
    }

    public long getN() {
        return n;
    }

    public void setN(long n) {
        this.n = n;
    }

    public long getL() {
        return l;
    }

    public void setL(long l) {
        this.l = l;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void setParticles(List<Particle> particles) {
        this.particles = particles;
    }

    public ParticleSystem(long n, long l, List<Particle> particles) {
        this.n = n;
        this.particles = particles;
        this.l = l;
    }

    public void addParticle(Particle particle){
        particles.add(particle);
    }

    public ParticleSystem(){}
}
