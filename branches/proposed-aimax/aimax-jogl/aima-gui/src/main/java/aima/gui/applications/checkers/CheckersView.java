package aima.gui.applications.checkers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.impl.DynamicAction;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.gui.framework.EmptyEnvironmentView;
import aima.core.environment.checkers.Checkerboard;

import javax.swing.JPanel;

/**
 * Displays the informations provided by a <code>Checkerboard</code> on a
 * panel using 2D-graphics.
 * 
 */
public class CheckersView extends JPanel {
	
	Checkerboard position = null;
	
	public void drawPosition(Checkerboard position)
	{
		this.position = position;
		this.repaint();
	}
	
	/**
	 * Creates a 2D-graphic showing the checkerboard
	 */
	@Override
	public void paint(Graphics g)
	{

		Graphics2D g2 = (Graphics2D) g;
		g2.setBackground(Color.white);
		g2.clearRect(0, 0, getWidth(), getHeight());
		Image checkerboard = Toolkit.getDefaultToolkit().getImage("aima-gui/src/main/resource/checkerboard.png");
		int xOffset = getWidth() / 2 - 202;
		
		// Clamp xOffset to 0
		if(xOffset < 0)
			xOffset = 0;
		
		int yOffset = getHeight() / 2 - 202;
		
		// Clamp yOffset to 0
		if(yOffset < 0)
			yOffset = 0;
		
		g2.drawImage(checkerboard, xOffset, yOffset, this);
	    g2.finalize();
	    
	    if(position != null)
	    {
	    	// Draw the pieces
	    	Image redChecker = Toolkit.getDefaultToolkit().getImage("aima-gui/src/main/resource/red-checker.png");
	    	Image redKing = Toolkit.getDefaultToolkit().getImage("aima-gui/src/main/resource/red-king.png");
	    	Image whiteChecker = Toolkit.getDefaultToolkit().getImage("aima-gui/src/main/resource/white-checker.png");
	    	Image whiteKing = Toolkit.getDefaultToolkit().getImage("aima-gui/src/main/resource/white-king.png");	    	
	    	for(int r = 0; r < 8; r++)
	    	{
	    		for(int c = 0; c < 4; c++)
	    		{
	    			int x = 402 - 100 * (c + 1) + xOffset;
		    		if(r % 2 == 0)
	    				x += 50;
    				int y = 402 - 50 * (7 - r + 1) + yOffset;
    				
	    			if(position.isRedChecker(r, c))
	    			{
	    				// Draw the red checker
	    				g2.drawImage(redChecker, x, y, this);
	    			}
	    			else if(position.isRedKing(r, c))
	    			{
	    				
	    			}
	    			else if(position.isWhiteChecker(r, c))
	    			{
	    				// Draw the white checker
	    				g2.drawImage(whiteChecker, x, y, this);	    				
	    			}
	    			else if(position.isWhiteKing(r, c))
	    			{
	    				
	    			}
	    		}
	    	}
	    }
	}
}
