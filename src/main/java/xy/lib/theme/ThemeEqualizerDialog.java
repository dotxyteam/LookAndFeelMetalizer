package xy.lib.theme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

/**
 * This class allows to preview and choose a configuration that can be used with
 * an {@link IEqualizedTheme} instance.
 * 
 * @author olitank
 *
 */
public class ThemeEqualizerDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private static final String TEST_MAIN_CLASS_PROPERTY_KEY = "xy.metalizer.test.main.class";

	private static final String DEFAULT_TEST_APPLICATION_URL_PROPERTY_KEY = "xy.metalizer.test.app.default.url";

	private static final String DEFAULT_TEST_APPLICATION_MAIN_CLASS_PROPERTY_KEY = "xy.metalizer.test.app.default.main.class";

	protected JPanel contentPanel;
	protected JPanel colorsRotationPanel;
	protected JLabel messageLabel;

	protected JButton defaultThemeButton;
	protected JButton randomizeButton;
	protected JButton okButton;
	protected JButton cancelButton;

	protected JSlider latitudeSlider;
	protected JSlider longitudeSlider;
	protected JSlider hueSlider;
	protected JSlider saturationSlider;
	protected JSlider brightnessSlider;
	protected JCheckBox colorsInvertedCheckBox;

	protected boolean themeUpdateRequested;

	protected LookAndFeel initialLookAndFeel;
	protected MetalTheme initialMetalTheme;
	protected ThemeEqualization defaultEqualization;
	protected IEqualizedTheme equalizedTheme;

	protected boolean themeAccepted = false;

	protected long previewDelayMilliseconds = 1000;

	protected Thread themeUpdater;

	protected boolean eventsDisabled = false;

	/**
	 * The main constructor. Builds an instance that will use the provided theme to
	 * hold and preview the settings.
	 * 
	 * @param parent The parent window or null.
	 * @param theme  The theme instance that will be used.
	 */
	public ThemeEqualizerDialog(Window parent, IEqualizedTheme theme) {
		super(parent);
		setIconImage(getIcon());
		if (parent != null) {
			if (parent.getIconImages() != null) {
				if (parent.getIconImages().size() > 0) {
					setIconImage(parent.getIconImages().get(0));
				}
			}
		}
		initializeControls();
		initializeControlValues(theme);
		startThemeUpdater();
		pack();
	}

	@Override
	public void dispose() {
		stopThemeUpdater();
		if (!themeAccepted) {
			restoreInitialLookAndFeel();
		}
		super.dispose();
	}

	/**
	 * @return The theme instance that is used to hold and preview the settings.
	 */
	public IEqualizedTheme getEqualizedTheme() {
		return equalizedTheme;
	}

	/**
	 * Changes the theme instance that is used to hold and preview the settings.
	 * 
	 * @param equalizedTheme The new theme instance.
	 */
	public void setEqualizedTheme(IEqualizedTheme equalizedTheme) {
		this.equalizedTheme = equalizedTheme;
		setDefaultEqualization(equalizedTheme.getEqualization().clone());
		updateUserInterface();
		themeUpdateRequested = true;
	}

	/**
	 * @return An object that holds the initial settings of the theme.
	 */
	public ThemeEqualization getDefaultEqualization() {
		return defaultEqualization;
	}

	/**
	 * Changes the object that holds the initial settings of the theme.
	 * 
	 * @param defaultEqualization The new object that will hold the initial settings
	 *                            of the theme.
	 */
	public void setDefaultEqualization(ThemeEqualization defaultEqualization) {
		this.defaultEqualization = defaultEqualization.clone();
	}

	protected void updateUserInterface() {
		updateControls();
		updateLabels();
	}

	protected void restoreInitialLookAndFeel() {
		showBusy(this, true);
		try {
			MetalLookAndFeel.setCurrentTheme(initialMetalTheme);
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

	protected long getPreviewDelayMiStringlliseconds() {
		return previewDelayMilliseconds;
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
		contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			messageLabel = new JLabel(translate("Change the Theme"));
			{
				changeFontSize(messageLabel, 24);
				messageLabel.setAlignmentX(CENTER_ALIGNMENT);
				messageLabel.setOpaque(true);
				messageLabel.setForeground(new Color(UIManager.getColor("Label.background").getRGB()));
				messageLabel.setBackground(new Color(UIManager.getColor("Label.foreground").getRGB()));
				messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
				contentPanel.add(messageLabel);
			}
			colorsRotationPanel = new JPanel();
			{
				colorsRotationPanel.setLayout(new BoxLayout(colorsRotationPanel, BoxLayout.Y_AXIS));
				colorsRotationPanel.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder(translate("Rotate Colors")), new EmptyBorder(10, 10, 10, 10)));
				latitudeSlider = new JSlider();
				{
					latitudeSlider.setMaximum(255);
					latitudeSlider.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							if (eventsDisabled) {
								return;
							}
							colorValueChanged();
						}
					});
					colorsRotationPanel.add(latitudeSlider);
				}
				longitudeSlider = new JSlider();
				{
					longitudeSlider.setMaximum(255);
					longitudeSlider.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							if (eventsDisabled) {
								return;
							}
							colorValueChanged();
						}
					});
					colorsRotationPanel.add(longitudeSlider);
				}
				contentPanel.add(colorsRotationPanel);
			}
			hueSlider = new JSlider();
			{
				hueSlider.setMaximum(255);
				hueSlider.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						if (eventsDisabled) {
							return;
						}
						colorValueChanged();
					}
				});
				contentPanel.add(hueSlider);
			}
			saturationSlider = new JSlider();
			{
				saturationSlider.setMaximum(255);
				saturationSlider.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						if (eventsDisabled) {
							return;
						}
						colorValueChanged();
					}
				});
				contentPanel.add(saturationSlider);
			}
			brightnessSlider = new JSlider();
			{
				brightnessSlider.setMaximum(255);
				brightnessSlider.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						if (eventsDisabled) {
							return;
						}
						colorValueChanged();
					}
				});
				contentPanel.add(brightnessSlider);
			}
			colorsInvertedCheckBox = new JCheckBox();
			{
				colorsInvertedCheckBox.setAlignmentX(CENTER_ALIGNMENT);
				colorsInvertedCheckBox.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (eventsDisabled) {
							return;
						}
						colorValueChanged();
					}
				});
				contentPanel.add(colorsInvertedCheckBox);
			}
		}
		JPanel buttonPane = new JPanel();
		{
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			randomizeButton = new JButton(translate("Randomize"));
			{
				randomizeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						randomize();
					}
				});
				buttonPane.add(randomizeButton);
			}
			defaultThemeButton = new JButton(translate("Restore Defaults"));
			{
				defaultThemeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						defaultsRequested();
					}
				});
				buttonPane.add(defaultThemeButton);
			}
			okButton = new JButton(translate("OK"));
			{
				okButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						okPressed();
					}
				});
				buttonPane.add(okButton);
			}
			cancelButton = new JButton(translate("Cancel"));
			{
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelPressed();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		getRootPane().setDefaultButton(okButton);
	}

	protected void randomize() {
		eventsDisabled = true;
		try {
			Random random = new Random(System.currentTimeMillis());
			latitudeSlider.setValue(Math.round(random.nextFloat() * 255));
			longitudeSlider.setValue(Math.round(random.nextFloat() * 255));
			hueSlider.setValue(Math.round(random.nextFloat() * 255));
			saturationSlider.setValue(Math.round(random.nextFloat() * 255));
			brightnessSlider.setValue(Math.round(random.nextFloat() * 255));
			colorsInvertedCheckBox.setSelected(random.nextBoolean());
		} finally {
			eventsDisabled = false;
		}
		themeUpdateRequested = true;
	}

	protected void defaultsRequested() {
		eventsDisabled = true;
		try {
			latitudeSlider.setValue(defaultEqualization.getLatitude());
			longitudeSlider.setValue(defaultEqualization.getLongitude());
			hueSlider.setValue(defaultEqualization.getHue());
			saturationSlider.setValue(defaultEqualization.getSaturation());
			brightnessSlider.setValue(defaultEqualization.getBrightness());
			colorsInvertedCheckBox.setSelected(defaultEqualization.areColorsInverted());
		} finally {
			eventsDisabled = false;
		}
		themeUpdateRequested = true;
	}

	protected static void changeFontSize(Component c, int newSize) {
		c.setFont(c.getFont().deriveFont((float) newSize));
	}

	protected void initializeControlValues(IEqualizedTheme equalizedTheme) {
		initialLookAndFeel = UIManager.getLookAndFeel();
		initialMetalTheme = MetalLookAndFeel.getCurrentTheme();
		setEqualizedTheme(equalizedTheme);
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

	protected void updateControls() {
		eventsDisabled = true;
		try {
			latitudeSlider.setValue(equalizedTheme.getEqualization().getLatitude());
			longitudeSlider.setValue(equalizedTheme.getEqualization().getLongitude());
			hueSlider.setValue(equalizedTheme.getEqualization().getHue());
			saturationSlider.setValue(equalizedTheme.getEqualization().getSaturation());
			brightnessSlider.setValue(equalizedTheme.getEqualization().getBrightness());
			colorsInvertedCheckBox.setSelected(equalizedTheme.getEqualization().areColorsInverted());
		} finally {
			eventsDisabled = false;
		}
	}

	protected void updateLabels() {
		latitudeSlider
				.setBorder(BorderFactory.createTitledBorder(translate("Latitude") + ": " + latitudeSlider.getValue()));
		longitudeSlider.setBorder(
				BorderFactory.createTitledBorder(translate("Longitude") + ": " + longitudeSlider.getValue()));
		hueSlider.setBorder(BorderFactory.createTitledBorder(translate("Hue") + ": " + hueSlider.getValue()));
		saturationSlider.setBorder(
				BorderFactory.createTitledBorder(translate("Saturation") + ": " + saturationSlider.getValue()));
		brightnessSlider.setBorder(
				BorderFactory.createTitledBorder(translate("Brightness") + ": " + brightnessSlider.getValue()));
		colorsInvertedCheckBox.setText(translate("Invert Colors"));
	}

	protected void updateTheme() {
		equalizedTheme.getEqualization().setHue(hueSlider.getValue());
		equalizedTheme.getEqualization().setSaturation(saturationSlider.getValue());
		equalizedTheme.getEqualization().setBrightness(brightnessSlider.getValue());
		equalizedTheme.getEqualization().setLatitude(latitudeSlider.getValue());
		equalizedTheme.getEqualization().setLongitude(longitudeSlider.getValue());
		equalizedTheme.getEqualization().setColorsInverted(colorsInvertedCheckBox.isSelected());
		equalizedTheme.activate();
		messageLabel.setForeground(new Color(UIManager.getColor("Label.background").getRGB()));
		messageLabel.setBackground(new Color(UIManager.getColor("Label.foreground").getRGB()));
	}

	protected static void showBusy(Window window, boolean b) {
		if (b) {
			window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} else {
			window.setCursor(Cursor.getDefaultCursor());
		}
	}

	protected String translate(String string) {
		return string;
	}

	protected static void tryToLaunchTheMainApplication(final String[] args) {
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

	protected static Image getIcon() {
		return Toolkit.getDefaultToolkit().getImage(ThemeEqualizerDialog.class.getResource("/xy/lib/theme/theme.gif"));
	}

	/**
	 * Creates and opens an instance of this class.
	 * 
	 * @param parent The parent window.
	 * @param theme  The theme instance that will be used to hold and preview the
	 *               settings.
	 * @return Whether the theme configuration was accepted or not.
	 */
	public static boolean open(Window parent, IEqualizedTheme theme) {
		ThemeEqualizerDialog dialog = new ThemeEqualizerDialog(parent, theme);
		dialog.setVisible(true);
		if (dialog.isThemeAccepted()) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) throws Exception {
		tryToLaunchTheMainApplication(args);
		ThemeEqualizerDialog dialog = new ThemeEqualizerDialog(null, new EqualizedMetalTheme());
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
}
