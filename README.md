LookAndFeelMetalizer
====================

LookAndFeelMetalizer allows to quickly modify the theme of the Metal look-and-feel
![alt text](https://raw.githubusercontent.com/dotxyteam/LookAndFeelMetalizer/master/screenshots/all.png)

Features
========

- lot of generated themes
- consistent color schemes
- theme selection dialog

Requirements
============

Tested on Java 1.8


Download
========

- Use Maven

    <pre><code>&lt;dependency&gt;
        &lt;groupId&gt;com.github.dotxyteam&lt;/groupId&gt;
        &lt;artifactId&gt;metalizer&lt;/artifactId&gt;
        &lt;version&gt;3.2.0&lt;/version&gt;
    &lt;/dependency&gt;</code></pre>
    
- Or Download The [JAR](https://github.com/dotxyteam/LookAndFeelMetalizer/releases)

Usage
=====

The jar file is runnable.

To test:
- either double-click on the jar file (OS-dependant)
- or run this command: java -jar <pecifyTheDownloadedJarFile>

By default it will:
- open the theme selection dialog
- try to download and open a test application

A Metalizer theme is an an instance of the 'IEqualizedTheme' interface.
It has the following implementations:

		IEqualizedTheme myTheme = new EqualizedMetalTheme();
		IEqualizedTheme myTheme = new EqualizedNimbusTheme();
		IEqualizedTheme myTheme = new EqualizedGlassTheme();

It has 4 values that can be changed: hue, saturation, brightness and color inversion.
The first 3 values are floats between 0f and 255f.
The last value is a boolean.
Open the theme selection dialog to find out your preferred values.
Example:

		float hue = 0f;
		float saturation = 0f;
		float brightness = 0f;
		boolean invertColors = false;
		
		myTheme.getEqualization().setHue(hue);
		myTheme.getEqualization().setSaturation(saturation);
		myTheme.getEqualization().setBrightness(brightness);
		myTheme.getEqualization().setColorsInverted(invertColors);

To enable the theme, 
include the following code in your application before creating any controls:

		myTheme.activate();

Use the following code to open the theme selection window:

		boolean themeAccepted = ThemeEqualizerDialog.open(null, myTheme);
		if (themeAccepted) {
			System.out.println("Selected theme: " + myTheme);
		} else {
			System.out.println("Theme rejected. The initial look-and-feel should be restored");
		}


Used by
=======

- [Phoyo photo booth software](http://www.phoyosystem.com/) 

Feedback
========

Feel free to [post any found bugs](https://github.com/dotxyteam/LookAndFeelMetalizer/issues) or send me a message: dotxyteam@yahoo.fr

Thanks!
