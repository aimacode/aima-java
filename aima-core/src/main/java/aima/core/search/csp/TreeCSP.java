package aima.core.search.csp;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.10(a), Page 223.<br>
 * <br>
 * The constraint graph of a tree-structured CSP. 
 * 
 * @author Peter Grubmair
 */
public class TreeCSP extends CSP {

	public static final Variable A = new Variable("A");
	public static final Variable B = new Variable("B");
	public static final Variable C = new Variable("C");
	public static final Variable D = new Variable("D");
	public static final Variable E = new Variable("E");
	public static final Variable F = new Variable("F");

	public static final String RED = "RED";
	public static final String GREEN = "GREEN";
	
	public TreeCSP() {
		addVariable(A);
		addVariable(B);
		addVariable(C);
		addVariable(D);
		addVariable(E);
		addVariable(F);

		Domain colors = new Domain(new Object[] { RED, GREEN });
		Domain restricted = new Domain( new Object[]{ RED }) ;

		for (Variable var : getVariables()) {
			if ( var != D ) {
			  setDomain(var, colors);
			} else {
			  setDomain(var, restricted ) ; // make it a little bit more interesting	
			}
		}		

		addConstraint(new NotEqualConstraint(A, B));
		addConstraint(new NotEqualConstraint(B, C));
		addConstraint(new NotEqualConstraint(B, D));
		addConstraint(new NotEqualConstraint(D, E));
		addConstraint(new NotEqualConstraint(D, F));

	}
}
