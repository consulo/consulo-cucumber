package org.jetbrains.plugins.cucumber.psi;

import javax.annotation.Nonnull;

import java.util.List;

/**
 * @author yole
 */
public interface GherkinScenarioOutline extends GherkinStepsHolder {
  @Nonnull
  List<GherkinExamplesBlock> getExamplesBlocks();
}
