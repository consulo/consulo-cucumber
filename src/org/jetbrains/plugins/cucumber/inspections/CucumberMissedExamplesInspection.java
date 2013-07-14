package org.jetbrains.plugins.cucumber.inspections;

import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.cucumber.CucumberBundle;
import org.jetbrains.plugins.cucumber.psi.GherkinElementVisitor;
import org.jetbrains.plugins.cucumber.psi.GherkinScenarioOutline;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinExamplesBlockImpl;

/**
 * @author yole
 */
public class CucumberMissedExamplesInspection extends GherkinInspection {
  @Override
  public boolean isEnabledByDefault() {
    return true;
  }

  @Nls
  @NotNull
  public String getDisplayName() {
    return CucumberBundle.message("inspection.missed.example.name");
  }

  @NotNull
  public String getShortName() {
    return "CucumberMissedExamples";
  }

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly) {
    return new GherkinElementVisitor() {
      @Override
      public void visitScenarioOutline(GherkinScenarioOutline outline) {
        super.visitScenarioOutline(outline);

        final GherkinExamplesBlockImpl block = PsiTreeUtil.getChildOfType(outline, GherkinExamplesBlockImpl.class);
        if (block == null) {
          final PsiElement descriptionLine = outline.getShortDescriptionText();
          if (descriptionLine != null) {
            holder.registerProblem(outline,
                                   new TextRange(0, descriptionLine.getTextRange().getEndOffset() - outline.getTextOffset()),
                                   CucumberBundle.message("inspection.missed.example.msg.name"),
                                   new CucumberCreateExamplesSectionFix());
          }
        }
      }
    };
  }

  @NotNull
  @Override
  public HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.ERROR;
  }
}