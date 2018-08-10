package xy.lib.theme;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class EqualizedGlassTheme extends EqualizedNimbusTheme {

	public EqualizedGlassTheme() {
		super();
		getEqualization().setBrightness(1.0f);
		getEqualization().setSaturation(0.0f);
	}

	@Override
	protected NimbusLookAndFeel getEqualizedNimbusLookAndFeel() {
		return new EqualizedGlassLookAndFeel(getEqualization());
	}

	@Override
	public Class<? extends LookAndFeel> getBaseLookAndFeelClass() {
		return GlassLookAndFeel.class;
	}

	public static class GlassLookAndFeel extends EqualizedGlassLookAndFeel {
		private static final long serialVersionUID = 1L;

		public GlassLookAndFeel() {
			super(new EqualizedGlassTheme().getEqualization());
		}

		@Override
		public String getName() {
			return "Glass";
		}
	}

	public static class EqualizedGlassLookAndFeel extends EqualizedNimbusLookAndFeel {

		private static final long serialVersionUID = 1L;

		public EqualizedGlassLookAndFeel(ThemeEqualization equalization) {
			super(equalization);
			enhanceColors();
		}

		protected void enhanceColors() {
			final Map<String, Accessor<Color>> colorByState = new HashMap<String, Accessor<Color>>();
			colorByState.put("Enabled", new Accessor<Color>() {
				@Override
				public Color get() {
					return getEnabledTextBackgroundColor();
				}
			});
			colorByState.put("Focused", new Accessor<Color>() {
				@Override
				public Color get() {
					return getFocusedTextBackgroundColor();
				}
			});
			colorByState.put("Disabled", new Accessor<Color>() {
				@Override
				public Color get() {
					return getDisabledTextBackgroundColor();
				}
			});
			colorByState.put("MouseOver", new Accessor<Color>() {
				@Override
				public Color get() {
					return getMouseOverTextBackgroundColor();
				}
			});
			colorByState.put("Focused+MouseOver", new Accessor<Color>() {
				@Override
				public Color get() {
					return colorByState.get("Focused").get();
				}
			});
			for (final String state : colorByState.keySet()) {
				for (String key : Arrays.asList("ComboBox[" + state + "].backgroundPainter",
						"TextField[" + state + "].backgroundPainter", "TextArea[" + state + "].backgroundPainter",
						"TextArea[" + state + "+NotInScrollPane].backgroundPainter",
						"PasswordField[" + state + "].backgroundPainter",
						"Slider:SliderTrack[" + state + "].backgroundPainter",
						"Spinner:Panel:\"Spinner.formattedTextField\"[" + state + "].backgroundPainter")) {
					getDefaults().put(key, new javax.swing.plaf.nimbus.AbstractRegionPainter() {
						@Override
						protected javax.swing.plaf.nimbus.AbstractRegionPainter.PaintContext getPaintContext() {
							return new javax.swing.plaf.nimbus.AbstractRegionPainter.PaintContext(null, null, false);
						}

						@Override
						protected void doPaint(Graphics2D g, JComponent c, int width, int height,
								Object[] extendedCacheKeys) {
							g.setColor(colorByState.get(state).get());
							g.fill(new Rectangle(0, 0, width, height));
						}
					});
				}
			}
		}

		public Color getMouseOverTextBackgroundColor() {
			return getFocusedTextBackgroundColor();
		}

		public ColorUIResource getDisabledTextBackgroundColor() {
			Color baseColor = UIManager.getColor("control");
			Color result = new Color(clampRGBComponent(baseColor.getRed() - 10),
					clampRGBComponent(baseColor.getGreen() - 10), clampRGBComponent(baseColor.getBlue() - 10));
			return new ColorUIResource(result);
		}

		public ColorUIResource getFocusedTextBackgroundColor() {
			Color baseColor = getEnabledTextBackgroundColor();
			Color result = new Color(clampRGBComponent(baseColor.getRed() + 5),
					clampRGBComponent(baseColor.getGreen() + 5), clampRGBComponent(baseColor.getBlue() + 5));
			return new ColorUIResource(result);

		}

		public ColorUIResource getEnabledTextBackgroundColor() {
			Color baseColor = getDefaults().getColor("controlShadow");
			Color result = new Color(clampRGBComponent(baseColor.getRed() - 25),
					clampRGBComponent(baseColor.getGreen() - 10), clampRGBComponent(baseColor.getBlue() + 0));
			return new ColorUIResource(result);
		}

		protected int clampRGBComponent(int value) {
			if (value < 0) {
				value = 0;
			}
			if (value > 255) {
				value = 255;
			}
			return value;
		}

		protected abstract static class Accessor<T> {
			public abstract T get();

			public void set(T t) {
				throw new UnsupportedOperationException();
			}

		}
	}

}
