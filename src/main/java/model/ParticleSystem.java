package model;

import util.PlainWritable;
import util.PrintFormatter;
import util.RandomUtils;

import java.awt.*;
import java.io.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.logging.Logger;

public class ParticleSystem implements PlainWritable {

    private double l;
    private List<Particle> particles = new ArrayList<>();
    private List<Wall> walls = new ArrayList<>();
    private double squareSize;
    private boolean isPeriodic;
    private double maxRadius = 0;

    private int squareCount;
    private double interactionRadius;

    private List<Particle>[][] neighbourhood;

    private NumberFormat df = new PrintFormatter().getDf();

    public Particle colParticle1;
    public Particle colParticle2;
    public BorderDirection borderDirection;

    public ParticleSystem(boolean isPeriodic, int squareCount){
        this.squareCount = squareCount;
        this.isPeriodic = isPeriodic;
    }

    public ParticleSystem(boolean isPeriodic, int squareCount, double interactionRadius) {
        this(isPeriodic,squareCount);
        this.interactionRadius = interactionRadius;
    }

    /**
     * Construct and initialize a particle system. This method will create N particles in a grid of size L
     * @param isPeriodic periodic grid
     * @param squareCount count of square in a grid
     * @param interactionRadius interaction radius
     * @param l size of the grid
     * @param n amount of particles
     */
    public ParticleSystem(boolean isPeriodic, int squareCount, double interactionRadius, double l, long n){
        this(isPeriodic, squareCount,interactionRadius);
        this.l = l;
        init();
        //Create N particles
        for(int i = 0; i < n; i++){
            Particle particle = new Particle(Math.random() * l, Math.random() * l);
            addParticleInNeighbourHood(particle);
            particles.add(particle);
        }
    }

    /**
     * Construct and initialize a particle system. This method will create N particles in a grid of size L
     * @param squareCount count of square in a grid
     * @param radius radius of a particle
     * @param l size of the grid
     * @param n amount of particles
     * @param walls list of walls (not the borders, the borders are implicit)
     */
    public ParticleSystem(int squareCount, double radius, double l, long n, List<Wall> walls, double maxSpeed, double minSpeed){
        this(false, squareCount);
        this.l = l;
        addWalls(walls);

        addBigParticle();

        //Create N particles that not overlap with other particles or wall
        for(int i = 0; i < n; i++){
            Particle newParticle = new Particle();
            newParticle.setRadius(radius);
            newParticle.setX(RandomUtils.between(radius, l - radius));
            newParticle.setY(RandomUtils.between(radius, l - radius));
            newParticle.setSpeed(RandomUtils.between(minSpeed, maxSpeed), RandomUtils.between(minSpeed, maxSpeed));
            boolean overlap = true;
            while(overlap) {
                overlap = false;
                if(overlapWithBorders(newParticle)){
                    newParticle.setX(RandomUtils.between(radius, l - radius));
                    newParticle.setY(RandomUtils.between(radius, l - radius));
                    overlap = true;
                }
                for(Particle particle :  particles) {
                    if (newParticle.overlap(particle)) {
                        newParticle.setX(RandomUtils.between(radius, l - radius));
                        newParticle.setY(RandomUtils.between(radius, l - radius));
                        overlap = true;
                    }
                }
                for(Wall wall : walls){
                    if (newParticle.overlap(wall)) {
                        newParticle.setX(RandomUtils.between(radius, l - radius));
                        newParticle.setY(RandomUtils.between(radius, l - radius));
                        overlap = true;
                    }
                }
            }
            addParticle(newParticle);
        }
        Logger.getAnonymousLogger().info("Sistema creado correctamente.");
    }

    private void addBigParticle() {
        Particle bigParticle = new Particle();
        bigParticle.setX(RandomUtils.between(Brownian.BROWNIAN_R2, l - Brownian.BROWNIAN_R2));
        bigParticle.setY(RandomUtils.between(Brownian.BROWNIAN_R2, l - Brownian.BROWNIAN_R2));
        bigParticle.setRadius(Brownian.BROWNIAN_R2);
        bigParticle.setMass(Brownian.BROWNIAN_M2) ;
        bigParticle.setSpeed(Brownian.BROWNIAN_V2);
        addParticle(bigParticle);
    }

