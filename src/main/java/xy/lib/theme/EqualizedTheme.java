package xy.lib.theme;

import java.awt.Color;
import java.awt.Window;

import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
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

	public Color adpapt(Color color) {
		return changeHSB255(color, hueOffset, saturationOffset,
				brightnessOffset);
	}

	private ColorUIResource changeHSB255(Color color, int hue2555Offset,
			int saturation2555Offset, int brightness2555Offset) {
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(),
				color.getBlue(), null);

		hsb[0] = adjustHSBOverflow(hsb[0] - defaultHsbOffset[0]
				+ (hue2555Offset / 255f));
		hsb[1] = adjustHSBOverflow(hsb[1] - defaultHsbOffset[1]
				+ (saturation2555Offset / 255f));
		hsb[2] = adjustHSBOverflow(hsb[2] - defaultHsbOffset[2]
				+ (brightness2555Offset / 255f));

		int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
		return new ColorUIResource(rgb);
	}

	private float adjustHSBOverflow(float value) {
		if (value < 0.0) {
			return adjustHSBOverflow(-value);
		}
		if (value > 1.0) {
			return adjustHSBOverflow((float) Math.ceil(value) - value);
		}
		value = Math.round(value * 100f) / 100f;
		return value;
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

	public void activate() {
		if (isAlreadyActive()) {
			return;
		}
		try {
			MetalLookAndFeel.setCurrentTheme(this);
			UIManager.setLookAndFeel(MetalLookAndFeel.class.getName());
		} catch (Exception e) {
			throw new AssertionError(e);
		}
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (Window window : Window.getWindows()) {
					SwingUtilities.updateComponentTreeUI(window);
				}
			}
		});
	}

	public boolean isAlreadyActive() {
		LookAndFeel currentLAF = UIManager.getLookAndFeel();
		if (!(currentLAF instanceof MetalLookAndFeel)) {
			return false;
		}
		if (!(this.equals(MetalLookAndFeel.getCurrentTheme()))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return brightnessOffset + hueOffset + saturationOffset;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EqualizedTheme)) {
			return false;
		}
		EqualizedTheme other = (EqualizedTheme) obj;
		if (other.brightnessOffset != brightnessOffset) {
			return false;
		}
		if (other.hueOffset != hueOffset) {
			return false;
		}
		if (other.saturationOffset != saturationOffset) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" + hueOffset + "," + saturationOffset
				+ "," + brightnessOffset + ")";
	}

}
