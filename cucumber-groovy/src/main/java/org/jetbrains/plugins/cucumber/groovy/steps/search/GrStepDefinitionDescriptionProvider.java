package org.jetbrains.plugins.cucumber.groovy.steps.search;

import javax.annotation.Nonnull;

import com.intellij.psi.ElementDescriptionLocation;
import com.intellij.psi.ElementDescriptionProvider;
import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageViewNodeTextLocation;
import com.intellij.usageView.UsageViewTypeLocation;

import javax.annotation.Nullable;
import org.jetbrains.plugins.cucumber.CucumberBundle;
import org.jetbrains.plugins.cucumber.groovy.GrCucumberUtil;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression;

/**
 * @author Max Medvedev
 */
public class GrStepDefinitionDescriptionProvider implements ElementDescriptionProvider {
  @Nullable
  @Override
  public String getElementDescription(@Nonnull PsiElement element, @Nonnull ElementDescriptionLocation location) {
    if (location instanceof UsageViewNodeTextLocation || location instanceof UsageViewTypeLocation) {
      if (GrCucumberUtil.isStepDefinition(element) || element instanceof GrReferenceExpression && GrCucumberUtil.isStepDefinition(element.getParent())) {
        return CucumberBundle.message("step.definition") ;
      }
    }
    return null;
  }
}
