package xy.lib.theme;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

public class ThemeEqualizerDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private static final String TEST_MAIN_CLASS_PROPERTY_KEY = "xy.test.main.class";

	private static final String DEFAULT_TEST_APPLICATION_URL_PROPERTY_KEY = "xy.test.app.default.url";

	private static final String DEFAULT_TEST_APPLICATION_MAIN_CLASS_PROPERTY_KEY = "xy.test.app.default.main.class";

	protected final JPanel contentPanel = new JPanel();
	protected JSlider hueSlider;
	protected JSlider saturationSlider;
	protected JSlider brightnessSlider;
	protected boolean themeUpdateRequested = false;

	protected LookAndFeel initialLookAndFeel;
	protected MetalTheme initialTheme;

	protected boolean themeAccepted = false;

	protected long previewDelayMilliseconds = 1000;

	protected JLabel messageLabel;
	protected JButton defaultThemeButton;
	protected JButton okButton;
	protected JButton cancelButton;

	private Thread themeUpdater;

	public ThemeEqualizerDialog(Window parent, EqualizedMetalTheme initialTheme) {
		super(parent);
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(ThemeEqualizerDialog.class.getResource("/xy/lib/theme/theme.gif")));
		if (parent != null) {
			if (parent.getIconImages() != null) {
				if (parent.getIconImages().size() > 0) {
					setIconImage(parent.getIconImages().get(0));
				}
			}
		}
		initializeControls();
		initializeControlValues(initialTheme);
		startThemeUpdater();
	}

	@Override
	public void dispose() {
		stopThemeUpdater();
		if (!themeAccepted) {
			restoreInitialLookAndFeel();
		}
		super.dispose();
	}

	protected void restoreInitialLookAndFeel() {
		showBusy(this, true);
		try {
			MetalLookAndFeel.setCurrentTheme(initialTheme);
			try {
				UIManager.setLookAndFeel(initialLookAndFeel);
			} catch (UnsupportedLookAndFeelException e) {
				throw new AssertionError(e);
			}
			for (Window window : Window.getWindows()) {
				SwingUtilities.updateComponentTreeUI(window);
			}
		} finally {
			showBusy(this, false);
		}
	}

	public long getPreviewDelayMilliseconds() {
		return previewDelayMilliseconds;
	}

	public void setPreviewDelayMilliseconds(long milliseconds) {
		this.previewDelayMilliseconds = milliseconds;
	}

	protected void startThemeUpdater() {
		themeUpdater = new Thread(ThemeEqualizerDialog.class.getName() + " - ThemeUpdater") {
			@Override
			public void run() {
				while (true) {
					try {
						if (isInterrupted()) {
							break;
						}
						if (themeUpdateRequested) {
							showBusy(ThemeEqualizerDialog.this, true);
							try {
								sleep(previewDelayMilliseconds);
								themeUpdateRequested = false;
								SwingUtilities.invokeAndWait(new Runnable() {
									@Override
									public void run() {
										updateTheme();
									}
								});
							} finally {
								showBusy(ThemeEqualizerDialog.this, false);
							}
						} else {
							sleep(previewDelayMilliseconds);
						}
					} catch (InterruptedException e) {
						interrupt();
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}
		};
		themeUpdater.setDaemon(true);
		themeUpdater.setPriority(Thread.MIN_PRIORITY);
		themeUpdater.start();
	}

	protected void stopThemeUpdater() {
		themeUpdater.interrupt();
		try {
			themeUpdater.join();
		} catch (InterruptedException e) {
			throw new AssertionError(e);
		}
	}

	protected void initializeControls() {
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new GridLayout(0, 1));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		{
			hueSlider = new JSlider();
			hueSlider.setMaximum(255);
			hueSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					colorValueChanged();
				}
			});
			{
				messageLabel = new JLabel(getString("Change the Look") + ":");
				changeFontSize(messageLabel, 24);
				messageLabel.setOpaque(true);
				messageLabel.setForeground(UIManager.getColor("Label.background"));
				messageLabel.setBackground(UIManager.getColor("Label.foreground"));
				messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
				contentPanel.add(messageLabel);
			}
			contentPanel.add(hueSlider);

			saturationSlider = new JSlider();
			saturationSlider.setMaximum(255);
			saturationSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					colorValueChanged();
				}
			});
			contentPanel.add(saturationSlider);

			brightnessSlider = new JSlider();
			brightnessSlider.setMaximum(255);
			brightnessSlider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					colorValueChanged();
				}
			});
			contentPanel.add(brightnessSlider);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setForeground(UIManager.getColor("ComboBox.selectionForeground"));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton(getString("OK"));
				okButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						okPressed();
					}
				});
				{
					defaultThemeButton = new JButton(getString("Restore Defaults"));
					defaultThemeButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							defaultThemeRequested();
						}
					});
					buttonPane.add(defaultThemeButton);
				}
				buttonPane.add(okButton);

				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton(getString("Cancel"));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelPressed();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	protected String getString(String string) {
		return string;
	}

	private static void changeFontSize(Component c, int newSize) {
		c.setFont(c.getFont().deriveFont((float) newSize));
	}

	protected void defaultThemeRequested() {
		setSelectedTheme(new EqualizedMetalTheme());
	}

	protected void initializeControlValues(EqualizedMetalTheme initialEqualizedTheme) {
		initialLookAndFeel = UIManager.getLookAndFeel();
		initialTheme = MetalLookAndFeel.getCurrentTheme();
		if (initialEqualizedTheme == null) {
			initialEqualizedTheme = new EqualizedMetalTheme();
		}
		setSelectedTheme(initialEqualizedTheme);
	}

	protected void cancelPressed() {
		themeAccepted = false;
		dispose();
	}

	protected void okPressed() {
		themeAccepted = true;
		dispose();
	}

	public boolean isThemeAccepted() {
		return themeAccepted;
	}

	protected void colorValueChanged() {
		updateLabels();
		themeUpdateRequested = true;
	}

	protected void updateLabels() {
		hueSlider.setBorder(BorderFactory.createTitledBorder(getString("Hue") + ": " + hueSlider.getValue()));
		saturationSlider.setBorder(
				BorderFactory.createTitledBorder(getString("Saturation") + ": " + saturationSlider.getValue()));
		brightnessSlider.setBorder(
				BorderFactory.createTitledBorder(getString("Brightness") + ": " + brightnessSlider.getValue()));
	}

	protected void updateTheme() {
		EqualizedMetalTheme selectedTheme = getSelectedTheme();
		selectedTheme.activate();
		messageLabel.setForeground(UIManager.getColor("Label.background"));
		messageLabel.setBackground(UIManager.getColor("Label.foreground"));
	}

	private static void showBusy(Window window, boolean b) {
		if (b) {
			window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} else {
			window.setCursor(Cursor.getDefaultCursor());
		}
	}

	public EqualizedMetalTheme getSelectedTheme() {
		EqualizedMetalTheme result = new EqualizedMetalTheme();
		result.getEqualization().setHue(hueSlider.getValue()/255f);
		result.getEqualization().setSaturation(saturationSlider.getValue()/255f);
		result.getEqualization().setBrightness(brightnessSlider.getValue()/255f);
		return result;
	}

	public void setSelectedTheme(EqualizedMetalTheme theme) {
		hueSlider.setValue(Math.round(theme.getEqualization().getHue() * 255));
		saturationSlider.setValue(Math.round(theme.getEqualization().getSaturation() * 255));
		brightnessSlider.setValue(Math.round(theme.getEqualization().getBrightness() * 255));	
	}

	public static void main(String[] args) throws Exception {
		tryToLaunchTheMainApplication(args);
		ThemeEqualizerDialog dialog = new ThemeEqualizerDialog(null, null);
		dialog.setModal(false);
		dialog.setAlwaysOnTop(true);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});
		dialog.setVisible(true);
	}

	public static EqualizedMetalTheme open(Window parent) {
		return open(parent, new EqualizedMetalTheme());
	}

	public static EqualizedMetalTheme open(Window parent, EqualizedMetalTheme initialTheme) {
		ThemeEqualizerDialog dialog = new ThemeEqualizerDialog(parent, initialTheme);
		dialog.setVisible(true);
		if (dialog.isThemeAccepted()) {
			return dialog.getSelectedTheme();
		} else {
			return null;
		}
	}

	private static void tryToLaunchTheMainApplication(final String[] args) {
		new Thread() {
			@Override
			public void run() {
				try {
					String mainClassName = System.getProperty(TEST_MAIN_CLASS_PROPERTY_KEY);
					ClassLoader classLoader = ClassLoader.getSystemClassLoader();
					if (mainClassName == null) {
						classLoader = URLClassLoader.newInstance(new URL[] { new URL(System.getProperty(
								DEFAULT_TEST_APPLICATION_URL_PROPERTY_KEY,
								"https://raw.githubusercontent.com/dotxyteam/LookAndFeelMetalizer/master/tools/SwingSet2.jar")) },
								getClass().getClassLoader());
						mainClassName = System.getProperty(DEFAULT_TEST_APPLICATION_MAIN_CLASS_PROPERTY_KEY,
								"SwingSet2");
					}
					Class<?> mainClass = Class.forName(mainClassName, true, classLoader);
					Method mainMethod = mainClass.getMethod("main", String[].class);
					mainMethod.invoke(null, new Object[] { args });
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

		}.start();
	}
}
