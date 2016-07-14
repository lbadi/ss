package model;

import util.RandomUtils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BehaviourSystem extends ParticleSystem{

    private static final int FALL_HEIGHT = 10;
    private static final double NODE_SIZE = 1;
    private static final double FELEER_STEP = 0.1;
    private static final double FELEER_LENGHT = 0.8;

    private static final int MAX_TRIES = 10000;
    private static final double beta = 0.9;
    private static final double tau = 0.5;

    public double apperture;

    public List<Particle> targets = new ArrayList<>();
    public ParticleDetectorWall particleDetectorWall;
    public Node[][] board;

    public BehaviourSystem(double width, double height,double apperture,int squareCount, double innerRadius,double outterRadius, int maxAgents, double vMax){
        super(false,squareCount);
        this.setL((long)height + FALL_HEIGHT);
        this.apperture = apperture;
        setInteractionRadius(outterRadius);
        createWalls(width,height);
        createAgents(outterRadius,maxAgents, width, height, innerRadius, vMax);
//        createBoard();
    }

    private void createWalls(double width, double height){
        List<Wall> walls = new ArrayList<>();
        walls.add(new Wall(0,height+FALL_HEIGHT,0,FALL_HEIGHT)); //left wall
        walls.add(new Wall(width,FALL_HEIGHT,width,FALL_HEIGHT+height));//right wall
        walls.add(new Wall(width,FALL_HEIGHT+height,0,height+FALL_HEIGHT)); //top wall
        walls.add(new Wall(0,FALL_HEIGHT,(width-apperture)/2,FALL_HEIGHT)); //bottom left wall
        walls.add(new Wall((width+apperture)/2,FALL_HEIGHT,width,FALL_HEIGHT)); //bottom right wall
        Wall trap = new Wall((width-apperture)/2 , FALL_HEIGHT ,(width+apperture)/2, FALL_HEIGHT);
        trap.setOpen(true);
        walls.add(trap);
        addWalls(walls);
        particleDetectorWall = new ParticleDetectorWall((width-apperture)/2 , FALL_HEIGHT ,(width+apperture)/2, FALL_HEIGHT);
    }

    private void createAgents(double rMax, int particleCount, double width, double
            height, double rMin, double vMax) {
        double offset = rMax * 1.01;
        double leftBound = 0 + offset;
        double rightBound = width - offset;
        double buttomBound = FALL_HEIGHT + offset;
        double topBound = FALL_HEIGHT+height - offset;
//        targets.add(new Particle(0,FALL_HEIGHT,1));
//        targets.add(new Particle(1,height+FALL_HEIGHT,1));
//        targets.add(new Particle(width-1,height-1,1));
//        targets.add(new Particle(width-1,FALL_HEIGHT+1,1));
        targets.add(new Particle(width/2.0,FALL_HEIGHT - 0.5,0.5));
        targets.add(new Particle(width/2.0,FALL_HEIGHT/2,0.5));


        for (int i = 0; i < particleCount; i++) {
            int tries = 0;
            Particle newParticle = new Particle(RandomUtils.between(leftBound,rightBound),RandomUtils.between(buttomBound,topBound));
            newParticle.setSpeed(0,0);
            newParticle.setRadius(rMax);
            newParticle.setrMax(rMax);
            newParticle.setrMin(rMin);
            newParticle.setMaxSpeed(vMax);
            Feeler feeler1 = new Feeler(new Vector(0,FELEER_LENGHT));
            Feeler feeler2 = new Feeler(new Vector(Math.PI/8,FELEER_LENGHT));
            Feeler feeler3 = new Feeler(new Vector(-Math.PI/8,FELEER_LENGHT));
            Feeler feeler4 = new Feeler(new Vector(Math.PI/4,FELEER_LENGHT));
            Feeler feeler5 = new Feeler(new Vector(-Math.PI/4,FELEER_LENGHT));

            newParticle.addFeeler(feeler1).addFeeler(feeler2).addFeeler(feeler3).addFeeler(feeler4).addFeeler(feeler5);
            if(i == 0) {
                newParticle.addTargets(targets);
            }
            boolean overlap = true;
            while (overlap && (tries++ < MAX_TRIES)) {
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





    public void writeFrameWithDirection(PrintWriter writer, int timeStep){
        StringBuilder sb = new StringBuilder();

        sb.append(getN() + targets.size() +getWalls().size() * 2 + 1).append("\n"); // 2 particulas por cada pared
        sb.append(timeStep).append("\n");
        writeCorners(sb);
        writeObjectives(sb);

        getParticles().stream().forEach(particle ->{
            sb.append(df.format(particle.getX()) + "\t" + df.format(particle.getY()) + "\t" + df.format(particle.getRadius()) + "\t");
            sb.append(df.format(1.0) + "\t" + df.format(1.0) + "\t" + df.format(1.0) + "\t");
            sb.append("\n");
        });
        writer.write(sb.toString());
    }

    public void writeObjectives(StringBuilder sb){
        targets.stream().forEach(particle ->{
            sb.append(df.format(particle.getX()) + "\t" + df.format(particle.getY()) + "\t" + df.format(particle.getRadius()) + "\t");
            sb.append(df.format(1.0) + "\t" + df.format(0.0) + "\t" + df.format(0.0) + "\t");
            sb.append("\n");
        });
    }

    public void writeCorners(StringBuilder sb){
        for(Wall wall : getWalls()){
            sb.append(wall.getX1() + "\t" + wall.getY1() + "\t" + 0.1 + "\t");
            sb.append(1.0 + "\t" + 1.0 + "\t" + 1.0 + "\t");
            sb.append("\n");

            sb.append(wall.getX2() + "\t" + wall.getY2() + "\t" + 0.1 + "\t");
            sb.append(1.0 + "\t" + 1.0 + "\t" + 1.0 + "\t");
            sb.append("\n");
        }
        sb.append(0 + "\t" + 0 + "\t" + 0.1 + "\t");
        sb.append(0.0 + "\t" + 0.0 + "\t" + 0.0 + "\t");
        sb.append("\n");

    }

    /**
     * Move the system using an aproximated method.
     * @param dt
     */
    public void move(double dt){
        for(Particle particle : getParticles()){
            Vector direction = new Vector(0,0);
            for(Particle particle2 : getParticles()){
                if(!particle.equals(particle2)){
                    if(particle.overlap(particle2)){
                        particle.dirty();
                        direction.sum(getContactVelocity(particle,particle2));
                    }
                }
            }
            for(Wall wall : getWalls()){
                if(particle.getOverlap(wall) != 0 && !wall.isOpen()){
                    particle.dirty();
                    direction.sum(getContactVelocity(particle,wall));
                }
            }
            if(!particle.isDirty()) {
                if(particle.getTarget() == null){
                    direction = new Vector(0,0);
                }else {
                    direction = getVelocity(particle, particle.getTarget().getX(), particle.getTarget().getY());
                }
            }
            particle.setSpeed(direction.getModuleX(),direction.getModuleY());
            detectCollisionWithDetector(particle);
        }
        refreshPosition(dt);
        refreshRadius(dt);
        refreshObjectives();

//        removeParticlesThatReachTheObjective();
//        removeParticlesThatReachTheObjective();

    }

    public void refreshPosition(double dt){
        for(Particle particle : getParticles()){
            particle.setX(particle.getX() + particle.getSpeedX() * dt);
            particle.setY(particle.getY() + particle.getSpeedY() * dt);
        }
    }

    public void detectCollisionWithDetector(Particle particle){
        if(!particle.isAlreadyCounted()){
            if (particle.getOverlap(particleDetectorWall) != 0){
                particle.setAlreadyCounted(true);
                particleDetectorWall.incCount();
            }
        }
    }

    public Vector getTargetDirection(Particle particle,double x, double y){
        Vector targetDirection = new Vector(particle.getX(),particle.getY(), x, y);
        return targetDirection;
    }

    public void refreshObjectives(){
        for(Particle particle : getParticles()){
            particle.reachTarget();
        }
    }

    public void removeParticlesThatReachTheObjective(){
        List<Particle> newParticles = new ArrayList<>();
        for(Particle particle : getParticles()){
            if(particle.getTarget() != null){
                newParticles.add(particle);
            }
        }
        setParticles(newParticles);
    }

    public Vector getVelocity(Particle particle,double x ,double y){
        Vector desireVelocity = getDesireVelocity(particle,x,y);
        Vector avoidVelocity = getAvoidDirection(particle);
        return desireVelocity.sum(avoidVelocity);
    }

    public Vector getDesireVelocity(Particle particle, double x, double y){
        Vector targetDirection = getTargetDirection(particle,x,y);
        //| vd | = vd = vd max [(r-rmin)/(r-rmax)]β
        double module = particle.getMaxSpeed() * Math.pow((particle.getRadius() - particle.getrMin()) / (particle.getrMax() - particle.getrMin()),beta);
        targetDirection.setModule(module);
        return targetDirection;
    }

    public Vector getAvoidDirection(Particle particle){
        Vector avoidVector = new Vector(0,0);
        for(Feeler feeler : particle.getFeelers()){
            for(Particle particle1 : getParticles()){
                if(!particle.equals(particle1)){
                    for(double i = 0; i<=1 ;i += FELEER_STEP  ) {
                        double x = particle.getX() + i * feeler.direction.getModule() * Math.cos((feeler.direction.getAngle() + particle.getAngle()));
                        double y = particle.getY() + i * feeler.direction.getModule() * Math.sin((feeler.direction.getAngle() + particle.getAngle()));
                        if(particle1.isInnerPoint(x,y)){
                            double innerLenght = (1-i) * feeler.direction.getModule(); //Cuanto estoy metido del feeler
                            double angle = - particle.getNormalAngleWith(particle1);
                            avoidVector.sum(new Vector(innerLenght,angle));
                            break;
                        }
                    }
                }
            }
        }
        return avoidVector.normalize();
    }

    public Vector getContactVelocity(Particle particle, Particle particle2){
        return new Vector(particle.getMaxSpeed(),particle2.getNormalAngleWith(particle));
    }
    public Vector getContactVelocity(Particle particle, Wall wall){
        return new Vector(particle.getMaxSpeed(),wall.getNormalAngle(particle));
    }

    public void refreshRadius(double dt){
        for(Particle particle : getParticles()){
            if(particle.isDirty()){
                particle.setRadius(particle.getrMin());
                particle.clean();
            }else if (particle.getRadius() < particle.getrMax()){
                particle.setRadius(particle.getRadius() + (particle.getrMax() / (tau/dt)));
                if(particle.getRadius() > particle.getrMax()){
                    particle.setRadius(particle.getrMax());
                }
            }
        }
    }

    public ParticleDetectorWall getParticleDetectorWall() {
        return particleDetectorWall;
    }

}
