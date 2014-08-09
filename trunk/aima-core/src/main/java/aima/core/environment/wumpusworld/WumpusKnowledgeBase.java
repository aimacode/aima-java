package aima.core.environment.wumpusworld;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;
import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.parsing.ast.Connective;

/**
 * A Knowledge base tailored to the Wumpus World environment.
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 */
public class WumpusKnowledgeBase extends KnowledgeBase {

	private int caveXDimension;
	private int caveYDimension;

	/**
	 * Create a Knowledge Base that contains the atemporal "wumpus physics" and
	 * temporal rules with time zero.
	 * 
	 * @param caveXandYDimensions
	 *            x and y dimensions of the wumpus world's cave.
	 * */
	public WumpusKnowledgeBase(int caveXandYDimensions) {
		super();

		this.caveXDimension = caveXandYDimensions;
		this.caveYDimension = caveXandYDimensions;

		// First tile rules
		this.tell("( NOT W1s1 )");
		this.tell("( NOT P1s1 )");

		// Atemporal rules about breeze and stench
		for (int x = 1; x <= caveXDimension; x++) {
			for (int y = 1; y <= caveYDimension; y++) {

				int c = 0;
				String sentence_b_str = "( B_" + x + "_" + y + " "
						+ Connective.BICONDITIONAL + " ";
				String sentence_s_str = "( S_" + x + "_" + y + " "
						+ Connective.BICONDITIONAL + " ";
				if (x > 1) {
					sentence_b_str += "( P_" + (x - 1) + "_" + y + " "
							+ Connective.OR + " ";
					sentence_s_str += "( W_" + (x - 1) + "_" + y + " "
							+ Connective.OR + " ";
					c++;
				}
				if (x < caveXDimension) {
					sentence_b_str += "( P_" + (x + 1) + "_" + y + " OR ";
					sentence_s_str += "( W_" + (x + 1) + "_" + y + " OR ";
					c++;
				}
				if (y > 1) {
					if (y == caveYDimension) {
						sentence_b_str += "P_" + x + "_" + (y - 1) + " ";
						sentence_s_str += "W_" + x + "_" + (y - 1) + " ";
					} else {
						sentence_b_str += "( P_" + x + "_" + (y - 1) + " OR ";
						sentence_s_str += "( W_" + x + "_" + (y - 1) + " OR ";
						c++;
					}
				}
				if (y < caveYDimension) {
					sentence_b_str += "P_" + x + "_" + (y + 1) + " ";
					sentence_s_str += "W_" + x + "_" + (y + 1) + " ";
				}

				for (int k = 0; k < c; k++) {
					sentence_b_str += ") ";
					sentence_s_str += ") ";
				}
				sentence_b_str += ") ";
				sentence_s_str += ") ";

				this.tell(sentence_b_str);
				this.tell(sentence_s_str);
			}
		}

		// Rule that describes existence of at least one Wumpus
		String sentence_w_str = "";
		for (int x = 1; x <= caveXDimension; x++) {
			for (int y = 1; y <= caveYDimension; y++) {
				if ((x == caveXDimension) && (y == caveYDimension)) {
					sentence_w_str += " W_" + x + "_" + y + " ";
				} else {
					sentence_w_str += "( W_" + x + "_" + y + " "
							+ Connective.OR + " ";
				}
			}
		}

		for (int i = 1; i < caveXDimension * caveYDimension; i++) {
			sentence_w_str += ") ";
		}

		this.tell(sentence_w_str);

		// Rule that describes existence of at most one Wumpus
		String sentence_w2_str;
		for (int x = 1; x <= caveXDimension; x++) {
			for (int y = 1; y <= caveYDimension; y++) {
				for (int xi = 1; xi <= caveXDimension; xi++) {
					for (int yi = 1; yi <= caveYDimension; yi++) {
						if (x != xi || y != yi) {
							sentence_w2_str = "( ( " + Connective.NOT + " W_"
									+ x + "_" + y + " ) " + Connective.OR
									+ " ( " + Connective.NOT + " W_" + xi + "_"
									+ yi + " ) )";
							this.tell(sentence_w2_str);
						}
					}
				}
			}
		}

		// temporal rules at time zero
		this.tell("L0_1_1");

		for (int x = 1; x <= caveXDimension; x++) {
			for (int y = 1; y <= caveYDimension; y++) {
				this.tell("( L0_" + x + "_" + y + " " + Connective.IMPLICATION
						+ " ( Breeze0 " + Connective.BICONDITIONAL + " B_" + x
						+ "_" + y + " ) )");
				this.tell("( L0_" + x + "_" + y + " " + Connective.IMPLICATION
						+ " ( Stench0 " + Connective.BICONDITIONAL + " S_" + x
						+ "_" + y + " ) )");

				if (x != 1 || y != 1)
					this.tell("( " + Connective.NOT + " L0" + x + "_" + y
							+ " )");
			}
		}

		this.tell("WumpusAlive0");
		this.tell("HaveArrow0");
		this.tell("FacingEast0");
		this.tell("( " + Connective.NOT + " FacingWest0 )");
		this.tell("( " + Connective.NOT + " FacingNorth0 )");
		this.tell("( " + Connective.NOT + " FacingSouth0 )");
	}
	
