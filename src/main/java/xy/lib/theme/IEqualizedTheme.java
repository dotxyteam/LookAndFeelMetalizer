package xy.lib.theme;

import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * Equalized themes interface. An equalized theme allows to globally change all
 * the colors of a look-and-feel while keeping them as consistent as possible.
 * 
 * @author olitank
 *
 */
public interface IEqualizedTheme {

	/**
	 * @return The object allowing to vary this theme.
	 */
	ThemeEqualization getEqualization();

	/**
	 * Sets this theme as current. Note that the {@link MetalLookAndFeel} will be
	 * set as the current look-and-feel and all open windows will be updated after
	 * the execution of this method.
	 */
	void activate();

}