LookAndFeelMetalizer
====================

Java library implementing a great number of themes of the Swing Metal Look And Feel

![Example 1](https://raw.github.com/olitank/LookAndFeelMetalizer/master/screenshots/all.png)

Features
========

- generated themes
- decent color schemes
- theme selection dialog

Download
========



Usage
=====


A Metalizer theme is a 'EqualizedTheme' object.
It is defined by 3 values: hue, saturation and brightness 
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

