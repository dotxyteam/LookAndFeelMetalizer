package xy.lib.theme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Window;
import java.util.Arrays;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import sun.swing.PrintColorUIResource;
import sun.swing.SwingLazyValue;
import sun.swing.SwingUtilities2;

/**
 * This class allows to easily change the colors of the swing Metal
 * look-and-feel ({@link MetalLookAndFeel}) while keeping them as consistent as
 * possible. It is based on the {@link OceanTheme}.
 * 
 * @author olitank
 *
 */
public class EqualizedTheme extends OceanTheme {

	private ThemeEqualization equalization;

	/**
	 * The default constructor. Builds a theme with default (unchanged) colors.
	 */
	public EqualizedTheme() {
		super();
		this.equalization = new ThemeEqualization(super.getSecondary3());
	}

	/**
	 * @return The object allowing to vary this theme.
	 */
	public ThemeEqualization getEqualization() {
		return equalization;
	}

	/**
	 * Sets this theme as current. Note that the {@link MetalLookAndFeel} will be
	 * set as the current look-and-feel and all open windows will be update after
	 * the execution of this method.
	 */
	public void activate() {
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

	@Override
	protected ColorUIResource getPrimary1() {
		return equalization.apply(super.getPrimary1());
	}

	@Override
	protected ColorUIResource getPrimary2() {
		return equalization.apply(super.getPrimary2());
	}

	@Override
	protected ColorUIResource getPrimary3() {
		return equalization.apply(super.getPrimary3());
	}

	@Override
	protected ColorUIResource getSecondary1() {
		return equalization.apply(super.getSecondary1());
	}

	@Override
	protected ColorUIResource getSecondary2() {
		return equalization.apply(super.getSecondary2());
	}

	@Override
	protected ColorUIResource getSecondary3() {
		return equalization.apply(super.getSecondary3());
	}

	@Override
	protected ColorUIResource getWhite() {
		return equalization.apply(super.getWhite());
	}

	@Override
	protected ColorUIResource getBlack() {
		return equalization.apply(super.getBlack());
	}

	@Override
	public ColorUIResource getControlTextColor() {
		return equalization.apply(super.getControlTextColor());
	}

	@Override
	public ColorUIResource getInactiveControlTextColor() {
		return equalization.apply(super.getInactiveControlTextColor());
	}

	@Override
	public ColorUIResource getMenuDisabledForeground() {
		return equalization.apply(super.getMenuDisabledForeground());
	}

	@Override
	public ColorUIResource getDesktopColor() {
		return equalization.apply(super.getDesktopColor());
	}

	private Object getIconResource(String iconID) {
		return SwingUtilities2.makeIcon(getClass(), OceanTheme.class, iconID);
	}

	private Icon getHastenedIcon(String iconID, UIDefaults table) {
		Object res = getIconResource(iconID);
		return (Icon) ((UIDefaults.LazyValue) res).createValue(table);
	}

	@Override
	public void addCustomEntriesToTable(UIDefaults table) {
		final ColorUIResource INACTIVE_CONTROL_TEXT_COLOR = equalization.apply(new ColorUIResource(0x999999));
		final ColorUIResource SECONDARY3 = equalization.apply(new ColorUIResource(0xEEEEEE));
		final ColorUIResource OCEAN_DROP = equalization.apply(new ColorUIResource(0xD2E9FF));
		final ColorUIResource SECONDARY2 = equalization.apply(new ColorUIResource(0xB8CFE5));
		final ColorUIResource SECONDARY1 = equalization.apply(new ColorUIResource(0x7A8A99));
		final ColorUIResource OCEAN_BLACK = equalization.apply(new PrintColorUIResource(0x333333, Color.BLACK));

		Object focusBorder = new SwingLazyValue("javax.swing.plaf.BorderUIResource$LineBorderUIResource",
				new Object[] { getPrimary1() });
		// .30 0 DDE8F3 white secondary2
		java.util.List<Object> buttonGradient = Arrays.asList(new Object[] { new Float(.3f), new Float(0f),
				equalization.apply(new ColorUIResource(0xDDE8F3)), getWhite(), getSecondary2() });

		// Other possible properties that aren't defined:
		//
		// Used when generating the disabled Icons, provides the region to
		// constrain grays to.
		// Button.disabledGrayRange -> Object[] of Integers giving min/max
		// InternalFrame.inactiveTitleGradient -> Gradient when the
		// internal frame is inactive.
		Color cccccc = equalization.apply(new ColorUIResource(0xCCCCCC));
		Color dadada = equalization.apply(new ColorUIResource(0xDADADA));
		Color c8ddf2 = equalization.apply(new ColorUIResource(0xC8DDF2));
		Object directoryIcon = getIconResource("icons/ocean/directory.gif");
		Object fileIcon = getIconResource("icons/ocean/file.gif");
		java.util.List<Object> sliderGradient = Arrays.asList(
				new Object[] { new Float(.3f), new Float(.2f), c8ddf2, getWhite(), new ColorUIResource(SECONDARY2) });

		Object[] defaults = new Object[] { "Button.gradient", buttonGradient, "Button.rollover", Boolean.TRUE,
				"Button.toolBarBorderBackground", INACTIVE_CONTROL_TEXT_COLOR, "Button.disabledToolBarBorderBackground",
				cccccc, "Button.rolloverIconType", "ocean",

				"CheckBox.rollover", Boolean.TRUE, "CheckBox.gradient", buttonGradient,

				"CheckBoxMenuItem.gradient", buttonGradient,

				// home2
				"FileChooser.homeFolderIcon", getIconResource("icons/ocean/homeFolder.gif"),
				// directory2
				"FileChooser.newFolderIcon", getIconResource("icons/ocean/newFolder.gif"),
				// updir2
				"FileChooser.upFolderIcon", getIconResource("icons/ocean/upFolder.gif"),

				// computer2
				"FileView.computerIcon", getIconResource("icons/ocean/computer.gif"), "FileView.directoryIcon",
				directoryIcon,
				// disk2
				"FileView.hardDriveIcon", getIconResource("icons/ocean/hardDrive.gif"), "FileView.fileIcon", fileIcon,
				// floppy2
				"FileView.floppyDriveIcon", getIconResource("icons/ocean/floppy.gif"),

				"Label.disabledForeground", getInactiveControlTextColor(),

				"Menu.opaque", Boolean.FALSE,

				"MenuBar.gradient",
				Arrays.asList(
						new Object[] { new Float(1f), new Float(0f), getWhite(), dadada, new ColorUIResource(dadada) }),
				"MenuBar.borderColor", cccccc,

				"InternalFrame.activeTitleGradient", buttonGradient,
				// close2
				"InternalFrame.closeIcon", new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return new IFIcon(getHastenedIcon("icons/ocean/close.gif", table),
								getHastenedIcon("icons/ocean/close-pressed.gif", table));
					}
				},
				// minimize
				"InternalFrame.iconifyIcon", new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return new IFIcon(getHastenedIcon("icons/ocean/iconify.gif", table),
								getHastenedIcon("icons/ocean/iconify-pressed.gif", table));
					}
				},
				// restore
				"InternalFrame.minimizeIcon", new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return new IFIcon(getHastenedIcon("icons/ocean/minimize.gif", table),
								getHastenedIcon("icons/ocean/minimize-pressed.gif", table));
					}
				},
				// menubutton3
				"InternalFrame.icon", getIconResource("icons/ocean/menu.gif"),
				// maximize2
				"InternalFrame.maximizeIcon", new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return new IFIcon(getHastenedIcon("icons/ocean/maximize.gif", table),
								getHastenedIcon("icons/ocean/maximize-pressed.gif", table));
					}
				},
				// paletteclose
				"InternalFrame.paletteCloseIcon", new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return new IFIcon(getHastenedIcon("icons/ocean/paletteClose.gif", table),
								getHastenedIcon("icons/ocean/paletteClose-pressed.gif", table));
					}
				},

				"List.focusCellHighlightBorder", focusBorder,

				"MenuBarUI", "javax.swing.plaf.metal.MetalMenuBarUI",

				"OptionPane.errorIcon", getIconResource("icons/ocean/error.png"), "OptionPane.informationIcon",
				getIconResource("icons/ocean/info.png"), "OptionPane.questionIcon",
				getIconResource("icons/ocean/question.png"), "OptionPane.warningIcon",
				getIconResource("icons/ocean/warning.png"),

				"RadioButton.gradient", buttonGradient, "RadioButton.rollover", Boolean.TRUE,

				"RadioButtonMenuItem.gradient", buttonGradient,

				"ScrollBar.gradient", buttonGradient,

				"Slider.altTrackColor", new ColorUIResource(0xD2E2EF), "Slider.gradient", sliderGradient,
				"Slider.focusGradient", sliderGradient,

				"SplitPane.oneTouchButtonsOpaque", Boolean.FALSE, "SplitPane.dividerFocusColor", c8ddf2,

				"TabbedPane.borderHightlightColor", getPrimary1(), "TabbedPane.contentAreaColor", c8ddf2,
				"TabbedPane.contentBorderInsets", new Insets(4, 2, 3, 3), "TabbedPane.selected", c8ddf2,
				"TabbedPane.tabAreaBackground", dadada, "TabbedPane.tabAreaInsets", new Insets(2, 2, 0, 6),
				"TabbedPane.unselectedBackground", SECONDARY3,

				"Table.focusCellHighlightBorder", focusBorder, "Table.gridColor", SECONDARY1,
				"TableHeader.focusCellBackground", c8ddf2,

				"ToggleButton.gradient", buttonGradient,

				"ToolBar.borderColor", cccccc, "ToolBar.isRollover", Boolean.TRUE,

				"Tree.closedIcon", directoryIcon,

				"Tree.collapsedIcon", new UIDefaults.LazyValue() {
					public Object createValue(UIDefaults table) {
						return new COIcon(getHastenedIcon("icons/ocean/collapsed.gif", table),
								getHastenedIcon("icons/ocean/collapsed-rtl.gif", table));
					}
				},

				"Tree.expandedIcon", getIconResource("icons/ocean/expanded.gif"), "Tree.leafIcon", fileIcon,
				"Tree.openIcon", directoryIcon, "Tree.selectionBorderColor", getPrimary1(), "Tree.dropLineColor",
				getPrimary1(), "Table.dropLineColor", getPrimary1(), "Table.dropLineShortColor", OCEAN_BLACK,

				"Table.dropCellBackground", OCEAN_DROP, "Tree.dropCellBackground", OCEAN_DROP,
				"List.dropCellBackground", OCEAN_DROP, "List.dropLineColor", getPrimary1() };
		table.putDefaults(defaults);
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
		EqualizedTheme other = (EqualizedTheme) obj;
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

	private static class IFIcon extends IconUIResource {
		private static final long serialVersionUID = 1L;
		private Icon pressed;

		public IFIcon(Icon normal, Icon pressed) {
			super(normal);
			this.pressed = pressed;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			ButtonModel model = ((AbstractButton) c).getModel();
			if (model.isPressed() && model.isArmed()) {
				pressed.paintIcon(c, g, x, y);
			} else {
				super.paintIcon(c, g, x, y);
			}
		}
	}

	private static class COIcon extends IconUIResource {
		private static final long serialVersionUID = 1L;
		private Icon rtl;

		public COIcon(Icon ltr, Icon rtl) {
			super(ltr);
			this.rtl = rtl;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			if (isLeftToRight(c)) {
				super.paintIcon(c, g, x, y);
			} else {
				rtl.paintIcon(c, g, x, y);
			}
		}

		static boolean isLeftToRight(Component c) {
			return c.getComponentOrientation().isLeftToRight();
		}
	}

}
