package simulation;

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


    public SolarSystemSimulation(long planetQuantity, double angularMomentum, String fileName) throws IOException{
        solarSystem = new SolarSystem(planetQuantity,angularMomentum);

        try {
            Files.deleteIfExists(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
    }

    /**
     * Simulate a solar system.
     *
     * @param k relation beetween dt and dt2. kdt = dt2
     * @param dt time beetween calculations
     * @param t total time of simulation
     */
    public void simulate(int k, double dt, int t){
        solarSystem.writeFrameWithDirection(writer, 1, 0);
        int framesWrited = 1;
        double totalTimeSimulated = 0;
        while(totalTimeSimulated < t){
            for(int i=0 ; i<k; i++){
                solarSystem.move(dt);
                totalTimeSimulated+=dt;
                System.out.println(totalTimeSimulated);
            }
            solarSystem.writeFrameWithDirection(writer,1,framesWrited++);
            writer.flush();
        }
        writer.flush();
        writer.close();
    }
}
