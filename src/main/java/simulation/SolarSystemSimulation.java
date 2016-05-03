package simulation;

import model.Oscilator;
import model.Particle;
import model.ParticleSystem;
import model.SolarSystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SolarSystemSimulation {

    private SolarSystem solarSystem;
    PrintWriter writer;
    PrintWriter csvWriter;


    public SolarSystemSimulation(long planetQuantity, double angularMomentum, String fileName, String csvfilename) throws IOException{
        solarSystem = new SolarSystem(planetQuantity,angularMomentum);

        try {
            Files.deleteIfExists(Paths.get(fileName));
            Files.deleteIfExists(Paths.get(csvfilename));

        } catch (IOException e) {
            e.printStackTrace();
        }
        writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
        csvWriter = new PrintWriter(new BufferedWriter(new FileWriter(csvfilename, true)));

    }

    /**
     * Simulate a solar system.
     *
     * @param k relation beetween dt and dt2. kdt = dt2
     * @param dt time beetween calculations
     * @param t total time of simulation
     */
    public void simulate(int k, double dt, int t){
        solarSystem.writeEnergysHeaderInTime(csvWriter);
        solarSystem.writeFrameWithDirection(writer, 0);
        solarSystem.writeEnergysInTime(csvWriter,0);
        int framesWrited = 1;
        double totalTimeSimulated = 0;
        while(totalTimeSimulated < t){
            for(int i=0 ; i<k; i++){
                if(totalTimeSimulated == 0) {
                    solarSystem.moveEuler(dt);
                }
                else{
                    solarSystem.moveBeeman(dt);
                }
//                solarSystem.move(dt);

                //Detect collisions
                //Refresh particle position in neighbourhood
                solarSystem.refreshSystem(solarSystem.getParticles());
                solarSystem.populateNeighbourhood();
                solarSystem.collidePlanets();
                totalTimeSimulated+=dt;
                System.out.println("Tiempo simulado : " + totalTimeSimulated);
                solarSystem.writeEnergysInTime(csvWriter,totalTimeSimulated);
            }
            solarSystem.writeFrameWithDirection(writer,framesWrited++);

        }
        csvWriter.flush();
        csvWriter.close();
        writer.flush();
        writer.close();
    }

}
