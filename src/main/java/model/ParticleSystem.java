package model;

import util.PlainWritable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ParticleSystem implements PlainWritable {

    private long n;
    private long l;
    private List<Particle> particles = new ArrayList<>();

    /**
     * Estático:
     N      (Heading con el Nro. total de Partículas)
     L      (Longitud del lado del área de simulación)
     r1 c1  (radio y color de la partícula 1)
     r2 c2  (radio y color de la partícula 2)
     ....
     rN cN  (radio y color de la partícula N)
     * @param plainObject
     * @return
     */
    @Override
    public PlainWritable readObject(String plainObject) {
        Scanner scanner = new Scanner(plainObject);
        setN(Long.parseLong(scanner.next()));
        setL(Long.parseLong(scanner.next()));
        List<Particle> particles = new ArrayList<>();
        while (scanner.hasNext()) {
            double radius = Double.parseDouble(scanner.next());
            double color = Double.parseDouble(scanner.next());
            Particle particle = new Particle(radius, color);
            particles.add(particle);
        }
        setParticles(particles);
        return this;
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

    public ParticleSystem() {
    }

}
