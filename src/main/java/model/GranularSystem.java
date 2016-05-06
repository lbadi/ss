package model;

import util.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 */
public class GranularSystem extends ParticleSystem{

    public double width;
    public double height;
    public double apperture;

    private static final int FALL_HEIGHT = 1;
    private static final int MAX_TRIES = 10000;
    private static final double MASS = 0.01;

    private static final double KN = Math.pow(10,5);
    private static final double KT = 2*KN;




    public GranularSystem(double width , double height, double apperture, int particleCount){
        super(false,1);
        this.setL((long)height + FALL_HEIGHT);
        this.width = width;
        this.height = height;
        this.apperture = apperture;
        double particleSize = apperture/10;
        setInteractionRadius(particleSize);
        createWalls(width,height, particleSize);
        createGrains(particleSize, particleCount);
    }

    private void createWalls(double width, double height, double size){
        List<Wall> walls = new ArrayList<>();
        walls.add(new Wall(height, 0,0 , height/2 + FALL_HEIGHT, size)); //left wall
        walls.add(new Wall(height, Math.PI,width, height/2 + FALL_HEIGHT, size));//right wall
        walls.add(new Wall(width, (3/2)*Math.PI,width/2, height + FALL_HEIGHT, size)); //top wall
        walls.add(new Wall((width - apperture) / 2, (1/2)*Math.PI,(width-apperture)/2 /2, FALL_HEIGHT,size)); //bottom left wall
        walls.add(new Wall((width - apperture) / 2, (1/2)*Math.PI,width/2 + apperture, FALL_HEIGHT,size)); //bottom right wall
        addWalls(walls);
    }

    private void createGrains(double particleSize, int particleCount) {
        double offset = particleSize * 2;
        double leftBound = 0 + offset;
        double rightBound = width - offset;
        double buttomBound = FALL_HEIGHT + particleSize*2;
        double topBound = FALL_HEIGHT+height;
        for (int i = 0; i < particleCount; i++) {
            int tries = 0;
            Particle newParticle = new Particle(RandomUtils.between(leftBound,rightBound),RandomUtils.between(buttomBound,topBound));
            newParticle.setMass(MASS);
            newParticle.setSpeed(0,0);
            newParticle.setRadius(particleSize / 2);
            boolean overlap = true;
            while ( overlap && (tries++ < MAX_TRIES)) {
                overlap=false;
                for (Particle particle : getParticles()) {
                    overlap=false;
                    if (newParticle.overlap(particle)) {
                        overlap = true;
                        newParticle.setX(RandomUtils.between(leftBound,rightBound));
                        newParticle.setY(RandomUtils.between(buttomBound , topBound));
                        break;
                    }
                }
                if(!overlap) {
                    addParticle(newParticle);
                    break;
                }
            }
            if(tries >= MAX_TRIES) {
                break;
            }
        }

    }

    public void moveEuler(double t){
        for(Particle particle : getParticles()){
            Vector force = new Vector(0,0);
            for(Particle neighbour : particle.getNeighbours()){
                if(!neighbour.equals(particle)) {
                    force.sum(getNormalForce(particle, neighbour))
                            .sum(getTangencialForce(particle, neighbour));
                }
            }
            for(Wall wall : getWalls()){
                //TODO Falta ver el overlap con los muros
            }
            Vector acceleration = new Vector(force.getModule()/particle.getMass(),force.getAngle());
            acceleration.sum(getGravityAceleration());
            particle.makeEulerStep(t, acceleration.getModule(), acceleration.getAngle());
        }
        removeOuterParticles();
    }

    //TODO hacerlo eficiente
    public void removeOuterParticles(){
        List<Particle> newParticles = new ArrayList<>();
        for(Particle particle : getParticles()){
            if(!isInBorder(particle.getX(),particle.getY())){
                newParticles.add(particle);
            }
        }
        refreshSystem(newParticles);
    }

    private Vector getGravityAceleration(){
        return new Vector(9.8,3.0/2*Math.PI);
    }

//    public void moveBeeman(double t){
//        for(Particle particle : getParticles()){
//            Vector acceleration = new Vector(gravityForce(particle)/particle.getMass(), particle.angleWith(sun));
//            particle.makeBeemanStep(t, acceleration, sun);
//            if(!isPeriodic()){
//                if(isInBorder(particle.getX() , particle.getY())){
//                    particle.markToBeRemove();
//                }
//            }
//        }
//    }

//    private void makeBeemanStep(double t, Vector acceleration){
//        Vector previousAcceleration = this.acceleration;
//        this.acceleration = acceleration;
//        double speedX = getSpeedX();
//        double speedY  = getSpeedY();
//        x = x + speedX  * t + ((2.0/3) * acceleration.getModule() * Math.cos(acceleration.getAngle()) * Math.pow(t,2)) - ((1.0/6) * previousAcceleration.getModule() * Math.cos(previousAcceleration.getAngle()) * Math.pow(t,2));
//        y = y + speedY  * t + ((2.0/3) * acceleration.getModule() * Math.sin(acceleration.getAngle()) * Math.pow(t,2)) - ((1.0/6) * previousAcceleration.getModule() * Math.sin(previousAcceleration.getAngle()) * Math.pow(t,2));
//        Vector nextAcceleration = new Vector(getSolarGravityForce(sun), angleWith(sun));
//        speedX = speedX + (1.0/3) * nextAcceleration.getModule() * Math.cos(nextAcceleration.getAngle()) * t + (5.0/6) * acceleration.getModule() * Math.cos(acceleration.getAngle()) * t - (1.0/6) * previousAcceleration.getModule() * Math.cos(previousAcceleration.getAngle()) * t;
//        speedY = speedY + (1.0/3) * nextAcceleration.getModule() * Math.sin(nextAcceleration.getAngle()) * t + (5.0/6) * acceleration.getModule() * Math.sin(acceleration.getAngle()) * t - (1.0/6) * previousAcceleration.getModule() * Math.sin(previousAcceleration.getAngle()) * t;
//        setSpeed(Math.sqrt(Math.pow(speedX, 2) + Math.pow(speedY, 2)));
//        setAngle(Math.atan2(speedY, speedX));
//    }

    private Vector getNormalForce(Particle particle, Particle particle2){
        double normalForce = -KN * (particle.getOverlap(particle2));
        double angleNormalForce = particle.getNormalVersorWith(particle2);
        return new Vector(normalForce,angleNormalForce);
    }

    private Vector getTangencialForce(Particle particle, Particle particle2){
        double tangencialSpeed = particle.getTangencialSpeedWith(particle2);
        double tangencialSpeed2 = particle2.getTangencialSpeedWith(particle);
        double tangencialRelativeSpeed = tangencialSpeed + tangencialSpeed2; //Por ahi es menos
        double tangencialForce = - KT * particle.getOverlap(particle2) * tangencialRelativeSpeed;
        double tangencialAngle = particle.tangencialWith(particle2);

        return new Vector(tangencialForce,tangencialAngle);
    }
}
