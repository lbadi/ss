package model;

import util.RandomUtils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/*
 */
public class BehaviourSystem extends ParticleSystem{

    private static final int FALL_HEIGHT = 1;

    private static final int MAX_TRIES = 10000;
    private static final double MASS = 0.01;

    public double apperture;


    public BehaviourSystem(double width, double height,double apperture,int squareCount, double innerRadius,double outterRadius, int maxAgents){
        super(false,squareCount);
        this.setL((long)height + FALL_HEIGHT);
        this.apperture = apperture;
        setInteractionRadius(outterRadius);

        createWalls(width,height);
        createAgents(outterRadius,maxAgents, width, height, innerRadius);


    }


    private void createWalls(double width, double height){
        List<Wall> walls = new ArrayList<>();
        walls.add(new Wall(0,height+FALL_HEIGHT,0,FALL_HEIGHT)); //left wall
        walls.add(new Wall(width,FALL_HEIGHT,width,FALL_HEIGHT+height));//right wall
        walls.add(new Wall(0,height+FALL_HEIGHT,width,FALL_HEIGHT+height)); //top wall
        walls.add(new Wall(0,FALL_HEIGHT,(width-apperture)/2,FALL_HEIGHT)); //bottom left wall
        walls.add(new Wall((width+apperture)/2,FALL_HEIGHT,width,FALL_HEIGHT)); //bottom right wall
        Wall trap = new Wall((width-apperture)/2 , FALL_HEIGHT ,(width+apperture)/2, FALL_HEIGHT);
        trap.setOpen(true);
        walls.add(trap);
        addWalls(walls);
//        particleDetectorWall = new ParticleDetectorWall((width-apperture)/2 , FALL_HEIGHT ,(width+apperture)/2, FALL_HEIGHT);
    }

    private void createAgents(double rMax, int particleCount, double width, double
                              height, double rMin) {
        double offset = rMax * 1.01;
        double leftBound = 0 + offset;
        double rightBound = width - offset;
        double buttomBound = FALL_HEIGHT + offset;
        double topBound = FALL_HEIGHT+height - offset;
        for (int i = 0; i < particleCount; i++) {
            int tries = 0;
            Particle newParticle = new Particle(RandomUtils.between(leftBound,rightBound),RandomUtils.between(buttomBound,topBound));
            newParticle.setMass(MASS);
            newParticle.setSpeed(0,0);
            newParticle.setRadius(rMax);
            newParticle.setrMax(rMax);
            newParticle.setrMin(rMin);
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

    public void writeFrameWithDirection(PrintWriter writer, int timeStep){
        StringBuilder sb = new StringBuilder();

        sb.append(getN() + getWalls().size() * 2 + 1).append("\n"); // 2 particulas por cada pared
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
     * @param t
     */
    public void move(double t){
        for(Particle particle : getParticles()){
            Vector direction = getTargetDirection(particle,1,0);
            particle.setSpeed(direction.getModuleX(),direction.getModuleY());
            particle.setX(particle.getX() + particle.getSpeedX() * t);
            particle.setY(particle.getY() + particle.getSpeedY() * t);
        }
    }

    public Vector getTargetDirection(Particle particle,double x, double y){
        Vector targetDirection = new Vector(particle.getX(),particle.getY(), x, y);
        return targetDirection;
    }


}
