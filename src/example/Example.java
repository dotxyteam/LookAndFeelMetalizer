import xy.lib.theme.EqualizedMetalTheme;
import xy.lib.theme.ThemeEqualizerDialog;

public class Example {

	public static void main(String[] args) {
		/*
		 * The jar file is runnable.
		 * 
		 * To run:
		 * 
		 * either double-click on the jar file (OS-dependant) or run this command: java
		 * -jar specifyTheDownloadedJarFile
		 * 
		 * By default it will:
		 * 
		 * open the theme selection dialog try to download and open a test application
		 * 
		 * A Metalizer theme is an 'EqualizedTheme' object. It is defined by 3 values:
		 * hue, saturation and brightness. Each value is an float between 0 and 1. Open
		 * the theme selection dialog to find out your preferred values. Note that the
		 * theme equalization dialog maps the values from 0f-1f to 0-255. Example:
		 */
		EqualizedMetalTheme myTheme = new EqualizedMetalTheme();
		myTheme.getEqualization().setHue(0);
		myTheme.getEqualization().setSaturation(0);
		myTheme.getEqualization().setBrightness(0);
		/*
		 * To enable the theme, include the following code in your application before
		 * creating any controls:
		 */
		try {
			myTheme.activate();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		/*
		 * If the controls are already created, then use:
		 * 
		 * SwingUtilities.updateComponentTreeUI(applicationWindow);
		 * 
		 * Use the following code to open the theme selection window:
		 */
		EqualizedMetalTheme selectedTheme = ThemeEqualizerDialog.open(null, myTheme);
		if (selectedTheme == null) {
			System.out.println("Theme selection cancelled");
		} else {
			System.out.println("Selected theme : hue=" + selectedTheme.getEqualization().getHue() + ", saturation="
					+ selectedTheme.getEqualization().getSaturation() + ", brightness="
					+ selectedTheme.getEqualization().getBrightness());
		}

	}

}
