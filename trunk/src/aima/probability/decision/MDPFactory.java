package aima.probability.decision;


public class MDPFactory {

	public static MDP createFourByThreeMDP() {
		MDP mdp =null;
		CellWorld cw = new CellWorld(4,3,0.4,0.1); 
		//cw.markBlock(2,2);
		return mdp;
	}

}
