package org.jetbrains.plugins.cucumber.psi;

import javax.annotation.Nonnull;
import com.intellij.openapi.fileTypes.LanguageFileType;
import consulo.ui.image.Image;

/**
 * @author yole
 */
public class GherkinFileType extends LanguageFileType {
  public static final GherkinFileType INSTANCE = new GherkinFileType();

  protected GherkinFileType() {
    super(GherkinLanguage.INSTANCE);
  }

  @Nonnull
  public String getName() {
    return "Cucumber";
  }

  @Nonnull
  public String getDescription() {
    return "Cucumber scenario files";
  }

  @Nonnull
  public String getDefaultExtension() {
    return "feature";
  }

  public Image getIcon() {
    return icons.CucumberIcons.Cucumber;
  }
}
