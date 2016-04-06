import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import simulation.OffLatice;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 */
@RunWith(value = Parameterized.class)
public class OffLaticeTest {

    OffLatice offLatice;
    int id;
    int squareCount;
    double interactionRadius;
    boolean periodic;
    double l;
    long n;
    double noise;
    long frames;
    String filename;

    private static FileWriter resultsWriter;
    public static final String CSV_FILENAME = "offLatice.csv";
    public static final String OUTPUT_PATH = "src/test/resources/output/";


    public OffLaticeTest(int id, int squareCount, double interactionRadius, boolean periodic,
                         double l, long n, double noise, long frames, String filename){
        this.id = id;
        this.squareCount = squareCount;
        this.interactionRadius = interactionRadius;
        this .periodic = periodic;
        this.l = l;
        this.n = n;
        this.noise = noise;
        this.frames = frames;
        this.filename = filename;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][]{
                /**
                 * id, squareCount, interactionRadius, periodic, l, n, noise, frame
                */
                {1, 5, 0.5, false,10, 10,0.3, 500, "src/test/resources/output/offLatice10-10-0.3.txt"},
                {1, 5, 0.5, false,10, 10,0.6, 500, "src/test/resources/output/offLatice10-10-0.6.txt"},
                {1, 5, 0.5, false,10, 10,0.9, 500, "src/test/resources/output/offLatice10-10-0.9.txt"},
                {1, 5, 0.5, false,10, 10,1.2, 500, "src/test/resources/output/offLatice10-10-1.2.txt"},
                {1, 5, 0.5, false,10, 10,1.5, 500, "src/test/resources/output/offLatice10-10-1.5.txt"},
                {1, 5, 0.5, false,10, 10,1.8, 500, "src/test/resources/output/offLatice10-10-1.8.txt"},
                {1, 5, 0.5, false,10, 10,2.1, 500, "src/test/resources/output/offLatice10-10-2.1.txt"},
                {1, 5, 0.5, false,10, 10,2.4, 500, "src/test/resources/output/offLatice10-10-2.4.txt"},


                {1, 5, 0.5, false,10, 50,0.3, 500, "src/test/resources/output/offLatice10-50-0.3.txt"},
                {1, 5, 0.5, false,10, 50,0.6, 500, "src/test/resources/output/offLatice10-50-0.6.txt"},
                {1, 5, 0.5, false,10, 50,0.9, 500, "src/test/resources/output/offLatice10-50-0.9.txt"},
                {1, 5, 0.5, false,10, 50,1.2, 500, "src/test/resources/output/offLatice10-50-1.2.txt"},
                {1, 5, 0.5, false,10, 50,1.5, 500, "src/test/resources/output/offLatice10-50-1.5.txt"},
                {1, 5, 0.5, false,10, 50,1.8, 500, "src/test/resources/output/offLatice10-50-1.8.txt"},
                {1, 5, 0.5, false,10, 50,2.1, 500, "src/test/resources/output/offLatice10-50-2.1.txt"},
                {1, 5, 0.5, false,10, 50,2.4, 500, "src/test/resources/output/offLatice10-50-2.4.txt"},

                {1, 5, 0.5, false,10, 100,0.3, 500, "src/test/resources/output/offLatice10-100-0.3.txt"},
                {1, 5, 0.5, false,10, 100,0.6, 500, "src/test/resources/output/offLatice10-100-0.6.txt"},
                {1, 5, 0.5, false,10, 100,0.9, 500, "src/test/resources/output/offLatice10-100-0.9.txt"},
                {1, 5, 0.5, false,10, 100,1.2, 500, "src/test/resources/output/offLatice10-100-1.2.txt"},
                {1, 5, 0.5, false,10, 100,1.5, 500, "src/test/resources/output/offLatice10-100-1.5.txt"},
                {1, 5, 0.5, false,10, 100,1.8, 500, "src/test/resources/output/offLatice10-100-1.8.txt"},
                {1, 5, 0.5, false,10, 100,2.1, 500, "src/test/resources/output/offLatice10-100-2.1.txt"},
                {1, 5, 0.5, false,10, 100,2.4, 500, "src/test/resources/output/offLatice10-100-2.4.txt"},

                {1, 5, 0.5, false,10, 400,0.3, 500, "src/test/resources/output/offLatice10-400-0.3.txt"},
                {1, 5, 0.5, false,10, 400,0.6, 500, "src/test/resources/output/offLatice10-400-0.6.txt"},
                {1, 5, 0.5, false,10, 400,0.9, 500, "src/test/resources/output/offLatice10-400-0.9.txt"},
                {1, 5, 0.5, false,10, 400,1.2, 500, "src/test/resources/output/offLatice10-400-1.2.txt"},
                {1, 5, 0.5, false,10, 400,1.5, 500, "src/test/resources/output/offLatice10-400-1.5.txt"},
                {1, 5, 0.5, false,10, 400,1.8, 500, "src/test/resources/output/offLatice10-400-1.8.txt"},
                {1, 5, 0.5, false,10, 400,2.1, 500, "src/test/resources/output/offLatice10-400-2.1.txt"},
                {1, 5, 0.5, false,10, 400,2.4, 500, "src/test/resources/output/offLatice10-400-2.4.txt"},


                {1, 5, 0.5, false,10, 1000,0.3, 500, "src/test/resources/output/offLatice10-1000-0.3.txt"},
                {1, 5, 0.5, false,10, 1000,0.6, 500, "src/test/resources/output/offLatice10-1000-0.6.txt"},
                {1, 5, 0.5, false,10, 1000,0.9, 500, "src/test/resources/output/offLatice10-1000-0.9.txt"},
                {1, 5, 0.5, false,10, 1000,1.2, 500, "src/test/resources/output/offLatice10-1000-1.2.txt"},
                {1, 5, 0.5, false,10, 1000,1.5, 500, "src/test/resources/output/offLatice10-1000-1.5.txt"},
                {1, 5, 0.5, false,10, 1000,1.8, 500, "src/test/resources/output/offLatice10-1000-1.8.txt"},
                {1, 5, 0.5, false,10, 1000,2.1, 500, "src/test/resources/output/offLatice10-1000-2.1.txt"},
                {1, 5, 0.5, false,10, 1000,2.4, 500, "src/test/resources/output/offLatice10-1000-2.4.txt"},




//                {1, 5, 0.5, false,10, 1000,0.3, 500, "src/test/resources/output/offLatice10-1000-0.3.txt"},
//                {1, 5, 0.5, false,100, 100,0.3, 500, "src/test/resources/output/offLatice100-100-0.3.txt"},
//                {1, 5, 0.5, false,1000, 100,0.3, 500, "src/test/resources/output/offLatice1000-100-0.3.txt"},
//                {1, 5, 0.5, false,100, 10000,0.3, 500, "src/test/resources/output/offLatice100-10000-0.3.txt"}


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
            offLatice = new OffLatice(squareCount,interactionRadius,periodic,l,n,noise, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void construct(){

        offLatice.simulate(frames);

    }

    @After
    public void writeResults() throws IOException {
        /**
         * Agregado de resultados de cada sistema al CSV general.
         */
        resultsWriter.append(Long.toString(offLatice.getParticleSystem().getN()));
        resultsWriter.append(',');
        resultsWriter.append(Double.toString(offLatice.getParticleSystem().getOrder()));
        resultsWriter.append(',');
        resultsWriter.append(Double.toString(offLatice.getNoise()));
        resultsWriter.append('\n');
    }

    @AfterClass
    public static void closeCSV() throws IOException {
        resultsWriter.flush();
        resultsWriter.close();
    }
}
