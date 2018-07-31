package org.jetbrains.plugins.cucumber.groovy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.plugins.cucumber.StepDefinitionCreator;
import org.jetbrains.plugins.cucumber.groovy.steps.GrStepDefinition;
import org.jetbrains.plugins.cucumber.groovy.steps.GrStepDefinitionCreator;
import org.jetbrains.plugins.cucumber.psi.GherkinFile;
import org.jetbrains.plugins.cucumber.psi.GherkinRecursiveElementVisitor;
import org.jetbrains.plugins.cucumber.psi.GherkinStep;
import org.jetbrains.plugins.cucumber.steps.AbstractStepDefinition;
import org.jetbrains.plugins.cucumber.steps.NotIndexedCucumberExtension;
import org.jetbrains.plugins.groovy.GroovyFileType;
import org.jetbrains.plugins.groovy.lang.psi.GroovyFile;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.GrStatement;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ContentFolder;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.util.PathUtil;
import com.intellij.util.containers.ContainerUtil;
import consulo.java.module.extension.JavaModuleExtension;
import consulo.roots.ContentFolderScopes;

/**
 * @author Max Medvedev
 */
public class GrCucumberExtension extends NotIndexedCucumberExtension {
  @Override
  public boolean isStepLikeFile(@Nonnull PsiElement child, @Nonnull PsiElement parent) {
    return child instanceof GroovyFile && ((GroovyFile)child).getName().endsWith(".groovy");
  }

  @Override
  public boolean isWritableStepLikeFile(@Nonnull PsiElement child, @Nonnull PsiElement parent) {
    return isStepLikeFile(child, parent);
  }

  @Nonnull
  @Override
  public FileType getStepFileType() {
    return GroovyFileType.GROOVY_FILE_TYPE;
  }

  @Nonnull
  @Override
  public StepDefinitionCreator getStepDefinitionCreator() {
    return new GrStepDefinitionCreator();
  }

  @Nullable
  public String getGlue(@Nonnull GherkinStep step) {
    for (PsiReference ref : step.getReferences()) {
      PsiElement refElement = ref.resolve();
      if (refElement != null && refElement instanceof GrMethodCall) {
        GroovyFile groovyFile = (GroovyFile)refElement.getContainingFile();
        VirtualFile vfile = groovyFile.getVirtualFile();
        if (vfile != null) {
          VirtualFile parentDir = vfile.getParent();
          return PathUtil.getLocalPath(parentDir);
        }
      }
    }
    return null;
  }

  @Nonnull
  @Override
  public Collection<String> getGlues(@Nonnull GherkinFile file, Set<String> gluesFromOtherFiles) {
    if (gluesFromOtherFiles == null) {
      gluesFromOtherFiles = ContainerUtil.newHashSet();
    }
    final Set<String> glues = gluesFromOtherFiles;

    file.accept(new GherkinRecursiveElementVisitor() {
      @Override
      public void visitStep(GherkinStep step) {
        final String glue = getGlue(step);
        if (glue != null) {
          glues.add(glue);
        }
      }
    });

    return glues;
  }

  @Override
  protected void loadStepDefinitionRootsFromLibraries(Module module, List<PsiDirectory> roots, Set<String> directories) {

  }

  @Nonnull
  @Override
  public List<AbstractStepDefinition> getStepDefinitions(@Nonnull PsiFile psiFile) {
    final List<AbstractStepDefinition> newDefs = new ArrayList<AbstractStepDefinition>();
    if (psiFile instanceof GroovyFile) {
      GrStatement[] statements = ((GroovyFile)psiFile).getStatements();
      for (GrStatement statement : statements) {
        if (GrCucumberUtil.isStepDefinition(statement)) {
          newDefs.add(GrStepDefinition.getStepDefinition((GrMethodCall)statement));
        }
      }
    }
    return newDefs;
  }

  @Override
  protected void collectAllStepDefsProviders(@Nonnull List<VirtualFile> providers, @Nonnull Project project) {
    final Module[] modules = ModuleManager.getInstance(project).getModules();
    for (Module module : modules) {
      if (ModuleUtilCore.getExtension(module, JavaModuleExtension.class) != null) {
        final VirtualFile[] roots = ModuleRootManager.getInstance(module).getContentRoots();
        ContainerUtil.addAll(providers, roots);
      }
    }
  }


  @Override
  public void findRelatedStepDefsRoots(@Nonnull final Module module, @Nonnull final PsiFile featureFile,
									   List<PsiDirectory> newStepDefinitionsRoots, Set<String> processedStepDirectories) {
    final ContentEntry[] contentEntries = ModuleRootManager.getInstance(module).getContentEntries();
    for (final ContentEntry contentEntry : contentEntries) {
      final ContentFolder[] sourceFolders = contentEntry.getFolders(ContentFolderScopes.all(false));
      for (ContentFolder sf : sourceFolders) {
        // ToDo: check if inside test folder
        VirtualFile sfDirectory = sf.getFile();
        if (sfDirectory != null && sfDirectory.isDirectory()) {
          PsiDirectory sourceRoot = PsiManager.getInstance(module.getProject()).findDirectory(sfDirectory);
          if (sourceRoot != null && !processedStepDirectories.contains(sourceRoot.getVirtualFile().getPath())) {
            newStepDefinitionsRoots.add(sourceRoot);
          }
        }
      }
    }
  }
}
