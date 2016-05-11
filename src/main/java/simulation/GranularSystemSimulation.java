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

        int framesWrited = 1;
        double totalTimeSimulated = 0;
        granularSystem.moveEuler(dt);
        totalTimeSimulated += dt;
        while(totalTimeSimulated < t){
            for(int i=0 ; i<k; i++){
//                granularSystem.moveBeeman(dt);
                granularSystem.moveEuler(dt);
                //Detect collisions
                //Refresh particle position in neighbourhood
//                granularSystem.refreshSystem(granularSystem.getParticles());
                granularSystem.populateNeighbourhood();
                totalTimeSimulated+=dt;
                System.out.println("Tiempo simulado : " + totalTimeSimulated);
            }
            granularSystem.writeFrameWithDirection(writer,framesWrited++);

        }

        writer.flush();
        writer.close();
    }

    public GranularSystem getGranularSystem() {
        return granularSystem;
    }
}
