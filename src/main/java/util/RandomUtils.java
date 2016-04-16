package util;

import java.util.Random;

public class RandomUtils {

    public static final Random r = new Random(System.currentTimeMillis());

    public static double between(double min, double max) {
        double randomValue = min + (max - min) * r.nextDouble();
        return randomValue;
    }

}
