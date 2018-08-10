package xy.lib.theme;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class EqualizedNimbusTheme implements IEqualizedTheme {

	private static NimbusLookAndFeel DEFAULT_NIMBUS_LAF = new NimbusLookAndFeel();

	private ThemeEqualization equalization;

	public EqualizedNimbusTheme() {
		super();
		this.equalization = new ThemeEqualization(DEFAULT_NIMBUS_LAF.getDefaults().getColor("control"));
	}

	@Override
	public ThemeEqualization getEqualization() {
		return equalization;
	}

	@Override
	public void activate() {
		try {
			UIManager.setLookAndFeel(getEqualizedNimbusLookAndFeel());
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

	protected NimbusLookAndFeel getEqualizedNimbusLookAndFeel() {
		return new EqualizedNimbusLookAndFeel(equalization);
	}

	@Override
	public Class<? extends LookAndFeel> getBaseLookAndFeelClass() {
		return NimbusLookAndFeel.class;
	}

	public static class EqualizedNimbusLookAndFeel extends NimbusLookAndFeel {

		private static final long serialVersionUID = 1L;

		private ThemeEqualization equalization;

		public EqualizedNimbusLookAndFeel(ThemeEqualization equalization) {
			this.equalization = equalization;
			updateColor("nimbusBase");
			updateColor("control");
			updateColor("text");
			updateColor("info");
			updateColor("nimbusAlertYellow");
			updateColor("nimbusDisabledText");
			updateColor("nimbusFocus");
			updateColor("nimbusGreen");
			updateColor("nimbusInfoBlue");
			updateColor("nimbusLightBackground");
			updateColor("nimbusRed");
			updateColor("nimbusSelectedText");
			updateColor("nimbusSelectionBackground");
			updateColor("text");
			fixLookAndFeelIssues();
		}

		protected void updateColor(String key) {
			Color baseColor = DEFAULT_NIMBUS_LAF.getDefaults().getColor(key);
			ColorUIResource newColor = equalization.apply(baseColor);
			getDefaults().put(key, newColor);
		}

		protected void fixLookAndFeelIssues() {
			getDefaults().put("ScrollBar.minimumThumbSize", new Dimension(30, 30));
		}

		public static boolean isBeningException(Throwable t) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			t.printStackTrace(new PrintStream(out));
			String printedStackTrace = out.toString();
			if (t instanceof ClassCastException) {
				if (printedStackTrace.contains("javax.swing.plaf.nimbus.NimbusStyle")) {
					return true;
				}
				if (printedStackTrace.contains("javax.swing.plaf.nimbus.DerivedColor")) {
					return true;
				}
				if (printedStackTrace.contains("sun.font.FontDesignMetrics$MetricsKey.init")) {
					return true;
				}
			}
			return false;
		}

	}

}
