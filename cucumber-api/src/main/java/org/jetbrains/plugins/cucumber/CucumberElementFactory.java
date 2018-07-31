package org.jetbrains.plugins.cucumber;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.LocalTimeCounter;
import javax.annotation.Nonnull;
import org.jetbrains.plugins.cucumber.psi.GherkinFileType;

/**
 * User: Andrey.Vokin
 * Date: 2/20/12
 */
public class CucumberElementFactory {

  public static PsiElement createTempPsiFile(@Nonnull final Project project, @Nonnull final String text) {

    return PsiFileFactory.getInstance(project).createFileFromText("temp." + GherkinFileType.INSTANCE.getDefaultExtension(),
                                                                  GherkinFileType.INSTANCE,
                                                                  text, LocalTimeCounter.currentTime(), true);
  }
}
