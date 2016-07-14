import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import simulation.BehaviourSystemSimulation;
import simulation.WaypointNavigationSystemSimulation;

import java.io.IOException;
import java.util.Arrays;

@RunWith(value = Parameterized.class)
public class WaypointNavigationTest {

    WaypointNavigationSystemSimulation waypointNavigationSystemSimulation;
    int id;
    int dim;
    double startX;
    double startY;
    double waypointSeparation;
    double goalX;
    double goalY;
    int maxObstacles;
    String fileName;
    int k;
    double dt;
    double t;

    public static final String OUTPUT_PATH = "src/test/resources/output/navigation%d.csv";

    public WaypointNavigationTest(int id, int dim, double startX, double startY, double waypointSeparation,
                                  double goalX, double goalY, int maxObstacles, String fileName, int k, double dt, double t) {
        this.id = id;
        this.dim = dim;
        this.startX = startX;
        this.startY = startY;
        this.waypointSeparation = waypointSeparation;
        this.goalX = goalX;
        this.goalY = goalY;
        this.maxObstacles = maxObstacles;
        this.fileName = fileName;
        this.k = k;
        this.dt = dt;
        this.t = t;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data1() {
        int id = 0;
        int dim = 10;
        double startX = 0.5;
        double startY = 5;
        double goalX = 9.5;
        double goalY = 3.5;
        double waypointSeparation = 0.5;
        int maxObstacles = 35;
        int k = 5;
        double dt = 0.01;
        double t = 100;
        return Arrays.asList(new Object[][]{
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t}
        });
    }

    @Before
    public void init() throws IOException {
        waypointNavigationSystemSimulation = new WaypointNavigationSystemSimulation(dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, fileName);
    }

    @Test
    public void simulate(){
        waypointNavigationSystemSimulation.simulate(k,dt,t);
    }

}
