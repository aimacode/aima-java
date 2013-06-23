package aima.core.logic.propositional.kb;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;
import aima.core.environment.wumpusworld.TurnAction;
import aima.core.environment.wumpusworld.WumpusPercept;

/**
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 */
public class WumpusKnowledgeBase extends KnowledgeBase {

	private int dimRow;

	/**
	 * Create a Knowledge Base that contains the atemporal "wumpus physics" and
	 * temporal rules with time zero.
	 * 
	 * @param dimRow
	 *            dimension of a row of the field
	 * */
	public WumpusKnowledgeBase(int dimRow) {
		super();

		this.dimRow = dimRow;

		// First tile rules
		this.tell("( NOT W1s1 )");
		this.tell("( NOT P1s1 )");

		// Atemporal rules about breeze and stench
		for (int i = 1; i <= dimRow; i++) {
			for (int j = 1; j <= dimRow; j++) {

				int c = 0;
				String sentence_b_str = "( B" + i + "s" + j + " <=> ";
				String sentence_s_str = "( S" + i + "s" + j + " <=> ";
				if (i > 1) {
					sentence_b_str += "( P" + (i - 1) + "s" + j + " OR ";
					sentence_s_str += "( W" + (i - 1) + "s" + j + " OR ";
					c++;
				}
				if (i < dimRow) {
					sentence_b_str += "( P" + (i + 1) + "s" + j + " OR ";
					sentence_s_str += "( W" + (i + 1) + "s" + j + " OR ";
					c++;
				}
				if (j > 1) {
					if (j == dimRow) {
						sentence_b_str += "P" + i + "s" + (j - 1) + " ";
						sentence_s_str += "W" + i + "s" + (j - 1) + " ";
					} else {
						sentence_b_str += "( P" + i + "s" + (j - 1) + " OR ";
						sentence_s_str += "( W" + i + "s" + (j - 1) + " OR ";
						c++;
					}
				}
				if (j < dimRow) {
					sentence_b_str += "P" + i + "s" + (j + 1) + " ";
					sentence_s_str += "W" + i + "s" + (j + 1) + " ";
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
		for (int i = 1; i <= dimRow; i++) {
			for (int j = 1; j <= dimRow; j++) {
				if ((i == dimRow) && (j == dimRow))
					sentence_w_str += " W" + dimRow + "s" + dimRow + " ";
				else
					sentence_w_str += "( W" + i + "s" + j + " OR ";
			}
		}

		for (int i = 1; i < dimRow * dimRow; i++)
			sentence_w_str += ") ";

		this.tell(sentence_w_str);

		// Rule that describes existence of at most one Wumpus
		String sentence_w2_str;
		for (int i = 1; i <= dimRow; i++) {
			for (int j = 1; j <= dimRow; j++) {
				for (int u = 1; u <= dimRow; u++) {
					for (int v = 1; v <= dimRow; v++) {
						if (i != u || j != v) {
							sentence_w2_str = "( ( NOT W" + i + "s" + j
									+ " ) OR ( NOT W" + u + "s" + v + " ) )";
							this.tell(sentence_w2_str);
						}
					}
				}
			}
		}

		// temporal rules at time zero
		this.tell("L1s1s0");

		for (int i = 1; i <= dimRow; i++) {
			for (int j = 1; j <= dimRow; j++) {
				this.tell("( L" + i + "s" + j + "s0 => ( Breeze0 <=> B" + i
						+ "s" + j + " ) )");
				this.tell("( L" + i + "s" + j + "s0 => ( Stench0 <=> S" + i
						+ "s" + j + " ) )");

				if (i != 1 || j != 1)
					this.tell("( NOT L" + i + "s" + j + "s" + "0 )");
			}
		}

		this.tell("WumpusAlive0");
		this.tell("HaveArrow0");
		this.tell("FacingEast0");
		this.tell("( NOT FacingWest0 )");
		this.tell("( NOT FacingNorth0 )");
		this.tell("( NOT FacingSouth0 )");
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
		if (actionName.equals("Turn"))
			this.tell(actionName
					+ ((DynamicAction) a)
							.getAttribute(TurnAction.ATTRIBUTE_DIRECTION)
					+ time);
		else
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
	public void makePerceptSentence(WumpusPercept p, int time) {
		if (p.isStench())
			this.tell("Stench" + time);
		else
			this.tell("( NOT Stench" + time + " )");

		if (p.isBreeze())
			this.tell("Breeze" + time);
		else
			this.tell("( NOT Breeze" + time + " )");

		if (p.isGlitter())
			this.tell("Glitter" + time);
		else
			this.tell("( NOT Glitter" + time + " )");

		if (p.isBump())
			this.tell("Bump" + time);
		else
			this.tell("( NOT Bump" + time + " )");

		if (p.isScream())
			this.tell("Scream" + time);
		else
			this.tell("( NOT Scream" + time + " )");
	}

	/**
	 * Add to KB all the sentences about the state of wumpus world at time time
	 * 
	 * @param time
	 *            current time
	 */
	public void addTemporalSentences(int time) {

		// sentences with time zero were added by constructor
		if (time == 0)
			return;

		int t = time - 1;

		// current location rules (L2s2s3 represent tile 2,2 at time 3)
		// ex.: ( L2s2s3 <=> ( ( L2s2s2 AND ( ( NOT Forward2 ) OR Bump3 ) ) OR (
		// ( L1s2s2 AND ( FacingEast2 AND Forward2 ) ) OR ( L2s1s2 AND (
		// FacingNorth2 AND Forward2 ) ) )
		for (int i = 1; i <= dimRow; i++) {
			for (int j = 1; j <= dimRow; j++) {
				this.tell("( L" + i + "s" + j + "s" + time + " => ( Breeze"
						+ time + " <=> B" + i + "s" + j + " ) )");
				this.tell("( L" + i + "s" + j + "s" + time + " => ( Stench"
						+ time + " <=> S" + i + "s" + j + " ) )");

				String s = "( L" + i + "s" + j + "s" + time + " <=> ( ( L" + i
						+ "s" + j + "s" + t + " AND ( ( NOT Forward" + t
						+ " ) OR Bump" + time + " ) )";

				int c = 0;
				if (i != 1) {
					s += " OR ( ( L" + (i - 1) + "s" + j + "s" + t
							+ " AND ( FacingEast" + t + " AND Forward" + t
							+ " ) )";
					c++;
				}

				if (i != dimRow) {
					s += " OR ( ( L" + (i + 1) + "s" + j + "s" + t
							+ " AND ( FacingWest" + t + " AND Forward" + t
							+ " ) )";
					c++;
				}

				if (j != 1) {
					if (j == dimRow) {
						s += " OR ( L" + i + "s" + (j - 1) + "s" + t
								+ " AND ( FacingNorth" + t + " AND Forward" + t
								+ " ) )";
					} else {
						s += " OR ( ( L" + i + "s" + (j - 1) + "s" + t
								+ " AND ( FacingNorth" + t + " AND Forward" + t
								+ " ) )";
						c++;
					}
				}

				if (j != dimRow) {
					s += " OR ( L" + i + "s" + (j + 1) + "s" + t
							+ " AND ( FacingSouth" + t + " AND Forward" + t
							+ " ) )";
				}

				for (int k = 0; k <= c + 1; k++) {
					s += " )";
				}

				// add sentence about location i,j
				this.tell(s);

				// add sentence about safety of location i,j
				this.tell("( OK" + i + "s" + j + "s" + time + " <=> ( ( NOT P"
						+ i + "s" + j + " ) AND ( NOT ( W" + i + "s" + j
						+ " AND WumpusAlive" + time + " ) ) ) )");
			}
		}

		// Rules about current orientation
		// ex.: ( FacingEast3 <=> ( ( FacingNorth2 AND TurnRight2 ) OR ( (
		// FacingSouth2 AND TurnLeft2 ) OR ( FacingEast2 AND ( ( NOT TurnRight2
		// ) AND ( NOT TurnLeft2 ) ) ) ) ) )
		String A = "( FacingNorth" + t + " AND TurnRight" + t + " )";
		String B = "( FacingSouth" + t + " AND TurnLeft" + t + " )";
		String C = "( FacingEast" + t + " AND ( ( NOT TurnRight" + t
				+ " ) AND ( NOT TurnLeft" + t + " ) ) )";
		String s = "( FacingEast" + (t + 1) + " <=> ( " + A + " OR ( " + B
				+ " OR " + C + " ) ) )";
		this.tell(s);

		A = "( FacingNorth" + t + " AND TurnLeft" + t + " )";
		B = "( FacingSouth" + t + " AND TurnRight" + t + " )";
		C = "( FacingWest" + t + " AND ( ( NOT TurnRight" + t
				+ " ) AND ( NOT TurnLeft" + t + " ) ) )";
		s = "( FacingWest" + (t + 1) + " <=> ( " + A + " OR ( " + B + " OR "
				+ C + " ) ) )";
		this.tell(s);

		A = "( FacingEast" + t + " AND TurnLeft" + t + " )";
		B = "( FacingWest" + t + " AND TurnRight" + t + " )";
		C = "( FacingNorth" + t + " AND ( ( NOT TurnRight" + t
				+ " ) AND ( NOT TurnLeft" + t + " ) ) )";
		s = "( FacingNorth" + (t + 1) + " <=> ( " + A + " OR ( " + B + " OR "
				+ C + " ) ) )";
		this.tell(s);

		A = "( FacingWest" + t + " AND TurnLeft" + t + " )";
		B = "( FacingEast" + t + " AND TurnRight" + t + " )";
		C = "( FacingSouth" + t + " AND ( ( NOT TurnRight" + t
				+ " ) AND ( NOT TurnLeft" + t + " ) ) )";
		s = "( FacingSouth" + (t + 1) + " <=> ( " + A + " OR ( " + B + " OR "
				+ C + " ) ) )";
		this.tell(s);

		// Rules about last action
		this.tell("( Forward" + (t) + " <=> ( NOT TurnRight" + (t) + " ) )");
		this.tell("( Forward" + (t) + " <=> ( NOT TurnLeft" + (t) + " ) )");

		// Rule about the arrow
		this.tell("( HaveArrow" + time + " <=> ( HaveArrow" + (time - 1)
				+ " AND ( NOT Shot" + (time - 1) + " ) ) )");
		// Rule about wumpus (dead or alive)
		this.tell("( WumpusAlive" + time + " <=> ( WumpusAlive" + (time - 1)
				+ " AND ( NOT Scream" + time + " ) ) )");
	}
}
