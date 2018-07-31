package org.jetbrains.plugins.cucumber.psi;

import javax.annotation.Nonnull;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;

/**
 * @author yole
 */
public class CucumberFileTypeFactory extends FileTypeFactory {
  public void createFileTypes(@Nonnull FileTypeConsumer consumer) {
    consumer.consume(GherkinFileType.INSTANCE, "feature");
  }
}
