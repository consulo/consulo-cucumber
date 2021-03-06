package org.jetbrains.plugins.cucumber.psi.impl;

import javax.annotation.Nonnull;

import org.jetbrains.plugins.cucumber.psi.GherkinElementVisitor;
import com.intellij.lang.ASTNode;

/**
 * @author yole
 */
public class GherkinFeatureHeaderImpl extends GherkinPsiElementBase {
  public GherkinFeatureHeaderImpl(@Nonnull final ASTNode node) {
    super(node);
  }

  @Override
  protected void acceptGherkin(GherkinElementVisitor gherkinElementVisitor) {
    gherkinElementVisitor.visitFeatureHeader(this);
  }

  @Override
  public String toString() {
    return "GherkinFeatureHeader";
  }
}
