package org.jetbrains.plugins.cucumber.steps.reference;

import javax.annotation.Nonnull;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinStepImpl;

/**
 * User: Andrey.Vokin
 * Date: 3/29/11
 */
public class CucumberReferenceContributor extends PsiReferenceContributor {
  @Override
  public void registerReferenceProviders(@Nonnull final PsiReferenceRegistrar registrar) {
    registrar.registerReferenceProvider(PlatformPatterns.psiElement(GherkinStepImpl.class),
                                        new CucumberStepReferenceProvider());

  }
}
