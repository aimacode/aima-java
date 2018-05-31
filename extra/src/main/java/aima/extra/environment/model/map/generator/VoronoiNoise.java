package aima.extra.environment.model.map.generator;

import java.util.Random;

/**
 * Created by @AdrianBZG on 2/04/17.
 *
 * Just an example noise to generate a 2D Map environment: Voronoi Noise (also
 * known as Cell Noise)
 */
public class VoronoiNoise implements IGenerator {
    private static final double voronoiFrequency = 1.0;
    private long seed;

    public VoronoiNoise() {
    }

    @Override
    public void newSeed() {
        seed = new Random().nextInt();
    }

    /**
     * To avoid having to store the feature points, we use a hash function
     * of the coordinates and the seed instead. Those big scary numbers are
     * arbitrary primes.
     */
    public static double valueNoise2D (int x, int z, long seed) {
        long n = (1619 * x + 6971 * z + 1013 * seed) & 0x7fffffff;
        n = (n >> 13) ^ n;
        return 1.0 - ((double)((n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff) / 1073741824.0);
    }

    @Override
    public double generateAtPoint(double x, double y) {
        x *= voronoiFrequency;
        y *= voronoiFrequency;

        int xInt = (x > .0? (int)x: (int)x - 1);
        int zInt = (y > .0? (int)y: (int)y - 1);

        double minDist = 32000000.0;

        double xCandidate = 0;
        double zCandidate = 0;

        for(int zCur = zInt - 2; zCur <= zInt + 2; zCur++) {
            for(int xCur = xInt - 2; xCur <= xInt + 2; xCur++) {

                double xPos = xCur + valueNoise2D(xCur, zCur, seed);
                double zPos = zCur + valueNoise2D(xCur, zCur, new Random(seed).nextLong());
                double xDist = xPos - x;
                double zDist = zPos - y;
                double dist = xDist * xDist + zDist * zDist;

                if(dist < minDist) {
                    minDist = dist;
                    xCandidate = xPos;
                    zCandidate = zPos;
                }
            }
        }

        double returnValue = ((double)VoronoiNoise.valueNoise2D (
                (int)(Math.floor (xCandidate)),
                (int)(Math.floor (zCandidate)), seed));

        return returnValue;
    }

    @Override
    public String getGeneratorName() {
        return "Voronoi Noise";
    }

    @Override
    public String toString() {
        return getGeneratorName();
    }
}
