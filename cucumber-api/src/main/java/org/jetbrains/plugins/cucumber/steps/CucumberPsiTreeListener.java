package org.jetbrains.plugins.cucumber.steps;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiTreeChangeAdapter;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Andrey.Vokin
 * Date: 12/13/10
 */
public class CucumberPsiTreeListener extends PsiTreeChangeAdapter {

  private Map<PsiElement, ChangesWatcher> changesWatchersMap;

  public CucumberPsiTreeListener() {
    changesWatchersMap = new HashMap<PsiElement, ChangesWatcher>();
  }

  public void addChangesWatcher(final PsiElement parent, final ChangesWatcher changesWatcher) {
    changesWatchersMap.put(parent, changesWatcher);
  }

  private void processChange(final PsiElement parent) {
    for (Map.Entry<PsiElement, ChangesWatcher> entry : changesWatchersMap.entrySet()) {
      if (PsiTreeUtil.isAncestor(entry.getKey(), parent, false)) {
        entry.getValue().onChange(parent);
      }
    }
  }

  @Override
  public void childAdded(@Nonnull PsiTreeChangeEvent event) {
    processChange(event.getParent());
  }

  public void childRemoved(@Nonnull final PsiTreeChangeEvent event) {
    processChange(event.getParent());
  }

  public void childReplaced(@Nonnull final PsiTreeChangeEvent event) {
    processChange(event.getParent());
  }

  public void childrenChanged(@Nonnull final PsiTreeChangeEvent event) {
    processChange(event.getParent());
  }

  public void childMoved(@Nonnull final PsiTreeChangeEvent event) {
    processChange(event.getOldParent());
    processChange(event.getNewParent());
  }

  public interface ChangesWatcher {
    void onChange(final PsiElement parentPsiElement);
  }
}
