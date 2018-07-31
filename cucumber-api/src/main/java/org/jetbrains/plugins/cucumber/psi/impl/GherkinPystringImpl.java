package org.jetbrains.plugins.cucumber.psi.impl;

import com.intellij.lang.ASTNode;
import javax.annotation.Nonnull;
import org.jetbrains.plugins.cucumber.psi.GherkinElementVisitor;
import org.jetbrains.plugins.cucumber.psi.GherkinPystring;

/**
 * User: Andrey.Vokin
 * Date: 4/19/11
 */
public class GherkinPystringImpl extends GherkinPsiElementBase implements GherkinPystring {
  public GherkinPystringImpl(@Nonnull final ASTNode node) {
    super(node);
  }

  @Override
  protected void acceptGherkin(GherkinElementVisitor gherkinElementVisitor) {
    gherkinElementVisitor.visitPystring(this);
  }

  @Override
  public String toString() {
    return "GherkinPystring";
  }
}
