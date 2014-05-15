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
import java.lang.reflect.Method;

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

	private static final String MAIN_CLASS_PROPERTY_KEY = "xy.main.class";

	protected final JPanel contentPanel = new JPanel();
	protected JSlider hueSlider;
	protected JSlider saturationSlider;
	protected JSlider brightnessSlider;
	protected Runnable willRunOnce;
	protected Object willRunOnceMutex = new Object();

	protected LookAndFeel initialLookAndFeel;
	protected MetalTheme initialTheme;

	protected boolean themeAccepted = false;

	protected long previewDelayMilliseconds = 1000;

	protected JLabel messageLabel;
	protected JButton defaultThemeButton;
	protected JButton okButton;
	protected JButton cancelButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception {
		tryToLaunchTheMainApplication(args);
		open(null);
	}

	private static void tryToLaunchTheMainApplication(final String[] args) {
		new Thread() {
			@Override
			public void run() {
				try {
					String mainClassName = System
							.getProperty(MAIN_CLASS_PROPERTY_KEY);
					if (mainClassName != null) {
						Class<?> mainClass = Class.forName(mainClassName);
						Method mainMethod = mainClass.getMethod("main",
								String[].class);
						mainMethod.invoke(null, new Object[] { args });
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

		}.start();
	}

	public static void open(Window parent) {
		open(parent, EqualizedTheme.getDefaultHueOffset(),
				EqualizedTheme.getDefaultSaturationOffset(),
				EqualizedTheme.getDefaultBrightnessOffset());
	}

	public static EqualizedTheme open(Window parent, int initialHueOffset,
			int initialSaturationOffset, int initialBrightnessOffset) {
		ThemeEqualizerDialog dialog = new ThemeEqualizerDialog(parent,
				new EqualizedTheme(initialHueOffset, initialSaturationOffset,
						initialBrightnessOffset));
		dialog.setVisible(true);
		if (dialog.isThemeAccepted()) {
			return dialog.getSelectedTheme();
		} else {
			return null;
		}
	}

	/**
	 * Create the dialog.
	 * 
	 * @param initialTheme
	 */
	public ThemeEqualizerDialog(Window parent, EqualizedTheme initialValues) {
		super(parent);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("theme.gif")));
		if (parent != null) {
			if (parent.getIconImages() != null) {
				if (parent.getIconImages().size() > 0) {
					setIconImage(parent.getIconImages().get(0));
				}
			}
			setModal(true);
		}
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
				messageLabel.setForeground(UIManager
						.getColor("Label.background"));
				messageLabel.setBackground(UIManager
						.getColor("Label.foreground"));
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
			buttonPane.setForeground(UIManager
					.getColor("ComboBox.selectionForeground"));
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
					defaultThemeButton = new JButton(
							getString("Restore Defaults"));
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
			initValues(initialValues);
		}
	}

	protected String getString(String string) {
		return string;
	}

	private static void changeFontSize(Component c, int newSize) {
		c.setFont(c.getFont().deriveFont((float) newSize));
	}

	protected void defaultThemeRequested() {
		setSelectedTheme(new EqualizedTheme(
				EqualizedTheme.getDefaultHueOffset(),
				EqualizedTheme.getDefaultSaturationOffset(),
				EqualizedTheme.getDefaultBrightnessOffset()));
	}

	public long getPreviewDelayMilliseconds() {
		return previewDelayMilliseconds;
	}

	public void setPreviewDelayMilliseconds(long milliseconds) {
		this.previewDelayMilliseconds = milliseconds;
	}

	protected void initValues(EqualizedTheme initialValues) {
		initialLookAndFeel = UIManager.getLookAndFeel();
		initialTheme = MetalLookAndFeel.getCurrentTheme();
		if (initialValues != null) {
			hueSlider.setValue(initialValues.getHueOffset());
			saturationSlider.setValue(initialValues.getSaturationOffset());
			brightnessSlider.setValue(initialValues.getBrightnessOffset());
		} else {
			hueSlider.setValue(EqualizedTheme.getDefaultHueOffset());
			saturationSlider.setValue(EqualizedTheme
					.getDefaultSaturationOffset());
			brightnessSlider.setValue(EqualizedTheme
					.getDefaultBrightnessOffset());
		}
	}

	protected void cancelPressed() {
		themeAccepted = false;
		dispose();
	}

	protected void okPressed() {
		themeAccepted = true;
		dispose();
	}

	@Override
	public void dispose() {
		if (!themeAccepted) {
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
		super.dispose();
	}

	public boolean isThemeAccepted() {
		return themeAccepted;
	}

	protected void colorValueChanged() {
		updateLabels();
		makeSureToRunOnce(new Runnable() {
			@Override
			public void run() {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override
						public void run() {
							showBusy(ThemeEqualizerDialog.this, true);
							updateTheme();
							showBusy(ThemeEqualizerDialog.this, false);
						}
					});
				} catch (Exception e) {
					throw new AssertionError(e);
				}
			}
		}, previewDelayMilliseconds);
	}

	protected void updateLabels() {
		hueSlider.setBorder(BorderFactory.createTitledBorder(getString("Hue")
				+ ": " + hueSlider.getValue()));
		saturationSlider.setBorder(BorderFactory
				.createTitledBorder(getString("Saturation") + ": "
						+ saturationSlider.getValue()));
		brightnessSlider.setBorder(BorderFactory
				.createTitledBorder(getString("Brightness") + ": "
						+ brightnessSlider.getValue()));
	}

	protected void makeSureToRunOnce(final Runnable runnable,
			final long delayMilliseconds) {
		if (willRunOnce != null) {
			return;
		}
		willRunOnce = runnable;
		new Thread() {
			@Override
			public void run() {
				synchronized (willRunOnceMutex) {
					try {
						Thread.sleep(delayMilliseconds);
					} catch (InterruptedException e) {
						throw new AssertionError(e);
					}
					willRunOnce = null;
					runnable.run();
				}
			}
		}.start();
	}

	protected void updateTheme() {
		EqualizedTheme selectedTheme = getSelectedTheme();
		if (isAlreadyActive(selectedTheme)) {
			return;
		}
		try {
			selectedTheme.apply();
		} catch (Exception e) {
			throw new AssertionError(e);
		}
		SwingUtilities.updateComponentTreeUI(ThemeEqualizerDialog.this);
		for (Window window : Window.getWindows()) {
			SwingUtilities.updateComponentTreeUI(window);
		}
		messageLabel.setForeground(UIManager.getColor("Label.background"));
		messageLabel.setBackground(UIManager.getColor("Label.foreground"));
	}

	protected boolean isAlreadyActive(EqualizedTheme theme) {
		LookAndFeel currentLAF = UIManager.getLookAndFeel();
		if (!(currentLAF instanceof MetalLookAndFeel)) {
			return false;
		}
		MetalTheme currrentTheme = MetalLookAndFeel.getCurrentTheme();
		if (!(currrentTheme instanceof EqualizedTheme)) {
			return false;
		}
		EqualizedTheme currrentEqualizedTheme = (EqualizedTheme) currrentTheme;
		if (currrentEqualizedTheme.getHueOffset() != theme.getHueOffset()) {
			return false;
		}
		if (currrentEqualizedTheme.getSaturationOffset() != theme
				.getSaturationOffset()) {
			return false;
		}
		if (currrentEqualizedTheme.getBrightnessOffset() != theme
				.getBrightnessOffset()) {
			return false;
		}
		return true;
	}

	private static void showBusy(Window window, boolean b) {
		if (b) {
			window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		} else {
			window.setCursor(Cursor.getDefaultCursor());
		}
	}

	public EqualizedTheme getSelectedTheme() {
		return new EqualizedTheme(hueSlider.getValue(),
				saturationSlider.getValue(), brightnessSlider.getValue());
	}

	public void setSelectedTheme(EqualizedTheme theme) {
		hueSlider.setValue(theme.getHueOffset());
		saturationSlider.setValue(theme.getSaturationOffset());
		brightnessSlider.setValue(theme.getBrightnessOffset());
	}
}
