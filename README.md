LookAndFeelMetalizer
====================

Java library offering a great number of themes of the Swing Metal Look And Feel

![Screenshots](https://raw.github.com/olitank/LookAndFeelMetalizer/master/screenshots/all.png)

Features
========

- lot of generated themes
- consistent color schemes
- theme selection dialog

Download
========

Library: [Metalizer-0.8.jar](https://raw.github.com/olitank/LookAndFeelMetalizer/master/dist/Metalizer-0.8.jar)

Sources: [master.zip](https://github.com/olitank/LookAndFeelMetalizer/archive/master.zip)

Usage
=====

A Metalizer theme is an 'EqualizedTheme' object.
It is defined by 3 values: hue, saturation and brightness.
Each value is an integer between 0 and 255.
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
        myTheme.apply();
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

Feel free to [post any found bugs](https://github.com/olitank/LookAndFeelMetalizer/issues) or send me a message: olitank@yahoo.fr

Thanks!
