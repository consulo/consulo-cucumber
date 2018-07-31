package org.jetbrains.plugins.cucumber.psi;

import javax.annotation.Nonnull;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.plugins.cucumber.CucumberBundle;

/**
 * User: Andrey.Vokin
 * Date: 4/4/11
 */
public class GherkinFindUsagesProvider implements FindUsagesProvider {
  @Override
  public WordsScanner getWordsScanner() {
    return new DefaultWordsScanner(new GherkinLexer(new PlainGherkinKeywordProvider()), TokenSet.EMPTY, TokenSet.EMPTY, TokenSet.EMPTY);
  }

  @Override
  public boolean canFindUsagesFor(@Nonnull PsiElement psiElement) {
    if (psiElement instanceof GherkinStep) {
      return true;
    }

    return false;
  }

  @Override
  public String getHelpId(@Nonnull PsiElement psiElement) {
    return "reference.dialogs.findUsages.other";
  }

  @Nonnull
  @Override
  public String getType(@Nonnull PsiElement element) {
    if (element instanceof GherkinStep) {
      return CucumberBundle.message("cucumber.step");
    } else if (element instanceof GherkinStepParameter) {
      return CucumberBundle.message("cucumber.step.parameter");
    }
    return element.toString();
  }

  @Nonnull
  @Override
  public String getDescriptiveName(@Nonnull PsiElement element) {
    return element instanceof PsiNamedElement ? ((PsiNamedElement)element).getName() : "";
  }

  @Nonnull
  @Override
  public String getNodeText(@Nonnull PsiElement element, boolean useFullName) {
    return getDescriptiveName(element);
  }
}
