package simulation;

import model.GranularSystem;
import model.Wall;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 */
public class GranularSystemSimulation {

    private GranularSystem granularSystem;
    private PrintWriter writer;

    public GranularSystemSimulation(double width , double height, double apperture,int grainsCount, String fileName) throws IOException{
        if(apperture >= width || width >= height){
            throw new IllegalArgumentException();
        }
        granularSystem = new GranularSystem(width,height,apperture,grainsCount);

        try {
            Files.deleteIfExists(Paths.get(fileName));

        } catch (IOException e) {
            e.printStackTrace();
        }
        writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
    }

    public void simulate(int k, double dt, int t){
        granularSystem.writeFrameWithDirection(writer,0);
        writer.flush();
        writer.close();
    }
}
