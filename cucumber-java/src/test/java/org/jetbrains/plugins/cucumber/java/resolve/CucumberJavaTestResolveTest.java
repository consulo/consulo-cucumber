package org.jetbrains.plugins.cucumber.java.resolve;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.testFramework.TestModuleDescriptor;
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor;
import consulo.roots.ContentFolderScopes;
import consulo.roots.impl.TestContentFolderTypeProvider;

/**
 * User: Andrey.Vokin
 * Date: 7/20/12
 */
public abstract class CucumberJavaTestResolveTest extends BaseCucumberJavaResolveTest {
  public void testNavigationFromStepToStepDef01() throws Exception {
    doTest("stepResolve_01", "I p<caret>ay 25", "i_pay");
  }
  public void testNavigationFromStepToStepDef02() throws Exception {
    doTest("stepResolve_01", "the followi<caret>ng groceries", "the_following_groceries");
  }
  public void testNavigationFromStepToStepDef03() throws Exception {
    doTest("stepResolve_01", "my change sh<caret>ould be 4", "my_change_should_be_");
  }

  public void testNavigationWithQuotes01() throws Exception {
    doTest("stepResolve_02", "I subtract 5 fr<caret>om 9", "I_subtract_from");
  }

  public void testNavigationWithQuotes02() throws Exception {
    doTest("stepResolve_02", "the resu<caret>lt is 4", "the_result_is");
  }

  public void testNavigationWithQuotes03() throws Exception {
    doTest("stepResolve_02", "tes<caret>t \"test\"", "test");
  }

  @Override
  protected TestModuleDescriptor getProjectDescriptor() {
    return DESCRIPTOR;
  }

  public static final DefaultLightProjectDescriptor DESCRIPTOR = new DefaultLightProjectDescriptor() {
    @Override
    public void configureModule(Module module, ModifiableRootModel model, ContentEntry contentEntry) {
      PsiTestUtil.addLibrary(module, model, "cucumber-java", "/community/lib", "cucumber-java-1.0.14.jar");

      VirtualFile sourceRoot = VirtualFileManager.getInstance().refreshAndFindFileByUrl("temp:///src");
      if (sourceRoot != null) {
        contentEntry.removeFolder(contentEntry.getFolders(ContentFolderScopes.test())[0]);
        contentEntry.addFolder(sourceRoot, TestContentFolderTypeProvider.getInstance());
      }
    }
  };
}
