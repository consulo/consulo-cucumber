package org.jetbrains.plugins.cucumber.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.plugins.cucumber.StepDefinitionCreator;
import org.jetbrains.plugins.cucumber.java.steps.JavaStepDefinition;
import org.jetbrains.plugins.cucumber.java.steps.JavaStepDefinitionCreator;
import org.jetbrains.plugins.cucumber.psi.GherkinFile;
import org.jetbrains.plugins.cucumber.psi.GherkinRecursiveElementVisitor;
import org.jetbrains.plugins.cucumber.psi.GherkinStep;
import org.jetbrains.plugins.cucumber.steps.AbstractCucumberExtension;
import org.jetbrains.plugins.cucumber.steps.AbstractStepDefinition;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.impl.java.stubs.index.JavaFullClassNameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.AnnotatedElementsSearch;
import com.intellij.util.Query;
import com.intellij.util.containers.ContainerUtil;

/**
 * User: Andrey.Vokin
 * Date: 7/16/12
 */
public class CucumberJavaExtension extends AbstractCucumberExtension {
  public static final String CUCUMBER_RUNTIME_JAVA_STEP_DEF_ANNOTATION = "cucumber.runtime.java.StepDefAnnotation";

  @Override
  public boolean isStepLikeFile(@Nonnull final PsiElement child, @Nonnull final PsiElement parent) {
    if (child instanceof PsiJavaFile) {
      return true;
    }
    return false;
  }

  @Override
  public boolean isWritableStepLikeFile(@Nonnull PsiElement child, @Nonnull PsiElement parent) {
    return isStepLikeFile(child, parent);
  }

  @Nonnull
  @Override
  public FileType getStepFileType() {
    return JavaFileType.INSTANCE;
  }

  @Nonnull
  @Override
  public StepDefinitionCreator getStepDefinitionCreator() {
    return new JavaStepDefinitionCreator();
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
        boolean covered = false;
        final String glue = CucumberJavaUtil.getPackageOfStep(step);
        if (glue != null) {
          final Set<String> toRemove = ContainerUtil.newHashSet();
          for (String existedGlue : glues) {
            if (glue.startsWith(existedGlue + ".")) {
              covered = true;
              break;
            }
            else if (existedGlue.startsWith(glue + ".")) {
              toRemove.add(existedGlue);
            }
          }

          for (String removing : toRemove) {
            glues.remove(removing);
          }

          if (!covered) {
            glues.add(glue);
          }
        }
      }
    });

    return glues;
  }

  @Override
  public List<AbstractStepDefinition> loadStepsFor(@Nullable PsiFile featureFile, @Nonnull Module module) {
    final GlobalSearchScope dependenciesScope = GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module, true);

    Collection<PsiClass> stepDefAnnotationCandidates = JavaFullClassNameIndex.getInstance().get(
      CUCUMBER_RUNTIME_JAVA_STEP_DEF_ANNOTATION.hashCode(), module.getProject(), dependenciesScope);

    PsiClass stepDefAnnotationClass = null;
    for (PsiClass candidate : stepDefAnnotationCandidates) {
      if (CUCUMBER_RUNTIME_JAVA_STEP_DEF_ANNOTATION.equals(candidate.getQualifiedName())) {
        stepDefAnnotationClass = candidate;
        break;
      }
    }
    if (stepDefAnnotationClass == null) {
      return Collections.emptyList();
    }

    final List<AbstractStepDefinition> result = new ArrayList<AbstractStepDefinition>();
    final Query<PsiClass> stepDefAnnotations = AnnotatedElementsSearch.searchPsiClasses(stepDefAnnotationClass, dependenciesScope);
    for (PsiClass annotationClass : stepDefAnnotations) {
      final Query<PsiMethod> javaStepDefinitions = AnnotatedElementsSearch.searchPsiMethods(annotationClass, dependenciesScope);
      for (PsiMethod stepDefMethod : javaStepDefinitions) {
        result.add(new JavaStepDefinition(stepDefMethod));
      }
    }
    return result;
  }

  @Override
  public Collection<? extends PsiFile> getStepDefinitionContainers(@Nonnull GherkinFile featureFile) {
    final Module module = ModuleUtilCore.findModuleForPsiElement(featureFile);
    if (module == null) {
      return Collections.emptySet();
    }

    List<AbstractStepDefinition> stepDefs = loadStepsFor(featureFile, module);

    Set<PsiFile> result = new HashSet<PsiFile>();
    for (AbstractStepDefinition stepDef : stepDefs) {
      result.add(stepDef.getElement().getContainingFile());
    }
    return result;
  }
}
