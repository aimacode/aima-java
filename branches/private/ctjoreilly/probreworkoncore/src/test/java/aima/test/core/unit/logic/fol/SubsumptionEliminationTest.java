package aima.test.core.unit.logic.fol;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import aima.core.logic.fol.CNFConverter;
import aima.core.logic.fol.SubsumptionElimination;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.Sentence;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class SubsumptionEliminationTest {

	@Test
	public void testFindSubsumedClauses() {
		// Taken from AIMA2e pg 679
		FOLDomain domain = new FOLDomain();
		domain.addPredicate("patrons");
		domain.addPredicate("hungry");
		domain.addPredicate("type");
		domain.addPredicate("fri_sat");
		domain.addPredicate("will_wait");
		domain.addConstant("Some");
		domain.addConstant("Full");
		domain.addConstant("French");
		domain.addConstant("Thai");
		domain.addConstant("Burger");
		FOLParser parser = new FOLParser(domain);

		String c1 = "patrons(v,Some)";
		String c2 = "patrons(v,Full) AND (hungry(v) AND type(v,French))";
		String c3 = "patrons(v,Full) AND (hungry(v) AND (type(v,Thai) AND fri_sat(v)))";
		String c4 = "patrons(v,Full) AND (hungry(v) AND type(v,Burger))";
		String sh = "FORALL v (will_wait(v) <=> (" + c1 + " OR (" + c2
				+ " OR (" + c3 + " OR (" + c4 + ")))))";

		Sentence s = parser.parse(sh);

		CNFConverter cnfConv = new CNFConverter(parser);

		CNF cnf = cnfConv.convertToCNF(s);

		// Contains 9 duplicates
		Assert.assertEquals(40, cnf.getNumberOfClauses());

		Set<Clause> clauses = new HashSet<Clause>(cnf.getConjunctionOfClauses());

		// duplicates removed
		Assert.assertEquals(31, clauses.size());

		clauses.removeAll(SubsumptionElimination.findSubsumedClauses(clauses));

		// subsumed clauses removed
		Assert.assertEquals(8, clauses.size());

		// Ensure only the 8 correct/expected clauses remain
		Clause cl1 = cnfConv
				.convertToCNF(
						parser
								.parse("(NOT(will_wait(v)) OR (patrons(v,Full) OR patrons(v,Some)))"))
				.getConjunctionOfClauses().get(0);
		Clause cl2 = cnfConv
				.convertToCNF(
						parser
								.parse("(NOT(will_wait(v)) OR (hungry(v) OR patrons(v,Some)))"))
				.getConjunctionOfClauses().get(0);
		Clause cl3 = cnfConv
				.convertToCNF(
						parser
								.parse("(NOT(will_wait(v)) OR (patrons(v,Some) OR (type(v,Burger) OR (type(v,French) OR type(v,Thai)))))"))
				.getConjunctionOfClauses().get(0);
		Clause cl4 = cnfConv
				.convertToCNF(
						parser
								.parse("(NOT(will_wait(v)) OR (fri_sat(v) OR (patrons(v,Some) OR (type(v,Burger) OR type(v,French)))))"))
				.getConjunctionOfClauses().get(0);
		Clause cl5 = cnfConv.convertToCNF(
				parser.parse("(NOT(patrons(v,Some)) OR will_wait(v))"))
				.getConjunctionOfClauses().get(0);
		Clause cl6 = cnfConv
				.convertToCNF(
						parser
								.parse("(NOT(hungry(v)) OR (NOT(patrons(v,Full)) OR (NOT(type(v,French)) OR will_wait(v))))"))
				.getConjunctionOfClauses().get(0);
		Clause cl7 = cnfConv
				.convertToCNF(
						parser
								.parse("(NOT(fri_sat(v)) OR (NOT(hungry(v)) OR (NOT(patrons(v,Full)) OR (NOT(type(v,Thai)) OR will_wait(v)))))"))
				.getConjunctionOfClauses().get(0);
		Clause cl8 = cnfConv
				.convertToCNF(
						parser
								.parse("(NOT(hungry(v)) OR (NOT(patrons(v,Full)) OR (NOT(type(v,Burger)) OR will_wait(v))))"))
				.getConjunctionOfClauses().get(0);

		Assert.assertTrue(clauses.contains(cl1));
		Assert.assertTrue(clauses.contains(cl2));
		Assert.assertTrue(clauses.contains(cl3));
		Assert.assertTrue(clauses.contains(cl4));
		Assert.assertTrue(clauses.contains(cl5));
		Assert.assertTrue(clauses.contains(cl6));
		Assert.assertTrue(clauses.contains(cl7));
		Assert.assertTrue(clauses.contains(cl8));
	}
}
