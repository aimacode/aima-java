package aima.extra.environment.model.map.generator;

/**
 * Created by @AdrianBZG on 2/04/17.
 */
public class NullGenerator implements IGenerator {
    public NullGenerator() {
    }

    @Override
    public void newSeed() {}

    @Override
    public double generateAtPoint(double x, double y) {
        return 0.0;
    }

    @Override
    public String getGeneratorName() {
        return "Null generator";
    }

    @Override
    public String toString() {
        return getGeneratorName();
    }
}
