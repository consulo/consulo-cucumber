package org.jetbrains.plugins.cucumber.psi.annotator;

import javax.annotation.Nonnull;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;

/**
 * @author Roman.Chernyatchik
 */
public class GherkinAnnotator implements Annotator {

    public void annotate(@Nonnull final PsiElement psiElement, @Nonnull final AnnotationHolder holder) {
        final GherkinAnnotatorVisitor visitor = new GherkinAnnotatorVisitor(holder);
        psiElement.accept(visitor);
    }
}
