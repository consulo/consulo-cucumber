package org.jetbrains.plugins.cucumber.inspections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.codeInspection.*;
import com.intellij.psi.PsiElement;
import org.jetbrains.plugins.cucumber.CucumberBundle;
import org.jetbrains.plugins.cucumber.inspections.suppress.GherkinSuppressionUtil;

/**
 * @author Roman.Chernyatchik
 */
public abstract class GherkinInspection extends LocalInspectionTool implements BatchSuppressableTool {
  @Nonnull
  public String getGroupDisplayName() {
    return CucumberBundle.message("cucumber.inspection.group.name");
  }

  @Nonnull
  @Override
  public SuppressQuickFix[] getBatchSuppressActions(@Nullable PsiElement element) {
    return GherkinSuppressionUtil.getDefaultSuppressActions(element, getShortName());
  }

  public boolean isSuppressedFor(@Nonnull final PsiElement element) {
    return GherkinSuppressionUtil.isSuppressedFor(element, getID());
  }
}