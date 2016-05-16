package model;

import util.RandomUtils;

import java.io.PrintWriter;
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

    public ParticleDetectorWall particleDetectorWall;

    private static final int FALL_HEIGHT = 1;
    private static final int INCLINATION = 5;

    private static final int MAX_TRIES = 10000;
    private static final double MASS = 0.01;

    private static final double KN = Math.pow(10,5);
    private static final double KT = 2*KN;




    public GranularSystem(double width , double height, double apperture, int particleCount, int squareCount){
        super(false,100);
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
        walls.add(new Wall(0,height+FALL_HEIGHT,0,FALL_HEIGHT)); //left wall
        walls.add(new Wall(width,FALL_HEIGHT,width,FALL_HEIGHT+height));//right wall
        walls.add(new Wall(0,height+FALL_HEIGHT,width,FALL_HEIGHT+height)); //top wall
        walls.add(new Wall(0,FALL_HEIGHT,(width-apperture)/2,FALL_HEIGHT)); //bottom left wall
        walls.add(new Wall((width+apperture)/2,FALL_HEIGHT,width,FALL_HEIGHT)); //bottom right wall
        addWalls(walls);
        particleDetectorWall = new ParticleDetectorWall((width-apperture)/2 , FALL_HEIGHT ,(width+apperture)/2, FALL_HEIGHT);
    }

    private void createGrains(double particleSize, int particleCount) {
        double offset = particleSize * 1.01;
        double leftBound = 0 + offset;
        double rightBound = width - offset;
        double buttomBound = FALL_HEIGHT + offset;
        double topBound = FALL_HEIGHT+height - offset;
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
                    for(Wall wall  : getWalls()){
                        if (newParticle.getOverlap(wall) != 0) {
                            overlap = true;
                            newParticle.setX(RandomUtils.between(leftBound,rightBound));
                            newParticle.setY(RandomUtils.between(buttomBound , topBound));
                            break;
                        }
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
            Vector nextAcceleration = nextAcceleration(particle);
            particle.setAcceleration(nextAcceleration);
        }
        makeEulerStep(t);
        removeOuterParticles();
    }

    public void makeEulerStep(double t){
        List<Particle> newParticles= new ArrayList<>();
        for(Particle particle : getParticles()){
            Particle newParticle = new Particle();
            newParticle.setMass(particle.getMass());
            newParticle.setRadius(particle.getRadius());
            newParticle.setAlreadyCounted(particle.isAlreadyCounted());
            double speedX = particle.getSpeedX();
            double speedY  = particle.getSpeedY();
            newParticle.setX(particle.getX()+ t * speedX + Math.pow(t,2) *  (particle.getAcceleration().getModuleX()) / 2);
            newParticle.setY(particle.getY()+ t * speedY + Math.pow(t,2) *  (particle.getAcceleration().getModuleY()) / 2);
            speedX =  speedX + t * particle.getAcceleration().getModuleX();
            speedY = speedY + t * particle.getAcceleration().getModuleY();
            newParticle.setSpeed(speedX,speedY);
            newParticle.setAcceleration(particle.getAcceleration());
            newParticles.add(newParticle);
        }
        setParticles(newParticles);
    }

    public Vector nextAcceleration(Particle particle){
        Vector force = new Vector(0,0);
        for(Particle neighbour : particle.getNeighbours()){
            if(!neighbour.equals(particle)) {
                force.sum(getNormalForce(particle, neighbour))
                        .sum(getTangencialForce(particle, neighbour));
            }
        }
        for(Wall wall : getWalls()){
            force.sum(getNormalForce(particle,wall))
                    .sum(getTangencialForce(particle,wall));
            //TODO Falta ver el overlap con los muros
        }
        if(!particle.isAlreadyCounted()){
            if (particle.getOverlap(particleDetectorWall) != 0){
                particle.setAlreadyCounted(true);
                particleDetectorWall.incCount();
            }
        }
        force.sum(getGravityAceleration().mult(particle.getMass()));
        Vector acceleration = new Vector(force.getModule()/particle.getMass(),force.getAngle());
        acceleration.sum(getGravityAceleration());
        return acceleration;
    }

    public void moveBeeman(double t){
        List<Particle> newParticles= new ArrayList<>();
        for(Particle particle : getParticles()){
            Particle newParticle = new Particle();
            newParticle.setMass(particle.getMass());
            newParticle.setRadius(particle.getRadius());
            newParticle.setAlreadyCounted(particle.isAlreadyCounted());
            double speedX = particle.getSpeedX();
            double speedY  = particle.getSpeedY();
            double x = particle.getX() + speedX  * t + ((2.0/3) * particle.getAcceleration().getModule() * Math.cos(particle.getAcceleration().getAngle()) * Math.pow(t,2)) - ((1.0/6) * particle.getPreviousAcceleration().getModule() * Math.cos(particle.getPreviousAcceleration().getAngle()) * Math.pow(t,2));
            double y = particle.getY() + speedY  * t + ((2.0/3) * particle.getAcceleration().getModule() * Math.sin(particle.getAcceleration().getAngle()) * Math.pow(t,2)) - ((1.0/6) * particle.getPreviousAcceleration().getModule() * Math.sin(particle.getPreviousAcceleration().getAngle()) * Math.pow(t,2));
            Vector nextAcceleration = nextAcceleration(particle);
            speedX = speedX + (1.0/3) * nextAcceleration.getModule() * Math.cos(nextAcceleration.getAngle()) * t + (5.0/6) * particle.getAcceleration().getModule() * Math.cos(particle.getAcceleration().getAngle()) * t - (1.0/6) * particle.getPreviousAcceleration().getModule() * Math.cos(particle.getPreviousAcceleration().getAngle()) * t;
            speedY = speedY + (1.0/3) * nextAcceleration.getModule() * Math.sin(nextAcceleration.getAngle()) * t + (5.0/6) * particle.getAcceleration().getModule() * Math.sin(particle.getAcceleration().getAngle()) * t - (1.0/6) * particle.getPreviousAcceleration().getModule() * Math.sin(particle.getPreviousAcceleration().getAngle()) * t;
            newParticle.setSpeed(speedX,speedY);
            newParticle.setX(x);
            newParticle.setY(y);
            newParticle.setAcceleration(nextAcceleration);
            newParticles.add(newParticle);
        }
        setParticles(newParticles);
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
        double normalForce = KN * (particle.getOverlap(particle2));
        double angleNormalForce = particle2.getNormalAngleWith(particle);
        return new Vector(normalForce,angleNormalForce);
    }

    private Vector getNormalForce(Particle particle, Wall wall){
        double normalForce = KN * (particle.getOverlap(wall));
        if(particle.getOverlap(wall) > 0){
            System.out.println();
        }
        double angleNormalForce = wall.getNormalAngle(particle);
        return new Vector(normalForce,angleNormalForce);
    }


    public Vector getTangencialForce(Particle particle, Particle particle2){
        Vector tangencialVector = particle.getTangencialVector(particle2);
        Vector relativeSpeed = particle.getSpeedAsVector().sum(particle2.getSpeedAsVector().mult(-1));
        double proyectedSpeed = relativeSpeed.scalarProductWith(tangencialVector);
        Vector tangencialForce = tangencialVector.mult(KT * particle.getOverlap(particle2) * Math.abs(proyectedSpeed));
        if(proyectedSpeed>=0){
            tangencialForce.invert();
        }
        return tangencialForce;
    }

    public Vector getTangencialForce(Particle particle, Wall wall){
        Vector tangencialVector = wall.getTangencialVector(particle);
        Vector relativeSpeed = particle.getSpeedAsVector();
        double proyectedSpeed = relativeSpeed.scalarProductWith(tangencialVector);
        Vector tangencialForce = tangencialVector.mult(KT * particle.getOverlap(wall) * Math.abs(proyectedSpeed));
        if(proyectedSpeed>=0){
            tangencialForce.invert();
        }
        return tangencialForce;
    }

    public ParticleDetectorWall getParticleDetectorWall() {
        return particleDetectorWall;
    }

    public void writeFrameWithDirection(PrintWriter writer, int timeStep){
        StringBuilder sb = new StringBuilder();

        sb.append(getN() + getWalls().size() * 2).append("\n"); // 2 particulas por cada pared
        sb.append(timeStep).append("\n");
        writeCorners(sb);

        getParticles().stream().forEach(particle ->{
            sb.append(df.format(particle.getX()) + "\t" + df.format(particle.getY()) + "\t" + df.format(particle.getRadius()) + "\t");
            sb.append(df.format((Math.cos(particle.getAngle())+1)/2) + "\t" + df.format((Math.sin(particle.getAngle())+1)/2) + "\t" + df.format(0.0) + "\t");
            sb.append("\n");
        });
        writer.write(sb.toString());
    }

    public void writeCorners(StringBuilder sb){
        for(Wall wall : getWalls()){
            sb.append(wall.getX1() + "\t" + wall.getY1() + "\t" + 1.0 + "\t");
            sb.append(1.0 + "\t" + 1.0 + "\t" + 1.0 + "\t");
            sb.append("\n");

            sb.append(wall.getX2() + "\t" + wall.getY2() + "\t" + 1.0 + "\t");
            sb.append(1.0 + "\t" + 1.0 + "\t" + 1.0 + "\t");
            sb.append("\n");
        }
    }
}
