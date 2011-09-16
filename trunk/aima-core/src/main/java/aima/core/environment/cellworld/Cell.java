package aima.core.environment.cellworld;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 645.<br>
 * <br>
 * A representation of a Cell in the environment detailed in Figure 17.1.
 * 
 * @param <C>
 *            the content type of the cell.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class Cell<C> {
	private int x = 1;
	private int y = 1;
	private C content = null;

	/**
	 * Construct a Cell.
	 * 
	 * @param x
	 *            the x position of the cell.
	 * @param y
	 *            the y position of the cell.
	 * @param content
	 *            the initial content of the cell.
	 */
	public Cell(int x, int y, C content) {
		this.x = x;
		this.y = y;
		this.content = content;
	}

	/**
	 * 
	 * @return the x position of the cell.
	 */
	public int getX() {
		return x;
	}

	/**
	 * 
	 * @return the y position of the cell.
	 */
	public int getY() {
		return y;
	}

	/**
	 * 
	 * @return the content of the cell.
	 */
	public C getContent() {
		return content;
	}

	/**
	 * Set the cell's content.
	 * 
	 * @param content
	 *            the content to be placed in the cell.
	 */
	public void setContent(C content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "<x=" + x + ", y=" + y + ", content=" + content + ">";
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Cell<?>) {
			Cell<?> c = (Cell<?>) o;
			return x == c.x && y == c.y && content.equals(c.content);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return x + 23 + y + 31 * content.hashCode();
	}
}
