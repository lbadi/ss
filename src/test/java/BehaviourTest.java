import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import simulation.BehaviourSystemSimulation;

import java.io.IOException;
import java.util.Arrays;

@RunWith(value = Parameterized.class)
public class BehaviourTest {

    BehaviourSystemSimulation behaviourSystemSimulation;
    int id;
    double width;
    double height;
    double apperture;
    int maxAgents;
    int squareCount;
    double innerRadius;
    double outterRadius;
    double vMax;
    int k;
    double dt;
    double t;

    public static final String CSV_FILENAME = "behaviour";
    public static final String OUTPUT_PATH = "src/test/resources/output/";

    public BehaviourTest(int id,double width, double height, double apperture, int maxAgents, int squareCount,
                         double innerRadius, double outterRadius,double vMax, int k, double dt, double t) {
        this.width = width;
        this.height = height;
        this.apperture = apperture;
        this.maxAgents = maxAgents;
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
        double width = 20;
        double height = 20;
        double apperture = 1.2;
        int squareCount = 1; //CellIndexMethod Off
        double rMin = 0.15;
        double rMax = 0.32;
        double vMax = 1.55;
        int k = 5;
        double dt = 0.05;
        double t = 200;
        return Arrays.asList(new Object[][]{
//        int id, double width, double height, double apperture, int maxAgents (<= N),
//        int squareCount, double innerRadius, double outterRadius, double vMax, int k, double dt, double t
                //N = 100
                {11, width, height, apperture, 100, squareCount, rMin, rMax, vMax, k, dt, t},
                {12, width, height, apperture, 100, squareCount, rMin, rMax, vMax, k, dt, t},
                {13, width, height, apperture, 100, squareCount, rMin, rMax, vMax, k, dt, t},
                //N = 200
                {21, width, height, apperture, 200, squareCount, rMin, rMax, vMax, k, dt, t},
                {22, width, height, apperture, 200, squareCount, rMin, rMax, vMax, k, dt, t},
                {23, width, height, apperture, 200, squareCount, rMin, rMax, vMax, k, dt, t},
                //N = 300
                {31, width, height, apperture, 300, squareCount, rMin, rMax, vMax, k, dt, t},
                {32, width, height, apperture, 300, squareCount, rMin, rMax, vMax, k, dt, t},
                {33, width, height, apperture, 300, squareCount, rMin, rMax, vMax, k, dt, t},

                {41, width, height, apperture * 3, 300, squareCount, rMin, rMax, vMax, k, dt, t},

        });
    }

    @Before
    public void init() throws IOException {
            behaviourSystemSimulation = new BehaviourSystemSimulation(width,height, apperture,maxAgents, OUTPUT_PATH + CSV_FILENAME + id + ".csv",
                    squareCount,innerRadius,outterRadius,vMax);
    }

    @Test
    public void simulate(){
        behaviourSystemSimulation.simulate(k,dt,t);
    }

}
