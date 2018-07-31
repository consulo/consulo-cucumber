package org.jetbrains.plugins.cucumber.psi.impl;

import com.intellij.lang.ASTNode;
import javax.annotation.Nonnull;
import org.jetbrains.plugins.cucumber.psi.GherkinElementVisitor;

/**
 * @author yole
 */
public class GherkinTableHeaderRowImpl extends GherkinTableRowImpl {
  public GherkinTableHeaderRowImpl(@Nonnull final ASTNode node) {
    super(node);
  }

  @Override
  protected void acceptGherkin(GherkinElementVisitor gherkinElementVisitor) {
    gherkinElementVisitor.visitTableHeaderRow(this);
  }

  @Override
  public String toString() {
    return "GherkinTableHeaderRow";
  }
}