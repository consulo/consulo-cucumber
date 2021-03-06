package org.jetbrains.plugins.cucumber.psi.structure;

import javax.annotation.Nonnull;
import org.jetbrains.plugins.cucumber.psi.GherkinFeature;
import org.jetbrains.plugins.cucumber.psi.GherkinFile;
import org.jetbrains.plugins.cucumber.psi.GherkinStep;
import org.jetbrains.plugins.cucumber.psi.GherkinStepsHolder;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder;
import com.intellij.lang.PsiStructureViewFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;

/**
 * @author yole
 */
public class GherkinStructureViewFactory implements PsiStructureViewFactory
{
	@Override
	public StructureViewBuilder getStructureViewBuilder(final PsiFile psiFile)
	{
		return new TreeBasedStructureViewBuilder()
		{
			@Nonnull
			@Override
			public StructureViewModel createStructureViewModel(Editor editor)
			{
				PsiElement root = PsiTreeUtil.getChildOfType(psiFile, GherkinFeature.class);
				if(root == null)
				{
					root = psiFile;
				}
				return new StructureViewModelBase(psiFile, new GherkinStructureViewElement(root)).withSuitableClasses(GherkinFile.class,
						GherkinFeature.class, GherkinStepsHolder.class, GherkinStep.class);
			}
		};
	}
}