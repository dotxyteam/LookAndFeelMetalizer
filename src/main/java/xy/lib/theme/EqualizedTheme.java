package xy.lib.theme;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class EqualizedTheme extends DefaultMetalTheme {

	private static DefaultMetalTheme original = new DefaultMetalTheme();
	private static float[] defaultHsbOffset = Color.RGBtoHSB(original
			.getControl().getRed(), original.getControl().getGreen(), original
			.getControl().getBlue(), null);

	private int hueOffset;
	private int saturationOffset;
	private int brightnessOffset;

	public EqualizedTheme(int hueOffset, int saturationOffset,
			int brightnessOffset) {
		super();
		this.hueOffset = hueOffset;
		this.saturationOffset = saturationOffset;
		this.brightnessOffset = brightnessOffset;
	}

	public int getHueOffset() {
		return hueOffset;
	}

	public int getSaturationOffset() {
		return saturationOffset;
	}

	public int getBrightnessOffset() {
		return brightnessOffset;
	}

	private ColorUIResource changeHSB255(ColorUIResource colorResource,
			int hue2555Offset, int saturation2555Offset,
			int brightness2555Offset) {
		float[] hsb = Color.RGBtoHSB(colorResource.getRed(),
				colorResource.getGreen(), colorResource.getBlue(), null);

		hsb[0] = modulo1(hsb[0] - defaultHsbOffset[0] + (hue2555Offset / 255f));
		hsb[1] = modulo1(hsb[1] - defaultHsbOffset[1]
				+ (saturation2555Offset / 255f));
		hsb[2] = modulo1(hsb[2] - defaultHsbOffset[2]
				+ (brightness2555Offset / 255f));

		int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
		return new ColorUIResource(rgb);
	}

	private float modulo1(float f) {
		if (f < 0) {
			f = (float) Math.ceil(f) - f;
		}
		f = Math.round(f * 100f) / 100f;
		f = f % 1.001f;
		return f;
	}

	public ColorUIResource getFocusColor() {
		return changeHSB255(original.getFocusColor(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getDesktopColor() {
		return changeHSB255(original.getDesktopColor(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getControl() {
		return changeHSB255(original.getControl(), hueOffset, saturationOffset,
				brightnessOffset);
	}

	public ColorUIResource getControlShadow() {
		return changeHSB255(original.getControlShadow(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getControlDarkShadow() {
		return changeHSB255(original.getControlDarkShadow(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getControlInfo() {
		return changeHSB255(original.getControlInfo(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getControlHighlight() {
		return changeHSB255(original.getControlHighlight(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getControlDisabled() {
		return changeHSB255(original.getControlDisabled(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getPrimaryControl() {
		return changeHSB255(original.getPrimaryControl(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getPrimaryControlShadow() {
		return changeHSB255(original.getPrimaryControlShadow(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getPrimaryControlDarkShadow() {
		return changeHSB255(original.getPrimaryControlDarkShadow(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getPrimaryControlInfo() {
		return changeHSB255(original.getPrimaryControlInfo(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getPrimaryControlHighlight() {
		return changeHSB255(original.getPrimaryControlHighlight(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getSystemTextColor() {
		return changeHSB255(original.getSystemTextColor(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getControlTextColor() {
		return changeHSB255(original.getControlTextColor(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getInactiveControlTextColor() {
		return changeHSB255(original.getInactiveControlTextColor(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getInactiveSystemTextColor() {
		return changeHSB255(original.getInactiveSystemTextColor(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getUserTextColor() {
		return changeHSB255(original.getUserTextColor(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getTextHighlightColor() {
		return changeHSB255(original.getTextHighlightColor(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getHighlightedTextColor() {
		return changeHSB255(original.getHighlightedTextColor(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getWindowBackground() {
		return changeHSB255(original.getWindowBackground(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getWindowTitleBackground() {
		return changeHSB255(original.getWindowTitleBackground(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getWindowTitleForeground() {
		return changeHSB255(original.getWindowTitleForeground(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getWindowTitleInactiveBackground() {
		return changeHSB255(original.getWindowTitleInactiveBackground(),
				hueOffset, saturationOffset, brightnessOffset);
	}

	public ColorUIResource getWindowTitleInactiveForeground() {
		return changeHSB255(original.getWindowTitleInactiveForeground(),
				hueOffset, saturationOffset, brightnessOffset);
	}

	public ColorUIResource getMenuBackground() {
		return changeHSB255(original.getMenuBackground(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getMenuForeground() {
		return changeHSB255(original.getMenuForeground(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getMenuSelectedBackground() {
		return changeHSB255(original.getMenuSelectedBackground(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getMenuSelectedForeground() {
		return changeHSB255(original.getMenuSelectedForeground(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getMenuDisabledForeground() {
		return changeHSB255(original.getMenuDisabledForeground(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getSeparatorBackground() {
		return changeHSB255(original.getSeparatorBackground(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getSeparatorForeground() {
		return changeHSB255(original.getSeparatorForeground(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getAcceleratorForeground() {
		return changeHSB255(original.getAcceleratorForeground(), hueOffset,
				saturationOffset, brightnessOffset);
	}

	public ColorUIResource getAcceleratorSelectedForeground() {
		return changeHSB255(original.getAcceleratorSelectedForeground(),
				hueOffset, saturationOffset, brightnessOffset);
	}

	public static int getDefaultHueOffset() {
		return Math.round(defaultHsbOffset[0] * 255);
	}

	public static int getDefaultSaturationOffset() {
		return Math.round(defaultHsbOffset[1] * 255);
	}

	public static int getDefaultBrightnessOffset() {
		return Math.round(defaultHsbOffset[2] * 255);
	}

	public void apply() throws Exception {
		MetalLookAndFeel.setCurrentTheme(this);
		UIManager.setLookAndFeel(MetalLookAndFeel.class.getName());
	}

}
