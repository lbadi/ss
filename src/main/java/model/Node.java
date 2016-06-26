package model;

import java.util.List;

public class Node {

    List<Particle> particles;
    List<Node> neighbours;

    public void addParticle(Particle particle){
        particles.add(particle);
    }
}
