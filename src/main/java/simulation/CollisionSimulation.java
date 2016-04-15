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
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Molecular dynamic simulation of Hard Spheres
 */
public class CollisionSimulation {

    private ParticleSystem particleSystem;
    private PrintWriter writer;

    private static final double DEFAULT_EPSILON = 0.001;

    public CollisionSimulation(int squareCount, double radius,  double l, long n, String fileName) throws IOException {
        try {
            Files.deleteIfExists(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Ver el radio de las particulas (No el radio de interacción). Hay que modificar para que sea el radio
        particleSystem = new ParticleSystem(squareCount, radius, l, n, new ArrayList<Wall>());
        Logger.getAnonymousLogger().info("Construcción completa");
        writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
    }

    // Simulate collision of hard spheres
    public void simulate(double time, double frameRate){
        int count = 0;
        particleSystem.writeFrameWithDirection(writer, count++);
        double timeToNextCollision;
        double accumulatedTime = 0;
        while(time>0){
            if(particleSystem.getDifferenceBetweenPromTime() < DEFAULT_EPSILON){
                timeToNextCollision = particleSystem.timeToNextCollisionWithHeuristic();
            }else {
                timeToNextCollision = particleSystem.timeToNextColision();
            }
            accumulatedTime += timeToNextCollision;
            if(accumulatedTime < frameRate) {
                for (Particle particle : particleSystem.getParticles()) {
                    particle.move(timeToNextCollision);
                }
                if(particleSystem.colParticle2 == null){
                    particleSystem.collide(particleSystem.colParticle1,particleSystem.borderDirection);
                }else{
                    particleSystem.collide(particleSystem.colParticle1,particleSystem.colParticle2);
                }
            }else{
                particleSystem.moveSystem(frameRate - (accumulatedTime - timeToNextCollision));
                particleSystem.writeFrameWithDirection(writer, count++);
                time -= frameRate;
                System.out.printf("Faltan %.2g segundos\n", time);
                double j;
                for(j = 2*frameRate; j<accumulatedTime && time>0; j+= frameRate) {
                    time -= frameRate;
                    System.out.printf("Faltan %.2g segundos\n", time);
                    particleSystem.moveSystem(frameRate);
                    particleSystem.writeFrameWithDirection(writer, count++);
                }
                particleSystem.moveSystem(accumulatedTime - (j - frameRate));
                accumulatedTime = 0;
            }
        }
        writer.flush();
        writer.close();
    }
}
