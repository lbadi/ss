import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import simulation.BehaviourSystemSimulation;
import simulation.WaypointNavigationSystemSimulation;
import util.PrintFormatter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;

@RunWith(value = Parameterized.class)
public class WaypointNavigationTest {

    public static NumberFormat df = new PrintFormatter().getDf();


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

    private static FileWriter resultsWriter;
    public static final String OUTPUT_PATH = "src/test/resources/output/navigation%d.csv";
    public static final String RESULT_OUTPUT_PATH = "src/test/resources/output/";

    public static final String CSV_RESULT_FILENAME = "results.csv";


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
        int dim = 25;
        double startX = 0.5;
        double startY = 5;
        double goalX = 23.25;
        double goalY = 22.25;
        double waypointSeparation = 1;
        int maxObstacles = 250;
        int k = 5;
        double dt = 0.01;
        double t = 100;
        return Arrays.asList(new Object[][]{

                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},

                {++id, dim, startX, startY, 1.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
                {++id, dim, startX, startY, 1.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
                {++id, dim, startX, startY, 1.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
                {++id, dim, startX, startY, 1.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
                {++id, dim, startX, startY, 1.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},

//                {++id, dim, startX, startY, 0.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
//                {++id, dim, startX, startY, 0.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
//                {++id, dim, startX, startY, 0.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
//                {++id, dim, startX, startY, 0.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
//                {++id, dim, startX, startY, 0.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},

                {++id, dim, startX, startY, 2, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
                {++id, dim, startX, startY, 2, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
                {++id, dim, startX, startY, 2, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
                {++id, dim, startX, startY, 2, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
                {++id, dim, startX, startY, 2, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), k, dt, t},
//                  --------------

                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},

                {++id, dim, startX, startY, 1.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},
                {++id, dim, startX, startY, 1.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},
                {++id, dim, startX, startY, 1.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},
                {++id, dim, startX, startY, 1.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},
                {++id, dim, startX, startY, 1.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},

//                {++id, dim, startX, startY, 0.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},
//                {++id, dim, startX, startY, 0.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},
//                {++id, dim, startX, startY, 0.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},
//                {++id, dim, startX, startY, 0.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},
//                {++id, dim, startX, startY, 0.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},

                {++id, dim, startX, startY, 2, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},
                {++id, dim, startX, startY, 2, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},
                {++id, dim, startX, startY, 2, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},
                {++id, dim, startX, startY, 2, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},
                {++id, dim, startX, startY, 2, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 3, dt, t},

//               -----------------

                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},
                {++id, dim, startX, startY, waypointSeparation, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},

                {++id, dim, startX, startY, 1.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},
                {++id, dim, startX, startY, 1.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},
                {++id, dim, startX, startY, 1.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},
                {++id, dim, startX, startY, 1.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},
                {++id, dim, startX, startY, 1.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},

                {++id, dim, startX, startY, 0.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},
                {++id, dim, startX, startY, 0.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},
                {++id, dim, startX, startY, 0.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},
                {++id, dim, startX, startY, 0.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},
                {++id, dim, startX, startY, 0.5, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},

                {++id, dim, startX, startY, 2, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},
                {++id, dim, startX, startY, 2, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},
                {++id, dim, startX, startY, 2, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},
                {++id, dim, startX, startY, 2, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},
                {++id, dim, startX, startY, 2, goalX, goalY, maxObstacles, String.format(OUTPUT_PATH, id), 7, dt, t},


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

    @BeforeClass
    public static void setUpCSV() throws IOException {
        resultsWriter = new FileWriter(RESULT_OUTPUT_PATH + CSV_RESULT_FILENAME);
        resultsWriter.append("T");
        resultsWriter.append(',');
        resultsWriter.append("WPS"); //WAYPOINT separation
        resultsWriter.append(',');
        resultsWriter.append("WINDOW");
        resultsWriter.append(',');
        resultsWriter.append("ENDED");
//        resultsWriter.append(',');
//        resultsWriter.append("Noise");
        resultsWriter.append('\n');
    }

    @After
    public void writeResults() throws IOException {
        /**
         * Agregado de resultados de cada sistema al CSV general.
         */
        resultsWriter.append(df.format(waypointNavigationSystemSimulation.getSimulationTime()));
        resultsWriter.append(',');
        resultsWriter.append(df.format(waypointSeparation));
        resultsWriter.append(',');
        resultsWriter.append(Integer.toString(k));
        resultsWriter.append(',');
        resultsWriter.append(Boolean.toString(waypointNavigationSystemSimulation.isReachTarget()));
        resultsWriter.append('\n');

//        resultsWriter.append(Double.toString(offLatice.getParticleSystem().getOrder()));
//        resultsWriter.append(',');
//        resultsWriter.append(Double.toString(offLatice.getNoise()));
//        resultsWriter.append('\n');
    }

    @AfterClass
    public static void closeCSV() throws IOException {
        resultsWriter.flush();
        resultsWriter.close();
    }

}
