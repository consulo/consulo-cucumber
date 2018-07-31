package org.jetbrains.plugins.cucumber.psi;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinPsiElementBase;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinSimpleReference;

/**
 * @author Roman.Chernyatchik
 */
public class GherkinTableCellImpl extends GherkinPsiElementBase implements GherkinTableCell  {
  public GherkinTableCellImpl(@Nonnull final ASTNode node) {
    super(node);
  }

  @Override
  protected void acceptGherkin(final GherkinElementVisitor gherkinElementVisitor) {
    gherkinElementVisitor.visitGherkinTableCell(this);
  }


  @Override
  public String toString() {
    return "GherkinTableCell";
  }

  @Override
  public PsiReference getReference() {
    return new GherkinSimpleReference(this);
  }

  @Override
  public String getName() {
    return getText();
  }

  @Override
  public PsiElement setName(@NonNls @Nonnull String name) throws IncorrectOperationException {
    final LeafPsiElement content = PsiTreeUtil.getChildOfType(this, LeafPsiElement.class);
    PsiElement[] elements = GherkinElementFactory.getTopLevelElements(getProject(), name);
    getNode().replaceChild(content, elements[0].getNode());
    return this;
  }

  @Override
  public PsiElement getNameIdentifier() {
    final LeafPsiElement content = PsiTreeUtil.getChildOfType(this, LeafPsiElement.class);
    return content;
  }

  @Nonnull
  @Override
  public SearchScope getUseScope() {
    return new LocalSearchScope(getContainingFile());
  }
}
