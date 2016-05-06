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
    private static FileWriter gearWriter;
    private static FileWriter beemanErrorWriter;
    private static FileWriter velretErrorWriter;
    private static FileWriter gearErrorWriter;

    private static final String ANALYTIC_FILENAME = "analytic.csv";
    private static final String BEEMAN_FILENAME = "beeman.csv";
    private static final String VELRET_FILENAME = "velret.csv";
    private static final String GEAR_FILENAME = "gear.csv";

    private static final String BEEMAN_ERROR_FILENAME = "E-beeman.csv";
    private static final String VELRET_ERROR_FILENAME = "E-velret.csv";
    private static final String GEAR_ERROR_FILENAME = "E-gear.csv";

    private static final String OUTPUT_PATH = "src/test/resources/output/";

    private static final double T = 5;
    private static final double DELTA_T = 0.0001;

    @BeforeClass
    public static void setUpCSV() throws IOException {
        analyticWriter = initFileWriter(getFileNamePath(ANALYTIC_FILENAME));
        beemanWriter = initFileWriter(getFileNamePath(BEEMAN_FILENAME));
        velretWriter = initFileWriter(getFileNamePath(VELRET_FILENAME));
        gearWriter = initFileWriter(getFileNamePath(GEAR_FILENAME));
        beemanErrorWriter = initFileWriter(getFileNamePath(BEEMAN_ERROR_FILENAME));
        velretErrorWriter = initFileWriter(getFileNamePath(VELRET_ERROR_FILENAME));
        gearErrorWriter = initFileWriter(getFileNamePath(GEAR_ERROR_FILENAME));
    }

    private static String getFileNamePath(String fileName) {
        return OUTPUT_PATH + DELTA_T + '-' + T + '-' + fileName;
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
        oscilatorSimulation.simulateBeeman(T, DELTA_T, beemanWriter, beemanErrorWriter);
    }

    @Test
    public void velretTest() throws IOException {
        oscilatorSimulation.simulateVelocityVelret(T, DELTA_T, velretWriter, velretErrorWriter);
    }

    @Test
    public void gearTest() throws IOException {
        oscilatorSimulation.simulateGear(T, DELTA_T, gearWriter, gearErrorWriter);
    }

    @AfterClass
    public static void closeCSV() throws IOException {
        beemanWriter.flush();
        beemanWriter.close();
        velretWriter.flush();
        velretWriter.close();
        analyticWriter.flush();
        analyticWriter.close();
        gearWriter.flush();
        gearWriter.close();
        beemanErrorWriter.flush();
        beemanErrorWriter.close();
        velretErrorWriter.flush();
        velretErrorWriter.close();
        gearErrorWriter.flush();
        gearErrorWriter.close();
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
