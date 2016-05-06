package model;

import util.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class GranularSystem extends ParticleSystem{

    public double width;
    public double height;
    public double apperture;

    private static final int FALL_HEIGHT = 1;
    private static final int MAX_TRIES = 10000;
    private static final double MASS = 0.01;



    public GranularSystem(double width , double height, double apperture, int particleCount){
        super(false,1);
        this.width = width;
        this.height = height;
        this.apperture = apperture;
        double particleSize = apperture/10;
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
}
