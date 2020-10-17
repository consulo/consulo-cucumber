package org.jetbrains.plugins.cucumber.groovy.steps;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import consulo.cucumber.api.icon.CucumberApiIconGroup;
import org.jetbrains.plugins.cucumber.groovy.GrCucumberUtil;
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrMethodCall;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.psi.PsiElement;
import com.intellij.util.Function;

/**
 * @author Max Medvedev
 */
public class GrCucumberLineMarkerProvider implements LineMarkerProvider
{
	private static final Function<GrMethodCall, String> TOOLTIP_PROVIDER = stepDef -> stepDef.getPresentation().getPresentableText();

	@Nullable
	@Override
	public LineMarkerInfo getLineMarkerInfo(@Nonnull PsiElement element)
	{
		if(GrCucumberUtil.isStepDefinition(element))
		{
			return new LineMarkerInfo<>(((GrMethodCall) element), element.getTextRange().getStartOffset(), CucumberApiIconGroup.cucumber(), Pass.UPDATE_ALL, TOOLTIP_PROVIDER, null);
		}
		else
		{
			return null;
		}
	}
}
