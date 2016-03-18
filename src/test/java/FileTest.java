import model.ParticleSystem;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.ObjectPlainWriter;

public class FileTest {

    private static Logger logger = Logger.getLogger(Main.class);
    private ParticleSystem particleSystem;
    private ObjectPlainWriter objectPlainWriter = new ObjectPlainWriter();
    private long start;
    private long end;

    @Test
    public void loadStaticFileTest() {
        String filename = "src/main/resources/Static100.txt";
        ObjectPlainWriter plainWriter = new ObjectPlainWriter();
        ParticleSystem particleSystem = new ParticleSystem(true, 5, 1);
        plainWriter.readObject(filename, particleSystem);
        Assert.assertEquals(particleSystem.getL(),100);
        Assert.assertEquals(particleSystem.getN(),100);
        Assert.assertEquals(particleSystem.getParticles().size(), 100);
        Assert.assertEquals(particleSystem.getParticles().get(0).getRadius(), 0.3700, 0);
        Assert.assertEquals(particleSystem.getParticles().get(0).getColor(), 1.0000, 0);
        Assert.assertEquals(particleSystem.getParticles().get(99).getRadius(), 0.3700, 0);
        Assert.assertEquals(particleSystem.getParticles().get(99).getColor(), 1.0000, 0);
    }
    //Change resource file to static100 and dynamic 100
    @Test
    public void test100() {
        loadFiles(5,1);
        particleSystem.populateNeighbourhood();
        Assert.assertEquals(particleSystem.getParticles().size(),100);
        Assert.assertEquals(particleSystem.getParticles().get(0).getX(), Double.valueOf(8.4615324e+00), 0);
        Assert.assertEquals(particleSystem.getParticles().get(0).getY(), Double.valueOf(4.5584588e+01), 0);
        Assert.assertEquals(particleSystem.getParticles().get(99).getX(), Double.valueOf(5.6410770e+01), 0);
        Assert.assertEquals(particleSystem.getParticles().get(99).getY(), Double.valueOf(4.7727423e+01), 0);
    }

    @Test
    public void testTime5_1(){
        loadFiles(5,1);
        particleSystem.populateNeighbourhood();
    }
    @Test
    public void testTime10_1(){
        loadFiles(5,1);
        particleSystem.populateNeighbourhood();
    }
    @Test
    public void testTime50_1(){
        loadFiles(5,1);
        particleSystem.populateNeighbourhood();
    }
    @Test
    public void testTime100_1(){
        loadFiles(5,1);
        particleSystem.populateNeighbourhood();
    }

    public void loadFiles(int m, int rc){
        String filename = "src/main/resources/Static100.txt";
        objectPlainWriter = new ObjectPlainWriter();
        particleSystem = new ParticleSystem(true, m , rc);
        objectPlainWriter.readObject(filename, particleSystem);
        String filenameDynamic = "src/main/resources/Dynamic100.txt";
        objectPlainWriter.readParticleSystem(filenameDynamic, particleSystem);
        start = System.currentTimeMillis();
    }

    @After
    public void finish(){
        end = System.currentTimeMillis();
        logger.info("Tiempo total: " + (end - start) + " ms");
        logger.info("Fin");
    }

    @Test
    public void test5() {
        particleSystem.populateNeighbourhood();
        Assert.assertEquals(particleSystem.getGrid()[2][2].size(), 2);
        Assert.assertEquals(particleSystem.getGrid()[0][0].get(0).getNeighbours().size(), 2);
        Assert.assertEquals(particleSystem.getGrid()[4][4].get(0).getNeighbours().size(), 2);
        objectPlainWriter.writeObject("src/main/resources/output5.txt", particleSystem);
    }

}
