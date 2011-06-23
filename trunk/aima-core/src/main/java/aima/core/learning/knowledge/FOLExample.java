package aima.core.learning.knowledge;

import java.util.ArrayList;
import java.util.List;

import aima.core.learning.framework.Example;
import aima.core.logic.fol.Connectors;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLExample {
	private FOLDataSetDomain folDSDomain = null;
	private Example example = null;
	private int egNo = 0;
	//
	private Constant ithExampleConstant = null;
	private Sentence classification = null;
	private Sentence description = null;

	//
	// PUBLIC METHODS
	//
	public FOLExample(FOLDataSetDomain folDSDomain, Example example, int egNo) {
		this.folDSDomain = folDSDomain;
		this.example = example;
		this.egNo = egNo;
		constructFOLEg();
	}

	public int getExampleNumber() {
		return egNo;
	}

	public Sentence getClassification() {
		return classification;
	}

	public Sentence getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return classification.toString() + " " + Connectors.AND + " "
				+ description.toString();
	}

	//
	// PRIVATE METHODS
	//
	private void constructFOLEg() {
		ithExampleConstant = new Constant(folDSDomain.getExampleConstant(egNo));

		List<Term> terms = new ArrayList<Term>();
		terms.add(ithExampleConstant);
		// Create the classification sentence
		classification = new Predicate(folDSDomain.getGoalPredicateName(),
				terms);
		if (!example.getAttributeValueAsString(
				folDSDomain.getDataSetTargetName()).equals(
				folDSDomain.getTrueGoalValue())) {
			// if not true then needs to be a Not sentence
			classification = new NotSentence(classification);
		}

		// Create the description sentence
		List<Sentence> descParts = new ArrayList<Sentence>();
		for (String dname : folDSDomain.getDescriptionDataSetNames()) {
			String foldDName = folDSDomain.getFOLName(dname);
			terms = new ArrayList<Term>();
			terms.add(ithExampleConstant);
			// If multivalued becomes a two place predicate
			// e.g: Patrons(X1, Some)
			// otherwise: Hungry(X1) or ~ Hungry(X1)
			// see pg 769 of AIMA
			Sentence part = null;
			if (folDSDomain.isMultivalued(dname)) {
				terms.add(new Constant(folDSDomain.getFOLName(example
						.getAttributeValueAsString(dname))));
				part = new Predicate(foldDName, terms);
			} else {
				part = new Predicate(foldDName, terms);
				// Need to determine if false
				if (!folDSDomain.getTrueGoalValue().equals(
						example.getAttributeValueAsString(dname))) {
					part = new NotSentence(part);
				}
			}
			descParts.add(part);
		}
		if (descParts.size() == 1) {
			description = descParts.get(0);
		} else if (descParts.size() > 1) {
			description = new ConnectedSentence(Connectors.AND,
					descParts.get(0), descParts.get(1));
			for (int i = 2; i < descParts.size(); i++) {
				description = new ConnectedSentence(Connectors.AND,
						description, descParts.get(i));
			}
		}
	}
}
