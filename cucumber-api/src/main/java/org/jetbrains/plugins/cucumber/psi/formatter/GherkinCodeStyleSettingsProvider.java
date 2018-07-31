package org.jetbrains.plugins.cucumber.psi.formatter;

import javax.annotation.Nonnull;

import com.intellij.application.options.CodeStyleAbstractConfigurable;
import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.openapi.options.Configurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider;

/**
 * @author Rustam Vishnyakov
 */
public class GherkinCodeStyleSettingsProvider extends CodeStyleSettingsProvider {
  @Nonnull
  @Override
  public Configurable createSettingsPage(CodeStyleSettings settings, CodeStyleSettings originalSettings) {
    return new CodeStyleAbstractConfigurable(settings, originalSettings, "Gherkin") {
      @Override
      protected CodeStyleAbstractPanel createPanel(CodeStyleSettings settings) {
        return new GherkinCodeStylePanel(getCurrentSettings(), settings);
      }

      @Override
      public String getHelpTopic() {
        return "reference.settingsdialog.codestyle.gherkin";
      }
    };
  }

  @Override
  public String getConfigurableDisplayName() {
    return "Gherkin";
  }
}
