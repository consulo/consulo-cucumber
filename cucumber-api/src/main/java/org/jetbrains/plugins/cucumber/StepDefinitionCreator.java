package org.jetbrains.plugins.cucumber;

import javax.annotation.Nonnull;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import org.jetbrains.plugins.cucumber.psi.GherkinStep;

/**
 * User: Andrey.Vokin
 * Date: 8/1/12
 */
public interface StepDefinitionCreator {
  /**
   * Creates step definition file
   * @param dir where to create file
   * @param name of created file
   * @return PsiFile object of created file
   */
  @Nonnull
  PsiFile createStepDefinitionContainer(@Nonnull final PsiDirectory dir, @Nonnull final String name);

  /**
   * Creates step definition
   * @param step to implement
   * @param file where to create step definition
   * @return true if success, false otherwise
   */
  boolean createStepDefinition(@Nonnull final GherkinStep step, @Nonnull final PsiFile file);

  /**
   * Validates name of new step definition file
   * @param fileName name of file to check
   * @return true if name is valid, false otherwise
   */
  public boolean validateNewStepDefinitionFileName(@Nonnull final Project project, @Nonnull final String fileName);

  @Nonnull
  PsiDirectory getDefaultStepDefinitionFolder(@Nonnull final GherkinStep step);

  @Nonnull
  public String getStepDefinitionFilePath(@Nonnull final PsiFile file);

  /**
   * Provides default name of step definition file
   * @return String representing default name of step definition file
   */
  @Nonnull
  String getDefaultStepFileName();
}
