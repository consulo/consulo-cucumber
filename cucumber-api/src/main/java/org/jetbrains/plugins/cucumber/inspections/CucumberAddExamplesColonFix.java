package org.jetbrains.plugins.cucumber.inspections;

import javax.annotation.Nonnull;
import org.jetbrains.plugins.cucumber.psi.GherkinElementFactory;
import com.intellij.codeInsight.FileModificationService;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * @author Dennis.Ushakov
 */
public class CucumberAddExamplesColonFix implements LocalQuickFix {

  @Nonnull
  public String getName() {
    return "Add missing ':' after examples keyword";
  }

  @Nonnull
  public String getFamilyName() {
    return getName();
  }


  public void applyFix(@Nonnull Project project, @Nonnull ProblemDescriptor descriptor) {
    final PsiElement examples = descriptor.getPsiElement();

    final PsiFile featureFile = examples.getContainingFile();

    if (!FileModificationService.getInstance().prepareFileForWrite(featureFile)) {
      return;
    }

    final PsiElement[] elements = GherkinElementFactory.getTopLevelElements(project, ":");
    examples.getParent().addAfter(elements[0], examples);
  }
}
