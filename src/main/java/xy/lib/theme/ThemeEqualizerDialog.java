package xy.lib.theme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

	private static final String TEST_MAIN_CLASS_PROPERTY_KEY = "xy.metalizer.test.main.class";

	private static final String DEFAULT_TEST_APPLICATION_URL_PROPERTY_KEY = "xy.metalizer.test.app.default.url";

	private static final String DEFAULT_TEST_APPLICATION_MAIN_CLASS_PROPERTY_KEY = "xy.metalizer.test.app.default.main.class";

	protected JPanel contentPanel = new JPanel();
	protected JLabel messageLabel;

	protected JButton defaultThemeButton;
	protected JButton okButton;
	protected JButton cancelButton;

	protected JSlider hueSlider;
	protected JSlider saturationSlider;
	protected JSlider brightnessSlider;
	protected JCheckBox colorsInvertedCheckBox;

	protected boolean themeUpdateRequested;

	protected LookAndFeel initialLookAndFeel;
	protected MetalTheme initialMetalTheme;
	protected ThemeEqualization defaultEqualization;

	private IEqualizedTheme equalizedTheme;

	protected boolean themeAccepted = false;

	protected long previewDelayMilliseconds = 1000;

	protected Thread themeUpdater;

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
	}

	@Override
	public void dispose() {
		stopThemeUpdater();
		if (!themeAccepted) {
			restoreInitialLookAndFeel();
		}
		super.dispose();
	}

	public IEqualizedTheme getEqualizedTheme() {
		return equalizedTheme;
	}

	public void setEqualizedTheme(IEqualizedTheme equalizedTheme) {
		this.equalizedTheme = equalizedTheme;
		setDefaultEqualization(equalizedTheme.getEqualization().clone());
		updateUI();
		themeUpdateRequested = true;
	}

	public ThemeEqualization getDefaultEqualization() {
		return defaultEqualization;
	}

	public void setDefaultEqualization(ThemeEqualization defaultEqualization) {
		this.defaultEqualization = defaultEqualization.clone();
	}

	public void updateUI() {
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

	public long getPreviewDelayMiStringlliseconds() {
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
			messageLabel = new JLabel(translate("Change the Look") + ":");
			{
				changeFontSize(messageLabel, 24);
				messageLabel.setOpaque(true);
				messageLabel.setForeground(new Color(UIManager.getColor("Label.background").getRGB()));
				messageLabel.setBackground(new Color(UIManager.getColor("Label.foreground").getRGB()));
				messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
				contentPanel.add(messageLabel);
			}
			hueSlider = new JSlider();
			{
				hueSlider.setMaximum(255);
				hueSlider.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
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
						colorValueChanged();
					}
				});
				contentPanel.add(brightnessSlider);
			}
			colorsInvertedCheckBox = new JCheckBox();
			{
				colorsInvertedCheckBox.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						colorValueChanged();
					}
				});
				contentPanel.add(colorsInvertedCheckBox);
			}
		}
		JPanel buttonPane = new JPanel();
		{
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setForeground(UIManager.getColor("ComboBox.selectionForeground"));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			okButton = new JButton(translate("OK"));
			{
				okButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						okPressed();
					}
				});
				buttonPane.add(okButton);
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

	protected void defaultsRequested() {
		equalizedTheme.getEqualization().setColor(defaultEqualization.getColor());
		equalizedTheme.getEqualization().setColorsInverted(defaultEqualization.areColorsInverted());;
		updateUI();
		themeUpdateRequested = true;
	}

	private static void changeFontSize(Component c, int newSize) {
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
		hueSlider.setValue(Math.round(equalizedTheme.getEqualization().getHue() * 255));
		saturationSlider.setValue(Math.round(equalizedTheme.getEqualization().getSaturation() * 255));
		brightnessSlider.setValue(Math.round(equalizedTheme.getEqualization().getBrightness() * 255));
		colorsInvertedCheckBox.setSelected(equalizedTheme.getEqualization().areColorsInverted());
	}

	protected void updateLabels() {
		hueSlider.setBorder(BorderFactory.createTitledBorder(translate("Hue") + ": " + hueSlider.getValue()));
		saturationSlider.setBorder(
				BorderFactory.createTitledBorder(translate("Saturation") + ": " + saturationSlider.getValue()));
		brightnessSlider.setBorder(
				BorderFactory.createTitledBorder(translate("Brightness") + ": " + brightnessSlider.getValue()));
		colorsInvertedCheckBox.setText(translate("Invert Colors"));
	}

	protected void updateTheme() {
		equalizedTheme.getEqualization().setHue(hueSlider.getValue() / 255f);
		equalizedTheme.getEqualization().setSaturation(saturationSlider.getValue() / 255f);
		equalizedTheme.getEqualization().setBrightness(brightnessSlider.getValue() / 255f);
		equalizedTheme.getEqualization().setColorsInverted(colorsInvertedCheckBox.isSelected());
		equalizedTheme.activate();
		messageLabel.setForeground(new Color(UIManager.getColor("Label.background").getRGB()));
		messageLabel.setBackground(new Color(UIManager.getColor("Label.foreground").getRGB()));
	}

	private static void showBusy(Window window, boolean b) {
		if (b) {
			window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} else {
			window.setCursor(Cursor.getDefaultCursor());
		}
	}

	protected String translate(String string) {
		return string;
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

	protected static Image getIcon() {
		return Toolkit.getDefaultToolkit().getImage(ThemeEqualizerDialog.class.getResource("/xy/lib/theme/theme.gif"));
	}

	public static List<IEqualizedTheme> getEqualizedThemeBaseOptions() {
		return Arrays.asList(new EqualizedMetalTheme(), new EqualizedNimbusTheme(), new EqualizedGlassTheme());
	}

	public static IEqualizedTheme askEqualizedThemeBase() {
		List<IEqualizedTheme> options = getEqualizedThemeBaseOptions();
		List<String> optionNames = new ArrayList<String>();
		for (IEqualizedTheme option : options) {
			Class<? extends LookAndFeel> baseLAFClass = option.getBaseLookAndFeelClass();
			LookAndFeel baseLAFInstance;
			try {
				baseLAFInstance = baseLAFClass.newInstance();
			} catch (Exception e) {
				throw new AssertionError(e);
			}
			String basLAFName = baseLAFInstance.getName();
			optionNames.add(basLAFName);
		}
		String selectedOptionName = (String) JOptionPane.showInputDialog(null, "Base Look & Feel", "",
				JOptionPane.PLAIN_MESSAGE, new ImageIcon(getIcon()), optionNames.toArray(), null);
		if (selectedOptionName == null) {
			return null;
		}
		int selectionIndex = optionNames.indexOf(selectedOptionName);
		return options.get(selectionIndex);
	}

	public static boolean open(Window parent) {
		IEqualizedTheme selectedTheme = askEqualizedThemeBase();
		if (selectedTheme == null) {
			return false;
		}
		return open(parent, selectedTheme);
	}

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
		IEqualizedTheme selectedTheme = askEqualizedThemeBase();
		if (selectedTheme == null) {
			return;
		}
		tryToLaunchTheMainApplication(args);
		ThemeEqualizerDialog dialog = new ThemeEqualizerDialog(null, selectedTheme);
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
