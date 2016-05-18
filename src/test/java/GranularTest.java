import model.Particle;
import model.Wall;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import simulation.GranularSystemSimulation;
import util.RandomUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 */
@RunWith(value = Parameterized.class)
public class GranularTest {

    GranularSystemSimulation granularSystemSimulation;
    int id;
    int squareCount;
    double width;
    double height;
    double apperture;
    int grainsCount;
    String filename;
    String resultFilename;
    boolean open;

    public static final String CSV_FILENAME = "granular";
    public static final String CSV_RESULT_FILENAME = "energy";
    public static final String OUTPUT_PATH = "src/test/resources/output/";

    public GranularTest(int id, int squareCount, double width, double height, double apperture,int grainsCount, String filename, String resultFilename, boolean open) {
        this.id = id;
        this.squareCount = squareCount;
        this.width = width;
        this.height = height;
        this.apperture = apperture;
        this.grainsCount = grainsCount;
        this.filename = filename;
        this.resultFilename = resultFilename;
        this.open = open;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][]{
                /**
                 * id, squareCount, width, height, aperture, grainsCount, filename, resultFilename
                 */
//                {1, 100, 5, 20, 1, 1000, OUTPUT_PATH + CSV_FILENAME, OUTPUT_PATH + CSV_RESULT_FILENAME, false},

//                {2, 100, 5, 20, 1, 1000, OUTPUT_PATH + CSV_FILENAME, OUTPUT_PATH + CSV_RESULT_FILENAME, true},
                {99, 15, 2, 3, 1, 10000, OUTPUT_PATH + CSV_FILENAME, OUTPUT_PATH + CSV_RESULT_FILENAME, true},
//                {3, 100, 2, 20, 1, 10000, OUTPUT_PATH + CSV_FILENAME, OUTPUT_PATH + CSV_RESULT_FILENAME, true},
//                {4, 100, 2, 30, 1, 10000, OUTPUT_PATH + CSV_FILENAME, OUTPUT_PATH + CSV_RESULT_FILENAME, true},
//
//                {5, 100, 2, 10, 1, 10000, OUTPUT_PATH + CSV_FILENAME, OUTPUT_PATH +  CSV_RESULT_FILENAME, false},
//                {6, 100, 2, 20, 1, 10000, OUTPUT_PATH + CSV_FILENAME, OUTPUT_PATH + CSV_RESULT_FILENAME, false},
//                {7, 100, 2, 30, 1, 10000, OUTPUT_PATH + CSV_FILENAME, OUTPUT_PATH + CSV_RESULT_FILENAME, false},


        });
    }

    @Before
    public void init(){
        try {
            granularSystemSimulation = new GranularSystemSimulation(width,height, apperture,grainsCount, filename + id + ".csv",squareCount,resultFilename+ id + ".csv", open);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void particleNormalTest(){
        Particle particle1 = new Particle(0,0);
        particle1.setMass(10);
        particle1.setSpeed(0,1);
        particle1.setRadius(10 / 2);

        Particle particle2 = new Particle(0,10);
        particle2.setMass(10);
        particle2.setSpeed(0,-1);
        particle2.setRadius(10 / 2);

        double normal1 = particle1.getNormalAngleWith(particle2);
        double normal2 = particle2.getNormalAngleWith(particle1);
        System.out.println(normal2);

    }

    @Test
    public void particleTangencialTest(){
        Particle particle1 = new Particle(0,0);
        particle1.setMass(10);
        particle1.setSpeed(-1,2);
        particle1.setRadius(1);

        Particle particle2 = new Particle(0,1);
        particle2.setMass(10);
        particle2.setSpeed(2,0);
        particle2.setRadius(1);

        List<Wall> walls = granularSystemSimulation.getGranularSystem().getWalls();

        granularSystemSimulation.getGranularSystem().getTangencialForce(particle1,particle2);
        granularSystemSimulation.getGranularSystem().getTangencialForce(particle2,particle1);
        for(Wall wall : walls) {
            granularSystemSimulation.getGranularSystem().getTangencialForce(particle1, wall);
        }

    }

    @Test
    public void twoParticles(){
        Particle particle1 = new Particle(2,15);
        particle1.setMass(0.01);
        particle1.setSpeed(0,0);
        particle1.setRadius(0.25);

        Particle particle2 = new Particle(9,3);
        particle2.setMass(0.01);
        particle2.setSpeed(0,0);
        particle2.setRadius(0.25);

        Particle particle3 = new Particle(9.1,4.0);
        particle3.setMass(0.01);
        particle3.setSpeed(0,0);
        particle3.setRadius(0.25);

        List<Particle> particles = new ArrayList<>();
        particles.add(particle1);
        particles.add(particle2);
        particles.add(particle3);

        granularSystemSimulation.getGranularSystem().setParticles(particles);
//        granularSystemSimulation.getGranularSystem().se
        granularSystemSimulation.simulate(4000,0.0000005,3);
    }

    @Test
    public void construct(){
        granularSystemSimulation.simulate(400,0.00001,30);
    }

}
