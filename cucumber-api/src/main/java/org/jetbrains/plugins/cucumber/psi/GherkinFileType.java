package org.jetbrains.plugins.cucumber.psi;

import javax.annotation.Nonnull;
import com.intellij.openapi.fileTypes.LanguageFileType;
import consulo.cucumber.api.icon.CucumberApiIconGroup;
import consulo.localize.LocalizeValue;
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
  public String getId() {
    return "Cucumber";
  }

  @Nonnull
  public LocalizeValue getDescription() {
    return LocalizeValue.localizeTODO("Cucumber scenario files");
  }

  @Nonnull
  public String getDefaultExtension() {
    return "feature";
  }

  public Image getIcon() {
    return CucumberApiIconGroup.cucumber();
  }
}
