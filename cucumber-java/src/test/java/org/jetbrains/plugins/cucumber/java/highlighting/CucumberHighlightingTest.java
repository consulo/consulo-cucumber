package org.jetbrains.plugins.cucumber.java.highlighting;

import org.jetbrains.plugins.cucumber.inspections.CucumberStepInspection;
import org.jetbrains.plugins.cucumber.java.CucumberJavaTestUtil;
import org.jetbrains.plugins.cucumber.steps.CucumberStepsIndex;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.testFramework.TestModuleDescriptor;
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;

/**
 * User: Andrey.Vokin
 * Date: 3/1/13
 */
public class CucumberHighlightingTest extends LightPlatformCodeInsightFixtureTestCase {
  public void testStepParameter() {
    doTest();
  }

  protected void doTest() {
    CucumberStepsIndex.getInstance(getProject()).reset();
    myFixture.enableInspections(new CucumberStepInspection());
    myFixture.copyDirectoryToProject(getTestName(true), "");
    myFixture.configureByFile(getTestName(true) + "/test.feature");
    myFixture.testHighlighting(true, true, true);
  }

  @Override
  protected String getBasePath() {
    return CucumberJavaTestUtil.RELATED_TEST_DATA_PATH + "highlighting";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myFixture.allowTreeAccessForAllFiles();
  }

  @Override
  protected boolean isWriteActionRequired() {
    return false;
  }

  @Override
  protected TestModuleDescriptor getProjectDescriptor() {
    return DESCRIPTOR;
  }

  public static final DefaultLightProjectDescriptor DESCRIPTOR = new DefaultLightProjectDescriptor() {
    @Override
    public void configureModule(Module module, ModifiableRootModel model, ContentEntry contentEntry) {
      PsiTestUtil.addLibrary(module, model, "cucumber-java", "/community/lib", "cucumber-java-1.0.14.jar");
    }
  };
}
