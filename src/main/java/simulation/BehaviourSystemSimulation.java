package simulation;

import model.BehaviourSystem;
import model.GranularSystem;
import util.PrintFormatter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;

/**
 */
public class BehaviourSystemSimulation {

    private BehaviourSystem behaviourSystem;
    private PrintWriter writer;

    public BehaviourSystemSimulation(double width , double height, double apperture, int maxAgents, String fileName,
                                     int squareCount,double innerRadius, double outterRadius ) throws IOException{
        if(apperture >= width || width >= height){
            throw new IllegalArgumentException();
        }
        behaviourSystem = new BehaviourSystem(width,height,apperture,squareCount,innerRadius,outterRadius,maxAgents);

        try {
            Files.deleteIfExists(Paths.get(fileName));

        } catch (IOException e) {
            e.printStackTrace();
        }
        writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
    }

    public void simulate(int k, double dt, double t){
        behaviourSystem.writeFrameWithDirection(writer,0);

        int framesWrited = 1;
        double totalTimeSimulated = 0;
        behaviourSystem.move(dt);
        totalTimeSimulated += dt;
        while(totalTimeSimulated < t){
            for(int i=0 ; i<k; i++){
//                granularSystem.moveBeeman(dt);
                behaviourSystem.move(dt);
                //Detect collisions
                //Refresh particle position in neighbourhood
//                granularSystem.refreshSystem(granularSystem.getParticles());
//                behaviourSystem.populateNeighbourhood();
                totalTimeSimulated+=dt;

                System.out.println("Tiempo simulado : " + totalTimeSimulated);
            }
            behaviourSystem.writeFrameWithDirection(writer,framesWrited++);
//            behaviourSystem.getParticleDetectorWall().resetCounts();
        }

        writer.flush();
        writer.close();

    }


    public BehaviourSystem getBehaviourSystem() {
        return behaviourSystem;
    }
}
