package aima.core.environment.vacuum;

/**
 * For simplicity consider the percept a view on a local state.
 * 
 * @author Ciaran O'Reilly
 */
public class VEPercept extends VELocalState {
	public VEPercept(String location, VacuumEnvironment.Status state) {
		super(location, state);
	}
	
	@Override
	public String toString() {
		return "Perceive(In("+location+"), Status("+status+"))";
	}
}
