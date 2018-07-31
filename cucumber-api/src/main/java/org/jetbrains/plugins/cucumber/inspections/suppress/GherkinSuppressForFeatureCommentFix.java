package org.jetbrains.plugins.cucumber.inspections.suppress;

import com.intellij.codeInsight.daemon.HighlightDisplayKey;
import com.intellij.codeInsight.daemon.impl.actions.AbstractBatchSuppressByNoInspectionCommentFix;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import javax.annotation.Nonnull;
import org.jetbrains.plugins.cucumber.CucumberBundle;
import org.jetbrains.plugins.cucumber.psi.GherkinFeature;

/**
 * @author Roman.Chernyatchik
 * @date Aug 13, 2009
 */
public class GherkinSuppressForFeatureCommentFix extends AbstractBatchSuppressByNoInspectionCommentFix {
  GherkinSuppressForFeatureCommentFix(@Nonnull final String actionShortName) {
    super(HighlightDisplayKey.find(actionShortName).getID(), false);
  }

  @Nonnull
  @Override
  public String getText() {
    return CucumberBundle.message("cucumber.inspection.suppress.feature");
  }

  @Override
  public PsiElement getContainer(PsiElement context) {
    // step
    return PsiTreeUtil.getNonStrictParentOfType(context, GherkinFeature.class);
  }
}
