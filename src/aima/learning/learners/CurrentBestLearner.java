package aima.learning.learners;

import java.util.ArrayList;
import java.util.List;

import aima.learning.framework.DataSet;
import aima.learning.framework.Example;
import aima.learning.framework.Learner;
import aima.learning.knowledge.CurrentBestLearning;
import aima.learning.knowledge.FOLDataSetDomain;
import aima.learning.knowledge.FOLExample;
import aima.learning.knowledge.Hypothesis;
import aima.logic.fol.inference.FOLModelElimination;
import aima.logic.fol.inference.InferenceResult;
import aima.logic.fol.kb.FOLKnowledgeBase;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class CurrentBestLearner implements Learner {
	private String trueGoalValue = null;
	private FOLDataSetDomain folDSDomain = null;
	private FOLKnowledgeBase kb = null;
	private Hypothesis currentBestHypothesis = null;

	//
	// PUBLIC METHODS
	//
	public CurrentBestLearner(String trueGoalValue) {
		this.trueGoalValue = trueGoalValue;
	}

	//
	// START-Learner
	public void train(DataSet ds) {
		folDSDomain = new FOLDataSetDomain(ds.specification, trueGoalValue);
		List<FOLExample> folExamples = new ArrayList<FOLExample>();
		int egNo = 1;
		for (Example e : ds.examples) {
			folExamples.add(new FOLExample(folDSDomain, e, egNo));
			egNo++;
		}

		// Setup a KB to be used for learning
		kb = new FOLKnowledgeBase(folDSDomain, new FOLModelElimination(1000));

		CurrentBestLearning cbl = new CurrentBestLearning(folDSDomain, kb);

		currentBestHypothesis = cbl.currentBestLearning(folExamples);
	}

	public String predict(Example e) {
		String prediction = "~" + e.targetValue();
		if (null != currentBestHypothesis) {
			FOLExample etp = new FOLExample(folDSDomain, e, 0);
			kb.clear();
			kb.tell(etp.getDescription());
			kb.tell(currentBestHypothesis.getHypothesis());

			InferenceResult ir = kb.ask(etp.getClassification());
			if (ir.isTrue()) {
				prediction = e.targetValue();
			} else if (ir.isUnknownDueToTimeout()) {
				// If unknown due to timeout then assume
				// is true by default, as can't prove
				// either way based on known hypothesis,
				// facts and available time.
				prediction = e.targetValue();
			}
		}

		return prediction;
	}

	public int[] test(DataSet ds) {
		int[] results = new int[] { 0, 0 };

		for (Example e : ds.examples) {
			if (e.targetValue().equals(predict(e))) {
				results[0] = results[0] + 1;
			} else {
				results[1] = results[1] + 1;
			}
		}
		return results;
	}
	// END-Learner
	//
}
