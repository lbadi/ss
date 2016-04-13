package simulation;

import model.Particle;
import model.ParticleSystem;
import model.Wall;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Molecular dynamic simulation of Hard Spheres
 */
public class CollisionSimulation {

    ParticleSystem particleSystem;
    PrintWriter writer;

    public CollisionSimulation(int squareCount, double radius,  double l, long n, String fileName) throws IOException {
        try {
            Files.deleteIfExists(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Ver el radio de las particulas (No el radio de interacción). Hay que modificar para que sea el radio
        List<Wall> walls = new ArrayList<>();
        particleSystem = new ParticleSystem(squareCount, radius, l, n, new ArrayList<Wall>());
        writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));

    }
    // Simulate collision of hard spheres
    public void simulate(double time, double frameRate){
        particleSystem.writeFrameWithDirection(writer, 1, 1);
        double t = 0;
        while(time>0){
            t += particleSystem.timeToNextColision();
            if(t < frameRate) {
                for (Particle particle : particleSystem.getParticles()) {
                    particle.move(t);
                }
                if(particleSystem.colParticle2 == null){
                    particleSystem.collide(particleSystem.colParticle1,particleSystem.borderDirection);
                }else{
                    particleSystem.collide(particleSystem.colParticle1,particleSystem.colParticle2);
                }
            }else{
                double j;
                for(j = frameRate; j<t && time>0; j+= frameRate) {
                    particleSystem.moveSystem(frameRate);
                    time=time-frameRate;
                    particleSystem.writeFrameWithDirection(writer, 1, 1);
                    System.out.println(time);
                }
                System.out.println("LLEGUE");
                particleSystem.moveSystem(t-j);
                t = 0;
            }
        }
        writer.flush();
        writer.close();
    }
}
