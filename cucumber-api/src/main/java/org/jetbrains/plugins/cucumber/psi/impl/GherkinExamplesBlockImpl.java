package org.jetbrains.plugins.cucumber.psi.impl;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;

import javax.annotation.Nullable;
import org.jetbrains.plugins.cucumber.psi.GherkinElementTypes;
import org.jetbrains.plugins.cucumber.psi.GherkinElementVisitor;
import org.jetbrains.plugins.cucumber.psi.GherkinExamplesBlock;
import org.jetbrains.plugins.cucumber.psi.GherkinTable;

/**
 * @author yole
 */
public class GherkinExamplesBlockImpl extends GherkinPsiElementBase implements GherkinExamplesBlock {
  private static final TokenSet TABLE_FILTER = TokenSet.create(GherkinElementTypes.TABLE);

  public GherkinExamplesBlockImpl(@Nonnull final ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "GherkinExamplesBlock:" + getElementText();
  }

  @Override
  protected String getPresentableText() {
    return buildPresentableText("Examples");
  }

  protected void acceptGherkin(GherkinElementVisitor gherkinElementVisitor) {
    gherkinElementVisitor.visitExamplesBlock(this);
  }

  @Nullable
  public GherkinTable getTable() {
    final ASTNode node = getNode();

    final ASTNode tableNode = node.findChildByType(TABLE_FILTER);
    return tableNode == null ? null : (GherkinTable)tableNode.getPsi();
  }
}
