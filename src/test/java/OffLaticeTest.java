import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import simulation.OffLatice;

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

    public OffLaticeTest(int id, int squareCount, double interactionRadius, boolean periodic,
                         double l, long n, double noise, long frames){
        this.id = id;
        this.squareCount = squareCount;
        this.interactionRadius = interactionRadius;
        this .periodic = periodic;
        this.l = l;
        this.n = n;
        this.noise = noise;
        this.frames = frames;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][]{
                /**
                 * id, squareCount, interactionRadius, periodic, l, n, noise
                */
                {1, 19, 0.5, false,10, 1000,0.3, 20}
        });
    }

    @Before
    public void init(){
        offLatice = new OffLatice(squareCount,interactionRadius,periodic,l,n,noise);
    }

    @Test
    public void construct(){

        offLatice.simulate(frames);

    }
}
