package org.jetbrains.plugins.cucumber.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import javax.annotation.Nonnull;

/**
 * @author yole
 */
public class GherkinElementType extends IElementType {
  public GherkinElementType(@Nonnull @NonNls String debugName) {
    super(debugName, GherkinLanguage.INSTANCE);
  }
}
