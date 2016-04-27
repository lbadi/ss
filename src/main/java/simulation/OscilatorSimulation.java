package simulation;

import model.Oscilator;

/**
 * Created by nicolas on 27/04/16.
 */
public class OscilatorSimulation {

    public void simulateVelret(double deltaT){
        double time = 5;
        Oscilator oscilator = new Oscilator(70, Math.pow(10,4), 100, 1);
        // ACA HABRIA QUE ESCRIBIR EL ARCHIVO PARA GRAFICAR
        double previousPosition = oscilator.getPosition();
        oscilator.makeEulerStep(deltaT);
        // ACA HABRIA QUE ESCRIBIR EL ARCHIVO PARA GRAFICAR
        time -= deltaT;
        while(time >= 0){
            oscilator.makeVerletStep(deltaT, previousPosition);
            previousPosition = oscilator.getPosition();
            // ACA HABRIA QUE ESCRIBIR EL ARCHIVO PARA GRAFICAR
            time -= deltaT;
        }
    }

    public void simulateBeeman(double deltaT){
        double time = 5;
        Oscilator oscilator = new Oscilator(70, Math.pow(10,4), 100, 1);
        // ACA HABRIA QUE ESCRIBIR EL ARCHIVO PARA GRAFICAR
        double previousAcceleration = oscilator.getAcceleration();
        oscilator.makeEulerStep(deltaT);
        // ACA HABRIA QUE ESCRIBIR EL ARCHIVO PARA GRAFICAR
        time -= deltaT;
        while(time >= 0){
            oscilator.makeBeemanStep(deltaT, previousAcceleration);
            previousAcceleration = oscilator.getAcceleration();
            // ACA HABRIA QUE ESCRIBIR EL ARCHIVO PARA GRAFICAR
            time -= deltaT;
        }
    }
}
