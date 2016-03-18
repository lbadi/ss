import model.ParticleSystem;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import util.ObjectPlainWriter;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

/**
 * Compare times
 */
@RunWith(value = Parameterized.class)
public class TimeTest {

    private static Logger logger = Logger.getLogger(Main.class);
    private ParticleSystem particleSystem;
    private ObjectPlainWriter objectPlainWriter = new ObjectPlainWriter();
    private long start;
    private long end;
    private int m;
    private int rc;

    public TimeTest(int m, int rc){
        this.m = m;
        this.rc = rc;
    }

    @Test
    public void testTime(){
        particleSystem.populateNeighbourhood();
    }

    @Before
    public void loadFiles(){
        String filename = "src/main/resources/Static100.txt";
        objectPlainWriter = new ObjectPlainWriter();
        particleSystem = new ParticleSystem(true, m , rc);
        objectPlainWriter.readObject(filename, particleSystem);
        String filenameDynamic = "src/main/resources/Dynamic100.txt";
        objectPlainWriter.readParticleSystem(filenameDynamic, particleSystem);
        start = System.currentTimeMillis();
    }

    //Declares parameters here
    @Parameters
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][] {
                { 1, 1},
                { 5, 1},
                { 30, 1},
                { 100, 1}
        });
    }

    @After
    public void finish(){
        end = System.currentTimeMillis();
        logger.info("Tiempo total M: " + particleSystem.getSquareCount() + " RC: " + particleSystem.getInteractionRadius()
                + " :  " + (end - start) + " ms");
        logger.info("Fin");
    }
}
