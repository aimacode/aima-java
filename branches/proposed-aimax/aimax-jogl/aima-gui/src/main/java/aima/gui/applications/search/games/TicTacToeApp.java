package aima.gui.applications.search.games;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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

import aima.core.environment.tictactoe.TicTacToe;
import aima.core.environment.tictactoe.TicTacToeBoard;
import aima.core.search.adversarial.GameState;

/**
 * Simple graphical Tic-Tac-Toe game application. It demonstrates the Minimax
 * algorithm for move selection as well as alpha-beta pruning.
 * 
 * @author Ruediger Lunde
 */
public class TicTacToeApp {
	
	/** Used for integration into the universal demo application. */
	public JFrame constructApplicationFrame() {
		JFrame frame = new JFrame();
		JPanel panel =  new TicTacToePanel();
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return frame;
	}
	
	/** Application starter. */
	public static void main(String[] args) {
		JFrame frame = new TicTacToeApp().constructApplicationFrame();
		frame.setSize(400, 400);
		frame.setVisible(true);
	}
	
	/** Simple panel to control the game. */
	private static class TicTacToePanel extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;
		JComboBox strategy;
		JButton clear;
		JButton proposal;
		JButton[] squares;
		JLabel status;
		TicTacToe game;
		
		/** Standard constructor. */
		TicTacToePanel() {
			this.setLayout(new BorderLayout());
			JToolBar tbar = new JToolBar();
			tbar.setFloatable(false);
			strategy = new JComboBox(new String[]{"Minimax", "Alpha-Beta"});
			strategy.setSelectedIndex(1);
			tbar.add(strategy);
			tbar.add(Box.createHorizontalGlue());
			clear = new JButton("Clear");
			clear.addActionListener(this);
			tbar.add(clear);
			proposal = new JButton("Propose Move");
			proposal.addActionListener(this);
			tbar.add(proposal);
			
			add(tbar, BorderLayout.NORTH);
			JPanel spanel = new JPanel();
			spanel.setLayout(new GridLayout(3,3));
			add(spanel, BorderLayout.CENTER);
			squares = new JButton[9];
			Font f = new java.awt.Font(Font.SANS_SERIF, Font.PLAIN, 32);
			for (int i = 0; i < 9; i++) {
				JButton square = new JButton("");
				square.setFont(f);
				square.setBackground(Color.WHITE);
				square.addActionListener(this);
				squares[i] = square;
				spanel.add(square);
			}
			status = new JLabel(" ");
			status.setBorder(BorderFactory.createEtchedBorder());
			add(status, BorderLayout.SOUTH);
			actionPerformed(null);
		}
		
		/** Handles all button events and updates the view. */
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae == null || ae.getSource() == clear) {
				game = new TicTacToe();
			} else if (!game.hasEnded()) {
				if (ae.getSource() == proposal) {
					if (strategy.getSelectedIndex() == 0)
						game.makeMiniMaxMove();
					else
						game.makeAlphaBetaMove();
				} else {
					for (int i = 0; i < 9; i++)
						if (ae.getSource() == squares[i])
							game.makeMove(i / 3, i % 3);
				}
			}
			TicTacToeBoard board = (TicTacToeBoard) game.getState().get("board");
			for (int i = 0; i < 9; i++) {
				String val = board.getValue(i / 3, i % 3);
				if (val == TicTacToeBoard.EMPTY)
					val = "";
				squares[i].setText(val);
			}
			GameState state = game.getState();
			if (game.getUtility(state) == 0 && game.hasEnded())
				status.setText("No winner...");
			else
				status.setText((game.hasEnded() ? "Just lost: " : "Next move: ")
					+ game.getPlayerToMove(game.getState()));
		}	
	}
}
