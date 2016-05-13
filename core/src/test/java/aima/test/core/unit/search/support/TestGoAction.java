package aima.test.core.unit.search.support;

public class TestGoAction {
	private String goTo;
	
    public TestGoAction(String goTo) {
        this.goTo = goTo;
    }

    public String getName() { return "Go(" + goTo + ")"; }
    
    public String getGoTo() { return goTo; }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof TestGoAction) {
            return this.getName().equals(((TestGoAction)obj).getName());
        }
        return super.equals(obj);
    }
    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
