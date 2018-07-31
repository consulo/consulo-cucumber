package org.jetbrains.plugins.cucumber.inspections;

import javax.annotation.Nonnull;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.cucumber.psi.GherkinTable;
import org.jetbrains.plugins.cucumber.psi.GherkinTableRow;

/**
 * @author yole
 */
public class RemoveTableColumnFix implements LocalQuickFix {
  private final GherkinTable myTable;
  private final int myColumnIndex;

  public RemoveTableColumnFix(GherkinTable table, int columnIndex) {
    myTable = table;
    myColumnIndex = columnIndex;
  }

  @Nonnull
  public String getName() {
    return "Remove unused column";
  }

  @Nonnull
  public String getFamilyName() {
    return "RemoveTableColumnFix";
  }

  public void applyFix(@Nonnull Project project, @Nonnull ProblemDescriptor descriptor) {
    final GherkinTableRow headerRow = myTable.getHeaderRow();
    if (headerRow != null) {
      headerRow.deleteCell(myColumnIndex);
    }
    for (GherkinTableRow row : myTable.getDataRows()) {
      row.deleteCell(myColumnIndex);
    }
  }
}
