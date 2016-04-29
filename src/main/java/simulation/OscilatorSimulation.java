package simulation;

import model.Oscilator;
import util.PrintFormatter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class OscilatorSimulation {

    private static final int M = 70;
    private static final int K_BASE = 10;
    private static final int K_EXP = 4;
    private static final int GAMMA = 100;
    private static final int INITIAL_POS = 1;

    private DecimalFormat df = new PrintFormatter().getDf();

    public void simulateVelret(double time, double deltaT, FileWriter fileWriter) throws IOException {
        Oscilator oscilator = new Oscilator(M, Math.pow(K_BASE,K_EXP), GAMMA, INITIAL_POS);
        fileWriter.write(df.format(time) + "," + df.format(oscilator.getPosition()) + "\n");
        double previousPosition = oscilator.getPosition();
        oscilator.makeEulerStep(deltaT);
        fileWriter.write(df.format(time) + "," + df.format(oscilator.getPosition()) + "\n");
        time -= deltaT;
        while(time >= 0){
            oscilator.makeVerletStep(deltaT, previousPosition);
            previousPosition = oscilator.getPosition();
            fileWriter.write(df.format(time) + "," + df.format(oscilator.getPosition()) + "\n");
            time -= deltaT;
        }
    }

    public void simulateBeeman(double time, double deltaT, FileWriter fileWriter) throws IOException {
        Oscilator oscilator = new Oscilator(M, Math.pow(K_BASE,K_EXP), GAMMA, INITIAL_POS);
        fileWriter.write(df.format(time) + "," + df.format(oscilator.getPosition()) + "\n");
        double previousAcceleration = oscilator.getAcceleration();
        oscilator.makeEulerStep(deltaT);
        fileWriter.write(df.format(time) + "," + df.format(oscilator.getPosition()) + "\n");
        time -= deltaT;
        while(time >= 0){
            oscilator.makeBeemanStep(deltaT, previousAcceleration);
            previousAcceleration = oscilator.getAcceleration();
            fileWriter.write(df.format(time) + "," + df.format(oscilator.getPosition()) + "\n");
            time -= deltaT;
        }
    }
}
