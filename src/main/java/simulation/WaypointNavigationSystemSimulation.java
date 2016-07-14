package simulation;

import model.BehaviourSystem;
import model.WaypointNavigationSystem;
import org.apache.log4j.Logger;
import util.PrintFormatter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;

/**
 * Created by leonelbadi on 14/7/16.
 */
public class WaypointNavigationSystemSimulation {

    private WaypointNavigationSystem waypointNavigationSystem;
    private PrintWriter writer;
    private PrintWriter resultWriter;

    public WaypointNavigationSystemSimulation(double startX, double startY, double waypointSeparation,
                                              double goalX, double goalY, int maxObstacles, String fileName ) throws IOException {
        waypointNavigationSystem = new WaypointNavigationSystem(startX,startY,waypointSeparation,goalX,goalY,maxObstacles);
        try {
            Files.deleteIfExists(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
    }

    public void simulate(int k, double dt, double t){
        waypointNavigationSystem.writeFrameWithDirection(writer,0);
        writer.flush();
        writer.close();

    }

}
