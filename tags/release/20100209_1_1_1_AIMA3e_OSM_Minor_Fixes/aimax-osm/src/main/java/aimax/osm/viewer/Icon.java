package aimax.osm.viewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public abstract class Icon {

	protected float size;
	
	public float size() {
		return size;
	}
	
	public abstract void draw(Graphics2D g2, int x, int y, float displayFactor);
	
	
	/////////////////////////////////////////////////////////////////
	// useful static creators
	
	public static Icon createCircle(float size, Color color) {
		return new SimpleIcon(Shape.CIRCLE, size, null, color, color, null);
	}
	
	public static Icon createCircle(float size, Color line, Color fill) {
		return new SimpleIcon(Shape.CIRCLE, size, null, line, fill, null);
	}
	
	public static Icon createCircle(float size, String symbol, Color color, Color sym) {
		return new SimpleIcon(Shape.CIRCLE, size, symbol, color, color, sym);
	}
	
	public static Icon createRectangle(float size, Color color) {
		return new SimpleIcon(Shape.RECTANGLE, size, null, color, color, null);
	}
	
	public static Icon createRectangle(float size, Color line, Color fill) {
		return new SimpleIcon(Shape.RECTANGLE, size, null, line, fill, null);
	}
	
	public static Icon createRectangle(float size, String symbol, Color color, Color sym) {
		return new SimpleIcon(Shape.RECTANGLE, size, symbol, color, color, sym);
	}
	
	public static Icon createTriangle(float size, Color color) {
		return new SimpleIcon(Shape.TRIANGLE, size, null, color, color, null);
	}
	
	public static Icon createTriangle(float size, Color line, Color fill) {
		return new SimpleIcon(Shape.TRIANGLE, size, null, line, fill, null);
	}
	
	public static Icon createTriangle(float size, String symbol, Color color, Color sym) {
		return new SimpleIcon(Shape.TRIANGLE, size, symbol, color, color, sym);
	}
	
	public static Icon createPin(float size, Color color) {
		return new SimpleIcon(Shape.PIN, size, null, color, color, null);
	}
	
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
	
	/////////////////////////////////////////////////////////////////
	// an implementation class
	
	public static class SimpleIcon extends Icon {
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
			g2.setStroke(getStroke(displayFactor, false));
			if (shape != Shape.PIN) {
				x -= offset;
				y -= offset;
			}
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
				case PIN:
					int l = dsize/3+1;
					g2.drawLine(x, y, x+dsize, y-dsize);
					g2.fillOval(x+dsize-l, y-dsize-l, 2*l, 2*l);
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
				case PIN:
					int l = dsize/3+1;
					g2.drawOval(x+dsize-l, y-dsize-l, 2*l, 2*l);
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
	
	public static enum Shape {
		SOLID_LINE, DASHED_LINE, // for lines
		CIRCLE, RECTANGLE, TRIANGLE, PIN, NONE // for points
	}
}
