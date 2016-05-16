package model;

import java.util.Map;

/**
 */
public class ParticleDetectorWall extends Wall{

    int counts = 0;

    public ParticleDetectorWall(double x1, double y1, double x2, double y2) {
        super(x1,y1,x2,y2);
    }

    public void incCount(){
        counts++;
    }

    public int getCounts() {
        return counts;
    }

    public void resetCounts(){
        counts = 0;
    }
}
