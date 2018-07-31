package org.jetbrains.plugins.cucumber.psi.impl;

import javax.annotation.Nonnull;
import javax.swing.Icon;

import javax.annotation.Nullable;
import org.jetbrains.plugins.cucumber.psi.GherkinElementVisitor;
import org.jetbrains.plugins.cucumber.psi.GherkinPsiElement;
import org.jetbrains.plugins.cucumber.psi.GherkinTokenTypes;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.TokenSet;
import com.intellij.util.Function;
import consulo.awt.TargetAWT;
import consulo.ide.IconDescriptorUpdaters;

/**
 * @author yole
 */
public abstract class GherkinPsiElementBase extends ASTWrapperPsiElement implements GherkinPsiElement {
  private static final TokenSet TEXT_FILTER = TokenSet.create(GherkinTokenTypes.TEXT);

  public GherkinPsiElementBase(@Nonnull final ASTNode node) {
    super(node);
  }

  @Nonnull
  protected String getElementText() {
    final ASTNode node = getNode();
    final ASTNode[] children = node.getChildren(TEXT_FILTER);
    return StringUtil.join(children, new Function<ASTNode, String>() {
      public String fun(ASTNode astNode) {
        return astNode.getText();
      }
    }, " ").trim();
  }

  @Nullable
  public PsiElement getShortDescriptionText() {
    final ASTNode node = getNode();
    final ASTNode[] children = node.getChildren(TEXT_FILTER);
    return children.length > 0 ? children[0].getPsi() : null;
  }

  @Override
  public ItemPresentation getPresentation() {
    return new ItemPresentation() {
      public String getPresentableText() {
        return GherkinPsiElementBase.this.getPresentableText();
      }

      public String getLocationString() {
        return null;
      }

      public Icon getIcon(final boolean open) {
        return TargetAWT.to(IconDescriptorUpdaters.getIcon(GherkinPsiElementBase.this, Iconable.ICON_FLAG_VISIBILITY));
      }
    };
  }

  protected String getPresentableText() {
    return toString();
  }

  protected String buildPresentableText(final String prefix) {
    final StringBuilder result = new StringBuilder(prefix);
    final String name = getElementText();
    if (!StringUtil.isEmpty(name)) {
      result.append(": ").append(name);
    }
    return result.toString();
  }

  @Override
  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof GherkinElementVisitor) {
      acceptGherkin((GherkinElementVisitor) visitor);
    }
    else {
      super.accept(visitor);
    }
  }

  protected abstract void acceptGherkin(GherkinElementVisitor gherkinElementVisitor);
}
