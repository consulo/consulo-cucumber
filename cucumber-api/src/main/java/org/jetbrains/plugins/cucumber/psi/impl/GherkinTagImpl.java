package org.jetbrains.plugins.cucumber.psi.impl;

import javax.annotation.Nonnull;

import org.jetbrains.plugins.cucumber.psi.GherkinElementVisitor;
import org.jetbrains.plugins.cucumber.psi.GherkinTag;
import com.intellij.lang.ASTNode;

/**
 * @author yole
 */
public class GherkinTagImpl extends GherkinPsiElementBase implements GherkinTag {
  public GherkinTagImpl(@Nonnull final ASTNode node) {
    super(node);
  }

  @Override
  protected void acceptGherkin(GherkinElementVisitor gherkinElementVisitor) {
    gherkinElementVisitor.visitTag(this);
  }

  @Override
  public String getName() {
    return getText();
  }

  @Override
  public String toString() {
    return "GherkinTag:" + getText();
  }
}
