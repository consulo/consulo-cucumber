package org.jetbrains.plugins.cucumber.groovy

import com.intellij.openapi.vfs.JarFileSystem
import com.intellij.testFramework.LightProjectDescriptor
import org.jetbrains.plugins.groovy.GroovyLightProjectDescriptor
import org.jetbrains.plugins.groovy.LightGroovyTestCase

/**
 * @author Max Medvedev
 */
abstract class GrCucumberLightTestCase extends LightGroovyTestCase {
  static class GrCucumberLightProjectDescriptor extends GroovyLightProjectDescriptor {
    @Override
    void configureModule(Module module, ModifiableRootModel model, ContentEntry contentEntry) {
      super.configureModule(module, model, contentEntry)

      final Library.ModifiableModel modifiableModel = model.moduleLibraryTable.createLibrary("GROOVY_CUCUMBER").modifiableModel
      TestUtils.mockGroovyCucumberLibraryNames.each { jar ->
        final VirtualFile libJar = JarFileSystem.instance.refreshAndFindFileByPath("$jar!/")
        assert libJar != null
        modifiableModel.addRoot(libJar, OrderRootType.CLASSES)
      }
      modifiableModel.commit()
    }

    protected GrCucumberLightProjectDescriptor() {
      super(org.jetbrains.plugins.groovy.util.TestUtils.mockGroovy2_1LibraryName)
    }

    public static final INSTANCE = new GrCucumberLightProjectDescriptor()
  }

  @Override
  protected LightProjectDescriptor getProjectDescriptor() {
    return GrCucumberLightProjectDescriptor.INSTANCE
  }
}
