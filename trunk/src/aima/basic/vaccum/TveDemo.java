package aima.basic.vaccum;

import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TveDemo extends javax.swing.JFrame {
	private javax.swing.JMenuItem aboutMenuItem;

	private javax.swing.JMenuItem openMenuItem;

	private javax.swing.JMenu fileMenu;

	private javax.swing.JMenuBar menuBar;

	private javax.swing.JPanel cleanerPanel;

	private javax.swing.JMenuItem exitMenuItem;

	private javax.swing.JMenu helpMenu;

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
		System.exit(0);
	}

	private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
		System.exit(0);
	}

	private void initComponents() {
		cleanerPanel = new CleanerPanel();
		menuBar = new javax.swing.JMenuBar();
		fileMenu = new javax.swing.JMenu();
		exitMenuItem = new javax.swing.JMenuItem();
		helpMenu = new javax.swing.JMenu();
		aboutMenuItem = new javax.swing.JMenuItem();

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});

		getContentPane().add(cleanerPanel, java.awt.BorderLayout.CENTER);

		fileMenu.setText("File");
		exitMenuItem.setText("Exit");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});

		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		helpMenu.setText("Help");
		aboutMenuItem.setText("About");
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);

		pack();
	}

	public static void main(String args[]) {
		new TveDemo().show();
	}

	public class CleanerPanel extends JPanel {

		JButton pieces[] = new JButton[2];

		private final ImageIcon STATUS_A = new ImageIcon(TveDemo.class
				.getResource("clean.gif"));

		private final ImageIcon STATUS_B = new ImageIcon(TveDemo.class
				.getResource("dirty.gif"));

		private final ImageIcon STATUS_C = new ImageIcon(TveDemo.class
				.getResource("cleaner_clean.gif"));

		private final ImageIcon STATUS_D = new ImageIcon(TveDemo.class
				.getResource("cleaner_dirty.gif"));

		public CleanerPanel() {
			setLayout(new GridLayout(1, 2));
			init();
		}

		private void init() {
			for (int i = 0; i < pieces.length; i++) {
				pieces[i] = new JButton(STATUS_D);
				this.add(pieces[i]);
			}
		}

		public void updateCells(int[] status) {
			for (int i = 0; i < pieces.length; i++) {
				switch (status[i]) {
				case 0:
					pieces[i].setIcon(STATUS_A);
					break;
				case 1:
					pieces[i].setIcon(STATUS_B);
					break;
				case 2:
					pieces[i].setIcon(STATUS_C);
					break;
				case 3:
					pieces[i].setIcon(STATUS_D);
					break;
				}
			}
		}

	}

}