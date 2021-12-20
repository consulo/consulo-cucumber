package org.jetbrains.plugins.cucumber.psi.structure;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import consulo.cucumber.api.icon.CucumberApiIconGroup;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.ui.image.Image;
import org.jetbrains.plugins.cucumber.psi.*;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinFeatureHeaderImpl;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinTableImpl;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinTagImpl;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
      return PlatformIconGroup.nodesFolder();
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
