package simulation;

import model.Particle;
import model.ParticleSystem;
import util.ObjectPlainWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Off-Latice Self-propelled agents
 */
public class OffLatice {


    private ParticleSystem particleSystem;
    private double noise;

    /**
     * @param squareCount how many squares per grid
     * @param interactionRadius interaction radius of a particle
     * @param periodic true: periodic grid.
     * @param l size of the grid
     * @param n particle count
     * l/n : density
     */
    public OffLatice(int squareCount, double interactionRadius, boolean periodic,  double l, long n, double noise){
        particleSystem  = new ParticleSystem(periodic,squareCount,interactionRadius, l, n);
        this.noise = noise;
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
                double newY = particle.getX() + particle.getSpeed() * Math.sin(particle.getAngle());
                if(!particleSystem.isInBorder(newX,newY)){
                    //TODO ver si es periodico que aparezca del otro lado
                    //Creo la nueva particula y la agrego al conjunto de nuevas particulas
                    Particle newParticle = new Particle(particle.getRadius(), particle.getColor(), newAngle, particle.getSpeed());
                    newParticle.setX(newX);
                    newParticle.setY(newY);
                    particlesXn2.add(newParticle);
                }else {
                    particlesXn2.add(particle);
                }

            }

            //Escribir el sistema de particulas en el instante i.
            //Es necesario ir escribiendo el archivo a medida que calculamos para ahorrar memoria y poder reutilizar
            // el sistema de particulas. Sino hay que guardarse N sistemas de particulas
            try {
                particleSystem.writeVisualization("src/test/resources/output/offLatice.txt", 1, i);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Crear el nuevo sistema en el instante i+1
            particleSystem.refreshSystem(particlesXn2);
            System.out.println(framesQuantity);

        }
    }

}
