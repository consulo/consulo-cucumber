package org.jetbrains.plugins.cucumber.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.TokenType;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Function;
import org.jetbrains.plugins.cucumber.psi.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yole
 */
public class GherkinStepImpl extends GherkinPsiElementBase implements GherkinStep {

  private static final TokenSet TEXT_FILTER = TokenSet.create(GherkinTokenTypes.TEXT, GherkinElementTypes.STEP_PARAMETER, TokenType.WHITE_SPACE, GherkinTokenTypes.STEP_PARAMETER_TEXT, GherkinTokenTypes.STEP_PARAMETER_BRACE);

  private static final Pattern PARAMETER_SUBSTITUTION_PATTERN = Pattern.compile("<([^>\n\r]+)>");
  private final Object LOCK = new Object();

  private List<String> mySubstitutions;

  public GherkinStepImpl(@Nonnull ASTNode node) {
    super(node);
  }

  @Override
  public String toString() {
    return "GherkinStep:" + getStepName();
  }

  @Nullable
  public ASTNode getKeyword() {
    return getNode().findChildByType(GherkinTokenTypes.STEP_KEYWORD);
  }

  @Nullable
  public String getStepName() {
    return getElementText();
  }

  @Override
  @Nonnull
  protected String getElementText() {
    final ASTNode node = getNode();
    final ASTNode[] children = node.getChildren(TEXT_FILTER);
    return StringUtil.join(children, new Function<ASTNode, String>() {
      public String fun(ASTNode astNode) {
        return astNode.getText();
      }
    }, "").trim();
  }

  @Nullable
  public GherkinPystring getPystring() {
    return PsiTreeUtil.findChildOfType(this, GherkinPystring.class);
  }

  @Nullable
  public GherkinTable getTable() {
    final ASTNode tableNode = getNode().findChildByType(GherkinElementTypes.TABLE);
    return tableNode == null ? null : (GherkinTable)tableNode.getPsi();
  }

  @Override
  protected String getPresentableText() {
    final ASTNode keywordNode = getKeyword();
    final String prefix = keywordNode != null ? keywordNode.getText() + ": " : "Step: ";
    return prefix + getStepName();
  }

  @Nonnull
  @Override
  public PsiReference[] getReferences() {
    return ReferenceProvidersRegistry.getReferencesFromProviders(this, GherkinStepImpl.class);
  }

  protected void acceptGherkin(GherkinElementVisitor gherkinElementVisitor) {
    gherkinElementVisitor.visitStep(this);
  }

  @Nonnull
  public List<String> getParamsSubstitutions() {
    synchronized (LOCK) {
      if (mySubstitutions == null) {
        final ArrayList<String> substitutions = new ArrayList<String>();


        // step name
        final String text = getStepName();
        if (StringUtil.isEmpty(text)) {
          return Collections.emptyList();
        }
        addSubstitutionFromText(text, substitutions);

        // pystring
        final GherkinPystring pystring = getPystring();
        String pystringText = pystring != null ? pystring.getText() : null;
        if (!StringUtil.isEmpty(pystringText)) {
          addSubstitutionFromText(pystringText, substitutions);
        }

        // table
        final GherkinTable table = getTable();
        final String tableText = table == null ? null : table.getText();
        if (tableText != null) {
          addSubstitutionFromText(tableText, substitutions);
        }

        mySubstitutions = substitutions.isEmpty() ?  Collections.<String>emptyList() : substitutions;
      }
      return mySubstitutions;
    }
  }

  private static void addSubstitutionFromText(String text, ArrayList<String> substitutions) {
    final Matcher matcher = PARAMETER_SUBSTITUTION_PATTERN.matcher(text);
    boolean result = matcher.find();
    if (!result) {
      return;
    }

    do {
      final String substitution = matcher.group(1);
      if (!StringUtil.isEmpty(substitution) && !substitutions.contains(substitution)) {
        substitutions.add(substitution);
      }
      result = matcher.find();
    } while (result);
  }

  @Override
  public void subtreeChanged() {
    super.subtreeChanged();
    clearCaches();
  }

  @Nullable
  public GherkinStepsHolder getStepHolder() {
    final PsiElement parent = getParent();
    return parent != null ? (GherkinStepsHolder)parent : null;
  }

  private void clearCaches() {
    synchronized (LOCK) {
      mySubstitutions = null;
    }
  }

  @Nullable
  public String getSubstitutedName() {
    List<String> sustitutedNameList = new ArrayList<String>(getSubstitutedNameList(1));
    return sustitutedNameList.size() > 0 ? sustitutedNameList.get(0) : getStepName();
  }

  public Set<String> getSubstitutedNameList(int maxCount) {
    final Set<String> result = new HashSet<String>();
    final GherkinStepsHolder holder = getStepHolder();
    final String stepName = getStepName();
    if (stepName != null) {
      if (holder instanceof GherkinScenarioOutline) {
        final GherkinScenarioOutline outline = (GherkinScenarioOutline)holder;
        final List<GherkinExamplesBlock> examplesBlocks = outline.getExamplesBlocks();
        for (GherkinExamplesBlock examplesBlock : examplesBlocks) {
          final GherkinTable table = examplesBlock.getTable();
          if (table != null) {
            final List<GherkinTableRow> rows = table.getDataRows();
            for (GherkinTableRow row : rows) {
              result.add(substituteText(stepName, table.getHeaderRow(), row));
              if (result.size() == maxCount) {
                return result;
              }
            }
          }
        }
      }
    }
    if (result.size() == 0 && stepName != null) {
      result.add(stepName);
    }
    return result;
  }

  @Nonnull
  public Set<String> getSubstitutedNameList() {
    return getSubstitutedNameList(Integer.MAX_VALUE);
  }

  private static String substituteText(String stepName, GherkinTableRow headerRow, GherkinTableRow row) {
    final List<GherkinTableCell> headerCells = headerRow.getPsiCells();
    final List<GherkinTableCell> dataCells = row.getPsiCells();
    for (int i = 0, headerCellsNumber = headerCells.size(), dataCellsNumber = dataCells.size(); i < headerCellsNumber && i < dataCellsNumber; i++) {
      final String cellText = headerCells.get(i).getText().trim();
      stepName = stepName.replace("<" + cellText + ">", dataCells.get(i).getText().trim());
    }
    return stepName;
  }
}
