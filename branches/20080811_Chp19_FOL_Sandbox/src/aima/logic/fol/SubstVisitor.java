/*
 * Created on Sep 20, 2004
 *
 */
package aima.logic.fol;

import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import aima.logic.fol.parsing.AbstractFOLVisitor;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.QuantifiedSentence;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Variable;
import aima.util.Converter;
import aima.util.LogicUtils;
import aima.util.SetOps;

/**
 * @author Ravi Mohan
 * 
 */

public class SubstVisitor extends AbstractFOLVisitor {
	Sentence substitutedSentence = null;

	Sentence originalSentence = null;

	private FOLParser parser;

	public SubstVisitor(FOLParser parser) {
		super(parser);
	}

	@Override
	public Object visitVariable(Variable variable, Object arg) {
		String value = variable.getValue();
		Properties substs = (Properties) arg;
		if (substs.keySet().contains(value)) {
			String key = variable.getValue();
			return new Variable(substs.getProperty(key));
		}
		return variable;

	}

	@Override
	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {
		// TODO - change properties for hashtable
		Hashtable<String, String> props = (Hashtable<String, String>) arg;
		Sentence quantified = sentence.getQuantified();
		Sentence quantifiedAfterSubs = (Sentence) quantified.accept(this, arg);
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Set<String> sentenceVariables = new
		// Converter<String>().listToSet(sentence
		// .getVariablesAsString());
		// Set unmatchedVariables = Util.difference(sentenceVariables, props
		// .keySet());
		// **********************************
		Set<String> sentenceVariablesStr = new Converter<String>()
				.listToSet(sentence.getVariablesAsString());
		Set<Variable> sentenceVariables = LogicUtils
				.stringsToVariables(sentenceVariablesStr);
		Set<Variable> propKeysVariables = LogicUtils.stringsToVariables(props
				.keySet());
		Set<Variable> unmatchedVariables = new SetOps<Variable>().difference(
				sentenceVariables, propKeysVariables);
		//		
		// *******************************************************
		// System.out.println("senArs = "+sentenceVariables);
		// System.out.println("props = "+props.keySet());
		// System.out.println("umatched = "+unmatchedVariables+"\n");

		if (!(unmatchedVariables.isEmpty())) {
			List<Variable> variables = new Converter<Variable>()
					.setToList(unmatchedVariables);
			QuantifiedSentence sen = new QuantifiedSentence(sentence
					.getQuantifier(), variables, quantifiedAfterSubs);
			// System.out.println(sen);
			return sen;
		} else {
			return recreate(quantifiedAfterSubs);

		}

	}

	public Sentence getSubstitutedSentence(Sentence beforeSubst, Properties p) {
		// System.out.println(beforeSubst.toString());
		Sentence sen = (Sentence) beforeSubst.accept(this, p);
		// System.out.println(sen.toString());
		Sentence afterSubst = recreate(sen);
		// System.out.println(afterSubst.toString());
		// System.out.println("***");
		return afterSubst;

	}

}