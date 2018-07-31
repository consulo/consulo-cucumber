package org.jetbrains.plugins.cucumber.inspections.suppress;

import com.intellij.codeInspection.SuppressQuickFix;
import com.intellij.codeInspection.SuppressionUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.plugins.cucumber.psi.GherkinSuppressionHolder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellij.codeInspection.SuppressionUtil.COMMON_SUPPRESS_REGEXP;

/**
 * @author Roman.Chernyatchik
 */
public class GherkinSuppressionUtil {
  // the same regexp as for ruby
  private static final Pattern SUPPRESS_IN_LINE_COMMENT_PATTERN = Pattern.compile("#" + COMMON_SUPPRESS_REGEXP);;

  private GherkinSuppressionUtil() {
  }

  public static SuppressQuickFix[] getDefaultSuppressActions(@Nullable final PsiElement element,
                                                                    @Nonnull final String actionShortName) {
    return new SuppressQuickFix[]{
      new GherkinSuppressForStepCommentFix(actionShortName),
      new GherkinSuppressForScenarioCommentFix(actionShortName),
      new GherkinSuppressForFeatureCommentFix(actionShortName),
    };
  }

  public static boolean isSuppressedFor(@Nonnull final PsiElement element, @Nonnull final String toolId) {
    return ApplicationManager.getApplication().runReadAction(new Computable<Boolean>() {
      public Boolean compute() {
        return getSuppressedIn(element, toolId) != null;
      }
    }).booleanValue();
  }

  @Nullable
  private static PsiComment getSuppressedIn(final PsiElement place, final String toolId) {
    // find suppression holder with suppression comment about given inspection tool
    PsiElement suppressionHolder = PsiTreeUtil.getNonStrictParentOfType(place, GherkinSuppressionHolder.class);
    while (suppressionHolder != null) {
      final PsiComment suppressionHolderElement = getSuppressionComment(toolId, suppressionHolder);
      if (suppressionHolderElement != null) {
        return suppressionHolderElement;
      }
      suppressionHolder = PsiTreeUtil.getParentOfType(suppressionHolder, GherkinSuppressionHolder.class);
    }
    return null;
  }

  @Nullable
  private static PsiComment getSuppressionComment(final String toolId,
                                                  @Nullable final PsiElement element) {
    if (element != null) {
      final PsiElement comment = PsiTreeUtil.skipSiblingsBackward(element, PsiWhiteSpace.class);
      if (comment instanceof PsiComment) {
        String text = comment.getText();
        Matcher matcher = SUPPRESS_IN_LINE_COMMENT_PATTERN.matcher(text);
        if (matcher.matches() && SuppressionUtil.isInspectionToolIdMentioned(matcher.group(1), toolId)) {
          return (PsiComment)comment;
        }
      }
    }
    return null;
  }
}
