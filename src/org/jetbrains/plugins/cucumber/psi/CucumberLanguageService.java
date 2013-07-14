package org.jetbrains.plugins.cucumber.psi;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.project.Project;

/**
 * @author yole
 */
public class CucumberLanguageService {
  private GherkinKeywordProvider myKeywordProvider;

  public static CucumberLanguageService getInstance(Project project) {
    return ServiceManager.getService(project, CucumberLanguageService.class);
  }

  public CucumberLanguageService(Project project) {
    // TODO - more correct is to use attached version or at least
    // refresh keyword provider after roots changed event
    final GherkinKeywordProviderBuilder[] builders = Extensions.getExtensions(GherkinKeywordProviderBuilder.EP_NAME);
    for (GherkinKeywordProviderBuilder builder : builders) {
      myKeywordProvider = builder.getKeywordProvider(project);
      if (myKeywordProvider != null) {
        break;
      }
    }
    if (myKeywordProvider == null) {
      myKeywordProvider = new PlainGherkinKeywordProvider();
    }
  }

  public GherkinKeywordProvider getKeywordProvider() {
    return myKeywordProvider;
  }
}
