package aima.test.unit.agent.support;

/**
 * @author Ciaran O'Reilly
 */
public class TestPercept {
    public final String location;
    public final boolean floorIsDirty;
    public TestPercept(String location, boolean floorIsDirty) {
        this.location     = location;
        this.floorIsDirty = floorIsDirty;
    }
}