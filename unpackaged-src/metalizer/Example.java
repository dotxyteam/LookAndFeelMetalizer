package metalizer;

import xy.lib.theme.EqualizedMetalTheme;
import xy.lib.theme.IEqualizedTheme;
import xy.lib.theme.ThemeEqualizerDialog;

public class Example {

	public static void main(String[] args) {
		IEqualizedTheme myTheme;
		myTheme = new EqualizedMetalTheme();
		//myTheme = new EqualizedNimbusTheme();
		//myTheme = new EqualizedGlassTheme();

		float hue = 0f;
		float saturation = 0f;
		float brightness = 0f;
		boolean invertColors = false;
		
		myTheme.getEqualization().setHue(hue);
		myTheme.getEqualization().setSaturation(saturation);
		myTheme.getEqualization().setBrightness(brightness);
		myTheme.getEqualization().setColorsInverted(invertColors);
		
		myTheme.activate();

		boolean themeAccepted = ThemeEqualizerDialog.open(null, myTheme);
		if (themeAccepted) {
			System.out.println("Selected theme: " + myTheme);
		} else {
			System.out.println("Theme rejected. The initial look-and-feel should be restored");
		}
	}

}
