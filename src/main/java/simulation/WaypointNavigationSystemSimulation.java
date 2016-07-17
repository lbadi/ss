package simulation;

import model.WaypointNavigationSystem;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WaypointNavigationSystemSimulation {

    private WaypointNavigationSystem waypointNavigationSystem;
    private PrintWriter writer;
    private PrintWriter resultWriter;
    private double simulationTime;
    private boolean reachTarget = false;


    public WaypointNavigationSystemSimulation(int dim, double startX, double startY, double waypointSeparation,
                                              double goalX, double goalY, int maxObstacles, String fileName) throws IOException {
        waypointNavigationSystem = new WaypointNavigationSystem(dim, startX,startY,waypointSeparation,goalX,goalY,maxObstacles);
        Files.deleteIfExists(Paths.get(fileName));
//        Files.deleteIfExists(Paths.get(resultFileName));

        writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
//        resultWriter = new PrintWriter(new BufferedWriter(new FileWriter(resultFileName, true)));

    }

    public void simulate(int k, double dt, double t){
        waypointNavigationSystem.writeFrameWithDirection(writer,0);
        int framesWrited = 1;
        double totalTimeSimulated = 0;
        waypointNavigationSystem.move(dt);
        totalTimeSimulated += dt;
        while(totalTimeSimulated < t){
            for(int i = 0; i < k; i++){
                waypointNavigationSystem.move(dt);
                totalTimeSimulated += dt;
            }
            waypointNavigationSystem.writeFrameWithDirection(writer,framesWrited++);
            if(waypointNavigationSystem.hasEnded()) {
                reachTarget = true;
                break;
            }
            Logger.getLogger(this.getClass()).info("Tiempo simulado: " + String.format("%.2f",totalTimeSimulated) + "s");
        }
        simulationTime = t;
        writer.flush();
        writer.close();
    }

    public double getSimulationTime() {
        return simulationTime;
    }

    public boolean isReachTarget() {
        return reachTarget;
    }
}