    public void init(){
        neighbourhood = new ArrayList[squareCount][squareCount];
        for(int i = 0 ; i<squareCount; i++){
            for(int j = 0 ; j<squareCount; j++){
                neighbourhood[i][j] = new ArrayList<>();
            }
        }
        squareSize = l / squareCount;
        if(squareSize <= interactionRadius + 2 * maxRadius){
            throw new IllegalArgumentException();
        }
    }

    /**
     * Estático:
     N      (Heading con el Nro. total de Partículas)
     L      (Longitud del lado del área de simulación)
     r1 c1  (radio y color de la partícula 1)
     r2 c2  (radio y color de la partícula 2)
     ....
     rN cN  (radio y color de la partícula N)
     * @param plainObject
     * @return
     */
    @Override
    public PlainWritable readObject(String plainObject) {
        Scanner scanner = new Scanner(plainObject);
//        setN(Long.parseLong(scanner.next()));
        setL(Long.parseLong(scanner.next()));
        List<Particle> particles = new ArrayList<>();
        while (scanner.hasNext()) {
            double radius = Double.parseDouble(scanner.next());
            double color = Double.parseDouble(scanner.next());
            if(radius > maxRadius){
                maxRadius = radius;
            }
            Particle particle = new Particle(radius, color);
            particles.add(particle);
        }
        setParticles(particles);
        return this;
    }

