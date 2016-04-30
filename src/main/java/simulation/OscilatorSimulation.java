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

    public void simulateVelocityVelret(double time, double deltaT, FileWriter fileWriter) throws IOException {
        double simTime = 0;
        Oscilator oscilator = new Oscilator(M, Math.pow(K_BASE, K_EXP), GAMMA, INITIAL_POS);
        fileWriter.write(df.format(simTime) + "," + df.format(oscilator.getPosition()) + "\n");
        oscilator.makeEulerStep(deltaT);
        fileWriter.write(df.format(simTime) + "," + df.format(oscilator.getPosition()) + "\n");
        simTime += deltaT;
        while (simTime < time) {
            oscilator.makeVelocityVerletStep(deltaT);
            fileWriter.write(df.format(simTime) + "," + df.format(oscilator.getPosition()) + "\n");
            simTime += deltaT;
        }
    }

    public void simulateBeeman(double time, double deltaT, FileWriter fileWriter) throws IOException {
        double simTime = 0;
        Oscilator oscilator = new Oscilator(M, Math.pow(K_BASE, K_EXP), GAMMA, INITIAL_POS);
        fileWriter.write(df.format(simTime) + "," + df.format(oscilator.getPosition()) + "\n");
        double previousAcceleration = oscilator.getAcceleration();
        oscilator.makeEulerStep(deltaT);
        fileWriter.write(df.format(simTime) + "," + df.format(oscilator.getPosition()) + "\n");
        simTime += deltaT;
        while (simTime < time) {
            oscilator.makeBeemanStep(deltaT, previousAcceleration);
            previousAcceleration = oscilator.getAcceleration();
            fileWriter.write(df.format(simTime) + "," + df.format(oscilator.getPosition()) + "\n");
            simTime += deltaT;
        }
    }

    public void simulateAnalytic(double time, double deltaT, FileWriter fileWriter) throws IOException {
        double simTime = 0;
        Oscilator oscilator = new Oscilator(M, Math.pow(K_BASE, K_EXP), GAMMA, INITIAL_POS);
        while (simTime < time) {
            fileWriter.write(df.format(simTime) + "," + df.format(oscilator.getAnalyticPosition(simTime)) + "\n");
            simTime += deltaT;
        }
    }

    public void simulateGear(double time, double deltaT, FileWriter fileWriter) throws IOException {
        double simTime = 0;
        Oscilator oscilator = new Oscilator(M, Math.pow(K_BASE, K_EXP), GAMMA, INITIAL_POS);
        fileWriter.write(df.format(simTime) + "," + df.format(oscilator.getPosition()) + "\n");
        oscilator.makeEulerStep(deltaT);
        fileWriter.write(df.format(simTime) + "," + df.format(oscilator.getPosition()) + "\n");
        simTime += deltaT;
        while (simTime < time) {
            oscilator.makeGearStep(deltaT);
            fileWriter.write(df.format(simTime) + "," + df.format(oscilator.getR0()) + "\n");
            simTime += deltaT;
        }
    }

}