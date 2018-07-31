package org.jetbrains.plugins.cucumber.psi;

import javax.annotation.Nonnull;

import org.jetbrains.plugins.cucumber.psi.impl.*;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import consulo.lang.LanguageVersion;

/**
 * @author yole
 */
public class GherkinParserDefinition implements ParserDefinition {
  private static final TokenSet WHITESPACE = TokenSet.create(TokenType.WHITE_SPACE);
  private static final TokenSet COMMENTS = TokenSet.create(GherkinTokenTypes.COMMENT);

  @Nonnull
  public Lexer createLexer(LanguageVersion languageVersion) {
    final CucumberLanguageService instance = null; //CucumberLanguageService.getInstance(project);
    return new GherkinLexer(instance == null? new PlainGherkinKeywordProvider() : instance.getKeywordProvider());
  }

  public PsiParser createParser(LanguageVersion languageVersion) {
    return new GherkinParser();
  }

  public IFileElementType getFileNodeType() {
    return GherkinElementTypes.GHERKIN_FILE;
  }

  @Nonnull
  public TokenSet getWhitespaceTokens(LanguageVersion languageVersion) {
    return WHITESPACE;
  }

  @Nonnull
  public TokenSet getCommentTokens(LanguageVersion languageVersion) {
    return COMMENTS;
  }

  @Nonnull
  public TokenSet getStringLiteralElements(LanguageVersion languageVersion) {
    return TokenSet.EMPTY;
  }

  @Nonnull
  public PsiElement createElement(ASTNode node) {
    if (node.getElementType() == GherkinElementTypes.FEATURE) return new GherkinFeatureImpl(node);
    if (node.getElementType() == GherkinElementTypes.FEATURE_HEADER) return new GherkinFeatureHeaderImpl(node);
    if (node.getElementType() == GherkinElementTypes.SCENARIO) return new GherkinScenarioImpl(node);
    if (node.getElementType() == GherkinElementTypes.STEP) return new GherkinStepImpl(node);
    if (node.getElementType() == GherkinElementTypes.SCENARIO_OUTLINE) return new GherkinScenarioOutlineImpl(node);
    if (node.getElementType() == GherkinElementTypes.EXAMPLES_BLOCK) return new GherkinExamplesBlockImpl(node);
    if (node.getElementType() == GherkinElementTypes.TABLE) return new GherkinTableImpl(node);
    if (node.getElementType() == GherkinElementTypes.TABLE_ROW) return new GherkinTableRowImpl(node);
    if (node.getElementType() == GherkinElementTypes.TABLE_CELL) return new GherkinTableCellImpl(node);
    if (node.getElementType() == GherkinElementTypes.TABLE_HEADER_ROW) return new GherkinTableHeaderRowImpl(node);
    if (node.getElementType() == GherkinElementTypes.TAG) return new GherkinTagImpl(node);
    if (node.getElementType() == GherkinElementTypes.STEP_PARAMETER) return new GherkinStepParameterImpl(node);
    if (node.getElementType() == GherkinElementTypes.PYSTRING) return new GherkinPystringImpl(node);
    return PsiUtilCore.NULL_PSI_ELEMENT;
  }

  public PsiFile createFile(FileViewProvider viewProvider) {
    return new GherkinFileImpl(viewProvider);
  }

  public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
    // Line break between line comment and other elements
    final IElementType leftElementType = left.getElementType();
    if (leftElementType == GherkinTokenTypes.COMMENT) {
      return SpaceRequirements.MUST_LINE_BREAK;
    }
    if (right.getElementType() == GherkinTokenTypes.EXAMPLES_KEYWORD) {
      return SpaceRequirements.MUST_LINE_BREAK;
    }
    return SpaceRequirements.MAY;
  }
}
