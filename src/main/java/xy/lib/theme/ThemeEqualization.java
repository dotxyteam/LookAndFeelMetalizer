package xy.lib.theme;

import java.awt.Color;

import javax.swing.plaf.ColorUIResource;

public class ThemeEqualization {

	private float defaultHue;
	private float defaultSaturation;
	private float defaultBrightness;

	private float hue;
	private float saturation;
	private float brightness;

	public ThemeEqualization(float defaultHue, float defaultSaturation, float defaultBrightness) {
		super();
		this.hue = this.defaultHue = defaultHue;
		this.saturation = this.defaultSaturation = defaultSaturation;
		this.brightness = this.defaultBrightness = defaultBrightness;
	}

	public ThemeEqualization(float[] defaultHsb) {
		this(defaultHsb[0], defaultHsb[1], defaultHsb[2]);
	}

	public ThemeEqualization(Color mainColor) {
		this(Color.RGBtoHSB(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), null));
	}

	public float getDefaultHue() {
		return defaultHue;
	}

	public float getDefaultSaturation() {
		return defaultSaturation;
	}

	public float getDefaultBrightness() {
		return defaultBrightness;
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

	public ColorUIResource apply(Color color) {
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

		hsb[0] = adjustHSBOverflow(hsb[0] - defaultHue + hue);
		hsb[1] = adjustHSBOverflow(hsb[1] - defaultSaturation + saturation);
		hsb[2] = adjustHSBOverflow(hsb[2] - defaultBrightness + brightness);

		int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(brightness);
		result = prime * result + Float.floatToIntBits(defaultBrightness);
		result = prime * result + Float.floatToIntBits(defaultHue);
		result = prime * result + Float.floatToIntBits(defaultSaturation);
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
		if (Float.floatToIntBits(defaultBrightness) != Float.floatToIntBits(other.defaultBrightness))
			return false;
		if (Float.floatToIntBits(defaultHue) != Float.floatToIntBits(other.defaultHue))
			return false;
		if (Float.floatToIntBits(defaultSaturation) != Float.floatToIntBits(other.defaultSaturation))
			return false;
		if (Float.floatToIntBits(hue) != Float.floatToIntBits(other.hue))
			return false;
		if (Float.floatToIntBits(saturation) != Float.floatToIntBits(other.saturation))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ThemeEqualization [defaultHue=" + defaultHue + ", defaultSaturation=" + defaultSaturation
				+ ", defaultBrightness=" + defaultBrightness + ", hue=" + hue + ", saturation=" + saturation
				+ ", brightness=" + brightness + "]";
	}

}
