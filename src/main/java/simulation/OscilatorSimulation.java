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

    public void simulateVelocityVelret(double time, double deltaT, FileWriter resultWriter, FileWriter errorWriter) throws IOException {
        double simTime = 0;
        Oscilator oscilator = new Oscilator(M, Math.pow(K_BASE, K_EXP), GAMMA, INITIAL_POS);
        resultWriter.write(df.format(simTime) + "," + df.format(oscilator.getPosition()) + "\n");
        errorWriter.write(df.format(simTime) + "," + df.format(oscilator.getError(simTime)) + "\n");
        oscilator.makeEulerStep(deltaT);
        simTime += deltaT;
        resultWriter.write(df.format(simTime) + "," + df.format(oscilator.getPosition()) + "\n");
        errorWriter.write(df.format(simTime) + "," + df.format(oscilator.getError(simTime)) + "\n");
        while (simTime < time) {
            oscilator.makeVelocityVerletStep(deltaT);
            simTime += deltaT;
            resultWriter.write(df.format(simTime) + "," + df.format(oscilator.getPosition()) + "\n");
            errorWriter.write(df.format(simTime) + "," + df.format(oscilator.getError(simTime)) + "\n");
        }
    }

    public void simulateBeeman(double time, double deltaT, FileWriter resultWriter, FileWriter errorWriter) throws IOException {
        double simTime = 0;
        Oscilator oscilator = new Oscilator(M, Math.pow(K_BASE, K_EXP), GAMMA, INITIAL_POS);
        resultWriter.write(df.format(simTime) + "," + df.format(oscilator.getPosition()) + "\n");
        errorWriter.write(df.format(simTime) + "," + df.format(oscilator.getError(simTime)) + "\n");
        double previousAcceleration = oscilator.getAcceleration();
        oscilator.makeEulerStep(deltaT);
        simTime += deltaT;
        resultWriter.write(df.format(simTime) + "," + df.format(oscilator.getPosition()) + "\n");
        errorWriter.write(df.format(simTime) + "," + df.format(oscilator.getError(simTime)) + "\n");
        while (simTime < time) {
            oscilator.makeBeemanStep(deltaT, previousAcceleration);
            previousAcceleration = oscilator.getAcceleration();
            simTime += deltaT;
            resultWriter.write(df.format(simTime) + "," + df.format(oscilator.getPosition()) + "\n");
            errorWriter.write(df.format(simTime) + "," + df.format(oscilator.getError(simTime)) + "\n");
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

    public void simulateGear(double time, double deltaT, FileWriter resultWriter, FileWriter errorWriter) throws IOException {
        double simTime = 0;
        Oscilator oscilator = new Oscilator(M, Math.pow(K_BASE, K_EXP), GAMMA, INITIAL_POS);
        resultWriter.write(df.format(simTime) + "," + df.format(oscilator.getPosition()) + "\n");
        errorWriter.write(df.format(simTime) + "," + df.format(oscilator.getError(simTime)) + "\n");
        oscilator.makeEulerStep(deltaT);
        simTime += deltaT;
        resultWriter.write(df.format(simTime) + "," + df.format(oscilator.getPosition()) + "\n");
        errorWriter.write(df.format(simTime) + "," + df.format(oscilator.getError(simTime)) + "\n");
        while (simTime < time) {
            oscilator.makeGearStep(deltaT);
            simTime += deltaT;
            resultWriter.write(df.format(simTime) + "," + df.format(oscilator.getR0()) + "\n");
            errorWriter.write(df.format(simTime) + "," + df.format(oscilator.getError(oscilator.getR0(),simTime)) + "\n");
        }
    }

}