import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import simulation.OscilatorSimulation;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OscilatorTest {

    private OscilatorSimulation oscilatorSimulation;

    private static FileWriter analyticWriter;
    private static FileWriter beemanWriter;
    private static FileWriter velretWriter;

    private static final String ANALYTIC_FILENAME = "analytic.csv";
    private static final String BEEMAN_FILENAME = "beeman.csv";
    private static final String VELRET_FILENAME = "velret.csv";
    private static final String OUTPUT_PATH = "src/test/resources/output/";

    private static final double T = 10;
    private static final double DELTA_T = 0.05;

    @BeforeClass
    public static void setUpCSV() throws IOException {
        analyticWriter = initFileWriter(OUTPUT_PATH + ANALYTIC_FILENAME);
        beemanWriter = initFileWriter(OUTPUT_PATH + BEEMAN_FILENAME);
        velretWriter = initFileWriter(OUTPUT_PATH + VELRET_FILENAME);
    }

    @Before
    public void init(){
        oscilatorSimulation = new OscilatorSimulation();
    }

    @Test
    public void analyticTest() throws IOException {
        oscilatorSimulation.simulateAnalytic(T, DELTA_T, analyticWriter);
    }

    @Test
    public void beemanTest() throws IOException {
        oscilatorSimulation.simulateBeeman(T, DELTA_T, beemanWriter);
    }

    @Test
    public void velretTest() throws IOException {
        oscilatorSimulation.simulateVelret(T, DELTA_T, velretWriter);
    }

    @AfterClass
    public static void closeCSV() throws IOException {
        beemanWriter.flush();
        beemanWriter.close();
        velretWriter.flush();
        velretWriter.close();
        analyticWriter.flush();
        analyticWriter.close();
    }

    public static FileWriter initFileWriter(String fileName) throws IOException {
        Files.deleteIfExists(Paths.get(fileName));
        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.append("t");
        fileWriter.append(",");
        fileWriter.append("x");
        fileWriter.append('\n');
        return fileWriter;
    }

}
