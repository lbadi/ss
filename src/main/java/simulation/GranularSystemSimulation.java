package simulation;

import model.GranularSystem;
import model.Wall;
import util.PrintFormatter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;

/**
 */
public class GranularSystemSimulation {

    private GranularSystem granularSystem;
    private PrintWriter writer;
    private PrintWriter csvWriter;

    public GranularSystemSimulation(double width , double height, double apperture,int grainsCount, String fileName, int squareCount,  String csvfilename) throws IOException{
        if(apperture >= width || width >= height){
            throw new IllegalArgumentException();
        }
        granularSystem = new GranularSystem(width,height,apperture,grainsCount, squareCount);

        try {
            Files.deleteIfExists(Paths.get(fileName));
            Files.deleteIfExists(Paths.get(csvfilename));

        } catch (IOException e) {
            e.printStackTrace();
        }
        writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
        csvWriter = new PrintWriter(new BufferedWriter(new FileWriter(csvfilename, true)));
    }

    public void simulate(int k, double dt, int t){
        writeEnergysHeaderInTime(csvWriter);
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
            writeEnergysInTime(csvWriter,totalTimeSimulated);
            granularSystem.getParticleDetectorWall().resetCounts();
        }

        writer.flush();
        writer.close();
        csvWriter.flush();
        csvWriter.close();
    }

    public void writeEnergysInTime(PrintWriter writer, double timeStep){
        DecimalFormat df = new PrintFormatter().getDf();
        StringBuilder sb = new StringBuilder();
        double kinetic = granularSystem.getKineticEnergy();
        int counts = granularSystem.getParticleDetectorWall().getCounts();
        sb.append(df.format(timeStep)).append(",")
                .append(df.format(kinetic)).append(",")
                .append(df.format(counts)).append("\n");
        writer.write(sb.toString());
    }

    public void writeEnergysHeaderInTime(PrintWriter writer){
        StringBuilder sb = new StringBuilder();
        sb.append("T").append(",")
                .append("K").append(",")
                .append("C").append("\n");
        writer.write(sb.toString());
    }


    public GranularSystem getGranularSystem() {
        return granularSystem;
    }
}
