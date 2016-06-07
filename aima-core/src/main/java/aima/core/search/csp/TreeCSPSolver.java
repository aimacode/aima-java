package aima.core.search.csp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.11, Page 224.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function TREE-CSP-SOLVER(csp) returns a solution, or failure
 *    n = numberOfVariablesIn( X )
 *    assignment = new emtpy Assignmnet()
 *    root = AnyVaraibleOf( X )
 *    X = TOPOLOGICAL-SORT(X,root)
 *    for j = n down to 2 do
 *        MAKE-ARC-CONSISTENT( PARENT( X(j), X(j) )
 *        if( it can not be made consisten ) then return failure
 *        
 *    for j = 1 to n do
 *       add { X(j) = some value from D(j)} to assignment
 *       if ( there is no consistent value ) then return failure
 *    
 *    return assignment        
 *    
 * </code>
 * </pre>
 * 
 * Figure 6.11 The TREE-CSP-SOLVER algorithm for solving tree-structured CSPs. If the 
 * CSP has a solution, we will find it in linear time, if not we will detect a contradiction.
 * 
 * @author Peter Grubmair
 */
public class TreeCSPSolver extends SolutionStrategy {
	
	public TreeCSPSolver() {}

	private class Node {
		
		final Variable variable;
		
		final Node parent;
		
		public Node( 
			Variable variable, Node parent
		) {
			this.variable = variable ;
			this.parent = parent; 
		}
	}
	
	private class LoopException extends Exception {

		private static final long serialVersionUID = 1L;

		public LoopException( String msg ) {
			super( msg ) ;
		}
	}
	
	private class ContradictionException extends Exception {
		
		private static final long serialVersionUID = 1L;
		
		public ContradictionException( String msg ) {
			super( msg );
		}
	}
	@Override
	public Assignment solve(CSP csp) {
		if ( !isBinaryCSP(csp)) {
			System.out.println( "The CSP is not binary!" );
			this.fireStateChanged(csp);
			return null ;
		}
		List<Node> topologicalNodes = new ArrayList<Node>() ;
		try {
			
			// this set of variables is used for detection of loops.
			Set<Variable> orderedVariables = new HashSet<Variable>() ;
			// This is necessary as the tree can consist of separated compartments.
			while( csp.getVariables().size() > topologicalNodes.size() ) {
				Variable rootVar = findUnusedVar( csp, orderedVariables ) ;
				topologicalNodes.addAll( 
					topologicalSort( 
						csp, 
						new Node( rootVar, null ),
						orderedVariables
					)
				);
			}		
		} catch ( LoopException lEx ) {
			System.out.println( lEx.getMessage() );
			return null ;
		}
		int j ;
		try {
			for( j = topologicalNodes.size()-1 ; j >= 1; j-- ) {
				makeArcConsistent(topologicalNodes.get(j), topologicalNodes.get(j-1)/*.parent*/,  csp ) ;
			}
		} catch ( ContradictionException unsolvEx ) {
			System.out.println(  unsolvEx.getMessage() );
			return null ;
		}
		
		try {
			return makeAssignments( topologicalNodes, csp ) ;
		} catch( ContradictionException contraDictEx ) {
			System.out.println( String.format( "CSP not solvable: %s" , contraDictEx.getMessage()));
		    return null ;
		}


	}
	
    Variable findUnusedVar( CSP csp, Set<Variable> orderedVariables ) {
    	for( Variable var: csp.getVariables() ) {
    		if( !orderedVariables.contains( var )) {
    			return var ;
    		}
    	}
    	return null ;
    }
	
	Assignment makeAssignments( List<Node> topologicalNodes, CSP csp ) throws ContradictionException {
		Assignment assignment = new Assignment();
		for( Node node : topologicalNodes ) {
			int i ;
			Domain domain = csp.getDomain( node.variable ) ;
			for( i = 0 ; i < domain.size(); i++ ) {
				assignment.setAssignment(node.variable, domain.get(i) );
				boolean allSatisfied = true ;
				for( Constraint constraint: csp.getConstraints( node.variable ) ) {
					if( !constraint.isSatisfiedWith(assignment) ) {
						allSatisfied = false; 
						break ;
					}
				}
				if ( !allSatisfied) {
					assignment.removeAssignment(node.variable);
					continue ;
				} else {
					break;
				}
			}
            if( !assignment.hasAssignmentFor(node.variable) ) {
            	throw new ContradictionException( 
            		String.format( "no value for variable", node.variable.getName() )
                ) ;		
            }
			this.fireStateChanged(assignment, csp);
		}
		return assignment;    
	}
	
	void makeArcConsistent( Node node, Node parent, CSP csp ) throws ContradictionException {
		
		List<Object> consistentParentValues = new ArrayList<Object>() ;
		Assignment assignment = new Assignment() ;
		for( Object parentValue : csp.getDomain(parent.variable).asList() ) {
			assignment.setAssignment(parent.variable, parentValue);
			boolean allFullfilled = true ;
			for( Constraint constraint: csp.getConstraints( parent.variable ) ) {
				boolean fullfilled = false ;
				for( Object nodeValue : csp.getDomain(node.variable).asList() ) {
					assignment.setAssignment(node.variable, nodeValue);
					if(  constraint.isSatisfiedWith(assignment) ) {
						assignment.removeAssignment( node.variable);
						fullfilled = true ;
						break ;
					}
					assignment.removeAssignment( node.variable ); 
				}
				if( !fullfilled ) {
					allFullfilled = false ;
					break ;
				}
			}
			if( allFullfilled ) {
				consistentParentValues.add( parentValue );
			}
			assignment.removeAssignment( parent.variable );
		}
		if( consistentParentValues.isEmpty() ) {
			throw new ContradictionException( 
				String.format( "variable %s has no values left.", parent.variable.getName() ) 
			);
		}
		csp.setDomain(parent.variable, new Domain(consistentParentValues) );
		this.fireStateChanged(csp);
	}
	
	// this solver can only solve binary CSPs.
	private boolean isBinaryCSP( CSP csp) {
		boolean result = true ;
		for( Constraint constraint: csp.getConstraints() ) {
			if( constraint.getScope().size() > 2 ) {
				result = false ;
				break ;
			}
		}
		return result ;
	}
	
	
	
	
	// topological sort by recursion.
	private List<Node> topologicalSort( CSP csp, Node parent, Set<Variable> orderedVariables  ) 
			throws LoopException {
		List<Node> orderedNodes = new ArrayList<Node>() ;
	    orderedVariables.add( parent.variable ) ;	
	    orderedNodes.add( parent ) ;
	    Set<Variable> children = findChildren( parent, csp ) ;
		
		if ( orderedVariables.removeAll( children ) ) {
			throw new LoopException( "CSP is not a tree" ) ;
		}
		
		for( Variable childVar: children ) {
			orderedNodes.addAll( 
				topologicalSort( 
					csp, new Node( childVar, parent  ), orderedVariables 
			)   );
		}
		return orderedNodes;
		
	
	}
    // multiple parallel binary constraints are allowed, therefore for each child variable there
	// is a set of constraints.
	private Set<Variable> findChildren(Node parent, CSP csp) {
		Set<Variable> children = new HashSet<Variable>() ;
		for( Constraint constraint: csp.getConstraints(parent.variable) ) {			
		    Variable child = csp.getNeighbor(parent.variable, constraint);
		    // child is every neighbor which is not the parent.
			if( child != null && ( parent.parent == null || child != parent.parent.variable) ) {
					children.add(child) ;
			}			
		}
		return children ;
	}

 
}
