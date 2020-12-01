package org.jetbrains.plugins.cucumber.psi;

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import consulo.ui.color.RGBColor;
import org.jetbrains.annotations.NonNls;

import java.awt.*;

/**
 * @author Roman.Chernyatchik
 */
public class GherkinHighlighter {
	public static final TextAttributesKey COMMENT = TextAttributesKey.createTextAttributesKey(
			"GHERKIN_COMMENT",
			DefaultLanguageHighlighterColors.DOC_COMMENT
	);

	public static final TextAttributesKey KEYWORD = TextAttributesKey.createTextAttributesKey(
			"GHERKIN_KEYWORD",
			DefaultLanguageHighlighterColors.KEYWORD
	);


	@NonNls
	static final String GHERKIN_TABLE_HEADER_CELL_ID = "GHERKIN_TABLE_HEADER_CELL";
	static final String GHERKIN_OUTLINE_PARAMETER_SUBSTITUTION_ID = "GHERKIN_OUTLINE_PARAMETER_SUBSTITUTION";
	public static final TextAttributes PARAMETER_DEFAULT_ATTRS = HighlighterColors.TEXT.getDefaultAttributes().clone();
	public static final TextAttributesKey TABLE_HEADER_CELL = TextAttributesKey.createTextAttributesKey(
			GHERKIN_TABLE_HEADER_CELL_ID,
			PARAMETER_DEFAULT_ATTRS
	);
	public static final TextAttributesKey OUTLINE_PARAMETER_SUBSTITUTION = TextAttributesKey.createTextAttributesKey(
			GHERKIN_OUTLINE_PARAMETER_SUBSTITUTION_ID,
			PARAMETER_DEFAULT_ATTRS
	);

	@NonNls
	static final String GHERKIN_TAG_ID = "GHERKIN_TAG";
	public static final TextAttributes TAG_ATTRS = HighlighterColors.TEXT.getDefaultAttributes().clone();
	public static final TextAttributesKey TAG = TextAttributesKey.createTextAttributesKey(
			GHERKIN_TAG_ID,
			TAG_ATTRS
	);

	@NonNls
	static final String GHERKIN_TABLE_CELL_ID = "GHERKIN_TABLE_CELL";
	@NonNls
	static final String GHERKIN_REGEXP_PARAMETER_ID = "GHERKIN_REGEXP_PARAMETER";
	public static final TextAttributes HEREDOC_CONTENT_DEFAULT_ATTRS = HighlighterColors.TEXT.getDefaultAttributes().clone();
	public static final TextAttributesKey TABLE_CELL = TextAttributesKey.createTextAttributesKey(
			GHERKIN_TABLE_CELL_ID,
			HEREDOC_CONTENT_DEFAULT_ATTRS
	);

	public static final TextAttributesKey REGEXP_PARAMETER = TextAttributesKey.createTextAttributesKey(
			GHERKIN_REGEXP_PARAMETER_ID,
			HEREDOC_CONTENT_DEFAULT_ATTRS
	);

	@NonNls
	static final String GHERKIN_PYSTRING_ID = "GHERKIN_PYSTRING";
	public static final TextAttributes GHERKIN_PYSTRING_ATTRS = HighlighterColors.TEXT.getDefaultAttributes().clone();
	public static final TextAttributesKey PYSTRING = TextAttributesKey.createTextAttributesKey(
			GHERKIN_PYSTRING_ID,
			GHERKIN_PYSTRING_ATTRS
	);

	public static final TextAttributesKey TEXT = TextAttributesKey.createTextAttributesKey("GHERKIN_TEXT",
			HighlighterColors.TEXT.getDefaultAttributes());

	public static final TextAttributesKey PIPE =
			TextAttributesKey.createTextAttributesKey("GHERKIN_TABLE_PIPE", KEYWORD.getDefaultAttributes());

	static {

		GHERKIN_PYSTRING_ATTRS.setFontType(Font.BOLD);
		GHERKIN_PYSTRING_ATTRS.setForegroundColor(new RGBColor(0, 128, 128));

		HEREDOC_CONTENT_DEFAULT_ATTRS.setFontType(Font.BOLD);
		HEREDOC_CONTENT_DEFAULT_ATTRS.setForegroundColor(new RGBColor(41, 123, 222));

		TAG_ATTRS.setFontType(Font.BOLD);
		TAG_ATTRS.setForegroundColor(new RGBColor(102, 14, 122));

		PARAMETER_DEFAULT_ATTRS.setFontType(Font.ITALIC);
		PARAMETER_DEFAULT_ATTRS.setForegroundColor(new RGBColor(195, 117, 34));
	}

	private GherkinHighlighter() {
	}
}
