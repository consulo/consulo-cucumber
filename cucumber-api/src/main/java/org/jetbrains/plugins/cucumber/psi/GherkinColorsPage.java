package org.jetbrains.plugins.cucumber.psi;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.plugins.cucumber.CucumberBundle;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Roman.Chernyatchik
 */
public class GherkinColorsPage implements ColorSettingsPage {

 private static final String DEMO_TEXT =
    "# language: en\n" +
    "Feature: Cucumber Colors Settings Page\n" +
    "  In order to customize Gherkin language (*.feature files) highlighting\n" +
    "  Our users can use this settings preview pane\n" +
    "\n" +
    "  @wip\n" +
    "  Scenario Outline: Different Gherkin language structures\n" +
    "    Given Some feature file with content\n" +
    "    \"\"\"\n" +
    "    Feature: Some feature\n" +
    "      Scenario: Some scenario\n" +
    "    \"\"\"\n" +
    "    And I want to add new cucumber step\n" +
    "    And Also a step with \"<regexp_param>regexp</regexp_param>\" parameter\n" +
    "    When I open <<outline_param>ruby_ide</outline_param>>\n" +
    "    Then Steps autocompletion feature will help me with all these tasks\n" +
    "\n" +
    "  Examples:\n" +
    "    | <th>ruby_ide</th> |\n" +
    "    | RubyMine |";

  private static final AttributesDescriptor[] ATTRS = new AttributesDescriptor[]{
    new AttributesDescriptor(CucumberBundle.message("color.settings.gherkin.text"), GherkinHighlighter.TEXT),
    new AttributesDescriptor(CucumberBundle.message("color.settings.gherkin.comment"), GherkinHighlighter.COMMENT),
    new AttributesDescriptor(CucumberBundle.message("color.settings.gherkin.keyword"), GherkinHighlighter.KEYWORD),
    new AttributesDescriptor(CucumberBundle.message("color.settings.gherkin.tag"), GherkinHighlighter.TAG),
    new AttributesDescriptor(CucumberBundle.message("color.settings.gherkin.pystring"), GherkinHighlighter.PYSTRING),
    new AttributesDescriptor(CucumberBundle.message("color.settings.gherkin.table.header.cell"), GherkinHighlighter.TABLE_HEADER_CELL),
    new AttributesDescriptor(CucumberBundle.message("color.settings.gherkin.table.cell"), GherkinHighlighter.TABLE_CELL),
    new AttributesDescriptor(CucumberBundle.message("color.settings.gherkin.table.pipe"), GherkinHighlighter.PIPE),
    new AttributesDescriptor(CucumberBundle.message("color.settings.gherkin.outline.param.substitution"), GherkinHighlighter.OUTLINE_PARAMETER_SUBSTITUTION),
    new AttributesDescriptor(CucumberBundle.message("color.settings.gherkin.regexp.param"), GherkinHighlighter.REGEXP_PARAMETER),
  };

  // Empty still
  private static final Map<String, TextAttributesKey> ADDITIONAL_HIGHLIGHT_DESCRIPTORS = new HashMap<String, TextAttributesKey>();
  static {
    ADDITIONAL_HIGHLIGHT_DESCRIPTORS.put("th", GherkinHighlighter.TABLE_HEADER_CELL);
    ADDITIONAL_HIGHLIGHT_DESCRIPTORS.put("outline_param", GherkinHighlighter.OUTLINE_PARAMETER_SUBSTITUTION);
    ADDITIONAL_HIGHLIGHT_DESCRIPTORS.put("regexp_param", GherkinHighlighter.REGEXP_PARAMETER);
  }

  private static final ColorDescriptor[] COLORS = new ColorDescriptor[0];

  @Nullable
  public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    return ADDITIONAL_HIGHLIGHT_DESCRIPTORS;
  }

  @Nonnull
  public String getDisplayName() {
    return CucumberBundle.message("color.settings.gherkin.name");
  }

  @Nonnull
  public AttributesDescriptor[] getAttributeDescriptors() {
    return ATTRS;
  }

  @Nonnull
  public ColorDescriptor[] getColorDescriptors() {
    return COLORS;
  }

  @Nonnull
  public SyntaxHighlighter getHighlighter() {
    return new GherkinSyntaxHighlighter(new PlainGherkinKeywordProvider());
  }

  @Nonnull
  public String getDemoText() {
    return DEMO_TEXT;
  }
}
