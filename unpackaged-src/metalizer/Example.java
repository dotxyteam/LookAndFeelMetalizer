package metalizer;

import xy.lib.theme.EqualizedTheme;
import xy.lib.theme.ThemeEqualizerDialog;

public class Example {

	public static void main(String[] args) {
		EqualizedTheme myTheme;
		myTheme = new EqualizedTheme();

		myTheme.getEqualization().setLatitude(37);
		myTheme.getEqualization().setLongitude(42);
		myTheme.getEqualization().setHue(90);
		myTheme.getEqualization().setSaturation(223);
		myTheme.getEqualization().setBrightness(107);
		myTheme.getEqualization().setColorsInverted(true);

		myTheme.activate();

		boolean themeAccepted = ThemeEqualizerDialog.open(null, myTheme);
		if (themeAccepted) {
			System.out.println("Selected theme: " + myTheme);
		} else {
			System.out.println("Theme rejected. The initial look-and-feel should be restored");
		}
	}

}
