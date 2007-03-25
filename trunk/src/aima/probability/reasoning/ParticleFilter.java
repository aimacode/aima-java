package aima.probability.reasoning;

import aima.probability.RandomVariable;

public class ParticleFilter {
    private HiddenMarkovModel hmm;
    private int numberOfParticles;

    public ParticleFilter(HiddenMarkovModel hmm , int numberOfParticles){
	this.hmm = hmm;
	this.numberOfParticles = numberOfParticles;
    }
    
    public RandomVariable filter(String perception){
	return null;
    }
    
    

}