    @Override
    public String writeObject() {
        StringBuilder sb = new StringBuilder();
        for(Particle particle : particles) {
            boolean atLeastOne = false;
            sb.append(particle.getId() + " ");
            for(Particle neighbour : particle.getNeighbours()) {
                sb.append(neighbour.getId() + ",");
                atLeastOne = true;
            }
            if(atLeastOne) {
                sb.deleteCharAt(sb.lastIndexOf(","));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public ParticleSystem readDynamic(String plainDynamic){
        init();
        Scanner scanner = new Scanner(plainDynamic);
        Iterator<Particle> it = particles.iterator();
        scanner.nextLine(); //Ignoramos la primer linea de tiempo
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
//            String[] words = line.split("\\t*\\s*");

//            try {
//                line = line.substring(1);
//
//            } catch (Exception ex) {
//                //TODO: Hacerlo mejor
//                line = line.substring(3);
//            }
//            String[] words = line.split("\\W+");
            if(false){
                //Es el separador de tiempo
            }else{
                Particle particle = it.next();
                //Leer particula
                particle.readObject(line);
                addParticleInNeighbourHood(particle);
            }
        }
        return this;
    }

    public long getN() {
        return particles.size();
    }

    public double getL() {
        return l;
    }

    public void setL(long l) {
        this.l = l;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void setParticles(List<Particle> particles) {
        this.particles = particles;
    }

    public void addParticleInNeighbourHood(Particle particle){
        int x = (int)Math.floor(particle.getX() / squareSize);
        int y = (int)Math.floor(particle.getY() / squareSize);

        if(isInBorder(x,y)){
            return;
        }
        neighbourhood[x][y].add(particle);
    }

    public List<Particle>[][] getGrid() {
        return neighbourhood;
    }

    public void populateNeighbourhood(){
        for(int i = 0 ; i<squareCount; i++){
            for(int j = 0 ; j<squareCount; j++){
                for(Particle particle : neighbourhood[i][j]){
                    visitNeighbourhood(particle, i, j);
                }
            }
        }
    }

    private void visitNeighbourhood(Particle particle, int i, int j){
        visitHouse(particle,i,j);
        visitHouse(particle,i,j+1);
        visitHouse(particle,i-1,j+1);
        visitHouse(particle,i-1,j);
        visitHouse(particle,i-1,j-1);
    }

    private boolean isInBorder(int i, int j){
        return ( i == -1 || j == -1 || i >= squareCount || j >= squareCount);
    }

    public boolean isInBorder(double x, double y){
        return (x>= l || x< 0 || y>=l || y<0);
    }

    private void visitHouse(Particle particle, int i, int j){
        if(!isPeriodic && isInBorder(i,j)){
            return;
        }
        if(i == -1){
            i = squareCount-1;
        }else if(i==squareCount){
            i = 0;
        }
        if(j == -1){
            j = squareCount-1;
        }else if(j==squareCount){
            j = 0;
        }
        for(Particle potentialNeighbour : neighbourhood[i][j]){
            if(!potentialNeighbour.equals(particle) && !potentialNeighbour.getNeighbours().contains(particle)) {
                if (particle.isNeighbour(potentialNeighbour,interactionRadius,l)) {
                    particle.addNeighbour(potentialNeighbour);
                }
            }
        }
    }

    public int getSquareCount() {
        return squareCount;
    }

    public double getInteractionRadius() {
        return interactionRadius;
    }

    public double getSquareSize() {
        return squareSize;
    }

    public List<Particle>[][] getNeighbourhood() {
        return neighbourhood;
    }

    public void writeFrame(PrintWriter writer, int id, int timeStep){

        Particle selectedParticle = this.getParticles().get(id);
        Color color = new Color(255,0,0); //Default color
        Color highlightedColor = new Color(0,255,0); //Highlighted color
        Color neighbourColor = new Color(0, 124,0);
        StringBuilder sb = new StringBuilder();
        sb.append(getN() + 4).append("\n"); //El 4 es de los corners
        sb.append(timeStep).append("\n");
        writeCorners(sb);
        particles.stream().forEach(particle ->{
            sb.append(df.format(particle.getX()) + "\t" + df.format(particle.getY()) + "\t" + df.format(particle.getRadius()) + "\t");
            if(particle.equals(selectedParticle)){
                //PINTAR
                sb.append(df.format(highlightedColor.getRed()/255.0) + "\t" + df.format(highlightedColor.getGreen()/255.0) + "\t" + df.format(highlightedColor.getBlue()/255.0) + "\t");
            }
            else if(selectedParticle.getNeighbours().contains(particle)){
                //Pintar
                sb.append(df.format(neighbourColor.getRed()/255.0) + "\t" + df.format(neighbourColor.getGreen()/255.0) + "\t" + df.format(neighbourColor.getBlue()/255.0) + "\t");

            }else{
                sb.append(df.format(color.getRed()/255.0) + "\t" + df.format(color.getGreen()/255.0) + "\t" + df.format(color.getBlue()/255.0) + "\t");
            }
            sb.append("\n");
        });
        writer.write(sb.toString());
    }

    public void writeFrameWithDirection(PrintWriter writer, int timeStep){
        StringBuilder sb = new StringBuilder();
        sb.append(getN() + 4).append("\n"); //El 4 es de los corners
        sb.append(timeStep).append("\n");
        writeCorners(sb);

        particles.stream().forEach(particle ->{
            sb.append(df.format(particle.getX()) + "\t" + df.format(particle.getY()) + "\t" + df.format(particle.getRadius()) + "\t");
            sb.append(df.format((Math.cos(particle.getAngle())+1)/2) + "\t" + df.format((Math.sin(particle.getAngle())+1)/2) + "\t" + df.format(0.0) + "\t");
            sb.append("\n");
        });
        writer.write(sb.toString());
    }

    private void writeCorners(StringBuilder sb){
        sb.append(0 + "\t" + 0 + "\t" + 0.01 + "\t");
        sb.append(0.0 + "\t" + 0.0 + "\t" + 0.0 + "\t");
        sb.append("\n");

        sb.append(getL() + "\t" + getL() + "\t" + 0.01 + "\t");
        sb.append(0.0 + "\t" + 0.0 + "\t" + 0.0 + "\t");
        sb.append("\n");

        sb.append(getL() + "\t" + 0 + "\t" + 0.01 + "\t");
        sb.append(0.0 + "\t" + 0.0 + "\t" + 0.0 + "\t");
        sb.append("\n");

        sb.append(0 + "\t" + getL() + "\t" + 0.01 + "\t");
        sb.append(0.0 + "\t" + 0.0 + "\t" + 0.0 + "\t");
        sb.append("\n");
    }

    public void writeVisualization(String filename, int id, int timeStep) throws IOException {
        //PrintWriter writer = new PrintWriter(filename);
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
        writeFrame(writer,id,timeStep);
        writer.flush();
        writer.close();
    }

    public void refreshSystem(List<Particle> newParticles){
        setParticles(newParticles);
        init();
        for(Particle particle : getParticles()){
            addParticleInNeighbourHood(particle);
        }
    }

    public double getOrder(){

        double sumSpeedX = 0;
        double sumSpeedY = 0;
        for(Particle particle : getParticles()){
            sumSpeedX += Math.cos(particle.getAngle()) * particle.getSpeed();
            sumSpeedY += Math.sin(particle.getAngle()) * particle.getSpeed();
        }

        double sumSpeed = Math.sqrt(Math.pow(sumSpeedX,2) + Math.pow(sumSpeedY,2));
        double order = sumSpeed / getN() / Particle.DEFAULT_SPEED;

        return order;
    }
    public void addWalls(List<Wall> walls){
        this.walls.addAll(walls);
    }

    public void addParticle(Particle particle){
        particles.add(particle);
    }

    private boolean overlapWithBorders(Particle particle){
        if((particle.getX() - particle.getRadius()) <= 0 || (particle.getX() + particle.getRadius()) >= l ){
            return true;
        }
        if((particle.getY() - particle.getRadius()) <= 0 || (particle.getY() + particle.getRadius()) >= l ){
            return true;
        }
        return false;
    }

    private double timeToBorder(Particle particle, double t){
        double calculatedT;
        if(new Double(particle.getSpeedX()).compareTo(0D) == 0) {
            calculatedT = 0;
        } else if(particle.getSpeedX() > 0){
            calculatedT = (l - (particle.getX() + particle.getRadius())) / particle.getSpeedX();
        } else {
            calculatedT = -1 * (particle.getX() - particle.getRadius()) / particle.getSpeedX();
            if(calculatedT < 0 ){
                calculatedT = 0;
            }
        }

        if(t > calculatedT){
            borderDirection = BorderDirection.VERTICAL;
            colParticle1 = particle;
            colParticle2 = null;
            t = calculatedT;
        }
        if(new Double(particle.getSpeedY()).compareTo(0D) == 0) {
            calculatedT = 0;
        } else if(particle.getSpeedY() > 0){
            calculatedT = (l - (particle.getY() + particle.getRadius())) / particle.getSpeedY();
        } else {
            calculatedT = -1 * (particle.getY() - particle.getRadius()) / particle.getSpeedY();
            if(calculatedT < 0 ){
                calculatedT = 0;
            }
        }
        if(t > calculatedT){
            borderDirection = BorderDirection.HORIZONTAL;
            colParticle1 = particle;
            colParticle2 = null;
            t = calculatedT;
        }
        return t;
    }

    /**
     * Inf if DV * DR > 0
     * Inf if d<0
     * -DV * DR + sqrt(d) / DV * DV
     * @return
     */
    public double timeToNextColision(){
        Double t = Double.MAX_VALUE;
        for(int i = 0 ; i < getParticles().size() ; i++){
            Particle particle = getParticles().get(i);
            Double calculatedTime = timeToBorder(particle, t);
            if(t > calculatedTime){
                t = calculatedTime;
            }
            for(int j = i+1 ; j < getParticles().size(); j++){
                Particle particle2 = getParticles().get(j);
                if(!particle.equals(particle2)) {
                    double deltaX = particle2.getX() - particle.getX();
                    double deltaVx = particle2.getSpeedX() - particle.getSpeedX();
                    double deltaY = particle2.getY() - particle.getY();
                    double deltaVy = particle2.getSpeedY() - particle.getSpeedY();
                    double dvdr = deltaX * deltaVx + deltaVy * deltaY;
                    if (dvdr < 0) {
                        double dvdv = Math.pow(deltaVx, 2) + Math.pow(deltaVy, 2);
                        double drdr = Math.pow(deltaX, 2) + Math.pow(deltaY, 2);
                        double sumRadius = particle.getRadius() + particle2.getRadius();
                        double d = Math.pow(dvdr, 2) - dvdv * (drdr - Math.pow(sumRadius, 2));
                        if (d >= 0) {
                            calculatedTime = -((dvdr + Math.sqrt(d)) / dvdv);
                            if (calculatedTime < t) {
                                //Esta particula choca
                                colParticle1 = particle;
                                colParticle2 = particle2;
                                t = calculatedTime;
                            }
                        }
                    }
                }
            }
        }
        return t;
    }


//    public double moveToNextTime(){
//        double t = timeToNextColision();
//        for(Particle particle : particles){
//            particle.move(t);
//        }
//        if(colParticle2 == null){
//            elasticCollide(colParticle1,borderDirection);
//        }else{
//            elasticCollide(colParticle1,colParticle2);
//        }
//        return t;
//    }

    public void elasticCollide(Particle particle , Particle particle2){
        double deltaX = particle2.getX() - particle.getX();
        double deltaVx = particle2.getSpeedX() - particle.getSpeedX();
        double deltaY = particle2.getY() - particle.getY();
        double deltaVy = particle2.getSpeedY() - particle.getSpeedY();
        double dvdr = deltaX * deltaVx + deltaVy * deltaY;
        double sumRadius = particle.getRadius() + particle2.getRadius();

        double jx,jy,j,vx1,vx2,vy1,vy2 ;

        j = 2 * particle.getMass() * particle2.getMass() * (dvdr) / (sumRadius * (particle.getMass() + particle2.getMass()));
        jx = j * deltaX / sumRadius;
        jy = j * deltaY / sumRadius;

        vx1 = particle.getSpeedX() + jx/particle.getMass();
        vx2 = particle2.getSpeedX() - jx/particle2.getMass();

        vy1 = particle.getSpeedY() + jy/particle.getMass();
        vy2 = particle2.getSpeedY() - jy/particle2.getMass();

        particle.setSpeed(vx1,vy1);
        particle2.setSpeed(vx2,vy2);
    }

    public void elasticCollide(Particle particle, BorderDirection border){
        if(border == BorderDirection.HORIZONTAL){
            //(vx, -vy)
            particle.setAngle(-particle.getAngle() + 2 * Math.PI);
        }else{
            //(-vx, vy)
            double newAngle  = Math.PI - particle.getAngle();
            if(newAngle < 0){
                newAngle += Math.PI * 2;
            }
            particle.setAngle(newAngle);
        }
    }

    public void moveSystem(double deltaT){
        for (Particle particle : getParticles()) {
            particle.move(deltaT);
        }
    }

    public double getPromSpeedX(){
        double totalSpeedX = 0;
        for(Particle particle : getParticles()){
            totalSpeedX += particle.getSpeedX();
        }
        return totalSpeedX / particles.size();
    }

    public double getPromSpeedY(){
        double totalSpeedY = 0;
        for(Particle particle : getParticles()){
            totalSpeedY += particle.getSpeedY();
        }
        return totalSpeedY / particles.size();
    }

    /**
     * Move the system using an aproximated method.
     * @param t
     */
    public void move(double t){
        //TODO utilizar uno de los metodos implementados en el punto 1 considerando la fuerza gravitatoria.
        for(Particle particle : getParticles()){
            particle.setX(particle.getX() + particle.getSpeedX() * t);
            particle.setY(particle.getY() + particle.getSpeedY() * t);
        }
    }
}
