package aima.test.core.unit.logic.fol;

import org.junit.Assert;
import org.junit.Test;

import aima.core.logic.fol.CNFConverter;
import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.Sentence;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class CNFConverterTest {

	@Test
	public void testExamplePg295AIMA2e() {
		FOLDomain domain = DomainFactory.weaponsDomain();
		FOLParser parser = new FOLParser(domain);

		Sentence origSentence = parser
				.parse("FORALL x ((((American(x) AND Weapon(y)) AND Sells(x, y, z)) AND Hostile(z)) => Criminal(x))");

		CNFConverter cnfConv = new CNFConverter(parser);

		CNF cnf = cnfConv.convertToCNF(origSentence);

		Assert
				.assertEquals(
						"[~American(x), ~Hostile(z), ~Sells(x,y,z), ~Weapon(y), Criminal(x)]",
						cnf.toString());
	}

	@Test
	public void testExamplePg296AIMA2e() {
		FOLDomain domain = DomainFactory.lovesAnimalDomain();
		FOLParser parser = new FOLParser(domain);

		Sentence origSentence = parser
				.parse("FORALL x (FORALL y (Animal(y) => Loves(x, y)) => EXISTS y Loves(y, x))");

		CNFConverter cnfConv = new CNFConverter(parser);

		CNF cnf = cnfConv.convertToCNF(origSentence);

		Assert
				.assertEquals(
						"[Animal(SF0(x)), Loves(SF1(x),x)],[~Loves(x,SF0(x)), Loves(SF1(x),x)]",
						cnf.toString());
	}

	@Test
	public void testExamplesPg299AIMA2e() {
		FOLDomain domain = DomainFactory.lovesAnimalDomain();
		FOLParser parser = new FOLParser(domain);

		// FOL A.
		Sentence origSentence = parser
				.parse("FORALL x (FORALL y (Animal(y) => Loves(x, y)) => EXISTS y Loves(y, x))");

		CNFConverter cnfConv = new CNFConverter(parser);

		CNF cnf = cnfConv.convertToCNF(origSentence);

		// CNF A1. and A2.
		Assert
				.assertEquals(
						"[Animal(SF0(x)), Loves(SF1(x),x)],[~Loves(x,SF0(x)), Loves(SF1(x),x)]",
						cnf.toString());

		// FOL B.
		origSentence = parser
				.parse("FORALL x (EXISTS y (Animal(y) AND Kills(x, y)) => FORALL z NOT(Loves(z, x)))");

		cnf = cnfConv.convertToCNF(origSentence);

		// CNF B.
		Assert.assertEquals("[~Animal(y), ~Kills(x,y), ~Loves(z,x)]", cnf
				.toString());

		// FOL C.
		origSentence = parser.parse("FORALL x (Animal(x) => Loves(Jack, x))");

		cnf = cnfConv.convertToCNF(origSentence);

		// CNF C.
		Assert.assertEquals("[~Animal(x), Loves(Jack,x)]", cnf.toString());

		// FOL D.
		origSentence = parser
				.parse("(Kills(Jack, Tuna) OR Kills(Curiosity, Tuna))");

		cnf = cnfConv.convertToCNF(origSentence);

		// CNF D.
		Assert.assertEquals("[Kills(Curiosity,Tuna), Kills(Jack,Tuna)]", cnf
				.toString());

		// FOL E.
		origSentence = parser.parse("Cat(Tuna)");

		cnf = cnfConv.convertToCNF(origSentence);

		// CNF E.
		Assert.assertEquals("[Cat(Tuna)]", cnf.toString());

		// FOL F.
		origSentence = parser.parse("FORALL x (Cat(x) => Animal(x))");

		cnf = cnfConv.convertToCNF(origSentence);

		// CNF F.
		Assert.assertEquals("[~Cat(x), Animal(x)]", cnf.toString());

		// FOL G.
		origSentence = parser.parse("NOT(Kills(Curiosity, Tuna))");

		cnf = cnfConv.convertToCNF(origSentence);

		// CNF G.
		Assert.assertEquals("[~Kills(Curiosity,Tuna)]", cnf.toString());
	}

	@Test
	public void testNestedExistsAndOrs() {
		FOLDomain domain = new FOLDomain();
		domain.addPredicate("P");
		domain.addPredicate("R");
		domain.addPredicate("Q");

		FOLParser parser = new FOLParser(domain);

		Sentence origSentence = parser
				.parse("EXISTS w (FORALL x ( (EXISTS z (Q(w, z))) => (EXISTS y (NOT(P(x, y)) AND R(y))) ) )");

		CNFConverter cnfConv = new CNFConverter(parser);

		CNF cnf = cnfConv.convertToCNF(origSentence);

		Assert.assertEquals("[~P(x,SF0(x)), ~Q(SC0,z)],[~Q(SC0,z), R(SF0(x))]",
				cnf.toString());

		// Ax.Ay.(p(x,y) => Ez.(q(x,y,z)))
		origSentence = parser
				.parse("FORALL x1 (FORALL y1 (P(x1, y1) => EXISTS z1 (Q(x1, y1, z1))))");

		cnf = cnfConv.convertToCNF(origSentence);

		Assert.assertEquals("[~P(x1,y1), Q(x1,y1,SF1(x1,y1))]", cnf.toString());

		// Ex.Ay.Az.(r(y,z) <=> q(x,y,z))
		origSentence = parser
				.parse("EXISTS x2 (FORALL y2 (FORALL z2 (R(y2, z2) <=> Q(x2, y2, z2))))");

		cnf = cnfConv.convertToCNF(origSentence);

		Assert.assertEquals(
				"[~R(y2,z2), Q(SC1,y2,z2)],[~Q(SC1,y2,z2), R(y2,z2)]", cnf
						.toString());

		// Ax.Ey.(~p(x,y) => Az.(q(x,y,z)))
		origSentence = parser
				.parse("FORALL x3 (EXISTS y3 (NOT(P(x3, y3)) => FORALL z3 (Q(x3, y3, z3))))");

		cnf = cnfConv.convertToCNF(origSentence);

		Assert
				.assertEquals("[P(x3,SF2(x3)), Q(x3,SF2(x3),z3)]", cnf
						.toString());

		// Ew.Ex.Ey.Ez.(r(x,y) & q(x,w,z))
		origSentence = parser
				.parse("NOT(EXISTS w4 (EXISTS x4 (EXISTS y4 ( EXISTS z4 (R(x4, y4) AND Q(x4, w4, z4))))))");

		cnf = cnfConv.convertToCNF(origSentence);

		Assert.assertEquals("[~Q(x4,w4,z4), ~R(x4,y4)]", cnf.toString());
	}

	@Test
	public void testImplicationsAndExtendedAndsOrs() {
		FOLDomain domain = new FOLDomain();
		domain.addPredicate("Cheat");
		domain.addPredicate("Extra");
		domain.addPredicate("Knows");
		domain.addPredicate("Diff");
		domain.addPredicate("F");
		domain.addPredicate("A");
		domain.addPredicate("Probation");
		domain.addPredicate("Award");

		FOLParser parser = new FOLParser(domain);
		CNFConverter cnfConv = new CNFConverter(parser);

		// cheat(x,y) => f(x,y)
		Sentence def1 = parser.parse("(Cheat(x,y) => F(x,y))");
		CNF cnfDef1 = cnfConv.convertToCNF(def1);

		Assert.assertEquals("[~Cheat(x,y), F(x,y)]", cnfDef1.toString());

		// extra(x,y) | knows(x) => a(x,y)
		Sentence def2 = parser.parse("((Extra(x,y) OR Knows(x)) => A(x,y))");
		CNF cnfDef2 = cnfConv.convertToCNF(def2);

		Assert.assertEquals("[~Extra(x,y), A(x,y)],[~Knows(x), A(x,y)]",
				cnfDef2.toString());

		// f(x,y) & f(x,z) & diff(y,z) <=> probation(x)
		Sentence def3 = parser
				.parse("(((NOT(((F(x,y) AND F(x,z)) AND Diff(y,z)))) OR Probation(x)) AND (((F(x,y) AND F(x,z)) AND Diff(y,z)) OR NOT(Probation(x))))");
		CNF cnfDef3 = cnfConv.convertToCNF(def3);

		Assert
				.assertEquals(
						"[~Diff(y,z), ~F(x,y), ~F(x,z), Probation(x)],[~Probation(x), F(x,y)],[~Probation(x), F(x,z)],[~Probation(x), Diff(y,z)]",
						cnfDef3.toString());

		// a(x,y) & a(x,z) & diff(y,z) <=> award(x)
		Sentence def4 = parser
				.parse("(((NOT(((A(x,y) AND A(x,z)) AND Diff(y,z)))) OR Award(x)) AND (((A(x,y) AND A(x,z)) AND Diff(y,z)) OR NOT(Award(x))))");
		CNF cnfDef4 = cnfConv.convertToCNF(def4);

		Assert
				.assertEquals(
						"[~A(x,y), ~A(x,z), ~Diff(y,z), Award(x)],[~Award(x), A(x,y)],[~Award(x), A(x,z)],[~Award(x), Diff(y,z)]",
						cnfDef4.toString());

		// f(x,y) <=> ~a(x,y)
		Sentence def5 = parser
				.parse("( ( NOT(F(x,y)) OR NOT(A(x,y))) AND ( F(x,y) OR NOT(NOT(A(x,y))) ) )");
		CNF cnfDef5 = cnfConv.convertToCNF(def5);

		Assert.assertEquals("[~A(x,y), ~F(x,y)],[A(x,y), F(x,y)]", cnfDef5
				.toString());
	}

	@Test
	public void testNegationsAndNestedImplications() {
		FOLDomain domain = new FOLDomain();
		domain.addPredicate("P");
		domain.addPredicate("Q");
		domain.addPredicate("R");
		domain.addConstant("A");

		FOLParser parser = new FOLParser(domain);
		CNFConverter cnfConv = new CNFConverter(parser);

		// ~(((~p or ~q) => ~(p or q)) => r)
		Sentence sent = parser
				.parse("NOT(((((NOT(P(A)) OR NOT(Q(A)))) => NOT((P(A) OR Q(A)))) => R(A)))");
		CNF cnf = cnfConv.convertToCNF(sent);

		Assert
				.assertEquals(
						"[~P(A), P(A)],[~P(A), Q(A)],[~Q(A), P(A)],[~Q(A), Q(A)],[~R(A)]",
						cnf.toString());
	}

	@Test
	public void testInductionAxiomSchema() {
		FOLDomain domain = new FOLDomain();
		domain.addPredicate("Equal");
		domain.addFunction("Plus");
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("N");
		domain.addConstant("ONE");
		domain.addConstant("ZERO");

		FOLParser parser = new FOLParser(domain);
		CNFConverter cnfConv = new CNFConverter(parser);

		// Base Case:
		Sentence sent = parser
				.parse("NOT(FORALL x (FORALL y (Equal(Plus(Plus(x,y),ZERO), Plus(x,Plus(y,ZERO))))))");
		CNF cnf = cnfConv.convertToCNF(sent);
		Assert.assertEquals(
				"[~Equal(Plus(Plus(SC0,SC1),ZERO),Plus(SC0,Plus(SC1,ZERO)))]",
				cnf.toString());

		// Instance of Induction Axion Scmema
		sent = parser
				.parse("(("
						+ "Equal(Plus(Plus(A,B),ZERO), Plus(A,Plus(B,ZERO)))"
						+ " AND "
						+ "(FORALL x (FORALL y (FORALL z("
						+ "Equal(Plus(Plus(x,y),z), Plus(x,Plus(y,z)))"
						+ " => "
						+ "Equal(Plus(Plus(x,y),Plus(z,ONE)), Plus(x,Plus(y,Plus(z,ONE))))"
						+ "))))" + ")" + " => "
						+ "FORALL x (FORALL y (FORALL z("
						+ "Equal(Plus(Plus(x,y),z), Plus(x,Plus(y,z)))"
						+ "))))");
		cnf = cnfConv.convertToCNF(sent);
		Assert
				.assertEquals(
						"[~Equal(Plus(Plus(A,B),ZERO),Plus(A,Plus(B,ZERO))), Equal(Plus(Plus(q0,q1),q2),Plus(q0,Plus(q1,q2))), Equal(Plus(Plus(SC2,SC3),SC4),Plus(SC2,Plus(SC3,SC4)))],[~Equal(Plus(Plus(A,B),ZERO),Plus(A,Plus(B,ZERO))), ~Equal(Plus(Plus(SC2,SC3),Plus(SC4,ONE)),Plus(SC2,Plus(SC3,Plus(SC4,ONE)))), Equal(Plus(Plus(q0,q1),q2),Plus(q0,Plus(q1,q2)))]",
						cnf.toString());

		// Goal
		sent = parser
				.parse("NOT(FORALL x (FORALL y (FORALL z (Equal(Plus(Plus(x,y),z), Plus(x,Plus(y,z)))))))");
		cnf = cnfConv.convertToCNF(sent);
		Assert.assertEquals(
				"[~Equal(Plus(Plus(SC5,SC6),SC7),Plus(SC5,Plus(SC6,SC7)))]",
				cnf.toString());
	}

	@Test
	public void testTermEquality() {
		FOLDomain domain = new FOLDomain();
		domain.addPredicate("P");
		domain.addPredicate("Q");
		domain.addPredicate("R");
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addConstant("D");
		domain.addFunction("Plus");
		domain.addConstant("ONE");
		domain.addConstant("ZERO");

		FOLParser parser = new FOLParser(domain);
		CNFConverter cnfConv = new CNFConverter(parser);

		// x=y
		Sentence sent = parser.parse("x = y");
		CNF cnf = cnfConv.convertToCNF(sent);

		Assert.assertEquals("[x = y]", cnf.toString());

		// x!=y
		sent = parser.parse("NOT(x = y)");
		cnf = cnfConv.convertToCNF(sent);

		Assert.assertEquals("[~x = y]", cnf.toString());

		// A=B
		sent = parser.parse("A = B");
		cnf = cnfConv.convertToCNF(sent);

		Assert.assertEquals("[A = B]", cnf.toString());

		// A!=B
		sent = parser.parse("NOT(A = B)");
		cnf = cnfConv.convertToCNF(sent);

		Assert.assertEquals("[~A = B]", cnf.toString());

		// ~(((~A=B or ~D=C) => ~(A=B or D=C)) => A=D)
		sent = parser
				.parse("NOT(((((NOT(A = B) OR NOT(D = C))) => NOT((A = B OR D = C))) => A = D))");
		cnf = cnfConv.convertToCNF(sent);

		Assert
				.assertEquals(
						"[~A = B, A = B],[~A = B, D = C],[~D = C, A = B],[~D = C, D = C],[~A = D]",
						cnf.toString());

		//
		// Induction Axiom Schema using Term Equality

		// Base Case:
		sent = parser
				.parse("NOT(FORALL x (FORALL y (Plus(Plus(x,y),ZERO) = Plus(x,Plus(y,ZERO)))))");
		cnf = cnfConv.convertToCNF(sent);
		Assert.assertEquals(
				"[~Plus(Plus(SC0,SC1),ZERO) = Plus(SC0,Plus(SC1,ZERO))]", cnf
						.toString());

		// Instance of Induction Axion Scmema
		sent = parser.parse("(("
				+ "Plus(Plus(A,B),ZERO) = Plus(A,Plus(B,ZERO))" + " AND "
				+ "(FORALL x (FORALL y (FORALL z("
				+ "Plus(Plus(x,y),z) = Plus(x,Plus(y,z))" + " => "
				+ "Plus(Plus(x,y),Plus(z,ONE)) = Plus(x,Plus(y,Plus(z,ONE)))"
				+ "))))" + ")" + " => " + "FORALL x (FORALL y (FORALL z("
				+ "Plus(Plus(x,y),z) = Plus(x,Plus(y,z))" + "))))");
		cnf = cnfConv.convertToCNF(sent);
		Assert
				.assertEquals(
						"[~Plus(Plus(A,B),ZERO) = Plus(A,Plus(B,ZERO)), Plus(Plus(q0,q1),q2) = Plus(q0,Plus(q1,q2)), Plus(Plus(SC2,SC3),SC4) = Plus(SC2,Plus(SC3,SC4))],[~Plus(Plus(A,B),ZERO) = Plus(A,Plus(B,ZERO)), ~Plus(Plus(SC2,SC3),Plus(SC4,ONE)) = Plus(SC2,Plus(SC3,Plus(SC4,ONE))), Plus(Plus(q0,q1),q2) = Plus(q0,Plus(q1,q2))]",
						cnf.toString());

		// Goal
		sent = parser
				.parse("NOT(FORALL x (FORALL y (FORALL z (Plus(Plus(x,y),z) = Plus(x,Plus(y,z))))))");
		cnf = cnfConv.convertToCNF(sent);
		Assert.assertEquals(
				"[~Plus(Plus(SC5,SC6),SC7) = Plus(SC5,Plus(SC6,SC7))]", cnf
						.toString());
	}
}
