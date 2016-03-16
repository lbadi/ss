import model.ParticleSystem;
import org.junit.Assert;
import org.junit.Test;
import util.ObjectPlainWriter;

public class FileTest {

    @Test
    public void loadStaticFileTest() {
        String filename = "src/main/resources/Static100.txt";
        ObjectPlainWriter plainWriter = new ObjectPlainWriter();
        ParticleSystem particleSystem = new ParticleSystem();
        plainWriter.readObject(filename, particleSystem);
        Assert.assertEquals(particleSystem.getL(),100);
        Assert.assertEquals(particleSystem.getN(),100);
        Assert.assertEquals(particleSystem.getParticles().size(), 100);
        Assert.assertEquals(particleSystem.getParticles().get(0).getRadius(), 0.3700, 0);
        Assert.assertEquals(particleSystem.getParticles().get(0).getColor(), 1.0000, 0);
        Assert.assertEquals(particleSystem.getParticles().get(99).getRadius(), 0.3700, 0);
        Assert.assertEquals(particleSystem.getParticles().get(99).getColor(), 1.0000, 0);
    }

    @Test
    public void loadDynamicFileTest() {
        String filename = "src/main/resources/Dynamic100.txt";
        ObjectPlainWriter plainWriter = new ObjectPlainWriter();
        ParticleSystem particleSystem = new ParticleSystem();
        plainWriter.readParticleSystem(filename, particleSystem);
        Assert.assertEquals(particleSystem.getParticles().size(),100);
        Assert.assertEquals(particleSystem.getParticles().get(0).getX(), Double.valueOf(8.4615324e+00), 0);
        Assert.assertEquals(particleSystem.getParticles().get(0).getY(), Double.valueOf(4.5584588e+01), 0);
        Assert.assertEquals(particleSystem.getParticles().get(99).getX(), Double.valueOf(5.6410770e+01), 0);
        Assert.assertEquals(particleSystem.getParticles().get(99).getY(), Double.valueOf(4.7727423e+01), 0);
    }

    @Test
    public void loadEntireGrid() {
        String filename = "src/main/resources/Static5.txt";
        ObjectPlainWriter plainWriter = new ObjectPlainWriter();
        ParticleSystem particleSystem = new ParticleSystem();
        plainWriter.readObject(filename, particleSystem);
        String filenameDynamic = "src/main/resources/Dynamic5.txt";
        plainWriter.readParticleSystem(filenameDynamic, particleSystem);
        particleSystem.populateNeighbourhood();
        Assert.assertEquals(particleSystem.getGrid()[2][2].size(), 2);
        Assert.assertEquals(particleSystem.getGrid()[0][0].get(0).getNeighbours().size(), 2);
        Assert.assertEquals(particleSystem.getGrid()[4][4].get(0).getNeighbours().size(), 2);
        plainWriter.writeObject("src/main/resources/output5.txt", particleSystem);
    }

}
