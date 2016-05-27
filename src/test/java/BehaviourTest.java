import model.Particle;
import model.Wall;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import simulation.BehaviourSystemSimulation;
import simulation.GranularSystemSimulation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 */
@RunWith(value = Parameterized.class)
public class BehaviourTest {

    BehaviourSystemSimulation behaviourSystemSimulation;
    int id;
    double width;
    double height;
    double apperture;
    int maxAgents;
    String fileName;
    int squareCount;
    double innerRadius;
    double outterRadius;
    double vMax;
    int k;
    double dt;
    double t;

    public static final String CSV_FILENAME = "behaviour";
    public static final String OUTPUT_PATH = "src/test/resources/output/";

    public BehaviourTest(int id,double width, double height, double apperture, int maxAgents, String fileName, int squareCount,
                         double innerRadius, double outterRadius,double vMax, int k, double dt, double t) {
        this.width = width;
        this.height = height;
        this.apperture = apperture;
        this.maxAgents = maxAgents;
        this.fileName = fileName;
        this.squareCount = squareCount;
        this.innerRadius = innerRadius;
        this.outterRadius = outterRadius;
        this.vMax = vMax;
        this.k = k;
        this.dt = dt;
        this.t = t;
        this.id = id;
    }


    @Parameterized.Parameters
    public static Iterable<Object[]> data1() {
        return Arrays.asList(new Object[][]{
//        int id, double width , double height, double apperture, int maxAgents, String fileName,
//        int squareCount,double innerRadius, double outterRadius, int k, double dt, double t
                {1, 20.0, 20.0, 1.2, 300, OUTPUT_PATH + CSV_FILENAME,1,0.15,0.32,1.55,5,0.05,80.0}
        });
    }



    @Before
    public void init() throws IOException {
            behaviourSystemSimulation = new BehaviourSystemSimulation(width,height, apperture,maxAgents, fileName + id + ".csv",
                    squareCount,innerRadius,outterRadius,vMax);
    }

    @Test
    public void simulate(){
        behaviourSystemSimulation.simulate(k,dt,t);
    }

}
