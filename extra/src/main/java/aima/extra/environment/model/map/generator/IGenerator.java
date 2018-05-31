package aima.extra.environment.model.map.generator;

/**
 * Created by @AdrianBZG on 2/04/17.
 */
public interface IGenerator {
    double generateAtPoint(double x, double y);
    String getGeneratorName();
    void newSeed();
}
