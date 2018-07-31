package org.jetbrains.plugins.cucumber.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.tree.TokenSet;
import javax.annotation.Nonnull;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinExamplesBlockImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yole
 */
public class GherkinFoldingBuilder implements FoldingBuilder, DumbAware {
  private static final TokenSet BLOCKS_TO_FOLD = TokenSet.create(GherkinElementTypes.SCENARIO,
                                                                 GherkinElementTypes.SCENARIO_OUTLINE,
                                                                 GherkinElementTypes.EXAMPLES_BLOCK,
                                                                 GherkinTokenTypes.PYSTRING);


  @Nonnull
  public FoldingDescriptor[] buildFoldRegions(@Nonnull ASTNode node, @Nonnull Document document) {
    List<FoldingDescriptor> descriptors = new ArrayList<FoldingDescriptor>();
    appendDescriptors(node, descriptors);
    return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
  }

  private void appendDescriptors(ASTNode node, List<FoldingDescriptor> descriptors) {
    if (BLOCKS_TO_FOLD.contains(node.getElementType()) && node.getTextRange().getLength() >= 2) {
      descriptors.add(new FoldingDescriptor(node, node.getTextRange()));
    }
    ASTNode child = node.getFirstChildNode();
    while (child != null) {
      appendDescriptors(child, descriptors);
      child = child.getTreeNext();
    }
  }

  public String getPlaceholderText(@Nonnull ASTNode node) {
    if (node.getPsi() instanceof GherkinStepsHolder ||
        node.getPsi() instanceof GherkinExamplesBlockImpl) {
      return ((NavigationItem) node.getPsi()).getPresentation().getPresentableText();
    }
    return "...";
  }

  public boolean isCollapsedByDefault(@Nonnull ASTNode node) {
    return false;
  }
}
