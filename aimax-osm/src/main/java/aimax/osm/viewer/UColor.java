package aimax.osm.viewer;

/**
 * A platform independent implementation for colors. Can be used with Android
 * and AWT. Letter U stands for unified.
 * 
 * @author Daniel Wonnenberg
 * @author Ruediger Lunde
 */

public class UColor {

	public static final UColor RED = new UColor(255, 0, 0);
	public static final UColor GREEN = new UColor(0, 255, 0);
	public static final UColor BLUE = new UColor(0, 0, 255);
	
	public static final UColor PINK = new UColor(255, 175, 175);
	public static final UColor ORANGE = new UColor(255, 200, 0);
	public static final UColor YELLOW = new UColor(255, 255, 0);
	public static final UColor CYAN = new UColor(0, 255, 255);

	public static final UColor WHITE = new UColor(255, 255, 255);
	public static final UColor LIGHT_GRAY = new UColor(192, 192,
			192);
	public static final UColor GRAY = new UColor(128, 128, 128);
	public static final UColor DARK_GRAY = new UColor(64, 64, 64);
	public static final UColor BLACK = new UColor(0, 0, 0);
	

	// //////////////////////////////////////////////////////////////////////////

	private int red;
	private int green;
	private int blue;
	private int alpha;

	public UColor(int red, int green, int blue) {
		this(red, green, blue, 255);
	}

	/**
	 * For the parameters use values between 0 and 255.
	 */
	public UColor(int red, int green, int blue, int alpha) {

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
		return number >= 0 && number <= 255;
	}

	public UColor brighter() {
		float fac = (float) Math.sqrt(2.0);
		return new UColor(round(red * fac), round(green * fac),
				round(blue * fac), alpha);

	}

	public UColor darker() {
		float fac = (float) Math.sqrt(0.5);
		return new UColor(round(red * fac), round(green * fac),
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
		if (otherObject != null && getClass() == otherObject.getClass()) {
			final UColor other = (UColor) otherObject;
			return other.red == red && other.green == green && other.blue == blue
					&& other.alpha == alpha;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return 3 * red + 11 * green + 23 * blue + 31 * alpha;
	}

	@Override
	public String toString() {
		String res = "UColor(";
		res += red + ",";
		res += green + ",";
		res += blue + ",";
		res += alpha + ")";
		return res;
	}
}
