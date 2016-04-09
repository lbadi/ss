package simulation;

import model.Particle;
import model.ParticleSystem;
import model.Wall;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Molecular dynamic simulation of Hard Spheres
 */
public class CollisionSimulation {

    ParticleSystem particleSystem;
    PrintWriter writer;

    public CollisionSimulation(int squareCount, double radius,  double l, long n, String fileName) throws IOException {
        try {
            Files.deleteIfExists(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Ver el radio de las particulas (No el radio de interacción). Hay que modificar para que sea el radio
        List<Wall> walls = new ArrayList<>();
        particleSystem = new ParticleSystem(squareCount, radius, l, n, new ArrayList<Wall>());
        writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));

    }
    // Simulate collision of hard spheres
    public void simulate(long framesQuantity){
        particleSystem.writeFrameWithDirection(writer, 1, 1);
//        for(int i=0; i<framesQuantity; i++){
            particleSystem.moveToNextTime();
            particleSystem.writeFrameWithDirection(writer, 1, 1);
//        }
        writer.flush();
        writer.close();
    }
}
