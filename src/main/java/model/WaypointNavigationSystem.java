package model;

import util.RandomUtils;

import java.io.PrintWriter;
import java.util.*;

public class WaypointNavigationSystem extends ParticleSystem{

    private static final double AGENT_RADIUS = 0.25;
    private static final double OBSTACLE_RADIUS = 0.25;
    private static final int INITIAL_OFFSET_RATIO = 1;
    private static final int WINDOW = 5;
    private static final double rMin = 0.15;
    private static final double rMax = 0.32;
    private static final double vMax = 1.55;
    private static final double beta = 0.9;
    private static final double tau = 0.5;
    private static final double FELEER_STEP = rMax/3;
    public double waypointSeparation;
    private List<Waypoint> waypoints = new ArrayList<>();
    private List<Obstacle> obstacles = new ArrayList<>();
    private Waypoint goal;
    private double goalX;
    private double goalY;
    private double startX;
    private double startY;
    private static final int DEPTH_LIMIT = 15;

    private List<Particle> allFeelers = new ArrayList<>();
    private Waypoint waypoint = null;

    public WaypointNavigationSystem(int dim, double startX, double startY, double waypointSeparation,
                                    double goalX, double goalY, int maxObstacles) {
        super(false, 1);
        this.waypointSeparation = waypointSeparation;
        setL(dim);
        this.startX = startX;
        this.startY = startY;
        this.goalX = goalX;
        this.goalY = goalY;
        goal = new Waypoint(goalX,goalY);
        createAgent(startX,startY);
        createObstacles(maxObstacles);
        createWaypoints();
        List<Particle> targets = new ArrayList<>();
        targets.addAll(aStar(getParticles().get(0)));
        getParticles().get(0).addTargets(targets);
    }

    private void createAgent(double startX, double startY){
        Particle particle = new Particle(startX,startY);
        particle.setRadius(AGENT_RADIUS);
        particle.setrMax(rMax);
        particle.setrMin(rMin);
        particle.setMaxSpeed(vMax);
        addParticle(particle);
    }

    private void createObstacles(int maxObstacles){
        for(int i = 0 ; i<maxObstacles; i++){
            Obstacle obstacle = new Obstacle();
            double x = RandomUtils.between(AGENT_RADIUS, getL() - AGENT_RADIUS);
            double y = RandomUtils.between(AGENT_RADIUS, getL() - AGENT_RADIUS);
            obstacle.setX(x);
            obstacle.setY(y);
            obstacle.setRadius(OBSTACLE_RADIUS);
            if(obstacle.distanceTo(goal) > waypointSeparation && obstacle.distanceTo(getParticles().get(0)) > waypointSeparation) {
                obstacles.add(obstacle);
            }
        }
    }

    private void createWaypoints(){
        Waypoint[][] waypoints = new Waypoint[(int)(getL() / waypointSeparation)][(int)(getL() / waypointSeparation)];
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

        sb.append(getN() + obstacles.size() + waypoints.size() + 4 + 2 + allFeelers.size()).append("\n");
        sb.append(timeStep).append("\n");

        writeCorners(sb);
        writeObstacles(sb);
        writeWaypoints(sb);
        writeFeelers(sb);

        sb.append(df.format(startX) + "\t" + df.format(startY) + "\t" + df.format(0.25) + "\t");
        sb.append(df.format(0.0) + "\t" + df.format(0.0) + "\t" + df.format(1.0) + "\t");
        sb.append("\n");

        sb.append(df.format(goalX) + "\t" + df.format(goalY) + "\t" + df.format(0.25) + "\t");
        sb.append(df.format(0.0) + "\t" + df.format(0.0) + "\t" + df.format(1.0) + "\t");
        sb.append("\n");

        getParticles().stream().forEach(particle ->{
            sb.append(df.format(particle.getX()) + "\t" + df.format(particle.getY()) + "\t" + df.format(particle.getRadius()) + "\t");
            sb.append(df.format(1.0) + "\t" + df.format(1.0) + "\t" + df.format(1.0) + "\t");
            sb.append("\n");
        });

        writer.write(sb.toString());
    }

