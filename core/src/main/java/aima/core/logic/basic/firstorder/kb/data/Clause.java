package aima.core.logic.basic.firstorder.kb.data;

import java.util.*;

public class Clause implements aima.core.logic.api.firstorderlogic.Clause {

    private Set<Literal> literals = new LinkedHashSet<>();
    private Set<Literal> positiveLiterals = new LinkedHashSet<>();
    private Set<Literal> negativeLiterals = new LinkedHashSet<>();

    public Clause(){
        this(new ArrayList<>());
    }

    public Clause(Literal... literals){
        this(Arrays.asList(literals));
    }

    public Clause(List<Literal> literals) {
        for (Literal literal :
                literals) {
            this.literals.add(literal);
            if (literal.isPositiveLiteral()){
                positiveLiterals.add(literal);
            }
            else {
                negativeLiterals.add(literal);
            }
        }
    }

    @Override
    public aima.core.logic.api.firstorderlogic.Clause standardizeVariables() {
        return null;
    }

    @Override
    public Literal getConsequence() {
        return null;
    }

    @Override
    public boolean isDefiniteClause() {
        return (positiveLiterals.size() == 1);
    }

    @Override
    public boolean isUnitClause() {
        return (literals.size()==1);
    }

    @Override
    public boolean isImplicationDefiniteClause() {
        return (isDefiniteClause() && negativeLiterals.size()>=1);
    }

    @Override
    public boolean isHornClause() {
        return (positiveLiterals.size()<=1);
    }

}
