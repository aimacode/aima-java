package aimax.osm.viewer;


/**
 * Defines a base class for scalable vector graphic icons.
 * 
 * @author Ruediger Lunde
 * 
 */
public abstract class EntityIcon {

	protected float size;

	public float size() {
		return size;
	}

	public abstract void draw(UnifiedImageBuilder<?> imageBdr, int x, int y,
			float displayFactor);

	// ///////////////////////////////////////////////////////////////
	// implementation classes

	/**
	 * Implements a universally usable icon, consisting of a primitive shape and
	 * a character symbol inside.
	 * 
	 * @author Ruediger Lunde
	 */
	public static class SimpleIcon extends EntityIcon {

		public static enum Shape {
			CIRCLE, RECTANGLE, TRIANGLE, NONE
		}

		Shape shape;
		String symbol;
		UColor lineColor;
		UColor fillColor;
		UColor symColor;

		public SimpleIcon(Shape shape, float size, String symbol,
				UColor line, UColor fill, UColor sym) {
			this.shape = shape;
			this.size = size;
			this.symbol = symbol;
			lineColor = line;
			fillColor = fill;
			symColor = sym;
		}

		public void draw(UnifiedImageBuilder<?> imageBdr, int x, int y,
				float displayFactor) {
			int dsize = Math.round(size * displayFactor);
			int offset = Math.round(size * displayFactor / 2f);
			x -= offset;
			y -= offset;
			imageBdr.setLineStyle(false, displayFactor);
			imageBdr.setAreaFilled(true);
			if (fillColor != null) {
				imageBdr.setColor(fillColor);
				switch (shape) {
				case CIRCLE:
					imageBdr.drawOval(x, y, dsize, dsize);
					break;
				case RECTANGLE:
					imageBdr.drawRect(x, y, dsize, dsize);
					break;
				case TRIANGLE:
					imageBdr.drawPolygon(new int[] { x, x + dsize, x + offset, x },
							new int[] { y + dsize, y + dsize, y, y + dsize }, 4);
					break;
				default:
					break;
				}
			}
			if (lineColor != null && !lineColor.equals(fillColor)) {
				imageBdr.setColor(lineColor);
				imageBdr.setLineStyle(false, displayFactor);
				imageBdr.setAreaFilled(false);
				switch (shape) {
				case CIRCLE:
					imageBdr.drawOval(x, y, dsize, dsize);
					break;
				case RECTANGLE:
					imageBdr.drawRect(x, y, dsize, dsize);
					break;
				case TRIANGLE:
					imageBdr.drawPolygon(new int[] { x, x + dsize, x + offset, x },
							new int[] { y + dsize, y + dsize, y, y + dsize }, 4);
					break;
				default:
					break;
				}
			}
			if (symbol != null) {
				float oldFontSize = imageBdr.getFontSize();
				imageBdr.setColor(symColor);
				imageBdr.setFontSize((size - 2) * displayFactor);
				imageBdr.drawString(symbol, Math.round(x + 2.5f * displayFactor),
						Math.round(y + dsize - 2f * displayFactor));
				imageBdr.setFontSize(oldFontSize);
			}
		}
	}

	/**
	 * Special icon representing a pin (used for markers on the map).
	 * 
	 * @author Ruediger Lunde
	 */
	public static class PinIcon extends EntityIcon {
		UColor lineColor;
		UColor fillColor;

		public PinIcon(float size, UColor line, UColor fill) {
			this.size = size;
			this.lineColor = line;
			this.fillColor = fill;
		}

		public void draw(UnifiedImageBuilder<?> imageBdr, int x, int y,
				float displayFactor) {
			int dsize = Math.round(size * displayFactor);
			imageBdr.setColor(lineColor);
			imageBdr.setLineStyle(false, 2f * displayFactor);
			imageBdr.setAreaFilled(false);
			imageBdr.drawLine(x, y, x + dsize, y - dsize);
			imageBdr.setColor(fillColor);
			imageBdr.setLineStyle(false, displayFactor);
			imageBdr.setAreaFilled(true);
			int l = dsize / 3 + 1;
			imageBdr.drawOval(x + dsize - l, y - dsize - l, 2 * l, 2 * l);
		}
	}

