package org.jetbrains.plugins.cucumber.psi;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.impl.PsiTreeChangePreprocessorBase;

/**
 * @author Roman.Chernyatchik
 */
public class GherkinPsiManager extends PsiTreeChangePreprocessorBase
{
	public GherkinPsiManager(@NotNull Project project)
	{
		super(project);
	}

	@Override
	protected boolean isMyFile(@NotNull PsiFile file)
	{
		return file instanceof GherkinFile;
	}

	@Override
	protected boolean isInsideCodeBlock(PsiElement element)
	{
		if(element instanceof PsiFileSystemItem)
		{
			return false;
		}

		if(element == null || element.getParent() == null)
		{
			return true;
		}

		while(true)
		{
			if(element instanceof GherkinFile || element instanceof GherkinStep || element instanceof GherkinFeature || element instanceof
					GherkinStepsHolder)
			{
				return false;
			}
			if(element instanceof PsiFile || element instanceof PsiDirectory || element == null)
			{
				return true;
			}
			PsiElement parent = element.getParent();
			if(parent instanceof GherkinTable || parent instanceof GherkinExamplesBlock || parent instanceof GherkinTableRow)
			{
				return true;
			}
			element = parent;
		}
	}
}
