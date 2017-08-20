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

Java >= 1.6


Download
========

- Use Maven

    &lt;dependency&gt;
        &lt;groupId&gt;xy.lib.theme&lt;/groupId&gt;
        &lt;artifactId&gt;Metalizer&lt;/artifactId&gt;
        &lt;version&gt;1.0.2&lt;/version&gt;
    &lt;/dependency&gt;
    
- Or Download The [JAR](https://github.com/dotxyteam/LookAndFeelMetalizer/releases)

Usage
=====

The jar file is runnable.

To run:
- either double-click on the jar file (OS-dependant)
- or run this command: java -jar <pecifyTheDownloadedJarFile>

By default it will:
- open the theme selection dialog
- try to download and open a test application

A Metalizer theme is an 'EqualizedTheme' object.
It is defined by 3 values: hue, saturation and brightness.
Each value is an integer between 0 and 255.
Open the theme selection dialog to find out your preferred values.
Example:

    int hue = 0;
    int saturation = 0;
    int brightness = 0;
    EqualizedTheme myTheme = new EqualizedTheme(hue, saturation,
            brightness);

To enable the theme, 
include the following code in your application
before creating any controls:

    try {
        myTheme.activate();
    } catch (Exception e1) {
        e1.printStackTrace();
    }

If the controls are already created, then use:

    SwingUtilities.updateComponentTreeUI(applicationWindow);
    
Use the following code to
open the theme selection window:

    EqualizedTheme selectedTheme = ThemeEqualizerDialog
            .open(applicationWindow, myTheme);
    System.out.println("Selected theme values: hue="
            + selectedTheme.getHueOffset() + ", saturation="
            + selectedTheme.getSaturationOffset() + ", brightness="
        + selectedTheme.getBrightnessOffset());


Feedback
========

Feel free to [post any found bugs](https://github.com/dotxyteam/LookAndFeelMetalizer/issues) or send me a message: dotxyteam@yahoo.fr

Thanks!
