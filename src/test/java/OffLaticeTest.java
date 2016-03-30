import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import simulation.OffLatice;

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
                {1, 5, 0.5, false,10, 100,0.3, 500, "src/test/resources/output/offLatice.txt"}
        });
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
}
