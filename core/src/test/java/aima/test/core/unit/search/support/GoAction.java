package aima.test.core.unit.search.support;

public class GoAction {
	private String goTo;
	
    public GoAction(String goTo) {
        this.goTo = goTo;
    }

    public String getName() { return "Go(" + goTo + ")"; }
    
    public String getGoTo() { return goTo; }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof GoAction) {
            return this.getName().equals(((GoAction)obj).getName());
        }
        return super.equals(obj);
    }
    @Override
    public int hashCode() {
        return getName().hashCode();
    }
    
    @Override
    public String toString() {
    	return getName();
    }
}
