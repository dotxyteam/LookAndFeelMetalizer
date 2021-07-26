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
        &lt;version&gt;3.4.0&lt;/version&gt;
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
It has the following implementation:

		IEqualizedTheme myTheme = new EqualizedMetalTheme();

It has 6 values that can be changed: latitude, longitude, hue, saturation, brightness and color inversion.
Open the theme selection dialog to find out your preferred values.
Example:

		myTheme.getEqualization().setLatitude(37);
		myTheme.getEqualization().setLongitude(42);
		myTheme.getEqualization().setHue(90);
		myTheme.getEqualization().setSaturation(223);
		myTheme.getEqualization().setBrightness(107);
		myTheme.getEqualization().setColorsInverted(true);

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