	/**
	 * Special icon representing a church.
	 * 
	 * @author Ruediger Lunde
	 */
	public static class ChurchIcon extends EntityIcon {
		UColor lineColor;
		UColor fillColor;

		public ChurchIcon(float size, UColor line, UColor fill) {
			this.size = size;
			this.lineColor = line;
			this.fillColor = fill;
		}

		public void draw(UnifiedImageBuilder<?> imageBdr, int x, int y,
				float displayFactor) {
			int dsize = Math.round(size * displayFactor);
			int offset = Math.round(size * displayFactor / 2f);
			x -= offset;
			y -= offset;
			imageBdr.setLineStyle(false, displayFactor);
			imageBdr.setColor(fillColor);
			imageBdr.setAreaFilled(true);
			imageBdr.drawOval(x, y, dsize, dsize);
			imageBdr.setColor(lineColor);
			imageBdr.setAreaFilled(false);
			imageBdr.drawOval(x, y, dsize, dsize);
			imageBdr.drawLine(x + offset, y, x + offset, y - dsize / 2);
			imageBdr.drawLine(x + offset / 2, (int) y - offset / 2, x + offset * 3
					/ 2, y - offset / 2);
		}
	}

	/**
	 * Special icon representing a castle.
	 * 
	 * @author Ruediger Lunde
	 */
	public static class CastleIcon extends EntityIcon {
		UColor lineColor;
		UColor fillColor;

		public CastleIcon(float size, UColor line, UColor fill) {
			this.size = size;
			this.lineColor = line;
			this.fillColor = fill;
		}

		public void draw(UnifiedImageBuilder<?> imageBdr, int x, int y,
				float displayFactor) {
			int dsize = Math.round(size * displayFactor);
			int offset = Math.round(size * displayFactor / 2f);
			int[] xCoords = new int[] { x + offset, x + dsize,
					x + offset * 2 / 3 };
			int[] yCoords = new int[] { y - dsize, y - dsize, y - dsize * 2 / 3 };
			imageBdr.setLineStyle(false, displayFactor);
			imageBdr.setColor(fillColor);
			imageBdr.setAreaFilled(true);
			imageBdr.drawPolygon(xCoords, yCoords, 3);
			imageBdr.setColor(lineColor);
			imageBdr.setAreaFilled(false);
			imageBdr.drawPolygon(xCoords, yCoords, 3);
			imageBdr.drawLine(x, y, x + offset, y - dsize);
			imageBdr.setColor(fillColor);
			imageBdr.setAreaFilled(true);
			imageBdr.drawOval(x - offset, y - offset, dsize, dsize);
			imageBdr.setColor(lineColor);
			imageBdr.setAreaFilled(false);
			imageBdr.drawOval(x - offset, y - offset, dsize, dsize);
		}
	}

	/**
	 * Special icon representing a camp site.
	 * 
	 * @author Ruediger Lunde
	 */
	public static class TentIcon extends EntityIcon {
		UColor lineColor;
		UColor fillColor;

		public TentIcon(float size, UColor line, UColor fill) {
			this.size = size;
			lineColor = line;
			fillColor = fill;
		}

		public void draw(UnifiedImageBuilder<?> imageBdr, int x, int y,
				float displayFactor) {
			int dsize = Math.round(size * displayFactor);
			int offset = Math.round(size * displayFactor / 2f);
			x -= offset;
			y -= offset;
			imageBdr.setColor(fillColor);
			imageBdr.setLineStyle(false, displayFactor);
			imageBdr.setAreaFilled(true);
			imageBdr.drawPolygon(new int[] { x, x + dsize, x + offset, x },
					new int[] { y + dsize, y + dsize, y, y + dsize }, 4);
			imageBdr.setColor(lineColor);
			imageBdr.setAreaFilled(false);
			imageBdr.drawPolyline(new int[] { x + offset + offset / 2, x, x + dsize,
					x + offset - offset / 2 }, new int[] { y - offset / 2,
					y + dsize, y + dsize, y - offset / 2 }, 4);
		}
	}
}
