package aima.core.logic.propositional.inference;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import aima.core.logic.propositional.LogicUtils;
import aima.core.logic.propositional.Model;
import aima.core.logic.propositional.SymbolClassifier;
import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.visitors.CNFClauseGatherer;
import aima.core.logic.propositional.visitors.ConvertToCNF;
import aima.core.logic.propositional.visitors.SymbolCollector;
import aima.core.util.SetOps;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class DPLL {

	/**
	 * Returns <code>true</code> if the specified sentence is satisfiable. A
	 * sentence is satisfiable if it is true in, or satisfied by, some model.
	 * 
	 * @param s
	 *            a sentence in propositional logic
	 * 
	 * @return <code>true</code> if the specified sentence is satisfiable.
	 */
	public boolean dpllSatisfiable(Sentence s) {

		return dpllSatisfiable(s, new Model());
	}

	/**
	 * Returns <code>true</code> if the specified sentence is satisfiable. A
	 * sentence is satisfiable if it is true in, or satisfied by, some model.
	 * 
	 * @param string
	 *            a String representation of a Sentence in propositional logic
	 * 
	 * @return <code>true</code> if the specified sentence is satisfiable.
	 */
	public boolean dpllSatisfiable(String string) {
		Sentence sen = (Sentence) new PLParser().parse(string);
		return dpllSatisfiable(sen, new Model());
	}

	/**
	 * Returns <code>true</code> if the specified sentence is satisfiable. A
	 * sentence is satisfiable if it is true in, or satisfied by, some model.
	 * 
	 * @param s
	 *            a sentence in propositional logic
	 * @param m
	 *            a model the sentence must be true in
	 * 
	 * @return <code>true</code> if the specified sentence is satisfiable.
	 */
	public boolean dpllSatisfiable(Sentence s, Model m) {
		Set<Sentence> clauses = new CNFClauseGatherer()
				.getClausesFrom(new ConvertToCNF().convert(s));
		List<PropositionSymbol> symbols = new ArrayList<PropositionSymbol>(SymbolCollector.getSymbolsFrom(s));
		// System.out.println(" numberOfSymbols = " + symbols.size());
		return dpll(clauses, symbols, m);
	}

	public List<Sentence> clausesWithNonTrueValues(List<Sentence> clauseList,
			Model model) {
		List<Sentence> clausesWithNonTrueValues = new ArrayList<Sentence>();
		for (int i = 0; i < clauseList.size(); i++) {
			Sentence clause = clauseList.get(i);
			if (!(isClauseTrueInModel(clause, model))) {
				if (!(clausesWithNonTrueValues.contains(clause))) {// defensive
					// programming not really necessary
					clausesWithNonTrueValues.add(clause);
				}
			}

		}
		return clausesWithNonTrueValues;
	}

	public SymbolValuePair findPureSymbolValuePair(List<Sentence> clauseList,
			Model model, List<PropositionSymbol> symbols) {
		List<Sentence> clausesWithNonTrueValues = clausesWithNonTrueValues(
				clauseList, model);
		Sentence nonTrueClauses = LogicUtils.chainWith(Connective.AND,
				clausesWithNonTrueValues);
		// System.out.println("Unsatisfied clauses = "
		// + clausesWithNonTrueValues.size());
		Set<PropositionSymbol> symbolsAlreadyAssigned = model.getAssignedSymbols();

		// debug
		// List symList = asList(symbolsAlreadyAssigned);
		//
		// System.out.println(" assignedSymbols = " + symList.size());
		// if (symList.size() == 52) {
		// System.out.println("untrue clauses = " + clausesWithNonTrueValues);
		// System.out.println("model= " + model);
		// }

		// debug
		List<PropositionSymbol> purePositiveSymbols = new ArrayList<PropositionSymbol>(SetOps
				.difference(new SymbolClassifier()
						.getPurePositiveSymbolsIn(nonTrueClauses),
						symbolsAlreadyAssigned));

		List<PropositionSymbol> pureNegativeSymbols = new ArrayList<PropositionSymbol>(SetOps
				.difference(new SymbolClassifier()
						.getPureNegativeSymbolsIn(nonTrueClauses),
						symbolsAlreadyAssigned));
		// if none found return "not found
		if ((purePositiveSymbols.size() == 0)
				&& (pureNegativeSymbols.size() == 0)) {
			return new SymbolValuePair();// automatically set to null values
		} else {
			if (purePositiveSymbols.size() > 0) {
				PropositionSymbol symbol = new PropositionSymbol(
						(purePositiveSymbols.get(0)).getSymbol());
				if (pureNegativeSymbols.contains(symbol)) {
					throw new RuntimeException("Symbol " + symbol.getSymbol()
							+ "misclassified");
				}
				return new SymbolValuePair(symbol, true);
			} else {
				PropositionSymbol symbol = new PropositionSymbol(
						(pureNegativeSymbols.get(0)).getSymbol());
				if (purePositiveSymbols.contains(symbol)) {
					throw new RuntimeException("Symbol " + symbol.getSymbol()
							+ "misclassified");
				}
				return new SymbolValuePair(symbol, false);
			}
		}
	}

	//
	// PRIVATE METHODS
	//

	private boolean dpll(Set<Sentence> clauses, List<PropositionSymbol> symbols,
			Model model) {
		// List<Sentence> clauseList = asList(clauses);
		List<Sentence> clauseList = new ArrayList<Sentence>(clauses);
		// System.out.println("clauses are " + clauses.toString());
		// if all clauses are true return true;
		if (areAllClausesTrue(model, clauseList)) {
			// System.out.println(model.toString());
			return true;
		}
		// if even one clause is false return false
		if (isEvenOneClauseFalse(model, clauseList)) {
			// System.out.println(model.toString());
			return false;
		}
		// System.out.println("At least one clause is unknown");
		// try to find a unit clause
		SymbolValuePair svp = findPureSymbolValuePair(clauseList, model,
				symbols);
		if (svp.notNull()) {
			List<PropositionSymbol> newSymbols = new ArrayList<PropositionSymbol>(symbols);
			newSymbols.remove(new PropositionSymbol(svp.symbol.getSymbol()));
			Model newModel = model.extend(new PropositionSymbol(svp.symbol.getSymbol()),
					svp.value.booleanValue());
			return dpll(clauses, newSymbols, newModel);
		}

		SymbolValuePair svp2 = findUnitClause(clauseList, model, symbols);
		if (svp2.notNull()) {
			List<PropositionSymbol> newSymbols = new ArrayList<PropositionSymbol>(symbols);
			newSymbols.remove(new PropositionSymbol(svp2.symbol.getSymbol()));
			Model newModel = model.extend(new PropositionSymbol(svp2.symbol.getSymbol()),
					svp2.value.booleanValue());
			return dpll(clauses, newSymbols, newModel);
		}

		PropositionSymbol symbol = (PropositionSymbol) symbols.get(0);
		// System.out.println("default behaviour selecting " + symbol);
		List<PropositionSymbol> newSymbols = new ArrayList<PropositionSymbol>(symbols);
		newSymbols.remove(0);
		return (dpll(clauses, newSymbols, model.extend(symbol, true)) || dpll(
				clauses, newSymbols, model.extend(symbol, false)));
	}

	private boolean isEvenOneClauseFalse(Model model, List<Sentence> clauseList) {
		for (int i = 0; i < clauseList.size(); i++) {
			Sentence clause = clauseList.get(i);
			if (model.isFalse(clause)) {
				// System.out.println(clause.toString() + " is false");
				return true;
			}

		}

		return false;
	}

	private boolean areAllClausesTrue(Model model, List<Sentence> clauseList) {

		for (int i = 0; i < clauseList.size(); i++) {
			Sentence clause = clauseList.get(i);
			// System.out.println("evaluating " + clause.toString());
			if (!isClauseTrueInModel(clause, model)) { // ie if false or
				// UNKNOWN
				// System.out.println(clause.toString()+ " is not true");
				return false;
			}

		}
		return true;
	}

	private boolean isClauseTrueInModel(Sentence clause, Model model) {
		List<PropositionSymbol> positiveSymbols = new ArrayList<PropositionSymbol>(new SymbolClassifier().getPositiveSymbolsIn(clause));
		List<PropositionSymbol> negativeSymbols = new ArrayList<PropositionSymbol>(new SymbolClassifier().getNegativeSymbolsIn(clause));

		for (PropositionSymbol symbol : positiveSymbols) {
			if ((model.isTrue(symbol))) {
				return true;
			}
		}
		for (PropositionSymbol symbol : negativeSymbols) {
			if ((model.isFalse(symbol))) {
				return true;
			}
		}
		return false;

	}

	private SymbolValuePair findUnitClause(List<Sentence> clauseList,
			Model model, List<PropositionSymbol> symbols) {
		for (int i = 0; i < clauseList.size(); i++) {
			Sentence clause = clauseList.get(i);
			if (clause.isPropositionSymbol()
					&& (!(model.getAssignedSymbols().contains(clause)))) {
				// System.out.println("found unit clause - assigning");
				return new SymbolValuePair(new PropositionSymbol(
						((PropositionSymbol) clause).getSymbol()), true);
			}

			if (clause.isUnarySentence()) {
				ComplexSentence sentence = (ComplexSentence) clause;
				Sentence negated = sentence.getSimplerSentence(0);
				if ((negated instanceof PropositionSymbol)
						&& (!(model.getAssignedSymbols().contains(negated)))) {
					// System.out.println("found unit clause type 2 -
					// assigning");
					return new SymbolValuePair(new PropositionSymbol(
							((PropositionSymbol) negated).getSymbol()), false);
				}
			}

		}

		return new SymbolValuePair();// failed to find any unit clause;

	}

	public class SymbolValuePair {
		public PropositionSymbol symbol;// public to avoid unnecessary get and set

		// accessors

		public Boolean value;

		public SymbolValuePair() {
			// represents "No Symbol found with a boolean value that makes all
			// its literals true
			symbol = null;
			value = null;
		}

		public SymbolValuePair(PropositionSymbol symbol, boolean bool) {
			// represents "Symbol found with a boolean value that makes all
			// its literals true
			this.symbol = symbol;
			value = new Boolean(bool);
		}

		public boolean notNull() {
			return (symbol != null) && (value != null);
		}

		@Override
		public String toString() {
			String symbolString, valueString;
			if (symbol == null) {
				symbolString = "NULL";
			} else {
				symbolString = symbol.toString();
			}
			if (value == null) {
				valueString = "NULL";
			} else {
				valueString = value.toString();
			}
			return symbolString + " -> " + valueString;
		}
	}
}