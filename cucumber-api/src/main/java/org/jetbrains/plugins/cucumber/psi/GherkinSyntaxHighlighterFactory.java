package org.jetbrains.plugins.cucumber.psi;

import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import javax.annotation.Nonnull;

/**
 * @author yole
 */
public class GherkinSyntaxHighlighterFactory extends SyntaxHighlighterFactory {
  @Nonnull
  public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
    final GherkinKeywordProvider keywordProvider = project != null
                                                   ? CucumberLanguageService.getInstance(project).getKeywordProvider()
                                                   : new PlainGherkinKeywordProvider(); 
    return new GherkinSyntaxHighlighter(keywordProvider);
  }
}
