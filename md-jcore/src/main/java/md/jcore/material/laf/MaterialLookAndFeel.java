package md.jcore.material.laf;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

import javax.swing.UIDefaults;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.plaf.basic.BasicPanelUI;
import javax.swing.plaf.basic.BasicRootPaneUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalProgressBarUI;

import md.jcore.material.MaterialConstants;

public class MaterialLookAndFeel extends MetalLookAndFeel {
	private static final long serialVersionUID = 1L;

	public static final Color darkGray1 = new Color(12, 12, 12), darkGray2 = new Color(30, 30, 30),
			darkGray3 = new Color(48, 48, 48), darkGray4 = new Color(64, 64, 64), darkGray5 = new Color(80, 80, 80),
			darkGray6 = new Color(96, 96, 96);

	public static final Color lightGray1 = new Color(240, 240, 240), lightGray2 = Color.WHITE,
			lightGray3 = new Color(224, 224, 224), lightGray4 = new Color(208, 208, 208),
			lightGray5 = new Color(192, 192, 192), lightGray6 = new Color(176, 176, 176);

	private UIDefaults defaults;
	private int theme;
	private Color primaryColor;
	private Color secondaryColor;

	public int getTheme() {
		return theme;
	}

	public Color getPrimaryColor() {
		return primaryColor;
	}

	public Color getSecondaryColor() {
		return secondaryColor;
	}

	@Override
	public UIDefaults getDefaults() {
		return defaults;
	}

	public MaterialLookAndFeel(int theme, Color primaryColor, Color secondaryColor) {
		reset(theme, primaryColor, secondaryColor);
	}

	public void reset(int theme, Color primaryColor, Color secondaryColor) {
		if (theme != MaterialConstants.THEME_LIGHT && theme != MaterialConstants.THEME_DARK) {
			throw new IllegalArgumentException("Illegal value for theme");
		} else if (primaryColor == null || secondaryColor == null) {
			throw new NullPointerException("Both primary and secondary color must be set");
		} else if (primaryColor.getAlpha() < 255 || secondaryColor.getAlpha() < 255) {
			throw new IllegalArgumentException("Both primary and secondary color must be opaque");
		}

		this.theme = theme;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;

		this.refresh();
	}

	private void refresh() {
		defaults = super.getDefaults();

		Color lighterPrimary = primaryColor.brighter();

		defaults.put("AppBar.foreground", lightGray1);
		defaults.put("Button.font", new Font("Roboto Medium", Font.PLAIN, 14));
		defaults.put("Label.font", new Font("Roboto", Font.PLAIN, 14));

		defaults.put("Locale", Locale.getDefault());

		// TODO ProgressBarUI
		// TODO TextFieldUI
		defaults.put("ButtonUI", MaterialButtonUI.class.getName());
		defaults.put("LabelUI", BasicLabelUI.class.getName());
		defaults.put("PanelUI", BasicPanelUI.class.getName());
		defaults.put("ProgressBarUI", MetalProgressBarUI.class.getName());
		defaults.put("RadioButtonUI", MaterialRadioButtonUI.class.getName());
		defaults.put("RootPaneUI", BasicRootPaneUI.class.getName());
		defaults.put("SeparatorUI", MaterialSeparatorUI.class.getName());
		defaults.put("ToolBarUI", MaterialToolBarUI.class.getName());

		switch (theme) {
		case MaterialConstants.THEME_LIGHT:
			defaults.put("errorColor", new Color(0xb71c1c));
			defaults.put("AppBar.background", primaryColor);
			defaults.put("Button.disabledForeground", lightGray6);
			defaults.put("Button.foreground", primaryColor);
			defaults.put("Button.primaryBackground", primaryColor);
			defaults.put("Button.primaryForeground", lightGray2);
			defaults.put("Card.background", lightGray2);
			defaults.put("Icon.color", primaryColor);
			defaults.put("Icon.default", darkGray1);
			defaults.put("Icon.disabled", lightGray6);
			defaults.put("Label.foreground", darkGray1);
			defaults.put("OptionPane.background", lightGray2);
			defaults.put("Panel.background", lightGray1);
			defaults.put("RadioButton.selectedMark", primaryColor);
			defaults.put("RadioButton.unselectedMark", darkGray5);
			defaults.put("ScrollBar.background", lightGray1);
			defaults.put("Separator.foreground", lightGray5);
			break;
		case MaterialConstants.THEME_DARK:
			defaults.put("errorColor", new Color(0xef5350));
			defaults.put("AppBar.background", darkGray3);
			defaults.put("Button.background", darkGray1);
			defaults.put("Button.disabledForeground", darkGray6);
			defaults.put("Button.foreground", lighterPrimary);
			defaults.put("Button.primaryBackground", lighterPrimary);
			defaults.put("Button.primaryForeground", darkGray1);
			defaults.put("Card.background", darkGray2);
			defaults.put("Icon.color", lighterPrimary);
			defaults.put("Icon.default", lightGray2);
			defaults.put("Icon.disabled", darkGray6);
			defaults.put("Label.foreground", lightGray1);
			defaults.put("OptionPane.background", darkGray2);
			defaults.put("Panel.background", darkGray1);
			defaults.put("RadioButton.selectedMark", lighterPrimary);
			defaults.put("RadioButton.unselectedMark", lightGray4);
			defaults.put("ScrollBar.background", darkGray1);
			defaults.put("Separator.foreground", darkGray5);
			break;
		}

		defaults.remove("Button.border");
	}

	@Override
	public String getName() {
		return "Material Look and Feel (MD jCore 1.0)";
	}

	@Override
	public String getID() {
		return "MD-jCore-1.0_Material-L&F";
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public boolean isNativeLookAndFeel() {
		return false;
	}

	@Override
	public boolean isSupportedLookAndFeel() {
		return true;
	}
}
