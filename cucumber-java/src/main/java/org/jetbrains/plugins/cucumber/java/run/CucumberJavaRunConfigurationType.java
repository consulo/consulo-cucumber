package org.jetbrains.plugins.cucumber.java.run;

import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.*;
import com.intellij.openapi.project.Project;
import consulo.cucumber.java.icon.CucumberJavaIconGroup;
import consulo.ui.image.Image;
import org.jetbrains.annotations.NonNls;
import javax.annotation.Nonnull;

/**
 * User: Andrey.Vokin
 * Date: 8/6/12
 */
public class CucumberJavaRunConfigurationType implements ConfigurationType {
  @NonNls
  public static final String ID = "CucumberJavaRunConfigurationType";

  private final ConfigurationFactory cucumberJavaRunConfigurationFactory;

  public CucumberJavaRunConfigurationType() {
    cucumberJavaRunConfigurationFactory = new ConfigurationFactoryEx(this) {
      @Override
      public Image getIcon() {
        return CucumberJavaRunConfigurationType.this.getIcon();
      }

      public RunConfiguration createTemplateConfiguration(Project project) {
        return new CucumberJavaRunConfiguration(getDisplayName(), project, this);
      }

      @Override
      public void onNewConfigurationCreated(@Nonnull RunConfiguration configuration) {
        ((ModuleBasedConfiguration)configuration).onNewConfigurationCreated();
      }
    };
  }

  public static CucumberJavaRunConfigurationType getInstance() {
    return ConfigurationTypeUtil.findConfigurationType(CucumberJavaRunConfigurationType.class);
  }

  @Override
  public String getDisplayName() {
    return "Cucumber java";
  }

  @Override
  public String getConfigurationTypeDescription() {
    return "Cucumber java";
  }

  @Override
  public Image getIcon() {
    return CucumberJavaIconGroup.cucumberJavaRunConfiguration();
  }

  @Nonnull
  @Override
  public String getId() {
    return ID;
  }

  @Override
  public ConfigurationFactory[] getConfigurationFactories() {
    return new ConfigurationFactory[]{cucumberJavaRunConfigurationFactory};
  }
}