	public boolean ask(String sentence) {
		throw new UnsupportedOperationException("TODO");
	}

	public int getCaveXDimension() {
		return caveXDimension;
	}

	public void setCaveXDimension(int caveXDimension) {
		this.caveXDimension = caveXDimension;
	}

	public int getCaveYDimension() {
		return caveYDimension;
	}

	public void setCaveYDimension(int caveYDimension) {
		this.caveYDimension = caveYDimension;
	}

	/**
	 * Add to KB sentences that describe the action a
	 * 
	 * @param a
	 *            action that must be added to KB
	 * @param time
	 *            current time
	 */
	public void makeActionSentence(Action a, int time) {
		String actionName = ((DynamicAction) a).getName();
		this.tell(actionName + time);
	}

	/**
	 * Add to KB sentences that describe the perception p
	 * 
	 * @param p
	 *            perception that must be added to KB
	 * @param time
	 *            current time
	 */
	public void makePerceptSentence(AgentPercept p, int time) {
		if (p.isStench()) {
			this.tell("Stench" + time);
		} else {
			this.tell("( " + Connective.NOT + " Stench" + time + " )");
		}

		if (p.isBreeze()) {
			this.tell("Breeze" + time);
		} else {
			this.tell("( " + Connective.NOT + " Breeze" + time + " )");
		}

		if (p.isGlitter()) {
			this.tell("Glitter" + time);
		} else {
			this.tell("( " + Connective.NOT + " Glitter" + time + " )");
		}

		if (p.isBump()) {
			this.tell("Bump" + time);
		} else {
			this.tell("( " + Connective.NOT + " Bump" + time + " )");
		}

		if (p.isScream()) {
			this.tell("Scream" + time);
		} else {
			this.tell("( " + Connective.NOT + " Scream" + time + " )");
		}
	}

