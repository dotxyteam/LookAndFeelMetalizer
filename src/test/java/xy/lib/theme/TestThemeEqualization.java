package xy.lib.theme;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class TestThemeEqualization {

	@Test
	public void test() {
		final List<Throwable> errors = new ArrayList<Throwable>();
		final ThemeEqualizerDialog dialog = new ThemeEqualizerDialog(null, new EqualizedTheme());
		try {
			dialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowOpened(WindowEvent e) {
					try {
						dialog.dispose();
					} catch (Throwable t) {
						errors.add(t);
					}
				}
			});
		} catch (Throwable t) {
			errors.add(t);
		}
		if (errors.size() > 0) {
			Assert.fail(errors.toString());
		}
	}

}
