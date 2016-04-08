import model.ParticleSystem;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.runner.RunWith;
import util.ObjectPlainWriter;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

/**
 * Compare times
 */
@RunWith(value = Parameterized.class)
public class ParticleSystemTest {

    private static Logger logger = Logger.getLogger(ParticleSystemTest.class);

    private ParticleSystem particleSystem;

    private static FileWriter resultsWriter;

    private long start;
    private long end;

    private int id;
    private int m;
    private double rc;
    private String staticFilename;
    private String dynamicFilename;
    private int selectedParticle;

    public static final String INPUT_PATH = "src/test/resources/input/";
    public static final String OUTPUT_PATH = "src/test/resources/output/";
    public static final String CSV_FILENAME = "times.csv";
    public static final String OUTPUT_FILENAME = "output-%d.txt";
    private static final String VISUAL_FILENAME = "visual-%d.txt";

    public ParticleSystemTest(int id, int m, double rc, String staticFilename, String dynamicFilename, int selectedParticle) {
        this.id = id;
        this.m = m;
        this.rc = rc;
        this.staticFilename = staticFilename;
        this.dynamicFilename = dynamicFilename;
        this.selectedParticle = selectedParticle;
    }

    @Parameters
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][] {
                /**
                 * id, M, intRadius, staticFile, dynamicFile, selectedParticle (con -1 es random)
                 */
//                {1, 1, 99, "Static100.txt", "Dynamic100.txt", -1},
//                {2, 5, 1, "Static100.txt", "Dynamic100.txt", -1},
//                {3, 70, 1, "Static100.txt", "Dynamic100.txt", -1},
//                {5, 150, 0.1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 1, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 2, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 3, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 4, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 5, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 6, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 7, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 8, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 9, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 10, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 11, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 12, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 13, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 14, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 15, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 16, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 17, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 18, 1, "Static10000.txt", "Dynamic10000.txt", -1},
//                {5, 19, 1, "Static10000.txt", "Dynamic10000.txt", -1}
                    {7, 6, 15, "Static500.txt", "Dynamic500.txt", 217}
//                {6, 20, 20, "Static100000.txt", "Dynamic100000.txt", -1}

        });
    }

    @BeforeClass
    public static void setUpCSV() throws IOException {
        resultsWriter = new FileWriter(OUTPUT_PATH + CSV_FILENAME);
        resultsWriter.append("M");
        resultsWriter.append(',');
        resultsWriter.append("rc");
        resultsWriter.append(',');
        resultsWriter.append("Time");
        resultsWriter.append('\n');
    }

    @Before
    public void loadFiles() {
        ObjectPlainWriter objectPlainWriter = new ObjectPlainWriter();
        particleSystem = new ParticleSystem(true, m , rc);
        objectPlainWriter.readObject(INPUT_PATH + staticFilename, particleSystem);
        objectPlainWriter.readParticleSystem(INPUT_PATH + dynamicFilename, particleSystem);
        start = System.currentTimeMillis();
    }

    @Test
    public void testTime() {
        particleSystem.populateNeighbourhood();
    }

    @After
    public void finish() throws IOException {
        end = System.currentTimeMillis();
        logger.info("M: " + particleSystem.getSquareCount()
                + " RC: " + particleSystem.getInteractionRadius()
                + " Tiempo: " + (end - start) + " ms");

        /**
         * Agregado de resultados de cada sistema al CSV general.
         */
        resultsWriter.append(Integer.toString(particleSystem.getSquareCount()));
        resultsWriter.append(',');
        resultsWriter.append(Double.toString(particleSystem.getInteractionRadius()));
        resultsWriter.append(',');
        resultsWriter.append(Long.toString(end - start));
        resultsWriter.append('\n');

        /**
         * Escritura del archivo OUTPUT para cada sistema.
         */
        PrintWriter writer = new PrintWriter(OUTPUT_PATH + String.format(OUTPUT_FILENAME, id));
        writer.write(particleSystem.writeObject());
        writer.flush();
        writer.close();

        /**
         * Escritura del archivo XYZ de visualización para cada sistema.
         */
        if(selectedParticle == -1) {
            selectedParticle = new Random().nextInt(particleSystem.getParticles().size());
        }
        particleSystem.writeVisualization(OUTPUT_PATH + String.format(VISUAL_FILENAME, id), selectedParticle, 0);
    }

    @AfterClass
    public static void closeCSV() throws IOException {
        resultsWriter.flush();
        resultsWriter.close();
    }

}
