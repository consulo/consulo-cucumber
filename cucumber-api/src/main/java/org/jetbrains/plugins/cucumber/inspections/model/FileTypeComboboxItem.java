package org.jetbrains.plugins.cucumber.inspections.model;

import javax.annotation.Nonnull;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.text.StringUtil;

/**
 * User: Andrey.Vokin
 * Date: 1/3/11
 */
public class FileTypeComboboxItem {

  private final FileType myFileType;

  private final String myDefaultFileName;

  public FileTypeComboboxItem(@Nonnull final FileType fileType, @Nonnull final String defaultFileName) {
    myFileType = fileType;
    myDefaultFileName = defaultFileName;
  }

  @Override
  public String toString() {
    return StringUtil.capitalizeWords(myFileType.getId().toLowerCase(), true);
  }

  public FileType getFileType() {
    return myFileType;
  }

  public String getDefaultFileName() {
    return myDefaultFileName;
  }
}
