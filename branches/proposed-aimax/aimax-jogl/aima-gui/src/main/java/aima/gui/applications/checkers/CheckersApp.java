package aima.gui.applications.checkers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import aima.core.environment.checkers.Checkers;
import aima.core.environment.checkers.Checkerboard;
import aima.core.search.adversarial.GameState;

/**
 * @author Mike Stampone
 */
public class CheckersApp {
	
	/** Used for integration into the universal demo application. */
	public JFrame constructApplicationFrame() {
		JFrame frame = new JFrame();
		JPanel panel =  new CheckersPanel();
		frame.add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		return frame;
	}
	
	/** Application starter. */
	public static void main(String[] args) {
		JFrame frame = new CheckersApp().constructApplicationFrame();
		frame.setSize(600, 600);
		frame.setVisible(true);
	}
	
	/** Simple panel to control the game. */
	private static class CheckersPanel extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;
		JComboBox strategy;
		JButton clear;
		JButton proposal;
		// JButton[] squares;
		JLabel status;
		Checkers game;
		CheckersView checkersView;
		
		/** Standard constructor. */
		CheckersPanel() {
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

			// Add the checkers view
			checkersView = new CheckersView();
			this.add(checkersView);
			
			status = new JLabel(" ");
			status.setBorder(BorderFactory.createEtchedBorder());
			add(status, BorderLayout.SOUTH);
			actionPerformed(null);
		}
		
		/** Handles all button events and updates the view. */
		@Override
		public void actionPerformed(ActionEvent ae) {

			if (ae == null || ae.getSource() == clear) {
				game = new Checkers();
				Checkerboard board = (Checkerboard) game.getState().get("board");
				checkersView.drawPosition(board);
			} else if (!game.hasEnded()) {
				if (ae.getSource() == proposal) {
					if (strategy.getSelectedIndex() == 0)
						game.makeMiniMaxMove();
					else
						game.makeAlphaBetaMove();
				} else {
					/*for (int i = 0; i < 9; i++)
						if (ae.getSource() == squares[i])
							game.makeMove(i / 3, i % 3);*/
				}
			}
			Checkerboard board = (Checkerboard) game.getState().get("board");
			checkersView.drawPosition(board);

			GameState state = game.getState();
			if (game.getUtility(state) == 0 && game.hasEnded())
				status.setText("No winner...");
			else
				status.setText((game.hasEnded() ? "Just lost: " : "Next move: ")
					+ game.getPlayerToMove(game.getState()));
			
		}	
	}
}
