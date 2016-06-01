package simulation;

import model.BehaviourSystem;
import org.apache.log4j.Logger;
import util.PrintFormatter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;

public class BehaviourSystemSimulation {

    private BehaviourSystem behaviourSystem;
    private PrintWriter writer;
    private PrintWriter resultWriter;

    public BehaviourSystemSimulation(double width , double height, double apperture, int maxAgents, String fileName, String resultFileName, int squareCount,double innerRadius, double outterRadius, double vMax ) throws IOException{
        if(apperture >= width || width > height){
            throw new IllegalArgumentException();
        }
        behaviourSystem = new BehaviourSystem(width,height,apperture,squareCount,innerRadius,outterRadius,maxAgents, vMax);
        try {
            Files.deleteIfExists(Paths.get(fileName));
            Files.deleteIfExists(Paths.get(resultFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
        resultWriter = new PrintWriter(new BufferedWriter(new FileWriter(resultFileName, true)));
    }

    public void simulate(int k, double dt, double t){
        writeResultHeader(resultWriter);
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
            writeResult(resultWriter,totalTimeSimulated);
            behaviourSystem.getParticleDetectorWall().resetCounts();
        }
        writer.flush();
        writer.close();
        resultWriter.flush();
        resultWriter.close();
    }

    private void writeResultHeader(PrintWriter writer){
        StringBuilder sb = new StringBuilder();
        sb.append("t").append(",").append("N").append("\n");
        writer.write(sb.toString());
    }

    private void writeResult(PrintWriter writer, double timeStep) {
        DecimalFormat df = new PrintFormatter().getDf();
        StringBuilder sb = new StringBuilder();
        int counts = behaviourSystem.getParticleDetectorWall().getCounts();
        sb.append(df.format(timeStep)).append(",")
                .append(df.format(counts)).append("\n");
        writer.write(sb.toString());
    }

}
