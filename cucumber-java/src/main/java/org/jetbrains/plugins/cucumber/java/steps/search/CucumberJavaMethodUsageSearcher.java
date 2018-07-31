package org.jetbrains.plugins.cucumber.java.steps.search;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.QueryExecutorBase;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.MethodReferencesSearch;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Processor;
import javax.annotation.Nonnull;
import org.jetbrains.plugins.cucumber.CucumberUtil;
import org.jetbrains.plugins.cucumber.java.CucumberJavaUtil;
import org.jetbrains.plugins.cucumber.psi.GherkinFileType;

/**
 * User: Andrey.Vokin
 * Date: 7/27/12
 */
public class CucumberJavaMethodUsageSearcher extends QueryExecutorBase<PsiReference, MethodReferencesSearch.SearchParameters> {
  @Override
  public void processQuery(@Nonnull final MethodReferencesSearch.SearchParameters p, @Nonnull final Processor<PsiReference> consumer) {
    final PsiMethod method = p.getMethod();

    final String regexp = ApplicationManager.getApplication().runReadAction(new Computable<String>() {
      @Override
      public String compute() {
        final PsiAnnotation stepAnnotation = CucumberJavaUtil.getCucumberStepAnnotation(method);
        return stepAnnotation != null ? CucumberJavaUtil.getPatternFromStepDefinition(stepAnnotation) : null;
      }
    });

    if (regexp == null) {
      return;
    }

    final String word = CucumberUtil.getTheBiggestWordToSearchByIndex(regexp);
    if (StringUtil.isEmpty(word)) {
      return;
    }

    if (p.getScope() instanceof GlobalSearchScope) {
      GlobalSearchScope restrictedScope = GlobalSearchScope.getScopeRestrictedByFileTypes((GlobalSearchScope)p.getScope(), GherkinFileType.INSTANCE);
      ReferencesSearch.search(new ReferencesSearch.SearchParameters(method, restrictedScope, false, p.getOptimizer())).forEach(consumer);
    }
  }
}
