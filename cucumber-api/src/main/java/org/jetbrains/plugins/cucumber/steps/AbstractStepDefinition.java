package org.jetbrains.plugins.cucumber.steps;

import com.intellij.compiler.MalformedPatternException;
import com.intellij.psi.PsiElement;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author yole, Andrey Vokin
 */
public abstract class AbstractStepDefinition {
  private static final String ourEscapePattern = "(\\$\\w+|#\\{.+?\\})";

  private static final String CUCUMBER_START_PREFIX = "\\A";

  private static final String CUCUMBER_END_SUFFIX = "\\z";

  private final SmartPsiElementPointer<PsiElement> myElementPointer;

  private String myCucumberRegex = null;

  public AbstractStepDefinition(@Nonnull final PsiElement element) {
    myElementPointer = SmartPointerManager.getInstance(element.getProject()).createSmartPsiElementPointer(element);

    myCucumberRegex = getCucumberRegexFromElement(element);
  }

  public abstract List<String> getVariableNames();

  public boolean matches(String stepName) {
    final Pattern pattern = getPattern();
    return pattern != null && pattern.matcher(stepName).find();
  }

  public PsiElement getElement() {
    return myElementPointer.getElement();
  }

  @Nullable
  public Pattern getPattern() {
    try {
      final String cucumberRegex = getCucumberRegex();
      if (cucumberRegex == null) return null;
      final StringBuilder patternText = new StringBuilder(cucumberRegex.replaceAll(ourEscapePattern, "(.*)"));
      if (patternText.toString().startsWith(CUCUMBER_START_PREFIX)) {
        patternText.replace(0, CUCUMBER_START_PREFIX.length(), "^");
      }

      if (patternText.toString().endsWith(CUCUMBER_END_SUFFIX)) {
        patternText.replace(patternText.length() - CUCUMBER_END_SUFFIX.length(), patternText.length(), "$");
      }

      return Pattern.compile(patternText.toString());
    }
    catch (MalformedPatternException e) {
      return null;
    }
  }

  @Nullable
  public String getCucumberRegex() {
    final String cucumberRegexFromSmartPointer = getCucumberRegexFromElement(getElement());
    if (cucumberRegexFromSmartPointer == null || myCucumberRegex == null || !cucumberRegexFromSmartPointer.equals(myCucumberRegex)) {
      return null;
    }

    return myCucumberRegex;
  }

  @Nullable
  protected abstract String getCucumberRegexFromElement(PsiElement element);

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AbstractStepDefinition that = (AbstractStepDefinition)o;

    if (myElementPointer != null ? !myElementPointer.equals(that.myElementPointer) : that.myElementPointer != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return myElementPointer != null ? myElementPointer.hashCode() : 0;
  }
}
