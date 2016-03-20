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
    private int rc;
    private String staticFilename;
    private String dynamicFilename;
    private int selectedParticle;

    public static final String INPUT_PATH = "src/test/resources/input/";
    public static final String OUTPUT_PATH = "src/test/resources/output/";
    public static final String CSV_FILENAME = "times.csv";
    public static final String OUTPUT_FILENAME = "output-%d.txt";
    private static final String VISUAL_FILENAME = "visual-%d-%d.txt";

    public ParticleSystemTest(int id, int m, int rc, String staticFilename, String dynamicFilename, int selectedParticle) {
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
                {1, 1, 20, "Static100.txt", "Dynamic100.txt", -1},
                {2, 1, 10, "Static100.txt", "Dynamic100.txt", -1},
                {3, 1, 5, "Static100.txt", "Dynamic100.txt", -1},
                {4, 1, 1, "Static100.txt", "Dynamic100.txt", -1}
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
        particleSystem.writeVisualization(OUTPUT_PATH + String.format(VISUAL_FILENAME, id, selectedParticle), selectedParticle);
    }

    @AfterClass
    public static void closeCSV() throws IOException {
        resultsWriter.flush();
        resultsWriter.close();
    }

}
