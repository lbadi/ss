import model.Particle;
import model.ParticleSystem;
import util.ObjectPlainWriter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
//        Particle particle = new Particle(1,2,3,4,1,1);
//        ObjectPlainWriter plainWriter = new ObjectPlainWriter();
//        plainWriter.writeObject("src/main/resources/dynamic.txt",particle);
//        Particle clone = new Particle();
//        plainWriter.readObject("src/main/resources/dynamic.txt", clone);
//        System.out.println(clone);
//        Particle particleClone = new Particle(5,6,7,8,1,1);
//        List<Particle> particles = new ArrayList<>();
//        particles.add(particle);
//        particles.add(particleClone);
//        ParticleSystem system = new ParticleSystem(100,100,particles);
//        plainWriter.writeObject("src/main/resources/static.txt", system);
        ObjectPlainWriter objectPlainWriter = new ObjectPlainWriter();

        String filename = "src/main/resources/Static100.txt";
        objectPlainWriter = new ObjectPlainWriter();
        ParticleSystem particleSystem = new ParticleSystem(true, 1 , 10);
        objectPlainWriter.readObject(filename, particleSystem);
        String filenameDynamic = "src/main/resources/Dynamic100.txt";
        objectPlainWriter.readParticleSystem(filenameDynamic, particleSystem);
        particleSystem.populateNeighbourhood();
        try {
            particleSystem.writeVisualization("visual.txt",1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void getNeighbours(String staticFilename, String dynamicFilename, int m, int interactionRadius){

    }

}