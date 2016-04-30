package model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SolarSystem extends ParticleSystem{

    private static double SUN_MASS = 2 * Math.pow(10,5);
    private static double MIN_POS = Math.pow(10,3);
    private static double MAX_POS = Math.pow(10,4);
    private static double COLLAPSE_DISTANCE = 1;
    private static double SUN_RADIUS = 1000;

    private static BigDecimal CONSTANT_DIST = new BigDecimal(Math.pow(10,6)); //Constantes que utilizamos para redur las distancias
    private static BigDecimal CONSTANT_MASS = new BigDecimal(Math.pow(10,25)); //Constantes que utilizamos para reducir la masa.

    private static double CONST_GRAVITY = 6.693 / Math.pow(10,4);

    private static long L = 2*(long)Math.pow(10,4);

    Particle sun;

    public SolarSystem(long planetsQuantity, double AngularMoment){
        super(false,1, COLLAPSE_DISTANCE);
        this.setL(L);
        this.setN(planetsQuantity+1); //+1 for the sun
        sun = new Particle(SUN_MASS);
        sun.setRadius(SUN_RADIUS); //Radio del sol
        sun.setX(L/2);
        sun.setY(L/2);
        sun.setSpeed(0);
        sun.setAngle(0);

        double planetInitialMass = SUN_MASS / planetsQuantity;
        List<Particle> celestialBodies = new ArrayList<>();
        celestialBodies.add(sun);
        //Iniciar los planetas
        for(long i=0; i<planetsQuantity ; i++){
            Particle planet = new Particle(planetInitialMass);
            double distanceFromCenter = Math.random() * (MAX_POS - MIN_POS) + MIN_POS;
            double randomAngle = Math.random() * 2 * Math.PI;
            double x = Math.cos(randomAngle) * distanceFromCenter + sun.getX();
            double y = Math.sin(randomAngle) * distanceFromCenter + sun.getY();
            planet.setX(x);
            planet.setY(y);
            planet.setRadius(COLLAPSE_DISTANCE * 100);
            double distanceToSun = planet.distanceTo(sun,L);
            double calculatedSpeed = AngularMoment / distanceToSun;
            planet.setSpeed(calculatedSpeed);
            double tangencialAngle = planet.tangencialWith(sun);
            planet.setAngle(tangencialAngle);
            if(Math.random() > 0.5){
                planet.inverseDirection();
            }

            celestialBodies.add(planet);
        }
        super.refreshSystem(celestialBodies);
    }

}