    public void writeFeelers(StringBuilder sb){
        allFeelers.stream().forEach(particle ->{
            sb.append(df.format(particle.getX()) + "\t" + df.format(particle.getY()) + "\t" + df.format(particle.getRadius()) + "\t");
            sb.append(df.format(0.5) + "\t" + df.format(0.5) + "\t" + df.format(0.5) + "\t");
            sb.append("\n");
        });
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

    /**
     * Move the system using an aproximated method.
     * @param dt
     */
    public void move(double dt){
        for(Particle particle : getParticles()){
            Vector direction = new Vector(0,0);
//            for(Particle particle2 : getParticles()){
//                if(!particle.equals(particle2)){
//                    if(particle.overlap(particle2)){
//                        particle.dirty();
//                        direction.sum(getContactVelocity(particle,particle2));
//                    }
//                }
//            }

            for(Obstacle obstacle : obstacles){
                if(!particle.equals(obstacles)){
                    if(particle.overlap(obstacle)){
                        particle.dirty();
                        direction.sum(getContactVelocity(particle,obstacle));
                    }
                }
            }
            if(!particle.isDirty()) {
                if(particle.getTarget() == null){
                    direction = new Vector(0,0);
                } else {
                    direction = getVelocity(particle, particle.getTarget().getX(), particle.getTarget().getY());
                }
            }
            particle.setSpeed(direction.getModuleX(),direction.getModuleY());
        }
        refreshPosition(dt);
        refreshRadius(dt);
        refreshObjectives();
    }

    public Vector getContactVelocity(Particle particle, Particle particle2){
        return new Vector(particle.getMaxSpeed(),particle2.getNormalAngleWith(particle));
    }

    public Vector getVelocity(Particle particle,double x ,double y){
        Vector desireVelocity = getDesireVelocity(particle,x,y);
        Vector avoidVelocity = getAvoidVelocity(particle);
        desireVelocity = desireVelocity.sum(avoidVelocity);
        double module = particle.getMaxSpeed() * Math.pow((particle.getRadius() - particle.getrMin()) / (particle.getrMax() - particle.getrMin()),beta);
        desireVelocity.setModule(module);
        return desireVelocity;
    }

    public Vector getAvoidVelocity(Particle particle){
        Vector avoidDirection = getAvoidDirection(particle);
        double module;
        if(avoidDirection == null){
            return new Vector(0,0);
        }
        module = particle.getMaxSpeed() * Math.pow((particle.getRadius() - particle.getrMin()) / (particle.getrMax() - particle.getrMin()), beta);
        avoidDirection.setModule(module);
        return avoidDirection;
    }

    public Vector getAvoidDirection(Particle particle){
        Particle closeFeeler = new Particle(particle.getX(),particle.getY(),particle.getRadius() * 1.1);
        Vector avoidDirection = new Vector(0,0);
        boolean overlapAtLeastOne = false;
        for(Obstacle obstacle : obstacles){
            if(closeFeeler.overlap(obstacle)){
                overlapAtLeastOne = true;
                avoidDirection.sum(new Vector(obstacle.getX(),obstacle.getY(),particle.getX(),particle.getY()));
            }
        }
        return overlapAtLeastOne ? avoidDirection : null;
    }

    public Vector getDesireVelocity(Particle particle, double x, double y){
        Vector targetDirection = getTargetDirection(particle,x,y);
        //| vd | = vd = vd max [(r-rmin)/(r-rmax)]β
        double module = particle.getMaxSpeed() * Math.pow((particle.getRadius() - particle.getrMin()) / (particle.getrMax() - particle.getrMin()),beta);
        targetDirection.setModule(module);
        return targetDirection;
    }

    public Vector getTargetDirection(Particle particle,double x, double y){
        Vector targetDirection = new Vector(particle.getX(),particle.getY(), x, y);
        return targetDirection;
    }

    public void refreshRadius(double dt){
        for(Particle particle : getParticles()){
            if(particle.isDirty()){
                particle.setRadius(particle.getrMin());
                particle.clean();
            } else if (particle.getRadius() < particle.getrMax()){
                particle.setRadius(particle.getRadius() + (particle.getrMax() / (tau/dt)));
                if(particle.getRadius() > particle.getrMax()){
                    particle.setRadius(particle.getrMax());
                }
            }
        }
    }

    public void refreshPosition(double dt){
        for(Particle particle : getParticles()){
            particle.setX(particle.getX() + particle.getSpeedX() * dt);
            particle.setY(particle.getY() + particle.getSpeedY() * dt);
        }
    }

    public void refreshObjectives(){
        for(Particle particle : getParticles()){
            particle.reachTarget();
        }
    }

    public List<Waypoint> aStar(Particle particle){
        List<Waypoint> explored = new ArrayList<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();
        Waypoint start = closestWaypoint(particle);
        double betterCostFound = 0;
        start.getWaypointsNeighbours().stream().forEach(neighbour -> {
            pq.add(new Node(neighbour,1,goal,new Node(start,0,goal,null)));
        });
        Node node = null;
        do {
            if(pq.size()==0){
                explored.add(goal);
                break;
            }
            node = pq.poll();
            explored.add(node.getWaypoint());
            for(Waypoint neighbour : node.getWaypoint().getWaypointsNeighbours()){
                if(!explored.contains(neighbour)) {
                    if(canSeeTarget(node.getWaypoint(),neighbour)) {
                        double newCost = node.getCost() + (node.getWaypoint().distanceToCenterOf(neighbour));
                        Node newNode = new Node(neighbour, newCost, goal, node);
                        pq.add(new Node(neighbour, newCost, goal, node));
                        if(newCost > betterCostFound + DEPTH_LIMIT) {
                            betterCostFound = newCost;
                            pq.clear();
                            pq.add(newNode);
                        }
                    }
                }
            }
        } while(node.getWaypoint().distanceToCenterOf(goal) > waypointSeparation);
        Deque<Waypoint> path = new LinkedList<>();
        path.addFirst(goal);
        while(node.getParent() != null){
            path.addFirst(node.getWaypoint());
            node = node.getParent();
        }
        return new ArrayList<>(path);
    }

    public boolean canSeeTarget(Waypoint origin, Waypoint target){
        double directionAngle = origin.getNormalAngleWith(target);
        double distance = origin.distanceToCenterOf(target);
        for(double i = 0; i<= distance;i += FELEER_STEP) {
            double x = origin.getX() + i * Math.cos(directionAngle);
            double y = origin.getY() + i * Math.sin(directionAngle);
            Particle feeler = new Particle(x,y,rMax);
//            if(waypoint == null) {
//                waypoint = origin;
//                allFeelers.add(feeler);
//            } else if(waypoint.equals(origin)) {
//                allFeelers.add(feeler);
//            }
            for(Obstacle obstacle: obstacles){
                if(feeler.overlap(obstacle)){
                    return false;
                }
            }
        }
        return true;
    }

    public Waypoint closestWaypoint(Particle particle){
        Double minDistance = null;
        Waypoint closesWaypoint = null;
        for(Waypoint waypoint : waypoints){
            double actualDistance = particle.distanceToCenterOf(waypoint);
            if(minDistance == null || actualDistance < minDistance){
                minDistance = actualDistance;
                closesWaypoint = waypoint;
            }
        }
        if(closesWaypoint == null){
            throw new IllegalArgumentException();
        }
        return closesWaypoint;
    }

    public boolean hasEnded() {
        return getParticles().get(0).distanceTo(goal) < 0.05;
    }

}
