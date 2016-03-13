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

}
