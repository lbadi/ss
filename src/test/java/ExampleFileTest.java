import model.ParticleSystem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.ObjectPlainWriter;

public class ExampleFileTest {

    private ParticleSystem particleSystem;
    private ObjectPlainWriter objectPlainWriter;

    private void loadFiles(String staticFilename, String dynamicFilename, int m, double rc) {
        objectPlainWriter = new ObjectPlainWriter();
        particleSystem = new ParticleSystem(true, m , rc);
        objectPlainWriter.readObject(staticFilename, particleSystem);
        objectPlainWriter.readParticleSystem(dynamicFilename, particleSystem);
    }

    @Before
    public void init() {
        objectPlainWriter = new ObjectPlainWriter();
    }

    @Test
    public void static100Test() {
        particleSystem = new ParticleSystem(true, 5, 1);
        objectPlainWriter.readObject(ParticleSystemTest.INPUT_PATH + "Static100.txt", particleSystem);
        Assert.assertEquals(particleSystem.getL(),100, 0);
        Assert.assertEquals(particleSystem.getN(),100, 0);
        Assert.assertEquals(particleSystem.getParticles().size(), 100);
        Assert.assertEquals(particleSystem.getParticles().get(0).getRadius(), 0.3700, 0);
        Assert.assertEquals(particleSystem.getParticles().get(0).getColor(), 1.0000, 0);
        Assert.assertEquals(particleSystem.getParticles().get(99).getRadius(), 0.3700, 0);
        Assert.assertEquals(particleSystem.getParticles().get(99).getColor(), 1.0000, 0);
    }

    @Test
    public void neighbourhood100Test() {
        loadFiles(ParticleSystemTest.INPUT_PATH + "Static100.txt",
                ParticleSystemTest.INPUT_PATH + "Dynamic100.txt",
                5,
                1);
        particleSystem.populateNeighbourhood();
        Assert.assertEquals(particleSystem.getParticles().size(),100);
        Assert.assertEquals(particleSystem.getParticles().get(0).getX(), Double.valueOf(8.4615324e+00), 0);
        Assert.assertEquals(particleSystem.getParticles().get(0).getY(), Double.valueOf(4.5584588e+01), 0);
        Assert.assertEquals(particleSystem.getParticles().get(99).getX(), Double.valueOf(5.6410770e+01), 0);
        Assert.assertEquals(particleSystem.getParticles().get(99).getY(), Double.valueOf(4.7727423e+01), 0);
    }

    @Test
    public void integral5Test() {
        loadFiles(ParticleSystemTest.INPUT_PATH + "Static5.txt",
                ParticleSystemTest.INPUT_PATH + "Dynamic5.txt",
                5,
                0.75);
        particleSystem.populateNeighbourhood();
        Assert.assertEquals(particleSystem.getGrid()[2][2].size(), 2);
        Assert.assertEquals(particleSystem.getGrid()[0][0].get(0).getNeighbours().size(), 2);
        Assert.assertEquals(particleSystem.getGrid()[4][4].get(0).getNeighbours().size(), 2);
    }

}
