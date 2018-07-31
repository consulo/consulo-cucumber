package org.jetbrains.plugins.cucumber.psi;

import javax.annotation.Nullable;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.project.Project;

/**
 * User: Andrey.Vokin
 * Date: 2/24/12
 */
public interface GherkinKeywordProviderBuilder {
  ExtensionPointName<GherkinKeywordProviderBuilder> EP_NAME = ExtensionPointName.create("cucumber.KeywordProvider");

  @Nullable
  GherkinKeywordProvider getKeywordProvider(Project project);
}
