import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import simulation.GranularSystemSimulation;

import java.io.IOException;
import java.util.Arrays;

/**
 */
@RunWith(value = Parameterized.class)
public class GranularTest {

    GranularSystemSimulation granularSystemSimulation;
    int id;
    int squareCount;
    double width;
    double height;
    double apperture;
    int grainsCount;
    String filename;

    public static final String CSV_FILENAME = "granular.csv";
    public static final String OUTPUT_PATH = "src/test/resources/output/";


    public GranularTest(int id, int squareCount, double width, double height, double apperture,int grainsCount, String filename) {
        this.id = id;
        this.squareCount = squareCount;
        this.width = width;
        this.height = height;
        this.apperture = apperture;
        this.grainsCount = grainsCount;
        this.filename = filename;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][]{
                /**
                 * id, squareCount, widht, height, apperture,grainsCount, filename
                */

                {1, 1, 10, 20, 1,5000, OUTPUT_PATH + CSV_FILENAME},


        });
    }
    @Before
    public void init(){
        try {
            granularSystemSimulation = new GranularSystemSimulation(width,height, apperture,grainsCount, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void construct(){
        granularSystemSimulation.simulate(1,0.01,10);
    }

}
