package xy.lib.theme;

import java.awt.Color;

import javax.swing.plaf.ColorUIResource;

/**
 * This class holds the settings of {@link EqualizedTheme} and is responsible
 * for the mapping of each color of the theme according to these settings.
 * 
 * The colors of the theme are changed so that they remain consistent, which
 * means that the contrasts between colors are maintained as much as possible.
 * 
 * Latitude and longitude correspond to rotations of colors in a space with 3
 * dimensions that are the red, green and blue components of each color.
 * 
 * The HSB (hue, saturation, brightness) settings held by this class are indeed
 * variations of the actual hue, saturation and brightness of each color of the
 * theme. Since the intent is to change multiple colors at the same time, this
 * class has pivot HSB values that allow to specify a main dominant color (it
 * will be dark when the brightness variation value is low, vivid when the
 * saturation variation value is high, etc) for convenience.
 * 
 * @author olitank
 *
 */
public class ThemeEqualization implements Cloneable {

	private int latitude;
	private int longitude;

	private int pivotHue;
	private int pivotSaturation;
	private int pivotBrightness;

	private int hue;
	private int saturation;
	private int brightness;

	private boolean colorsInverted = false;

	/**
	 * Main constructor. Builds an instance with HSB values equal to pivot HSB
	 * values.
	 * 
	 * @param latitude       A rotational variation of colors. Must be between 0 and
	 *                       255.
	 * @param longitude      Another rotational variation of colors. Must be between
	 *                       0 and 255.
	 * @param hue            The variation of the hue of each color. Must be between
	 *                       0 and 255.
	 * @param saturation     The variation of the saturation of each color. Must be
	 *                       between 0 and 255.
	 * @param brightness     The variation of the brightness of each color. Must be
	 *                       between 0 and 255.
	 * @param colorsInverted Whether colors are inverted or not.
	 */
	public ThemeEqualization(int latitude, int longitude, int hue, int saturation, int brightness,
			boolean colorsInverted) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.hue = this.pivotHue = hue;
		this.saturation = this.pivotSaturation = saturation;
		this.brightness = this.pivotBrightness = brightness;
		this.colorsInverted = colorsInverted;
	}

	/**
	 * Builds a neutral (no variation) instance with pivot HSB values adjusted
	 * according the given main dominant color.
	 * 
	 * @param mainColor The main dominant color.
	 */
	public ThemeEqualization(Color mainColor) {
		float[] hsbvals = Color.RGBtoHSB(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), null);
		this.latitude = 0;
		this.longitude = 0;
		this.hue = this.pivotHue = Math.round(hsbvals[0] * 255);
		this.saturation = this.pivotSaturation = Math.round(hsbvals[1] * 255);
		this.brightness = this.pivotBrightness = Math.round(hsbvals[2] * 255);
		this.colorsInverted = false;
	}

	/**
	 * @return The value (between 0 and 255) of the first rotational variation of
	 *         colors.
	 */
	public int getLatitude() {
		return latitude;
	}

	/**
	 * Updates the value (between 0 and 255) of the first rotational variation of
	 * colors.
	 * 
	 * @param latitude The new color variation value (between 0 and 255).
	 */
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return The value (between 0 and 255) of the second rotational variation of
	 *         colors.
	 */
	public int getLongitude() {
		return longitude;
	}

	/**
	 * Updates the value (between 0 and 255) of the second rotational variation of
	 * colors.
	 * 
	 * @param longitude The new color variation value (between 0 and 255).
	 */
	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return The pivot value (between 0 and 255) of the hue variation of each
	 *         color.
	 */
	public int getPivotHue() {
		return pivotHue;
	}

	/**
	 * Updates the pivot value (between 0 and 255) of the hue variation of each
	 * color.
	 * 
	 * @param pivotHue The new pivot value (between 0 and 255).
	 */
	public void setPivotHue(int pivotHue) {
		this.pivotHue = pivotHue;
	}

	/**
	 * @return The pivot value (between 0 and 255) of the saturation variation of
	 *         each color.
	 */
	public int getPivotSaturation() {
		return pivotSaturation;
	}

	/**
	 * Updates the pivot value (between 0 and 255) of the saturation variation of
	 * each color.
	 * 
	 * @param pivotSaturation The new pivot value (between 0 and 255).
	 */
	public void setPivotSaturation(int pivotSaturation) {
		this.pivotSaturation = pivotSaturation;
	}

	/**
	 * @return The pivot value (between 0 and 255) of the brightness variation of
	 *         each color.
	 */
	public int getPivotBrightness() {
		return pivotBrightness;
	}

	/**
	 * Updates the pivot value (between 0 and 255) of the brightness variation of
	 * each color.
	 * 
	 * @param pivotBrightness The new pivot value (between 0 and 255).
	 */
	public void setPivotBrightness(int pivotBrightness) {
		this.pivotBrightness = pivotBrightness;
	}

	/**
	 * @return The variation (between 0 and 255) of the hue of each color.
	 */
	public int getHue() {
		return hue;
	}

	/**
	 * Updates the value (between 0 and 255) of the brightness variation of each
	 * color.
	 * 
	 * @param hue The new variation value (between 0 and 255).
	 */
	public void setHue(int hue) {
		this.hue = hue;
	}

	/**
	 * @return The variation (between 0 and 255) of the saturation of each color.
	 */
	public int getSaturation() {
		return saturation;
	}

	/**
	 * Updates the value (between 0 and 255) of the saturation variation of each
	 * color.
	 * 
	 * @param saturation The new variation value (between 0 and 255).
	 */
	public void setSaturation(int saturation) {
		this.saturation = saturation;
	}

	/**
	 * @return The variation (between 0 and 255) of the brightness of each color.
	 */
	public int getBrightness() {
		return brightness;
	}

	/**
	 * Updates the value (between 0 and 255) of the brightness variation of each
	 * color.
	 * 
	 * @param brightness The new variation value (between 0 and 255).
	 */
	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	/**
	 * @return Whether colors are inverted or not.
	 */
	public boolean areColorsInverted() {
		return colorsInverted;
	}

	/**
	 * Updates the color inversion flag.
	 * 
	 * @param colorsInverted The new color inversion flag.
	 */
	public void setColorsInverted(boolean colorsInverted) {
		this.colorsInverted = colorsInverted;
	}

	/**
	 * @param color The initial color.
	 * @return The given color mapped (equalized) according the settings.
	 */
	public ColorUIResource apply(Color color) {
		double x = (color.getRed() / 255.0) - 0.5;
		double y = (color.getGreen() / 255.0) - 0.5;
		double z = (color.getBlue() / 255.0) - 0.5;

		{
			double longitudeRadians = (longitude / 255.0) * 2 * Math.PI;
			double rotatedAroundZAxis1 = x * Math.cos(longitudeRadians) - y * Math.sin(longitudeRadians);
			double rotatedAroundZAxis2 = x * Math.sin(longitudeRadians) + y * Math.cos(longitudeRadians);
			x = rotatedAroundZAxis1;
			y = rotatedAroundZAxis2;
		}
		{
			double latitudeRadians = (latitude / 255.0) * 2 * Math.PI;
			double rotatedAroundXAxis1 = y * Math.cos(latitudeRadians) - z * Math.sin(latitudeRadians);
			double rotatedAroundXAxis2 = y * Math.sin(latitudeRadians) + z * Math.cos(latitudeRadians);
			y = rotatedAroundXAxis1;
			z = rotatedAroundXAxis2;
		}

		int red = (int) Math.round((x + 0.5) * 255);
		int green = (int) Math.round((y + 0.5) * 255);
		int blue = (int) Math.round((z + 0.5) * 255);

		red = Math.max(0, Math.min(red, 255));
		green = Math.max(0, Math.min(green, 255));
		blue = Math.max(0, Math.min(blue, 255));

		color = new Color(red, green, blue);

		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

		float hueOffset = (-pivotHue + hue) / 255f;
		float saturationOffset = (-pivotSaturation + saturation) / 255f;
		float brightnessOffset = (-pivotBrightness + brightness) / 255f;

		hsb[0] = adjustHSBOverflow(hsb[0] + hueOffset);
		hsb[1] = adjustHSBOverflow(hsb[1] + saturationOffset);
		hsb[2] = adjustHSBOverflow(hsb[2] + brightnessOffset);

		int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);

		color = new Color(rgb);

		if (colorsInverted) {
			color = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
		}

		return new ColorUIResource(color);
	}

	protected float adjustHSBOverflow(float value) {
		if (value < 0.0) {
			return adjustHSBOverflow(-value);
		}
		if (value > 1.0) {
			return adjustHSBOverflow((float) Math.ceil(value) - value);
		}
		value = Math.round(value * 100f) / 100f;
		return value;
	}

	@Override
	public ThemeEqualization clone() {
		try {
			return (ThemeEqualization) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + brightness;
		result = prime * result + (colorsInverted ? 1231 : 1237);
		result = prime * result + hue;
		result = prime * result + latitude;
		result = prime * result + longitude;
		result = prime * result + pivotBrightness;
		result = prime * result + pivotHue;
		result = prime * result + pivotSaturation;
		result = prime * result + saturation;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ThemeEqualization other = (ThemeEqualization) obj;
		if (brightness != other.brightness)
			return false;
		if (colorsInverted != other.colorsInverted)
			return false;
		if (hue != other.hue)
			return false;
		if (latitude != other.latitude)
			return false;
		if (longitude != other.longitude)
			return false;
		if (pivotBrightness != other.pivotBrightness)
			return false;
		if (pivotHue != other.pivotHue)
			return false;
		if (pivotSaturation != other.pivotSaturation)
			return false;
		if (saturation != other.saturation)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ThemeEqualization [latitude=" + latitude + ", longitude=" + longitude + ", pivotHue=" + pivotHue
				+ ", pivotSaturation=" + pivotSaturation + ", pivotBrightness=" + pivotBrightness + ", hue=" + hue
				+ ", saturation=" + saturation + ", brightness=" + brightness + ", colorsInverted=" + colorsInverted
				+ "]";
	}

}
