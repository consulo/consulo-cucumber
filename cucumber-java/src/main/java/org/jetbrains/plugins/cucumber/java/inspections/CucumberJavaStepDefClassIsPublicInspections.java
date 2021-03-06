package org.jetbrains.plugins.cucumber.java.inspections;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nls;
import org.jetbrains.plugins.cucumber.java.CucumberJavaBundle;
import org.jetbrains.plugins.cucumber.java.CucumberJavaUtil;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.ex.BaseLocalInspectionTool;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiModifier;

/**
 * User: Andrey.Vokin
 * Date: 1/9/13
 */
public class CucumberJavaStepDefClassIsPublicInspections extends BaseLocalInspectionTool {
  public boolean isEnabledByDefault() {
    return true;
  }

  @Nls
  @Nonnull
  public String getDisplayName() {
    return CucumberJavaBundle.message("cucumber.java.inspections.step.def.class.is.public.title");
  }

  @Nonnull
  public String getShortName() {
    return "CucumberJavaStepDefClassIsPublic";
  }

  @Nonnull
  public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, boolean isOnTheFly) {
    return new CucumberJavaStepDefClassIsPublicVisitor(holder);
  }

  static class CucumberJavaStepDefClassIsPublicVisitor extends JavaElementVisitor {
    final ProblemsHolder holder;

    CucumberJavaStepDefClassIsPublicVisitor(final ProblemsHolder holder) {
      this.holder = holder;
    }

    @Override
    public void visitClass(PsiClass aClass) {
      if (!CucumberJavaUtil.isStepDefinitionClass(aClass)) {
        return;
      }

      if (!aClass.hasModifierProperty(PsiModifier.PUBLIC)) {
        PsiElement elementToHighlight = aClass.getNameIdentifier();
        if (elementToHighlight == null) {
          elementToHighlight = aClass;
        }
        holder.registerProblem(elementToHighlight, CucumberJavaBundle.message("cucumber.java.inspection.step.def.class.is.public.message"));
      }
    }
  }
}
