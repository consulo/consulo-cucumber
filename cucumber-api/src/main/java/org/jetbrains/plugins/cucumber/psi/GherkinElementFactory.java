package org.jetbrains.plugins.cucumber.psi;

import javax.annotation.Nonnull;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.plugins.cucumber.CucumberElementFactory;

/**
 * @author Roman.Chernyatchik
 * @date Sep 5, 2009
 */
public class GherkinElementFactory {
  private static final Logger LOG = Logger.getInstance(GherkinElementFactory.class.getName());

  private GherkinElementFactory() {
  }

  public static GherkinFeature createFeatureFromText(final Project project, @Nonnull final String text) {
    final PsiElement[] list = getTopLevelElements(project, text);
    for (PsiElement psiElement : list) {
      if (psiElement instanceof GherkinFeature) {
        return (GherkinFeature)psiElement;
      }
    }

    LOG.error("Failed to create Feature from text:\n" + text);
    return null;
  }

  public static GherkinStepsHolder createScenarioFromText(final Project project, final String language, @Nonnull final String text) {
    final GherkinKeywordProvider provider = CucumberLanguageService.getInstance(project).getKeywordProvider();
    final GherkinKeywordTable keywordsTable = provider.getKeywordsTable(language);
    String featureText = "# language: " + language + "\n" + keywordsTable.getFeatureSectionKeyword() + ": Dummy\n" + text;
    GherkinFeature feature = createFeatureFromText(project, featureText);
    return feature.getScenarios() [0];
  }

  public static PsiElement[] getTopLevelElements(final Project project, @Nonnull final String text) {
    return CucumberElementFactory.createTempPsiFile(project, text).getChildren();
  }
}
