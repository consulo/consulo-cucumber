package org.jetbrains.plugins.cucumber.spellchecker;

import javax.annotation.Nonnull;

import org.jetbrains.plugins.cucumber.inspections.suppress.GherkinSuppressionUtil;
import org.jetbrains.plugins.cucumber.psi.GherkinElementType;
import com.intellij.codeInspection.SuppressQuickFix;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.spellchecker.quickfixes.AcceptWordAsCorrect;
import com.intellij.spellchecker.quickfixes.ChangeTo;
import com.intellij.spellchecker.quickfixes.SpellCheckerQuickFix;
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy;
import com.intellij.spellchecker.tokenizer.SuppressibleSpellcheckingStrategy;
import com.intellij.spellchecker.tokenizer.Tokenizer;

/**
 * @author oleg
 */
public class GherkinSpellcheckerStrategy extends SuppressibleSpellcheckingStrategy {
  @Nonnull
  @Override
  public Tokenizer getTokenizer(final PsiElement element) {
    if (element instanceof LeafElement) {
      final ASTNode node = element.getNode();
      if (node != null && node.getElementType() instanceof GherkinElementType){
        return SpellcheckingStrategy.TEXT_TOKENIZER;
      }
    }
    return super.getTokenizer(element);
  }

  @Override
  public boolean isSuppressedFor(@Nonnull PsiElement element, @Nonnull String name) {
    return GherkinSuppressionUtil.isSuppressedFor(element, name);
  }

  @Override
  public SuppressQuickFix[] getSuppressActions(@Nonnull final PsiElement element, @Nonnull final String name) {
    return GherkinSuppressionUtil.getDefaultSuppressActions(element, name);
  }

  @Override
  public SpellCheckerQuickFix[] getRegularFixes(PsiElement element,
                                                int offset,
                                                @Nonnull TextRange textRange,
                                                boolean useRename,
                                                String wordWithTypo) {
    return new SpellCheckerQuickFix[]{new ChangeTo(wordWithTypo), new AcceptWordAsCorrect(wordWithTypo)};
  }
}