package org.jetbrains.plugins.cucumber.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.jetbrains.plugins.cucumber.psi.GherkinFeature;
import org.jetbrains.plugins.cucumber.psi.GherkinFile;
import org.jetbrains.plugins.cucumber.psi.GherkinStep;
import org.jetbrains.plugins.cucumber.psi.GherkinStepsHolder;
import org.jetbrains.plugins.cucumber.steps.AbstractStepDefinition;
import org.jetbrains.plugins.cucumber.steps.CucumberStepsIndex;
import com.intellij.navigation.GotoRelatedItem;
import com.intellij.navigation.GotoRelatedProvider;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * User: Andrey.Vokin
 * Date: 3/25/13
 */
public class CucumberGoToRelatedProvider extends GotoRelatedProvider {
  @Nonnull
  public List<? extends GotoRelatedItem> getItems(@Nonnull DataContext context) {
    final PsiFile file = context.getData(LangDataKeys.PSI_FILE);
    if (file != null) {
      return getItems(file);
    }
    return Collections.emptyList();
  }

  @Nonnull
  @Override
  public List<? extends GotoRelatedItem> getItems(@Nonnull PsiElement psiElement) {
    final PsiFile file = psiElement.getContainingFile();
    if (file instanceof GherkinFile) {
      final List<GherkinStep> steps = new ArrayList<GherkinStep>();
      final GherkinFile gherkinFile = (GherkinFile)file;
      final GherkinFeature[] features = gherkinFile.getFeatures();
      for (GherkinFeature feature : features) {
        final GherkinStepsHolder[] stepHolders = feature.getScenarios();
        for (GherkinStepsHolder stepHolder : stepHolders) {
          Collections.addAll(steps, stepHolder.getSteps());
        }
      }
      final CucumberStepsIndex index = CucumberStepsIndex.getInstance(file.getProject());
      final List<PsiFile> resultFiles = new ArrayList<PsiFile>();
      final List<GotoRelatedItem> result = new ArrayList<GotoRelatedItem>();
      for (GherkinStep step : steps) {
        AbstractStepDefinition stepDef = index.findStepDefinition(gherkinFile, step.getSubstitutedName());
        final PsiElement stepDefMethod = stepDef != null ? stepDef.getElement() : null;

        if (stepDefMethod != null) {
          final PsiFile stepDefFile = stepDefMethod.getContainingFile();
          if (!resultFiles.contains(stepDefFile)) {
            resultFiles.add(stepDefFile);
            result.add(new GotoRelatedItem(stepDefFile, "Step definition file"));
          }
        }
      }
      return result;
    }
    return Collections.emptyList();
  }
}
