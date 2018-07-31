/*
 * Copyright 2000-2012 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jetbrains.plugins.cucumber.groovy.steps.search;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.find.findUsages.FindUsagesHandlerFactory;
import com.intellij.psi.PsiElement;

/**
 * @author Max Medvedev
 */
public class GrStepFindUsageHandlerFactory extends FindUsagesHandlerFactory {
  @Override
  public boolean canFindUsages(@Nonnull PsiElement element) {
    return GrCucumberStepDefinitionSearcher.getStepDefinition(element) != null;
  }

  @Nullable
  @Override
  public FindUsagesHandler createFindUsagesHandler(@Nonnull PsiElement element, boolean forHighlightUsages) {
    return new FindUsagesHandler(element) {};
  }
}
