package aima.gui.swing.applications.search.games;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import aima.core.environment.connectfour.ConnectFourAIPlayer;
import aima.core.environment.connectfour.ConnectFourGame;
import aima.core.environment.connectfour.ConnectFourState;
import aima.core.search.adversarial.AdversarialSearch;
import aima.core.search.adversarial.AlphaBetaSearch;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import aima.core.search.adversarial.MinimaxSearch;
import aima.core.search.framework.Metrics;

/**
 * Simple graphical Connect Four game application. It demonstrates the Minimax
 * algorithm with alpha-beta pruning, iterative deepening, and action ordering.
 * The implemented action ordering strategy tries to maximize the impact of the
 * chosen action for later game phases.
 * 
 * @author Ruediger Lunde
 */
public class ConnectFourApp {

	/** Used for integration into the universal demo application. */
	public JFrame constructApplicationFrame() {
		JFrame frame = new JFrame();
		JPanel panel = new ConnectFourPanel();
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return frame;
	}

	/** Application starter. */
	public static void main(String[] args) {
		JFrame frame = new ConnectFourApp().constructApplicationFrame();
		frame.setSize(450, 450);
		frame.setVisible(true);
	}

	/** Simple panel to control the game. */
	private static class ConnectFourPanel extends JPanel implements
			ActionListener {
		private static final long serialVersionUID = 1L;
		JComboBox<String> strategyCombo;
		JComboBox<String> timeCombo;
		JButton clearButton;
		JButton proposeButton;
		JLabel statusBar;

		ConnectFourGame game;
		ConnectFourState currState;
		Metrics searchMetrics;

		/** Standard constructor. */
		ConnectFourPanel() {
			game = new ConnectFourGame();
			currState = game.getInitialState();
			setLayout(new BorderLayout());
			setBackground(Color.BLUE);

			JToolBar toolBar = new JToolBar();
			toolBar.setFloatable(false);
			strategyCombo = new JComboBox<String>(new String[] {
					"Minimax (not recommended)",
					"Alpha-Beta (not recommended)",
					"Iterative Deepening Alpha-Beta", "Advanced Alpha-Beta",
					"Advanced Alpha-Beta (log)" });
			strategyCombo.setSelectedIndex(3);
			toolBar.add(strategyCombo);
			timeCombo = new JComboBox<String>(new String[] { "5sec", "10sec", "15sec",
					"20sec" });
			timeCombo.setSelectedIndex(0);
			toolBar.add(timeCombo);
			toolBar.add(Box.createHorizontalGlue());
			clearButton = new JButton("Clear");
			clearButton.addActionListener(this);
			toolBar.add(clearButton);
			proposeButton = new JButton("Propose Move");
			proposeButton.addActionListener(this);
			toolBar.add(proposeButton);
			add(toolBar, BorderLayout.NORTH);

			int rows = currState.getRows();
			int cols = currState.getCols();
			JPanel boardPanel = new JPanel();
			boardPanel.setLayout(new GridLayout(rows, cols, 5, 5));
			boardPanel.setBorder(BorderFactory.createEtchedBorder());
			boardPanel.setBackground(Color.BLUE);
			for (int i = 0; i < rows * cols; i++) {
				GridElement element = new GridElement(i / cols, i % cols);
				boardPanel.add(element);
				element.addActionListener(this);
			}
			add(boardPanel, BorderLayout.CENTER);

			statusBar = new JLabel(" ");
			statusBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			add(statusBar, BorderLayout.SOUTH);

			updateStatus();
		}

		/** Handles all button events and updates the view. */
		@Override
		public void actionPerformed(ActionEvent e) {
			searchMetrics = null;
			if (e == null || e.getSource() == clearButton) {
				currState = game.getInitialState();
			} else if (!game.isTerminal(currState)) {
				if (e.getSource() == proposeButton) {
					proposeMove();
				} else if (e.getSource() instanceof GridElement) {
					GridElement el = (GridElement) e.getSource();
					currState = game.getResult(currState, el.col); // take next
																	// turn
				}
			}
			repaint(); // paint all disks!
			updateStatus();
		}

		/** Uses adversarial search for selecting the next action. */
		private void proposeMove() {
			Integer action;
			int time = (timeCombo.getSelectedIndex() + 1) * 5;
			AdversarialSearch<ConnectFourState, Integer> search;
			switch (strategyCombo.getSelectedIndex()) {
			case 0:
				search = MinimaxSearch.createFor(game);
				break;
			case 1:
				search = AlphaBetaSearch.createFor(game);
				break;
			case 2:
				search = IterativeDeepeningAlphaBetaSearch.createFor(game, 0.0,
						1.0, time);
				break;
			case 3:
				search = new ConnectFourAIPlayer(game, time);
				break;
			default:
				search = new ConnectFourAIPlayer(game, time);
				((ConnectFourAIPlayer) search).setLogEnabled(true);
			}
			action = search.makeDecision(currState);
			searchMetrics = search.getMetrics();
			currState = game.getResult(currState, action);
		}

		/** Updates the status bar. */
		private void updateStatus() {
			String statusText;
			if (!game.isTerminal(currState)) {
				String toMove = (String) game.getPlayer(currState);
				statusText = "Next move: " + toMove;
				statusBar.setForeground(toMove.equals("red") ? Color.RED
						: Color.YELLOW);
			} else {
				String winner = null;
				for (int i = 0; i < 2; i++)
					if (game.getUtility(currState, game.getPlayers()[i]) == 1)
						winner = game.getPlayers()[i];
				if (winner != null)
					statusText = "Color " + winner
							+ " has won. Congratulations!";
				else
					statusText = "No winner :-(";
				statusBar.setForeground(Color.WHITE);
			}
			if (searchMetrics != null)
				statusText += "    " + searchMetrics;
			statusBar.setText(statusText);
		}

		/** Represents a space within the grid where discs can be placed. */
		@SuppressWarnings("serial")
		private class GridElement extends JButton {
			int row;
			int col;

			GridElement(int row, int col) {
				this.row = row;
				this.col = col;
				setBackground(Color.BLUE);
			}

			public void paintComponent(Graphics g) {
				super.paintComponent(g); // should have look and feel of a
											// button...
				int playerNum = currState.getPlayerNum(row, col);
				if (playerNum != 0) {
					drawDisk(g, playerNum); // draw disk on top!
				}
				for (int pNum = 1; pNum <= 2; pNum++)
					if (currState.isWinPositionFor(row, col, pNum))
						drawWinSituation(g, pNum);
			}

			/** Fills a simple oval. */
			void drawDisk(Graphics g, int playerNum) {
				int size = Math.min(getWidth(), getHeight());
				g.setColor(playerNum == 1 ? Color.RED : Color.YELLOW);
				g.fillOval((getWidth() - size) / 2, (getHeight() - size) / 2,
						size, size);
			}

			/** Draws a simple oval. */
			void drawWinSituation(Graphics g, int playerNum) {
				int size = Math.min(getWidth(), getHeight());
				g.setColor(playerNum == 1 ? Color.RED : Color.YELLOW);
				g.drawOval((getWidth() - size) / 2 + playerNum,
						(getHeight() - size) / 2 + playerNum, size - 2
								* playerNum, size - 2 * playerNum);
			}
		}
	}
}
