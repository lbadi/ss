package model;

import util.RandomUtils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WaypointNavigationSystem extends ParticleSystem{

    private static final double AGENT_RADIUS = 0.25;
    private static final double OBSTACLE_RADIUS = 0.25;
    private static final int X = 10;
    private static final int Y = 10;
    private static final int INITIAL_OFFSET_RATIO = 1;
    private static final int WINDOW = 3;


    public double waypointSeparation;
    private List<Waypoint> waypoints = new ArrayList<>();
    private List<Obstacle> obstacles = new ArrayList<>();
    double goalX;
    double goalY;
    double startX;
    double startY;

    public WaypointNavigationSystem(double startX, double startY, double waypointSeparation,
                                    double goalX, double goalY, int maxObstacles) {
        super(false, 1);
        this.waypointSeparation = waypointSeparation;
        setL(X);
        this.startX = startX;
        this.startY = startY;
        this.goalX = goalX;
        this.goalY = goalY;
        createAgent(startX,startY);
        createObstacles(maxObstacles);
        createWaypoints();
    }

    private void createAgent(double startX, double startY){
        Particle particle = new Particle(startX,startY);
        particle.setRadius(AGENT_RADIUS);
        addParticle(particle);
    }

    private void createObstacles(int maxObstacles){
        for(int i = 0 ; i<maxObstacles; i++){
            Obstacle obstacle = new Obstacle();
            double x = RandomUtils.between(INITIAL_OFFSET_RATIO * OBSTACLE_RADIUS,X-INITIAL_OFFSET_RATIO * OBSTACLE_RADIUS);
            double y = RandomUtils.between(0,Y);
            obstacle.setX(x);
            obstacle.setY(y);
            obstacle.setRadius(OBSTACLE_RADIUS);
            obstacles.add(obstacle);
        }
    }

    private void createWaypoints(){
        Waypoint[][] waypoints = new Waypoint[(int)(X / waypointSeparation)][(int)(Y / waypointSeparation)];
        for(int i = 0; i < waypoints.length; i++){
            for(int j = 0; j< waypoints[0].length; j++){
                double x = waypointSeparation * (i+1) - waypointSeparation/2;
                double y = waypointSeparation * (j+1) - waypointSeparation/2;
                waypoints[i][j] = new Waypoint(x,y);
            }
        }
        for(int i = 0; i < waypoints.length; i++){
            for(int j = 0; j< waypoints[0].length; j++){
                for (int x = -WINDOW / 2; x <= WINDOW / 2; x++) {
                    for (int y = -WINDOW / 2; y <= WINDOW / 2; y++) {
                        if (x + i >= 0 && x + i < waypoints.length && y + j >= 0 && y + j < waypoints[0].length) {
                            waypoints[i][j].addNeighbour(waypoints[x + i][y + j]);
                        }
                    }
                }
            }
        }

        for(int i = 0; i < waypoints.length; i++){
            for(int j = 0; j< waypoints[0].length; j++){
                this.waypoints.add(waypoints[i][j]);
            }
        }
    }

    public void writeFrameWithDirection(PrintWriter writer, int timeStep){
        StringBuilder sb = new StringBuilder();

        sb.append(getN() + obstacles.size() + waypoints.size() + 4 + 2).append("\n");
        sb.append(timeStep).append("\n");
        writeCorners(sb);
        writeObstacles(sb);
        writeWaypoints(sb);



        sb.append(df.format(startX) + "\t" + df.format(startY) + "\t" + df.format(0.1) + "\t");
        sb.append(df.format(0.0) + "\t" + df.format(0.0) + "\t" + df.format(1.0) + "\t");
        sb.append("\n");

        sb.append(df.format(goalX) + "\t" + df.format(goalY) + "\t" + df.format(0.1) + "\t");
        sb.append(df.format(0.0) + "\t" + df.format(0.0) + "\t" + df.format(1.0) + "\t");
        sb.append("\n");

        getParticles().stream().forEach(particle ->{
            sb.append(df.format(particle.getX()) + "\t" + df.format(particle.getY()) + "\t" + df.format(particle.getRadius()) + "\t");
            sb.append(df.format(1.0) + "\t" + df.format(1.0) + "\t" + df.format(1.0) + "\t");
            sb.append("\n");
        });

        writer.write(sb.toString());
    }

    public void writeObstacles(StringBuilder sb){
        obstacles.stream().forEach(particle ->{
            sb.append(df.format(particle.getX()) + "\t" + df.format(particle.getY()) + "\t" + df.format(particle.getRadius()) + "\t");
            sb.append(df.format(1.0) + "\t" + df.format(0.0) + "\t" + df.format(0.0) + "\t");
            sb.append("\n");
        });
    }

    public void writeWaypoints(StringBuilder sb){
        waypoints.stream().forEach(particle ->{
            sb.append(df.format(particle.getX()) + "\t" + df.format(particle.getY()) + "\t" + df.format(0.1) + "\t");
            sb.append(df.format(0.0) + "\t" + df.format(0.5) + "\t" + df.format(0.5) + "\t");
            sb.append("\n");
        });
    }




}
