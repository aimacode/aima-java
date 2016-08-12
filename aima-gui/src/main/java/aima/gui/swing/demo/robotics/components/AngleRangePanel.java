package aima.gui.swing.demo.robotics.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import aima.gui.swing.demo.robotics.util.GuiBase;

public class AngleRangePanel extends AnglePanel {

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(GuiBase.getTextColor());
		
		//paints a coordinate system and draws a circle 
		//around the zero point
		g2d.drawOval(circleCenterX - RADIUS,circleCenterY - RADIUS, 2 * RADIUS, 2 * RADIUS);
		g2d.drawLine(circleCenterX - (int) (1.5 * RADIUS), circleCenterY, circleCenterX + (int) (1.5 * RADIUS), circleCenterY);
		g2d.drawLine(circleCenterX, circleCenterY - (int) (1.5 * RADIUS),circleCenterX, circleCenterY + (int) (1.5 * RADIUS));
	
		//paints every angle on the circular track
		if(angles != null) {
			int i=0;
			for(; i < selectedAngleIndex; i++) {
				drawAngle(g2d, i);
			}
			i++;
			for(; i < angles.length; i++) {
				drawAngle(g2d, i);
			}
			//Selected angle has to be drawn last:
			g2d.setColor(Color.RED);//green new Color(0, 150 , 0)
			drawAngle(g2d, selectedAngleIndex);
		}
	}
}
