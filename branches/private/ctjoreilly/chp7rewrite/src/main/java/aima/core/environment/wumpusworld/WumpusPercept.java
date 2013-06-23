package aima.core.environment.wumpusworld;

import aima.core.agent.Percept;

/**
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 */
public class WumpusPercept implements Percept {
	private boolean stench;
	private boolean breeze;
	private boolean glitter;
	private boolean bump;
	private boolean scream;
	
	public WumpusPercept() {
		setStench(false);
		setBreeze(false);
		setGlitter(false);
		setBump(false);
		setScream(false);		
	}
	
	public WumpusPercept(boolean s, boolean b, boolean g, boolean bu, boolean sc) {
		setStench(s);
		setBreeze(b);
		setGlitter(g);
		setBump(bu);
		setScream(sc);
	}

	public boolean isStench() {
		return stench;
	}

	public void setStench(boolean stench) {
		this.stench = stench;
	}

	public boolean isBreeze() {
		return breeze;
	}

	public void setBreeze(boolean breeze) {
		this.breeze = breeze;
	}

	public boolean isGlitter() {
		return glitter;
	}

	public void setGlitter(boolean glitter) {
		this.glitter = glitter;
	}

	public boolean isBump() {
		return bump;
	}

	public void setBump(boolean bump) {
		this.bump = bump;
	}

	public boolean isScream() {
		return scream;
	}

	public void setScream(boolean scream) {
		this.scream = scream;
	}
}
