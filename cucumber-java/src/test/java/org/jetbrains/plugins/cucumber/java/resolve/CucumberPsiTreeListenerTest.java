package org.jetbrains.plugins.cucumber.java.resolve;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.jetbrains.plugins.cucumber.steps.CucumberStepsIndex;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.testFramework.TestModuleDescriptor;
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor;

/**
 * User: Andrey.Vokin
 * Date: 8/9/12
 */
public class CucumberPsiTreeListenerTest extends BaseCucumberJavaResolveTest {
  public void testCreationOfStepDefinition() throws Exception {
    doTestCreation("treeListener", "I p<caret>ay 25", "@cucumber.annotation.en.When(\"^I pay (\\\\d+)$\")\npublic void i_pay(int amount) {}");
  }

  public void testDeletionOfStepDefinition() throws Exception {
    doTestDeletion("treeListener", "my change sh<caret>ould be 4", "my_change_should_be_");
  }

  private PsiClass getStepDefClass() {
    final PsiFile stepDefFile = findPsiFileInTempDirBy("ShoppingStepdefs.java");
    final PsiJavaFile javaFile = (PsiJavaFile)stepDefFile;
    final PsiClass psiClass = PsiTreeUtil.getChildOfType(javaFile, PsiClass.class);
    assert psiClass != null;

    return psiClass;
  }

  private String createStepDefinition(@Nonnull final String stepDef) {
    final PsiClass psiClass = getStepDefClass();
    final PsiFile psiFile = psiClass.getContainingFile();

    final Ref<String> createdMethodName = new Ref<String>();

    new WriteCommandAction(getProject(), psiFile) {
      @Override
      protected void run(final Result result) throws Throwable {
        final PsiElementFactory factory = JavaPsiFacade.getInstance(getProject()).getElementFactory();
        final PsiMethod method = factory.createMethodFromText(stepDef, psiClass);
        psiClass.add(method);
        createdMethodName.set(method.getName());
      }
    }.execute();

    return createdMethodName.get();
  }

  private void deleteStepDefinition(@Nonnull final String stepDefName) {
    final PsiClass psiClass = getStepDefClass();
    final PsiFile psiFile = psiClass.getContainingFile();

    new WriteCommandAction(getProject(), psiFile) {
      @Override
      protected void run(Result result) throws Throwable {

        for (PsiMethod method : psiClass.getAllMethods()) {
          if (method.getName().equals(stepDefName)) {
            method.delete();
            break;
          }
        }
      }
    }.execute();
  }

  private void doTestCreation(@Nonnull final String folder, @Nonnull final String step, @Nonnull final String stepDefinitionContent)
    throws Exception {
    init(folder);

    checkReference(step, null);
    final String stepDefinitionName = createStepDefinition(stepDefinitionContent);
    CucumberStepsIndex.getInstance(getProject()).flush();
    checkReference(step, stepDefinitionName);
  }

  private void doTestDeletion(@Nonnull final String folder, @Nonnull final String step, @Nonnull final String stepDefinitionName)
    throws IOException {
    init(folder);

    checkReference(step, stepDefinitionName);
    deleteStepDefinition(stepDefinitionName);
    CucumberStepsIndex.getInstance(getProject()).flush();
    checkReference(step, null);
  }

  @Override
  protected TestModuleDescriptor getProjectDescriptor() {
    return DESCRIPTOR;
  }

  public static final DefaultLightProjectDescriptor DESCRIPTOR = new DefaultLightProjectDescriptor() {
    @Override
    public void configureModule(Module module, ModifiableRootModel model, ContentEntry contentEntry) {
      PsiTestUtil.addLibrary(module, model, "cucumber-java", PathManager.getHomePath() + "/community/lib", "cucumber-java-1.0.14.jar");
    }
  };
}
