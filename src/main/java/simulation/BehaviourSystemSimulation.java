package simulation;

import model.BehaviourSystem;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BehaviourSystemSimulation {

    private BehaviourSystem behaviourSystem;
    private PrintWriter writer;

    public BehaviourSystemSimulation(double width , double height, double apperture, int maxAgents, String fileName,
                                     int squareCount,double innerRadius, double outterRadius, double vMax ) throws IOException{
        if(apperture >= width || width > height){
            throw new IllegalArgumentException();
        }
        behaviourSystem = new BehaviourSystem(width,height,apperture,squareCount,innerRadius,outterRadius,maxAgents, vMax);
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
            for(int i = 0; i < k; i++){
                behaviourSystem.move(dt);
                totalTimeSimulated += dt;
            }
            behaviourSystem.writeFrameWithDirection(writer,framesWrited++);
            Logger.getLogger(this.getClass()).info("Tiempo simulado: " + String.format("%.2f",totalTimeSimulated) + "s");
        }
        writer.flush();
        writer.close();
    }

}