	/**
	 * Add to KB all the sentences about the state of wumpus world at a time
	 * step.
	 * 
	 * @param time
	 *            current time step.
	 */
	public void addTemporalSentences(int time) {

		// sentences with time zero were added by constructor
		if (time == 0) {
			return;
		}

		int t = time - 1;

		// current location rules (L3_2_2 represent tile 2,2 at time 3)
		// ex.: ( L3_2_2 <=> ( ( L2_2_2 & ( ( ~Forward2 ) | Bump3 ) ) | (
		// ( L2_1_2 & ( FacingEast2 & Forward2 ) ) | ( L2_2_1 & (
		// FacingNorth2 & Forward2 ) ) )
		for (int x = 1; x <= caveXDimension; x++) {
			for (int y = 1; y <= caveYDimension; y++) {
				this.tell("( L" + time + "_" + x + "_" + y + " "
						+ Connective.IMPLICATION + " ( Breeze" + time + " "
						+ Connective.BICONDITIONAL + " B_" + x + "_" + y
						+ " ) )");
				this.tell("( L" + time + "_" + x + "_" + y + " "
						+ Connective.BICONDITIONAL + " ( Stench" + time + " "
						+ Connective.BICONDITIONAL + " S_" + x + "_" + y
						+ " ) )");

				String s = "( L" + time + "_" + x + "_" + y + " "
						+ Connective.BICONDITIONAL + " ( ( L" + t + "_" + x
						+ "_" + y + " " + Connective.AND + " ( ( "
						+ Connective.NOT + " Forward" + t + " ) "
						+ Connective.OR + " Bump" + time + " ) )";

				int c = 0;
				if (x != 1) {
					s += " " + Connective.OR + " ( ( L" + t + "_" + (x - 1)
							+ "_" + y + " " + Connective.AND + " ( FacingEast"
							+ t + " " + Connective.AND + " Forward" + t
							+ " ) )";
					c++;
				}

				if (x != caveXDimension) {
					s += " " + Connective.OR + " ( ( L" + t + "_" + (x + 1)
							+ "_" + y + " " + Connective.AND + " ( FacingWest"
							+ t + " " + Connective.AND + " Forward" + t
							+ " ) )";
					c++;
				}

				if (y != 1) {
					if (y == caveYDimension) {
						s += " " + Connective.OR + " ( L" + t + "_" + x + "_"
								+ (y - 1) + " " + Connective.AND
								+ " ( FacingNorth" + t + " " + Connective.AND
								+ " Forward" + t + " ) )";
					} else {
						s += " " + Connective.OR + " ( ( L" + t + "_" + x + "s"
								+ (y - 1) + " " + Connective.AND
								+ " ( FacingNorth" + t + " " + Connective.AND
								+ " Forward" + t + " ) )";
						c++;
					}
				}

				if (y != caveYDimension) {
					s += " OR ( L" + t + "_" + x + "_" + (y + 1) + " "
							+ Connective.AND + " ( FacingSouth" + t + " "
							+ Connective.AND + " Forward" + t + " ) )";
				}

				for (int k = 0; k <= c + 1; k++) {
					s += " )";
				}

				// add sentence about location i,j
				this.tell(s);

				// add sentence about safety of location i,j
				this.tell("( OK" + time + "_" + x + "_" + y + " "
						+ Connective.BICONDITIONAL + " ( ( " + Connective.NOT
						+ " P_" + x + "_" + y + " ) " + Connective.AND + " ( "
						+ Connective.NOT + " ( W_" + x + "_" + y + " "
						+ Connective.AND + " WumpusAlive" + time + " ) ) ) )");
			}
		}

		// Rules about current orientation
		// e.g.: ( FacingEast3 <=> ( ( FacingNorth2 & TurnRight2 ) | ( (
		// FacingSouth2 & TurnLeft2 ) | ( FacingEast2 & ( ( ~TurnRight2
		// ) & ( ~TurnLeft2 ) ) ) ) ) )
		String A = "( FacingNorth" + t + " " + Connective.AND + " TurnRight"
				+ t + " )";
		String B = "( FacingSouth" + t + " " + Connective.AND + " TurnLeft" + t
				+ " )";
		String C = "( FacingEast" + t + " " + Connective.AND + " ( ( "
				+ Connective.NOT + " TurnRight" + t + " ) " + Connective.AND
				+ " ( " + Connective.NOT + " TurnLeft" + t + " ) ) )";
		String s = "( FacingEast" + (t + 1) + " " + Connective.BICONDITIONAL
				+ " ( " + A + " " + Connective.OR + " ( " + B + " "
				+ Connective.OR + " " + C + " ) ) )";
		this.tell(s);

		A = "( FacingNorth" + t + " " + Connective.AND + " TurnLeft" + t + " )";
		B = "( FacingSouth" + t + " " + Connective.AND + " TurnRight" + t
				+ " )";
		C = "( FacingWest" + t + " " + Connective.AND + " ( ( "
				+ Connective.NOT + " TurnRight" + t + " ) " + Connective.AND
				+ " ( " + Connective.NOT + " TurnLeft" + t + " ) ) )";
		s = "( FacingWest" + (t + 1) + " " + Connective.BICONDITIONAL + " ( "
				+ A + " " + Connective.OR + " ( " + B + " " + Connective.OR
				+ " " + C + " ) ) )";
		this.tell(s);

		A = "( FacingEast" + t + " " + Connective.AND + " TurnLeft" + t + " )";
		B = "( FacingWest" + t + " " + Connective.AND + " TurnRight" + t + " )";
		C = "( FacingNorth" + t + " " + Connective.AND + " ( ( "
				+ Connective.NOT + " TurnRight" + t + " ) " + Connective.AND
				+ " ( " + Connective.NOT + " TurnLeft" + t + " ) ) )";
		s = "( FacingNorth" + (t + 1) + " " + Connective.BICONDITIONAL + " ( "
				+ A + " " + Connective.OR + " ( " + B + " " + Connective.OR
				+ " " + C + " ) ) )";
		this.tell(s);

		A = "( FacingWest" + t + " " + Connective.AND + " TurnLeft" + t + " )";
		B = "( FacingEast" + t + " " + Connective.AND + " TurnRight" + t + " )";
		C = "( FacingSouth" + t + " " + Connective.AND + " ( ( "
				+ Connective.NOT + " TurnRight" + t + " ) " + Connective.AND
				+ " ( " + Connective.NOT + " TurnLeft" + t + " ) ) )";
		s = "( FacingSouth" + (t + 1) + " " + Connective.BICONDITIONAL + " ( "
				+ A + " " + Connective.OR + " ( " + B + " " + Connective.OR
				+ " " + C + " ) ) )";
		this.tell(s);

		// Rules about last action
		this.tell("( Forward" + (t) + " " + Connective.BICONDITIONAL + " ( "
				+ Connective.NOT + " TurnRight" + (t) + " ) )");
		this.tell("( Forward" + (t) + " " + Connective.BICONDITIONAL + " ( "
				+ Connective.NOT + " TurnLeft" + (t) + " ) )");

		// Rule about the arrow
		this.tell("( HaveArrow" + time + " " + Connective.BICONDITIONAL
				+ " ( HaveArrow" + (time - 1) + " " + Connective.AND + " ( "
				+ Connective.NOT + " Shot" + (time - 1) + " ) ) )");
		// Rule about wumpus (dead or alive)
		this.tell("( WumpusAlive" + time + " " + Connective.BICONDITIONAL
				+ " ( WumpusAlive" + (time - 1) + " " + Connective.AND + " ( "
				+ Connective.NOT + " Scream" + time + " ) ) )");
	}
}
