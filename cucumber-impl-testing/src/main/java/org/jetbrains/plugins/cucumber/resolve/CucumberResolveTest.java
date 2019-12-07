package org.jetbrains.plugins.cucumber.resolve;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.plugins.cucumber.CucumberCodeInsightTestCase;
import org.jetbrains.plugins.cucumber.steps.CucumberStepsIndex;
import org.jetbrains.plugins.cucumber.steps.reference.CucumberStepReference;
import com.intellij.openapi.application.PathManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;

/**
 * User: Andrey.Vokin
 * Date: 7/20/12
 */
public abstract class CucumberResolveTest extends CucumberCodeInsightTestCase {
  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  protected void checkReference(@Nonnull final String step, @Nullable final String stepDefinitionName) {
    final CucumberStepReference ref = (CucumberStepReference)findReferenceBySignature(step);
    assert ref != null;

    final ResolveResult[] result = ref.multiResolve(true);
    boolean ok = stepDefinitionName == null;
    for (ResolveResult rr : result) {
      final PsiElement resolvedElement = rr.getElement();
      if (resolvedElement != null) {
        if (stepDefinitionName == null) {
          ok = false;
        } else {
          final String resolvedStepDefName = getStepDefinitionName(resolvedElement);
          if (resolvedStepDefName != null && resolvedStepDefName.equals(stepDefinitionName)) {
            ok = true;
            break;
          }
        }
      }
    }
    assertTrue(ok);
  }

  public void doTest(@Nonnull final String folder, @Nonnull final String step, @Nullable final String stepDefinitionName) throws Exception {
    init(folder);

    checkReference(step, stepDefinitionName);
  }

  protected void init(String folder) {
    CucumberStepsIndex.getInstance(myFixture.getProject()).reset();
    myFixture.copyDirectoryToProject(folder, "");
    myFixture.configureFromExistingVirtualFile(myFixture.findFileInTempDir("test.feature"));
  }

  @Override
  protected String getTestDataPath() {
    return getRelatedTestDataPath();
  }

  @Nullable
  protected abstract String getStepDefinitionName(@Nonnull PsiElement stepDefinition);

  protected abstract String getRelatedTestDataPath();
}
