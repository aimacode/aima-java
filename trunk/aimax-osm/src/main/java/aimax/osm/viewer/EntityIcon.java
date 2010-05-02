package aimax.osm.viewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public abstract class EntityIcon {

	protected float size;
	
	public float size() {
		return size;
	}
	
	public abstract void draw(Graphics2D g2, int x, int y, float displayFactor);
	
	
	
	/////////////////////////////////////////////////////////////////
	// implementation classes
	
	protected static BasicStroke currStroke;
	protected static BasicStroke currStroke2;
	protected static float currDisplayFactor;
	protected static BasicStroke getStroke(float displayFactor, boolean s2) {
		if (displayFactor != currDisplayFactor) {
			currStroke = new BasicStroke(displayFactor);
			currStroke2 = new BasicStroke(2f*displayFactor);
			currDisplayFactor = displayFactor;
		}
		return s2 ? currStroke2 : currStroke;
	}
	
	public static class SimpleIcon extends EntityIcon {
		
		public static enum Shape {
			SOLID_LINE, DASHED_LINE, // for lines
			CIRCLE, RECTANGLE, TRIANGLE, NONE // for points
		}
		
		Shape shape;
		String symbol;
		Color lineColor;
		Color fillColor;
		Color symColor;
		
		public SimpleIcon(Shape shape, float size, String symbol, Color line, Color fill, Color sym) {
			this.shape = shape;
			this.size = size;
			this.symbol = symbol;
			lineColor = line;
			fillColor = fill;
			symColor = sym;
		}
		
		public void draw(Graphics2D g2, int x, int y, float displayFactor) {
			int dsize = Math.round(size * displayFactor);
			int offset = Math.round(size * displayFactor / 2f);
			x -= offset;
			y -= offset;
			g2.setStroke(getStroke(displayFactor, false));
			if (fillColor != null) {
				g2.setColor(fillColor);
				switch (shape) {
				case CIRCLE:
					g2.fillOval(x, y, dsize, dsize); break;
				case RECTANGLE:
					g2.fillRect(x, y, dsize, dsize); break;
				case TRIANGLE:
					g2.fillPolygon(
						new int[] {x, x+dsize, x+offset, x},
						new int[] {y+dsize, y+dsize, y, y+dsize}, 4);
					break;
				}
			}
			if (lineColor != null && !lineColor.equals(fillColor)) {
				g2.setColor(lineColor);
				g2.setStroke(getStroke(displayFactor, true));
				switch (shape) {
				case CIRCLE:
					g2.drawOval(x, y, dsize, dsize); break;
				case RECTANGLE:
					g2.drawRect(x, y, dsize, dsize); break;
				case TRIANGLE:
					g2.drawPolygon(
						new int[] {x, x+dsize, x+offset, x},
						new int[] {y+dsize, y+dsize, y, y+dsize}, 4);
					break;
				}
			}
			if (symbol != null) {
				Font font = g2.getFont();
				g2.setColor(symColor);
				g2.setFont(font.deriveFont((size-2)*displayFactor));
				g2.drawString(symbol, x+2.5f*displayFactor, y+dsize-2f*displayFactor);
				g2.setFont(font);
			}
		}
	}
	
	public static class PinIcon extends EntityIcon {
		Color lineColor;
		Color fillColor;
		
		public PinIcon(float size, Color line, Color fill) {
			this.size = size;
			this.lineColor = line;
			this.fillColor = fill;
		}
		
		public void draw(Graphics2D g2, int x, int y, float displayFactor) {
			int dsize = Math.round(size * displayFactor);
			g2.setStroke(getStroke(displayFactor, true));
			g2.setColor(lineColor);
			g2.drawLine(x, y, x+dsize, y-dsize);
			g2.setStroke(getStroke(displayFactor, false));
			int l = dsize/3+1;
			g2.setColor(fillColor);
			l = dsize/3+1;
			g2.fillOval(x+dsize-l, y-dsize-l, 2*l, 2*l);
		}
	}
	
	
	public static class ChurchIcon extends EntityIcon {
		Color lineColor;
		Color fillColor;
		
		public ChurchIcon(float size, Color line, Color fill) {
			this.size = size;
			this.lineColor = line;
			this.fillColor = fill;
		}
		
		public void draw(Graphics2D g2, int x, int y, float displayFactor) {
			int dsize = Math.round(size * displayFactor);
			int offset = Math.round(size * displayFactor / 2f);
			x -= offset;
			y -= offset;
			g2.setStroke(getStroke(displayFactor, false));
			if (fillColor != null) {
				g2.setColor(fillColor);
				g2.fillOval(x, y, dsize, dsize);
			}
			if (lineColor != null && !lineColor.equals(fillColor)) {
				g2.setColor(lineColor);
				g2.drawOval(x, y, dsize, dsize);
				g2.drawLine(x+offset, y, x+offset, y-dsize/2);
				g2.drawLine(x+offset/2, (int)y-offset/2, x+offset*3/2, y-offset/2);
			}
		}
	}
	
	
	public static class TentIcon extends EntityIcon {
		Color lineColor;
		Color fillColor;
	
		public TentIcon(float size, Color line, Color fill) {
			this.size = size;
			lineColor = line;
			fillColor = fill;
		}
		
		public void draw(Graphics2D g2, int x, int y, float displayFactor) {
			int dsize = Math.round(size * displayFactor);
			int offset = Math.round(size * displayFactor / 2f);
			x -= offset;
			y -= offset;
			g2.setColor(fillColor);
			g2.setStroke(getStroke(displayFactor, false));
			g2.fillPolygon(
			new int[] {x, x+dsize, x+offset, x},
			new int[] {y+dsize, y+dsize, y, y+dsize}, 4);
			g2.setColor(lineColor);
			//g2.setStroke(getStroke(displayFactor, true));
			g2.drawPolyline(
				new int[] {x+offset+offset/2, x, x+dsize, x+offset-offset/2},
				new int[] {y-offset/2, y+dsize, y+dsize, y-offset/2}, 4);
		}
	}
}
