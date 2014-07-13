package aimax.osm.viewer;

/**
 * A platform independent implementation for colors. Can be used with Android
 * and AWT.
 * 
 * @author Daniel Wonnenberg
 */

public class UnifiedColor {

	public static final UnifiedColor RED = new UnifiedColor(255, 0, 0);
	public static final UnifiedColor GREEN = new UnifiedColor(0, 255, 0);
	public static final UnifiedColor BLUE = new UnifiedColor(0, 0, 255);
	
	public static final UnifiedColor PINK = new UnifiedColor(255, 175, 175);
	public static final UnifiedColor ORANGE = new UnifiedColor(255, 200, 0);
	public static final UnifiedColor YELLOW = new UnifiedColor(255, 255, 0);
	public static final UnifiedColor CYAN = new UnifiedColor(0, 255, 255);

	public static final UnifiedColor WHITE = new UnifiedColor(255, 255, 255);
	public static final UnifiedColor LIGHT_GRAY = new UnifiedColor(192, 192,
			192);
	public static final UnifiedColor GRAY = new UnifiedColor(128, 128, 128);
	public static final UnifiedColor DARK_GRAY = new UnifiedColor(64, 64, 64);
	public static final UnifiedColor BLACK = new UnifiedColor(0, 0, 0);
	

	// //////////////////////////////////////////////////////////////////////////

	private int red;
	private int green;
	private int blue;
	private int alpha;

	public UnifiedColor(int red, int green, int blue) {
		this(red, green, blue, 255);
	}

	/**
	 * For the parameters use values between 0 and 255.
	 */
	public UnifiedColor(int red, int green, int blue, int alpha) {

		if (!isInRange(red) || !isInRange(green) || !isInRange(blue)
				|| !isInRange(alpha)) {
			throw new IllegalArgumentException("Check parameters (0 to 255)");
		}
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	private boolean isInRange(int number) {

		boolean res;
		if (number >= 0 && number <= 255) {
			res = true;
		} else {
			res = false;
		}
		return res;
	}

	public UnifiedColor brighter() {
		float fac = (float) Math.sqrt(2.0);
		return new UnifiedColor(round(red * fac), round(green * fac),
				round(blue * fac), alpha);

	}

	public UnifiedColor darker() {
		float fac = (float) Math.sqrt(0.5);
		return new UnifiedColor(round(red * fac), round(green * fac),
				round(blue * fac), alpha);
	}

	private int round(float colorValue) {
		return Math.min(255, Math.round(colorValue));
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}

	public int getAlpha() {
		return alpha;
	}

	@Override
	public boolean equals(Object otherObject) {

		if (otherObject == null) {
			return false;
		}
		if (this == otherObject) {
			return true;
		}
		if (this.getClass() != otherObject.getClass()) {
			return false;
		}

		final UnifiedColor other = (UnifiedColor) otherObject;
		if (other.red == red && other.green == green && other.blue == blue
				&& other.alpha == alpha) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return 2 * red + 3 * green + 5 * blue + 7 * alpha;
	}

	@Override
	public String toString() {

		String res = "";
		res += red + ",";
		res += green + ",";
		res += blue + ",";
		res += alpha;
		return res;
	}
}
