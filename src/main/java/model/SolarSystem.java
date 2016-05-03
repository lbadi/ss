package model;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 *
 */
public class SolarSystem extends ParticleSystem{

    private static double SUN_MASS = 2 * Math.pow(10,5);
    private static double MIN_POS = Math.pow(10,3);
    private static double MAX_POS = Math.pow(10,4);
    private static double COLLAPSE_DISTANCE = 1;
    private static double SUN_RADIUS = 20;

    private static BigDecimal CONSTANT_DIST = new BigDecimal(Math.pow(10,6)); //Constantes que utilizamos para redur las distancias
    private static BigDecimal CONSTANT_MASS = new BigDecimal(Math.pow(10,25)); //Constantes que utilizamos para reducir la masa.

    private static double CONST_GRAVITY = 6.693 / Math.pow(10,4);
    private static long  L = (long)(2*Math.pow(10,4));

    Particle sun;

    public SolarSystem(long planetsQuantity){
        super(false,1, COLLAPSE_DISTANCE);
        this.setL(L);
//        this.setN(planetsQuantity+1); //+1 for the sun
        sun = new Particle(SUN_MASS);
        sun.setRadius(SUN_RADIUS); //Radio del sol
        sun.setX(L/2);
        sun.setY(L/2);
        sun.setSpeed(0);
        sun.setAngle(0);

        double planetInitialMass = SUN_MASS / planetsQuantity;
        List<Particle> celestialBodies = new ArrayList<>();
//        celestialBodies.add(sun);
        //Iniciar los planetas
        for(long i=0; i<planetsQuantity ; i++){
            Particle planet = new Particle(planetInitialMass);
            double distanceFromCenter = Math.random() * (MAX_POS - MIN_POS) + MIN_POS;
            double randomAngle = Math.random() * 2 * Math.PI;
            double x = Math.cos(randomAngle) * distanceFromCenter + sun.getX();
            double y = Math.sin(randomAngle) * distanceFromCenter + sun.getY();
            planet.setX(x);
            planet.setY(y);
            planet.setRadius(COLLAPSE_DISTANCE*2);
            double tangencialAngle = planet.tangencialWith(sun);
            planet.setAngle(tangencialAngle);
            double distanceToSun = planet.distanceToCenterOf(sun);
            double angularMoment = calculateAngularMomentFor(planetsQuantity);
            double calculatedSpeed = angularMoment / distanceToSun / planet.getMass();
            planet.setSpeed(calculatedSpeed);
            planet.setAngularMoment(angularMoment);


            celestialBodies.add(planet);
        }
        super.refreshSystem(celestialBodies);
    }

    /**
     * Plastic collision between two celestial bodies.
     *
     * @param colPlanet
     * @param colPlanet2
     * @return A particle or null if colPlanet or colPlanet2 doesnt exist when this methods was call.
     */
    public Particle plasticCollide(Particle colPlanet, Particle colPlanet2){
        if(colPlanet.isMarkToBeRemove() || colPlanet2.isMarkToBeRemove()){ //This mean that a particle doesnt exist anymore
            return null;
        }
        Particle newParcticle = new Particle();
        newParcticle.setMass(colPlanet.getMass() + colPlanet2.getMass());
        double x = (colPlanet.getX() * colPlanet.getMass() + colPlanet2.getX() * colPlanet2.getMass()) / (colPlanet2.getMass() + colPlanet.getMass());
        double y = (colPlanet.getY() * colPlanet.getMass() + colPlanet2.getY() * colPlanet2.getMass()) / (colPlanet2.getMass() + colPlanet.getMass());
        newParcticle.setX(x);
        newParcticle.setY(y);
        newParcticle.setRadius(colPlanet.getRadius() + colPlanet2.getRadius());
        double newTangencialSpeed = (colPlanet.getAngularMoment(sun) + colPlanet2.getAngularMoment(sun)) / newParcticle.distanceToCenterOf(sun) / newParcticle.getMass();
        double newNormalSpeed = (colPlanet.getNormalSpeedWith(sun) * colPlanet.getMass() + colPlanet2.getNormalSpeedWith(sun) * colPlanet2.getMass())
                / colPlanet.getMass() * colPlanet2.getMass();
        newParcticle.setSpeed(newTangencialSpeed, newNormalSpeed);
        colPlanet.markToBeRemove();
        colPlanet2.markToBeRemove();

        return newParcticle;
    }

    public void removeMarkedPlanets(){
        List<Particle> planets = new ArrayList<>();
        for(Particle planet : getParticles()){
            if(!planet.isMarkToBeRemove()){
                planets.add(planet);
            }
        }
        refreshSystem(planets);
    }

