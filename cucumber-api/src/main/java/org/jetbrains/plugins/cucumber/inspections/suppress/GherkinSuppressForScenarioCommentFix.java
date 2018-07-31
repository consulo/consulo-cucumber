package org.jetbrains.plugins.cucumber.inspections.suppress;

import com.intellij.codeInsight.daemon.HighlightDisplayKey;
import com.intellij.codeInsight.daemon.impl.actions.AbstractBatchSuppressByNoInspectionCommentFix;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import javax.annotation.Nonnull;
import org.jetbrains.plugins.cucumber.CucumberBundle;
import org.jetbrains.plugins.cucumber.psi.GherkinStepsHolder;

/**
 * @author Roman.Chernyatchik
 * @date Aug 13, 2009
 */
public class GherkinSuppressForScenarioCommentFix extends AbstractBatchSuppressByNoInspectionCommentFix {
  GherkinSuppressForScenarioCommentFix(@Nonnull final String actionShortName) {
    super(HighlightDisplayKey.find(actionShortName).getID(), false);
  }

  @Nonnull
  @Override
  public String getText() {
    return CucumberBundle.message("cucumber.inspection.suppress.scenario");
  }

  @Override
  public PsiElement getContainer(PsiElement context) {
    // steps holder
    return PsiTreeUtil.getNonStrictParentOfType(context, GherkinStepsHolder.class);
  }
}
