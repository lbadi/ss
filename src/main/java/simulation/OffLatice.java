package simulation;

import model.Particle;
import model.ParticleSystem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Off-Latice Self-propelled agents
 */
public class OffLatice {


    private ParticleSystem particleSystem;
    private double noise;
    PrintWriter writer;

    /**
     * @param squareCount how many squares per grid
     * @param interactionRadius interaction radius of a particle
     * @param periodic true: periodic grid.
     * @param l size of the grid
     * @param n particle count
     * l/n : density
     */
    public OffLatice(int squareCount, double interactionRadius, boolean periodic,  double l, long n, double noise, String fileName) throws IOException {
        try {
            Files.deleteIfExists(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        particleSystem  = new ParticleSystem(periodic,squareCount,interactionRadius, l, n);
        this.noise = noise;
        writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
    }

    /**
     * Simulate self-propelled agents
     * @param framesQuantity simulation max time
     */
    public void simulate(long framesQuantity){

        for(int i = 0; i< framesQuantity; i++){
            particleSystem.populateNeighbourhood();

            List<Particle> particlesXn = particleSystem.getParticles();
            List<Particle> particlesXn2 = new ArrayList<>();
            for(Particle particle : particlesXn){
                //Calcular posición y angulo nuevo de la particula
                double deltaAngle = Math.random() * noise - noise/2;
                double newAngle = particle.calculatePromAngle() + deltaAngle;
                double newX = particle.getX() + particle.getSpeed() * Math.cos(particle.getAngle());
                double newY = particle.getY() + particle.getSpeed() * Math.sin(particle.getAngle());
                Particle newParticle = new Particle(particle.getRadius(), particle.getColor(), newAngle, particle.getSpeed());
                if(particleSystem.isInBorder(newX,newY)){
                    //Creo la nueva particula y la agrego al conjunto de nuevas particulas
                    //x>= l || x< 0 || y>=l || y<0
                    if(newX >= particleSystem.getL()){
                        newX -= particleSystem.getL();
                    }
                    else if(newX < 0){
                        newX += particleSystem.getL();
                    }
                    if(newY >= particleSystem.getL()){
                        newY -= particleSystem.getL();
                    }
                    else if(newY < 0){
                        newY += particleSystem.getL();
                    }
                }
                newParticle.setX(newX);
                newParticle.setY(newY);
                particlesXn2.add(newParticle);

            }

            //Escribir el sistema de particulas en el instante i.
            //Es necesario ir escribiendo el archivo a medida que calculamos para ahorrar memoria y poder reutilizar
            // el sistema de particulas. Sino hay que guardarse N sistemas de particulas
            particleSystem.writeFrameWithDirection(writer, 1, i);

            //Crear el nuevo sistema en el instante i+1
            particleSystem.refreshSystem(particlesXn2);

            System.out.println(i);
        }
        writer.flush();
        writer.close();
    }

    public ParticleSystem getParticleSystem() {
        return particleSystem;
    }

    public double getNoise() {
        return noise;
    }
}
