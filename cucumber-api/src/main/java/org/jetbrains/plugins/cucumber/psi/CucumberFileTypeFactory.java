package org.jetbrains.plugins.cucumber.psi;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;

/**
 * @author yole
 */
public class CucumberFileTypeFactory extends FileTypeFactory {
  public void createFileTypes(@NotNull FileTypeConsumer consumer) {
    consumer.consume(GherkinFileType.INSTANCE, "feature");
  }
}