    public void collidePlanets(){
        List<Particle> newPlanets = new ArrayList<>();
        for(Particle planet : getParticles()){
            if(!collideWithSun(sun)){
                for(Particle collPlanet : planet.getNeighbours()){
                    Particle newPlanet = plasticCollide(planet, collPlanet);
                    if(newPlanet != null) {
                        newPlanets.add(newPlanet);
                    }
                    System.out.println("Colision");
                }
            }
        }
        System.out.println("Cantidad de planetas Antes : " + getParticles().size());
        removeMarkedPlanets();
        newPlanets.forEach(particle -> addParticle(particle));
    }

    public void writeFrameWithDirection(PrintWriter writer, int timeStep){
        StringBuilder sb = new StringBuilder();
        sb.append(getN() + 5).append("\n"); //El 5 es de los corners + sol
        sb.append(timeStep).append("\n");
        writeCorners(sb);

        getParticles().stream().forEach(particle ->{
            sb.append(df.format(particle.getX()) + "\t" + df.format(particle.getY()) + "\t" + df.format(particle.getRadius() * 10) + "\t");
            sb.append(df.format((Math.cos(particle.getAngle())+1)/2) + "\t" + df.format((Math.sin(particle.getAngle())+1)/2) + "\t" + df.format(0.0) + "\t");
            sb.append("\n");
        });

        sb.append(df.format(sun.getX()) + "\t" + df.format(sun.getY()) + "\t" + df.format(sun.getRadius() * 10) + "\t");
        sb.append(df.format((Math.cos(sun.getAngle())+1)/2) + "\t" + df.format((Math.sin(sun.getAngle())+1)/2) + "\t" + df.format(0.0) + "\t");
        sb.append("\n");

        writer.write(sb.toString());
    }

    public void moveEuler(double t){
        for(Particle particle : getParticles()){
            Vector acceleration = new Vector(gravityForce(particle)/particle.getMass(), particle.angleWith(sun));
            particle.makeEulerStep(t, acceleration.getModule(), acceleration.getAngle());
        }
    }

    public void moveBeeman(double t){
        for(Particle particle : getParticles()){
            Vector acceleration = new Vector(gravityForce(particle)/particle.getMass(), particle.angleWith(sun));
            particle.makeBeemanStep(t, acceleration, sun);
            if(!isPeriodic()){
                if(isInBorder(particle.getX() , particle.getY())){
                    particle.markToBeRemove();
                }
            }
        }
    }

    public double gravityForce(Particle particle){
        return (CONST_GRAVITY * SUN_MASS * particle.getMass()) / particle.distanceToCenterOf(sun);
    }

    public static double getConstGravity() {
        return CONST_GRAVITY;
    }

    public static double getSunMass() {
        return SUN_MASS;
    }

    public double getKineticEnergy(){
        double totalKineticEnergy = 0;
        for(Particle particle : getParticles()){
            totalKineticEnergy += particle.getSpeed() * particle.getMass();
        }
        return totalKineticEnergy;
    }

    public double getPotentialEnergy(){
        double totalPotentialEnergy = 0;
        for(Particle particle : getParticles()){
            totalPotentialEnergy += (particle.getMass() * sun.getMass() * getConstGravity() / particle.distanceToCenterOf(sun)) * -1;
        }
        return totalPotentialEnergy;
    }

    public void writeEnergysInTime(PrintWriter writer, double timeStep){
        StringBuilder sb = new StringBuilder();
        double kinetic = getKineticEnergy();
        double potential = getPotentialEnergy();
        sb.append(df.format(timeStep)).append(",")
                .append(df.format(kinetic)).append(",")
                .append(df.format(potential)).append(",")
                .append(df.format(kinetic+potential)).append('\n');
        writer.write(sb.toString());
    }

    public void writeEnergysHeaderInTime(PrintWriter writer){
        StringBuilder sb = new StringBuilder();
        sb.append("T").append(",")
                .append("K").append(",")
                .append("U").append(",")
                .append("K+U").append('\n');
        writer.write(sb.toString());
    }

    private static int cant = 0;
    public boolean collideWithSun(Particle particle){

        if(particle.distanceTo(sun) < COLLAPSE_DISTANCE){
            System.out.println("COLISION SOl : " + ++cant);
            particle.markToBeRemove();
            return true;
        }
        return false;
    }

    private double calculateAngularMomentFor(double planetsQuantity){
        double promDistanceToSun = (MIN_POS + MAX_POS) / 2.0;
        double initialMass = SUN_MASS / planetsQuantity;
        double mu = (SUN_MASS * initialMass) / (SUN_MASS + initialMass);
        return Math.sqrt((CONST_GRAVITY * SUN_MASS * (initialMass) / promDistanceToSun) * 2 * mu * Math.pow(promDistanceToSun, 2));
    }

}
