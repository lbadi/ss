package model;

import java.io.PrintWriter;
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
    private static double SUN_RADIUS = 10;

    private static BigDecimal CONSTANT_DIST = new BigDecimal(Math.pow(10,6)); //Constantes que utilizamos para redur las distancias
    private static BigDecimal CONSTANT_MASS = new BigDecimal(Math.pow(10,25)); //Constantes que utilizamos para reducir la masa.

    private static double CONST_GRAVITY = 6.693 / Math.pow(10,4);

    Particle sun;

    public SolarSystem(long planetsQuantity, double angularMoment, long l){
        super(false,1, COLLAPSE_DISTANCE);
        this.setL(l);
//        this.setN(planetsQuantity+1); //+1 for the sun
        sun = new Particle(SUN_MASS);
        sun.setRadius(SUN_RADIUS); //Radio del sol
        sun.setX(l/2);
        sun.setY(l/2);
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
            planet.setRadius(COLLAPSE_DISTANCE);
            double distanceToSun = planet.distanceTo(sun,l);
            double calculatedSpeed = angularMoment / distanceToSun;
            planet.setSpeed(calculatedSpeed);
            planet.setAngularMoment(angularMoment);
            double tangencialAngle = planet.tangencialWith(sun);
            planet.setAngle(tangencialAngle);
            if(Math.random() > 0.5){
                planet.inverseDirection();
            }

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
        double newSpeed = (colPlanet.getAngularMoment() + colPlanet2.getAngularMoment()) / newParcticle.distanceTo(sun);
        newParcticle.setSpeed(newSpeed);
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
            for(Particle collPlanet : planet.getNeighbours()){
                Particle newPlanet = plasticCollide(planet,collPlanet);
                if(newPlanet != null) {
                    newPlanets.add(newPlanet);
                }
                System.out.println("Colision");
            }
        }
        System.out.println("Cantidad de planetas Antes : " + getParticles().size());
        removeMarkedPlanets();
        newPlanets.forEach(particle -> addParticle(particle));
    }

    public void writeFrameWithDirection(PrintWriter writer, int timeStep){
        StringBuilder sb = new StringBuilder();
        sb.append(getN() + 4).append("\n"); //El 4 es de los corners
        sb.append(timeStep).append("\n");
        writeCorners(sb);

        getParticles().stream().forEach(particle ->{
            sb.append(df.format(particle.getX()) + "\t" + df.format(particle.getY()) + "\t" + df.format(particle.getRadius() * 10) + "\t");
            sb.append(df.format((Math.cos(particle.getAngle())+1)/2) + "\t" + df.format((Math.sin(particle.getAngle())+1)/2) + "\t" + df.format(0.0) + "\t");
            sb.append("\n");
        });
        writer.write(sb.toString());
    }

}
