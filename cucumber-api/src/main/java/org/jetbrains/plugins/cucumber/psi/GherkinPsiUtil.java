package org.jetbrains.plugins.cucumber.psi;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import consulo.annotation.access.RequiredReadAction;
import org.jetbrains.plugins.cucumber.psi.impl.GherkinFileImpl;
import org.jetbrains.plugins.cucumber.steps.AbstractStepDefinition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Roman.Chernyatchik
 * @date May 21, 2009
 */
public class GherkinPsiUtil
{
	private GherkinPsiUtil()
	{
	}

	@Nullable
	public static GherkinFileImpl getGherkinFile(@Nonnull final PsiElement element)
	{
		if(!element.isValid())
		{
			return null;
		}
		final PsiFile containingFile = element.getContainingFile();
		return containingFile instanceof GherkinFileImpl ? (GherkinFileImpl) containingFile : null;
	}

	@Nullable
	@RequiredReadAction
	public static List<TextRange> buildParameterRanges(@Nonnull GherkinStep step, @Nonnull AbstractStepDefinition definition, final int shiftOffset)
	{
		final List<TextRange> parameterRanges = new ArrayList<TextRange>();
		final Pattern pattern = definition.getPattern();
		if(pattern == null)
		{
			return null;
		}
		Matcher matcher = pattern.matcher(step.getStepName());
		if(matcher.find())
		{
			final int groupCount = matcher.groupCount();
			for(int i = 1; i < groupCount; i++)
			{
				final int start = matcher.start(i);
				final int end = matcher.end(i);
				parameterRanges.add(new TextRange(start, end).shiftRight(shiftOffset));
			}
		}

		int k = step.getText().indexOf(step.getStepName());
		k += step.getStepName().length();
		if(k < step.getText().length() - 1)
		{
			String text = step.getText().substring(k + 1);
			boolean inParam = false;
			int paramStart = 0;
			int i = 0;
			while(i < text.length())
			{
				if(text.charAt(i) == '<')
				{
					paramStart = i;
					inParam = true;
				}

				if(text.charAt(i) == '>' && inParam)
				{
					parameterRanges.add(new TextRange(paramStart, i + 1).shiftRight(shiftOffset + step.getStepName().length() + 1));
					inParam = false;
				}
				i++;
			}

		}


		return parameterRanges;
	}
}
