import model.Particle;
import model.ParticleSystem;
import model.Wall;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import simulation.CollisionSimulation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test the collision simulation
 */

@RunWith(value = Parameterized.class)
public class CollisionTest {

    CollisionSimulation collision;
    int id;
    int squareCount;
    double radius;
    double l;
    long n;
    long frames;
    String filename;

    private static FileWriter resultsWriter;
    public static final String CSV_FILENAME = "offLatice.csv";
    public static final String OUTPUT_PATH = "src/test/resources/output/";


    public CollisionTest(int id, int squareCount, double radius,
                         double l, long n, long frames, String filename){
        this.id = id;
        this.squareCount = squareCount;
        this.radius = radius;
        this.l = l;
        this.n = n;
        this.frames = frames;
        this.filename = filename;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][]{
                /**
                 * int id, int squareCount, double radius, double l, long n, long frames, String filename
                */
                {1, 1, 0.2, 10,10, 100, "src/test/resources/output/offLatice10-1000-0.3.txt"},
        });
    }

    //int squareCount, double radius,  double l, long n, String fileName
    @Before
    public void init(){
        try {
            collision = new CollisionSimulation(squareCount,radius, l, n , filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOverlap(){
        Particle particle = new Particle(1);
        Particle newParticle = new Particle(1);

        particle.setX(5.6341);
        particle.setY(7.0581);

        newParticle.setX(4.5741);
        newParticle.setY(6.0848);
        boolean overlap = particle.overlap(newParticle);
        Assert.assertEquals(overlap,true);
    }

    @Test
    public void frontalCollisionTest(){
        ParticleSystem particleSystem = new ParticleSystem(squareCount, radius, l, n, new ArrayList<Wall>());
        Particle particle = new Particle(1);
        Particle newParticle = new Particle(1);

        particle.setX(2);
        particle.setY(2);
        particle.setAngle(Math.PI/2);
        particle.setSpeed(1);

        newParticle.setX(3);
        newParticle.setY(3);
        newParticle.setAngle(Math.PI);
        newParticle.setSpeed(1);

        List<Particle> particleList = new ArrayList<>();
        particleList.add(newParticle);
        particleList.add(particle);

        particleSystem.setParticles(particleList);
        double t = particleSystem.timeToNextColision();
        System.out.println(t);

    }

    @Test
    public void simulate(){
        collision.simulate(frames,1);
    }
}
