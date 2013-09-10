package org.jetbrains.plugins.cucumber.java.run;

import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.*;
import com.intellij.openapi.project.Project;
import icons.CucumberJavaIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

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
      public Icon getIcon() {
        return CucumberJavaRunConfigurationType.this.getIcon();
      }

      public RunConfiguration createTemplateConfiguration(Project project) {
        return new CucumberJavaRunConfiguration(getDisplayName(), project, this);
      }

      @Override
      public void onNewConfigurationCreated(@NotNull RunConfiguration configuration) {
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
  public Icon getIcon() {
    return CucumberJavaIcons.CucumberJavaRunConfiguration;
  }

  @NotNull
  @Override
  public String getId() {
    return ID;
  }

  @Override
  public ConfigurationFactory[] getConfigurationFactories() {
    return new ConfigurationFactory[]{cucumberJavaRunConfigurationFactory};
  }
}