package model;

import util.PlainWritable;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

public class ParticleSystem implements PlainWritable {

    private long n;
    private double l;
    private List<Particle> particles = new ArrayList<>();
    private double squareSize;
    private boolean isPeriodic;

    private int squareCount;
    private double interactionRadius;

    private List<Particle>[][] neighbourhood;

    public ParticleSystem(boolean isPeriodic, int squareCount, double interactionRadius) {
        this.squareCount = squareCount;
        this.interactionRadius = interactionRadius;
        this.isPeriodic = isPeriodic;
    }

    public void init(){
        neighbourhood = new ArrayList[squareCount][squareCount];
        for(int i = 0 ; i<squareCount; i++){
            for(int j = 0 ; j<squareCount; j++){
                neighbourhood[i][j] = new ArrayList<>();
            }
        }
        squareSize = l / squareCount;
        if(l/squareCount <= interactionRadius){
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
        setN(Long.parseLong(scanner.next()));
        setL(Long.parseLong(scanner.next()));
        List<Particle> particles = new ArrayList<>();
        while (scanner.hasNext()) {
            double radius = Double.parseDouble(scanner.next());
            double color = Double.parseDouble(scanner.next());
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
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            try {
                line = line.substring(3);
            } catch (Exception ex) {
                //TODO: Hacerlo mejor
                line = line.substring(1);
            }
            String[] words = line.split("\\W+");

            if(words.length == 1){
                //Es el separador de tiempo
            }else{
                Particle particle = it.next();
                //Leer particula
                particle.readObject(line);
                addParticle(particle);
            }
        }
        return this;
    }

    public long getN() {
        return n;
    }

    public void setN(long n) {
        this.n = n;
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

    public void addParticle(Particle particle){
        int x = (int)Math.floor(particle.getX() / squareSize);
        int y = (int)Math.floor(particle.getY() / squareSize);
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
        return ( i == -1 || j == -1 || i == squareCount || j == squareCount);
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

    public void writeVisualization(String filename, int id) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(filename);
        Particle selectedParticle = this.getParticles().get(id);
        Color color = new Color(255,0,0); //Default color
        Color highlightedColor = new Color(0,255,0); //Highlighted color
        Color neighbourColor = new Color(0, 124,0);
        StringBuilder sb = new StringBuilder();
        sb.append(this.getN()).append("\n");
        sb.append("0").append("\n"); //TODO TIMESTEPS
        particles.stream().forEach(particle ->{
            sb.append(particle.getX() + "\t" + particle.getY() + "\t" + particle.getRadius() + "\t");
            if(particle.equals(selectedParticle)){
                //PINTAR
                sb.append(highlightedColor.getRed()/255.0 + "\t" + highlightedColor.getGreen()/255.0 + "\t" + highlightedColor.getBlue()/255.0 + "\t");
            }
            else if(selectedParticle.getNeighbours().contains(particle)){
                //Pintar
                sb.append(neighbourColor.getRed()/255.0 + "\t" + neighbourColor.getGreen()/255.0 + "\t" + neighbourColor.getBlue()/255.0 + "\t");

            }else{
                sb.append(color.getRed()/255.0 + "\t" + color.getGreen()/255.0 + "\t" + color.getBlue()/255.0 + "\t");
            }
            sb.append("\n");
        });
        writer.write(sb.toString());
        writer.flush();
        writer.close();
    }
}
