import model.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import simulation.CollisionSimulation;
import util.PrintFormatter;
import util.RandomUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Test the collision simulation
 */

@RunWith(value = Parameterized.class)
public class CollisionTest {

    private CollisionSimulation collision;
    private long id;
    private int squareCount;
    private double radius;
    private double l;
    private long n;
    private long frames;
    private double frameRate;
    private double maxSpeed;
    private double minSpeed;
    private String filename;
    private DecimalFormat df = new PrintFormatter().getDf();

    private static FileWriter resultsWriter;
    public static final String CSV_FILENAME = "dmer.csv";
    public static final String OUTPUT_PATH = "src/test/resources/output/";

    private static AtomicLong counter = new AtomicLong();

    public CollisionTest(long id, int squareCount, double radius,
                         double l, long n, long frames, double frameRate, double maxSpeed, double minSpeed){
        this.id = id;
        this.squareCount = squareCount;
        this.radius = radius;
        this.l = l;
        this.n = n;
        this.frames = frames;
        this.frameRate = frameRate;
        this.maxSpeed = maxSpeed;
        this.minSpeed = minSpeed;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][]{
                /**
                 * int id, int squareCount, double radius, double l, long n, long frames, double frameRate
                */
//                {counter.incrementAndGet(), 1, Brownian.BROWNIAN_R1, Brownian.BROWNIAN_L, 10, 20, 0.1,Brownian.BROWNIAN_V_MAX,Brownian.BROWNIAN_V_MIN},
//                {counter.incrementAndGet(), 1, Brownian.BROWNIAN_R1, Brownian.BROWNIAN_L, 10, 20, 0.1,Brownian.BROWNIAN_V_MAX,Brownian.BROWNIAN_V_MIN},
//                {counter.incrementAndGet(), 1, Brownian.BROWNIAN_R1, Brownian.BROWNIAN_L, 10, 20, 0.1,Brownian.BROWNIAN_V_MAX,Brownian.BROWNIAN_V_MIN},
//                {counter.incrementAndGet(), 1, Brownian.BROWNIAN_R1, Brownian.BROWNIAN_L, 100, 20, 0.1,Brownian.BROWNIAN_V_MAX,Brownian.BROWNIAN_V_MIN},
//                {counter.incrementAndGet(), 1, Brownian.BROWNIAN_R1, Brownian.BROWNIAN_L, 100, 20, 0.1,Brownian.BROWNIAN_V_MAX,Brownian.BROWNIAN_V_MIN},
//                {counter.incrementAndGet(), 1, Brownian.BROWNIAN_R1, Brownian.BROWNIAN_L, 100, 20, 0.1,Brownian.BROWNIAN_V_MAX,Brownian.BROWNIAN_V_MIN},
//                {counter.incrementAndGet(), 1, Brownian.BROWNIAN_R1, Brownian.BROWNIAN_L, 250, 20, 0.1,Brownian.BROWNIAN_V_MAX,Brownian.BROWNIAN_V_MIN},
//                {counter.incrementAndGet(), 1, Brownian.BROWNIAN_R1, Brownian.BROWNIAN_L, 250, 20, 0.1,Brownian.BROWNIAN_V_MAX,Brownian.BROWNIAN_V_MIN},
//                {counter.incrementAndGet(), 1, Brownian.BROWNIAN_R1, Brownian.BROWNIAN_L, 250, 20, 0.1,Brownian.BROWNIAN_V_MAX,Brownian.BROWNIAN_V_MIN}
                {counter.incrementAndGet(), 1, Brownian.BROWNIAN_R1, Brownian.BROWNIAN_L, 500, 20, 0.1,Brownian.BROWNIAN_V_MAX,Brownian.BROWNIAN_V_MIN},
                {counter.incrementAndGet(), 1, Brownian.BROWNIAN_R1, Brownian.BROWNIAN_L, 500, 20, 0.1,5*Brownian.BROWNIAN_V_MAX,5*Brownian.BROWNIAN_V_MIN},
                {counter.incrementAndGet(), 1, Brownian.BROWNIAN_R1, Brownian.BROWNIAN_L, 500, 20, 0.1,10*Brownian.BROWNIAN_V_MAX,10*Brownian.BROWNIAN_V_MIN}
        });
    }

    @BeforeClass
    public static void setUpCSV() throws IOException {
        resultsWriter = new FileWriter(OUTPUT_PATH + CSV_FILENAME);
        resultsWriter.append("N");
        resultsWriter.append(',');
        resultsWriter.append("T");
        resultsWriter.append(',');
        resultsWriter.append("F");
        resultsWriter.append(',');
        resultsWriter.append("Vx");
        resultsWriter.append(',');
        resultsWriter.append("Vy");
        resultsWriter.append('\n');
    }

    //int squareCount, double radius,  double l, long n, String fileName
    @Before
    public void init() throws IOException {
        String fileName = String.format("src/test/resources/output/dmer-%d-%d-%d-%.2g.txt", this.id, this.n, this.frames, this.frameRate);
        collision = new CollisionSimulation(squareCount,radius, l, n, fileName, maxSpeed, minSpeed);
    }

    @Test
    public void testOverlap(){
        Particle particle = new Particle(5.6341, 7.0581, 1);
        Particle newParticle = new Particle(4.5741, 6.0848, 1);
        boolean overlap = particle.overlap(newParticle);
        Assert.assertEquals(overlap,true);
    }

    @Test
    public void frontalCollisionTest(){
        ParticleSystem particleSystem = new ParticleSystem(squareCount, radius, l, n, new ArrayList<Wall>(),maxSpeed,minSpeed);
        Particle particle = new Particle(2,2,1);
        Particle newParticle = new Particle(3,3,1);

        particle.setAngle(Math.PI/2);
        particle.setSpeed(1);

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
        collision.simulate(frames,frameRate);
    }

    @Test
    public void randomGeneratorTest() {
        for(int i = 0; i < 10; i++) {
            double r = RandomUtils.between(Brownian.BROWNIAN_V_MIN, Brownian.BROWNIAN_V_MAX);
            Assert.assertTrue(r > Brownian.BROWNIAN_V_MIN & r < Brownian.BROWNIAN_V_MAX);
        }
    }

    @After
    public void writeResults() throws IOException {
        /**
         * Agregado de resultados de cada sistema al CSV general.
         */
        for(ResultContainer result : collision.getResults()){
            resultsWriter.append(df.format(collision.getParticleSystem().getN()));
            resultsWriter.append(',');
            resultsWriter.append(df.format(result.getT()));
            resultsWriter.append(',');
            resultsWriter.append(df.format(result.getPromFrecuency()));
            resultsWriter.append(',');
            resultsWriter.append(df.format(result.getPromSpeedX()));
            resultsWriter.append(',');
            resultsWriter.append(df.format(result.getPromSpeedY()));
            resultsWriter.append('\n');
        }
    }

    @AfterClass
    public static void closeCSV() throws IOException {
        resultsWriter.flush();
        resultsWriter.close();
    }

}
