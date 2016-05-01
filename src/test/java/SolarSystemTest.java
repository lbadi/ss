import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import simulation.OffLatice;
import simulation.SolarSystemSimulation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 */
@RunWith(value = Parameterized.class)
public class SolarSystemTest {

    SolarSystemSimulation solarSystemSimulation;
    int id;
    int squareCount;
    int planetQuantity;
    double angularMomentum;
    int k;
    double dt;
    int t;
    long l;
    String filename;

    private static FileWriter resultsWriter;
    public static final String CSV_FILENAME = "offLatice.csv";
    public static final String OUTPUT_PATH = "src/test/resources/output/";


    public SolarSystemTest(int id,int planetQuantity, double angularMomentum, int k,
                           double dt, int t, int squareCount,long l, String filename){
        this.id = id;
        this.planetQuantity = planetQuantity;
        this.angularMomentum = angularMomentum;
        this.k = k;
        this.dt = dt;
        this.t = t;
        this.l = l;
        this.squareCount = squareCount;

        this.filename = filename;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][]{
                /**
                 * id, planetQuantity, angularMomentum, k, dt, t, squareCount, l, filename
                */

              //  {1, 10000, 100000, 1, 0.1, 10,10000,2*(long)Math.pow(11,4), "src/test/resources/output/solar.csv"},
                {1, 10, 100000, 1, 0.1, 10,1,2000, "src/test/resources/output/solar.csv"},

        });
    }
    @BeforeClass
    public static void setUpCSV() throws IOException {
        resultsWriter = new FileWriter(OUTPUT_PATH + CSV_FILENAME);
        resultsWriter.append("N");
        resultsWriter.append(',');
        resultsWriter.append("Orden");
        resultsWriter.append(',');
        resultsWriter.append("Noise");
        resultsWriter.append('\n');
    }

    @Before
    public void init(){
        try {
            solarSystemSimulation = new SolarSystemSimulation(planetQuantity,angularMomentum, filename,l);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void construct(){
        solarSystemSimulation.simulate(k,dt,t);
    }

    @After
    public void writeResults() throws IOException {
        /**
         * Agregado de resultados de cada sistema al CSV general.
         */
//        resultsWriter.append(Long.toString(offLatice.getParticleSystem().getN()));
//        resultsWriter.append(',');
//        resultsWriter.append(Double.toString(offLatice.getParticleSystem().getOrder()));
//        resultsWriter.append(',');
//        resultsWriter.append(Double.toString(offLatice.getNoise()));
//        resultsWriter.append('\n');
    }

    @AfterClass
    public static void closeCSV() throws IOException {
        resultsWriter.flush();
        resultsWriter.close();
    }
}
