package org.jetbrains.plugins.cucumber.psi.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import consulo.cucumber.api.icon.CucumberApiIconGroup;
import org.jetbrains.plugins.cucumber.psi.GherkinFeature;
import org.jetbrains.plugins.cucumber.psi.GherkinPsiElement;
import org.jetbrains.plugins.cucumber.psi.GherkinPystring;
import org.jetbrains.plugins.cucumber.psi.GherkinStep;
import org.jetbrains.plugins.cucumber.psi.GherkinStepsHolder;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinFeatureHeaderImpl;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinTableImpl;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinTagImpl;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import consulo.ui.image.Image;

/**
 * @author yole
 */
public class GherkinStructureViewElement extends PsiTreeElementBase<PsiElement> {
  protected GherkinStructureViewElement(PsiElement psiElement) {
    super(psiElement);
  }

  @Nonnull
  public Collection<StructureViewTreeElement> getChildrenBase() {
    List<StructureViewTreeElement> result = new ArrayList<StructureViewTreeElement>();
    for (PsiElement element : getElement().getChildren()) {
      if (element instanceof GherkinPsiElement &&
          !(element instanceof GherkinFeatureHeaderImpl) &&
          !(element instanceof GherkinTableImpl) &&
          !(element instanceof GherkinTagImpl) &&
          !(element instanceof GherkinPystring)) {
        result.add(new GherkinStructureViewElement(element));
      }
    }
    return result;
  }

  @Override
  public Image getIcon() {
    final PsiElement element = getElement();
    if (element instanceof GherkinFeature
        || element instanceof GherkinStepsHolder) {
      return CucumberApiIconGroup.steps_group_closed();
    }
    if (element instanceof GherkinStep) {
      return CucumberApiIconGroup.cucumber();
    }
    return null;
  }


  public String getPresentableText() {
    return ((NavigationItem) getElement()).getPresentation().getPresentableText();
  }
}
