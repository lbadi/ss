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
        

//                {1, 5, 0.5, false,10, 1000,0.3, 500, "src/test/resources/output/offLatice10-1000-0.3.txt"},
//                {1, 5, 0.5, false,100, 100,0.3, 500, "src/test/resources/output/offLatice100-100-0.3.txt"},
//                {1, 5, 0.5, false,1000, 100,0.3, 500, "src/test/resources/output/offLatice1000-100-0.3.txt"},
//                {1, 5, 0.5, false,100, 10000,0.3, 500, "src/test/resources/output/offLatice100-10000-0.3.txt"}

                {1, 2, 1, true,3.1, 40,0, 300, "src/test/resources/output/offLatice10-10-1.txt"},
//                {1, 2, 1, true,3.1, 40,0.5, 300, "src/test/resources/output/offLatice10-10-1.txt"},
//                {1, 2, 1, true,3.1, 40,1, 300, "src/test/resources/output/offLatice10-10-1.txt"},
//                {1, 2, 1, true,3.1, 40,1.5, 300, "src/test/resources/output/offLatice10-10-1.5.txt"},
//                {1, 2, 1, true,3.1, 40,2, 300, "src/test/resources/output/offLatice10-10-2.txt"},
//                {1, 2, 1, true,3.1, 40,2.5, 300, "src/test/resources/output/offLatice10-10-2.5.txt"},
//                {1, 2, 1, true,3.1, 40,3, 300, "src/test/resources/output/offLatice10-10-3.txt"},
//                {1, 2, 1, true,3.1, 40,3.5, 300, "src/test/resources/output/offLatice10-10-3.5.txt"},
//                {1, 2, 1, true,3.1, 40,4, 300, "src/test/resources/output/offLatice10-10-4.txt"},
//                {1, 2, 1, true,3.1, 40,4.5, 300, "src/test/resources/output/offLatice10-10-4.5.txt"},
//                {1, 2, 1, true,3.1, 40,5, 300, "src/test/resources/output/offLatice10-10-4.5.txt"},
//
//                {1, 4, 1, true,5, 100,0, 300, "src/test/resources/output/offLatice10-10-1.txt"},
//                {1, 4, 1, true,5, 100,0.5, 300, "src/test/resources/output/offLatice10-10-1.txt"},
//                {1, 4, 1, true,5, 100,1, 300, "src/test/resources/output/offLatice10-10-1.txt"},
//                {1, 4, 1, true,5, 100,1.5, 300, "src/test/resources/output/offLatice10-10-1.5.txt"},
//                {1, 4, 1, true,5, 100,2, 300, "src/test/resources/output/offLatice10-10-2.txt"},
//                {1, 4, 1, true,5, 100,2.5, 300, "src/test/resources/output/offLatice10-10-2.5.txt"},
//                {1, 4, 1, true,5, 100,3, 300, "src/test/resources/output/offLatice10-10-3.txt"},
//                {1, 4, 1, true,5, 100,3.5, 300, "src/test/resources/output/offLatice10-10-3.5.txt"},
//                {1, 4, 1, true,5, 100,4, 300, "src/test/resources/output/offLatice10-10-4.txt"},
//                {1, 4, 1, true,5, 100,4.5, 300, "src/test/resources/output/offLatice10-10-4.5.txt"},
//                {1, 4, 1, true,5, 100,5, 300, "src/test/resources/output/offLatice10-10-4.5.txt"},
//
//                {1, 8, 1, true,10, 400,0, 300, "src/test/resources/output/offLatice10-10-1.txt"},
//                {1, 8, 1, true,10, 400,0.5, 300, "src/test/resources/output/offLatice10-10-1.5.txt"},
//                {1, 8, 1, true,10, 400,1, 300, "src/test/resources/output/offLatice10-10-1.txt"},
//                {1, 8, 1, true,10, 400,1.5, 300, "src/test/resources/output/offLatice10-10-1.5.txt"},
//                {1, 8, 1, true,10, 400,2, 300, "src/test/resources/output/offLatice10-10-2.txt"},
//                {1, 8, 1, true,10, 400,2.5, 300, "src/test/resources/output/offLatice10-10-2.5.txt"},
//                {1, 8, 1, true,10, 400,3, 300, "src/test/resources/output/offLatice10-10-3.txt"},
//                {1, 8, 1, true,10, 400,3.5, 300, "src/test/resources/output/offLatice10-10-3.5.txt"},
//                {1, 8, 1, true,10, 400,4, 300, "src/test/resources/output/offLatice10-10-4.txt"},
//                {1, 8, 1, true,10, 400,4.5, 300, "src/test/resources/output/offLatice10-10-4.5.txt"},
//                {1, 8, 1, true,10, 400,5, 300, "src/test/resources/output/offLatice10-10-4.5.txt"},
//
//                {1, 28, 1, true,31.6, 4000,0, 300, "src/test/resources/output/offLatice10-10-1.txt"},
//                {1, 28, 1, true,31.6, 4000,0.5, 300, "src/test/resources/output/offLatice10-10-1.5.txt"},
//                {1, 28, 1, true,31.6, 4000,1, 300, "src/test/resources/output/offLatice10-10-1.txt"},
//                {1, 28, 1, true,31.6, 4000,1.5, 300, "src/test/resources/output/offLatice10-10-1.5.txt"},
//                {1, 28, 1, true,31.6, 4000,2, 300, "src/test/resources/output/offLatice10-10-2.txt"},
//                {1, 28, 1, true,31.6, 4000,2.5, 300, "src/test/resources/output/offLatice10-10-2.5.txt"},
//                {1, 28, 1, true,31.6, 4000,3, 300, "src/test/resources/output/offLatice10-10-3.txt"},
//                {1, 28, 1, true,31.6, 4000,3.5, 300, "src/test/resources/output/offLatice10-10-3.5.txt"},
//                {1, 28, 1, true,31.6, 4000,4, 300, "src/test/resources/output/offLatice10-10-4.txt"},
//                {1, 28, 1, true,31.6, 4000,4.5, 300, "src/test/resources/output/offLatice10-10-4.5.txt"},
//                {1, 28, 1, true,31.6, 4000,5, 300, "src/test/resources/output/offLatice10-10-4.5.txt"},
//
//                {1, 40, 1, true,50, 10000,0, 300, "src/test/resources/output/offLatice10-10-1.txt"},
//                {1, 40, 1, true,50, 10000,0.5, 300, "src/test/resources/output/offLatice10-10-1.5.txt"},
//                {1, 40, 1, true,50, 10000,1, 300, "src/test/resources/output/offLatice10-10-1.txt"},
//                {1, 40, 1, true,50, 10000,1.5, 300, "src/test/resources/output/offLatice10-10-1.5.txt"},
//                {1, 40, 1, true,50, 10000,2, 300, "src/test/resources/output/offLatice10-10-2.txt"},
//                {1, 40, 1, true,50, 10000,2.5, 300, "src/test/resources/output/offLatice10-10-2.5.txt"},
//                {1, 40, 1, true,50, 10000,3, 300, "src/test/resources/output/offLatice10-10-3.txt"},
//                {1, 40, 1, true,50, 10000,3.5, 300, "src/test/resources/output/offLatice10-10-3.5.txt"},
//                {1, 40, 1, true,50, 10000,4, 300, "src/test/resources/output/offLatice10-10-4.txt"},
//                {1, 40, 1, true,50, 10000,4.5, 300, "src/test/resources/output/offLatice10-10-4.5.txt"},
//                {1, 40, 1, true,50, 10000,5, 300, "src/test/resources/output/offLatice10-10-4.5.txt"},

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
