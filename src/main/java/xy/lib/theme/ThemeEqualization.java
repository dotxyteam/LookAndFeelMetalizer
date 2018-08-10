package xy.lib.theme;

import java.awt.Color;

import javax.swing.plaf.ColorUIResource;

public class ThemeEqualization implements Cloneable {

	private float pivotHue;
	private float pivotSaturation;
	private float pivotBrightness;

	private float hue;
	private float saturation;
	private float brightness;

	private boolean colorsInverted = false;

	public ThemeEqualization(float pivotHue, float pivotSaturation, float pivotBrightness, float hue, float saturation,
			float brightness) {
		super();
		this.pivotHue = pivotHue;
		this.pivotSaturation = pivotSaturation;
		this.pivotBrightness = pivotBrightness;
		this.hue = hue;
		this.saturation = saturation;
		this.brightness = brightness;
	}

	public ThemeEqualization(float[] pivotHsb, float[] hsb) {
		this(pivotHsb[0], pivotHsb[1], pivotHsb[2], hsb[0], hsb[1], hsb[2]);
	}

	public ThemeEqualization(float pivotHue, float pivotSaturation, float pivotBrightness) {
		this(pivotHue, pivotSaturation, pivotBrightness, pivotHue, pivotSaturation, pivotBrightness);
	}

	public ThemeEqualization(Color pivotColor, Color color) {
		this(Color.RGBtoHSB(pivotColor.getRed(), pivotColor.getGreen(), pivotColor.getBlue(), null),
				Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null));
	}

	public ThemeEqualization(float[] pivotHsb) {
		this(pivotHsb[0], pivotHsb[1], pivotHsb[2]);
	}

	public ThemeEqualization(Color pivotColor) {
		this(pivotColor, pivotColor);
	}

	public Color getPivotColor() {
		return new Color(Color.HSBtoRGB(pivotHue, pivotSaturation, pivotBrightness));
	}

	public Color getColor() {
		return new Color(Color.HSBtoRGB(hue, saturation, brightness));
	}

	public float getPivotHue() {
		return pivotHue;
	}

	public float getPivotSaturation() {
		return pivotSaturation;
	}

	public float getPivotBrightness() {
		return pivotBrightness;
	}

	public float getHue() {
		return hue;
	}

	public float getSaturation() {
		return saturation;
	}

	public float getBrightness() {
		return brightness;
	}

	public void setHue(float hue) {
		this.hue = hue;
	}

	public void setSaturation(float saturation) {
		this.saturation = saturation;
	}

	public void setBrightness(float brightness) {
		this.brightness = brightness;
	}

	public boolean areColorsInverted() {
		return colorsInverted;
	}

	public void setColorsInverted(boolean colorsInverted) {
		this.colorsInverted = colorsInverted;
	}

	public void setHSB(float hue, float saturation, float brightness) {
		this.hue = hue;
		this.saturation = saturation;
		this.brightness = brightness;
	}

	public void setColor(Color color) {
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		setHSB(hsb[0], hsb[1], hsb[2]);
	}

	public ColorUIResource apply(Color color) {
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

		float hueOffset = -pivotHue + hue;
		float saturationOffset = -pivotSaturation + saturation;
		float brightnessOffset = -pivotBrightness + brightness;

		hsb[0] = adjustHSBOverflow(hsb[0] + hueOffset);
		hsb[1] = adjustHSBOverflow(hsb[1] + saturationOffset);
		hsb[2] = adjustHSBOverflow(hsb[2] + brightnessOffset);

		int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);

		if (colorsInverted) {
			Color newColor = new Color(rgb);
			newColor = new Color(255 - newColor.getRed(), 255 - newColor.getGreen(), 255 - newColor.getBlue());
			rgb = newColor.getRGB();
		}

		return new ColorUIResource(rgb);
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
		result = prime * result + Float.floatToIntBits(brightness);
		result = prime * result + Float.floatToIntBits(pivotBrightness);
		result = prime * result + Float.floatToIntBits(pivotHue);
		result = prime * result + Float.floatToIntBits(pivotSaturation);
		result = prime * result + Float.floatToIntBits(hue);
		result = prime * result + Float.floatToIntBits(saturation);
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
		if (Float.floatToIntBits(brightness) != Float.floatToIntBits(other.brightness))
			return false;
		if (Float.floatToIntBits(pivotBrightness) != Float.floatToIntBits(other.pivotBrightness))
			return false;
		if (Float.floatToIntBits(pivotHue) != Float.floatToIntBits(other.pivotHue))
			return false;
		if (Float.floatToIntBits(pivotSaturation) != Float.floatToIntBits(other.pivotSaturation))
			return false;
		if (Float.floatToIntBits(hue) != Float.floatToIntBits(other.hue))
			return false;
		if (Float.floatToIntBits(saturation) != Float.floatToIntBits(other.saturation))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ThemeEqualization [pivotHue=" + pivotHue + ", pivotSaturation=" + pivotSaturation + ", pivotBrightness="
				+ pivotBrightness + ", hue=" + hue + ", saturation=" + saturation + ", brightness=" + brightness + "]";
	}

}
