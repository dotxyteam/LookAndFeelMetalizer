package xy.lib.theme;

import javax.swing.LookAndFeel;

public interface IEqualizedTheme {

	public abstract void activate();

	ThemeEqualization getEqualization();

	public abstract Class<? extends LookAndFeel> getBaseLookAndFeelClass();
	
}
