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
    String resultFileName;

    private static FileWriter resultsWriter;
    public static final String CSV_FILENAME = "offLatice.csv";
    public static final String OUTPUT_PATH = "src/test/resources/output/";


    public SolarSystemTest(int id,int planetQuantity, double angularMomentum, int k,
                           double dt, int t, int squareCount,long l, String filename,String resultFileName){
        this.id = id;
        this.planetQuantity = planetQuantity;
        this.angularMomentum = angularMomentum;
        this.k = k;
        this.dt = dt;
        this.t = t;
        this.l = l;
        this.squareCount = squareCount;

        this.filename = filename;
        this.resultFileName = resultFileName;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][]{
                /**
                 * id, planetQuantity, angularMomentum, k, dt, t, squareCount, l, filename, energyFilename
                */

                /**Orbitan**/
//                {1, 100, 5000000000.0, 5, 0.1, 500,10000,2000, "src/test/resources/output/solar-100-5000000000.csv", "src/test/resources/output/solar-100-5000000000-energy.csv"},
//                {1, 100, 4000000000.0, 5, 0.1, 500,10000,2000, "src/test/resources/output/solar-100-4000000000.csv", "src/test/resources/output/solar-100-4000000000-energy.csv"},
//                {1, 100, 3000000000.0, 5, 0.1, 500,10000,2000, "src/test/resources/output/solar-100-3000000000.csv", "src/test/resources/output/solar-100-3000000000-energy.csv"},
//                {1, 100, 2000000000.0, 5, 0.1, 500,10000,2000, "src/test/resources/output/solar-100-2000000000.csv", "src/test/resources/output/solar-100-2000000000-energy.csv"},
//
//                {1, 1000, 8000000, 5, 0.1, 500,10000,2000, "src/test/resources/output/solar-1000-8000000.csv", "src/test/resources/output/solar-1000-8000000-energy.csv"},
//                {1, 1000, 25000000, 5, 0.1, 500,10000,2000, "src/test/resources/output/solar-1000-25000000.csv", "src/test/resources/output/solar-1000-25000000-energy.csv"},
//                {1, 1000, 15000000, 5, 0.1, 500,10000,2000, "src/test/resources/output/solar-1000-15000000.csv", "src/test/resources/output/solar-1000-15000000-energy.csv"},
//                {1, 1000, 20000000, 5, 0.1, 500,10000,2000, "src/test/resources/output/solar-1000-20000000.csv", "src/test/resources/output/solar-1000-20000000-energy.csv"},
//                {1, 1000, 30000000, 5, 0.1, 500,10000,2000, "src/test/resources/output/solar-1000-30000000.csv", "src/test/resources/output/solar-1000-30000000-energy.csv"},
//                {1, 1000, 40000000, 5, 0.1, 500,10000,2000, "src/test/resources/output/solar-1000-40000000.csv", "src/test/resources/output/solar-1000-40000000-energy.csv"},


//                {1, 10000, 700000, 5, 0.1, 500,10000,2000, "src/test/resources/output/solar-10000-70000.csv", "src/test/resources/output/solar-10000-70000-energy.csv"},
//                {1, 10000, 500000, 5, 0.1, 500,10000,2000, "src/test/resources/output/solar-10000-50000.csv", "src/test/resources/output/solar-10000-50000-energy.csv"},
//                {1, 15000, 300000, 2, 0.1, 500,10000,2000, "src/test/resources/output/solar-15000-30000.csv" , "src/test/resources/output/solar-15000-30000-ernergy.csv"},

                {1, 50000, 100000, 5, 0.1, 500,10000,2000, "src/test/resources/output/solar-50000-10000.csv", "src/test/resources/output/solar-50000-10000-energy.csv"},


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
            solarSystemSimulation = new SolarSystemSimulation(planetQuantity,angularMomentum, filename,resultFileName);
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
