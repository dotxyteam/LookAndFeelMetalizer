package xy.lib.theme;

import java.awt.Window;

import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class EqualizedMetalTheme extends DefaultMetalTheme {

	private static DefaultMetalTheme DEFAULT_METAL_THEME = new DefaultMetalTheme();

	private ThemeEqualization equalization;

	public EqualizedMetalTheme() {
		super();
		this.equalization = new ThemeEqualization(DEFAULT_METAL_THEME.getControl());
	}

	public ThemeEqualization getEqualization() {
		return equalization;
	}

	public ColorUIResource getFocusColor() {
		return equalization.apply(DEFAULT_METAL_THEME.getFocusColor());
	}

	public ColorUIResource getDesktopColor() {
		return equalization.apply(DEFAULT_METAL_THEME.getDesktopColor());
	}

	public ColorUIResource getControl() {
		return equalization.apply(DEFAULT_METAL_THEME.getControl());
	}

	public ColorUIResource getControlShadow() {
		return equalization.apply(DEFAULT_METAL_THEME.getControlShadow());
	}

	public ColorUIResource getControlDarkShadow() {
		return equalization.apply(DEFAULT_METAL_THEME.getControlDarkShadow());
	}

	public ColorUIResource getControlInfo() {
		return equalization.apply(DEFAULT_METAL_THEME.getControlInfo());
	}

	public ColorUIResource getControlHighlight() {
		return equalization.apply(DEFAULT_METAL_THEME.getControlHighlight());
	}

	public ColorUIResource getControlDisabled() {
		return equalization.apply(DEFAULT_METAL_THEME.getControlDisabled());
	}

	public ColorUIResource getPrimaryControl() {
		return equalization.apply(DEFAULT_METAL_THEME.getPrimaryControl());
	}

	public ColorUIResource getPrimaryControlShadow() {
		return equalization.apply(DEFAULT_METAL_THEME.getPrimaryControlShadow());
	}

	public ColorUIResource getPrimaryControlDarkShadow() {
		return equalization.apply(DEFAULT_METAL_THEME.getPrimaryControlDarkShadow());
	}

	public ColorUIResource getPrimaryControlInfo() {
		return equalization.apply(DEFAULT_METAL_THEME.getPrimaryControlInfo());
	}

	public ColorUIResource getPrimaryControlHighlight() {
		return equalization.apply(DEFAULT_METAL_THEME.getPrimaryControlHighlight());
	}

	public ColorUIResource getSystemTextColor() {
		return equalization.apply(DEFAULT_METAL_THEME.getSystemTextColor());
	}

	public ColorUIResource getControlTextColor() {
		return equalization.apply(DEFAULT_METAL_THEME.getControlTextColor());
	}

	public ColorUIResource getInactiveControlTextColor() {
		return equalization.apply(DEFAULT_METAL_THEME.getInactiveControlTextColor());
	}

	public ColorUIResource getInactiveSystemTextColor() {
		return equalization.apply(DEFAULT_METAL_THEME.getInactiveSystemTextColor());
	}

	public ColorUIResource getUserTextColor() {
		return equalization.apply(DEFAULT_METAL_THEME.getUserTextColor());
	}

	public ColorUIResource getTextHighlightColor() {
		return equalization.apply(DEFAULT_METAL_THEME.getTextHighlightColor());
	}

	public ColorUIResource getHighlightedTextColor() {
		return equalization.apply(DEFAULT_METAL_THEME.getHighlightedTextColor());
	}

	public ColorUIResource getWindowBackground() {
		return equalization.apply(DEFAULT_METAL_THEME.getWindowBackground());
	}

	public ColorUIResource getWindowTitleBackground() {
		return equalization.apply(DEFAULT_METAL_THEME.getWindowTitleBackground());
	}

	public ColorUIResource getWindowTitleForeground() {
		return equalization.apply(DEFAULT_METAL_THEME.getWindowTitleForeground());
	}

	public ColorUIResource getWindowTitleInactiveBackground() {
		return equalization.apply(DEFAULT_METAL_THEME.getWindowTitleInactiveBackground());
	}

	public ColorUIResource getWindowTitleInactiveForeground() {
		return equalization.apply(DEFAULT_METAL_THEME.getWindowTitleInactiveForeground());
	}

	public ColorUIResource getMenuBackground() {
		return equalization.apply(DEFAULT_METAL_THEME.getMenuBackground());
	}

	public ColorUIResource getMenuForeground() {
		return equalization.apply(DEFAULT_METAL_THEME.getMenuForeground());
	}

	public ColorUIResource getMenuSelectedBackground() {
		return equalization.apply(DEFAULT_METAL_THEME.getMenuSelectedBackground());
	}

	public ColorUIResource getMenuSelectedForeground() {
		return equalization.apply(DEFAULT_METAL_THEME.getMenuSelectedForeground());
	}

	public ColorUIResource getMenuDisabledForeground() {
		return equalization.apply(DEFAULT_METAL_THEME.getMenuDisabledForeground());
	}

	public ColorUIResource getSeparatorBackground() {
		return equalization.apply(DEFAULT_METAL_THEME.getSeparatorBackground());
	}

	public ColorUIResource getSeparatorForeground() {
		return equalization.apply(DEFAULT_METAL_THEME.getSeparatorForeground());
	}

	public ColorUIResource getAcceleratorForeground() {
		return equalization.apply(DEFAULT_METAL_THEME.getAcceleratorForeground());
	}

	public ColorUIResource getAcceleratorSelectedForeground() {
		return equalization.apply(DEFAULT_METAL_THEME.getAcceleratorSelectedForeground());
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((equalization == null) ? 0 : equalization.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EqualizedMetalTheme other = (EqualizedMetalTheme) obj;
		if (equalization == null) {
			if (other.equalization != null)
				return false;
		} else if (!equalization.equals(other.equalization))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EqualizedMetalTheme [equalization=" + equalization + "]";
	}

}
