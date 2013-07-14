package org.jetbrains.plugins.cucumber.java.run;

import com.intellij.diagnostic.logging.LogConfigurationPanel;
import com.intellij.execution.*;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.configurations.*;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.testframework.sm.SMRunnerUtil;
import com.intellij.execution.testframework.sm.SMTestRunnerConnectionUtil;
import com.intellij.execution.testframework.sm.runner.SMTRunnerConsoleProperties;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.util.JavaParametersUtil;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.options.SettingsEditorGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NullableComputable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.PathUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.cucumber.CucumberBundle;
import org.jetbrains.plugins.cucumber.CucumberUtil;
import org.jetbrains.plugins.cucumber.java.CucumberJavaBundle;

import java.io.File;

/**
 * User: Andrey.Vokin
 * Date: 8/6/12
 */

public class CucumberJavaRunConfiguration extends ApplicationConfiguration {
  private NullableComputable<String> glueInitializer = null;

  public String GLUE;

  public CucumberJavaRunConfiguration(String name, Project project, CucumberJavaRunConfigurationType applicationConfigurationType) {
    super(name, project, applicationConfigurationType);
  }

  protected CucumberJavaRunConfiguration(String name, Project project, ConfigurationFactory factory) {
    super(name, project, factory);
  }

  protected ModuleBasedConfiguration createInstance() {
    return new CucumberJavaRunConfiguration(getName(), getProject(), CucumberJavaRunConfigurationType.getInstance());
  }

  @Override
  public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    SettingsEditorGroup<CucumberJavaRunConfiguration> group = new SettingsEditorGroup<CucumberJavaRunConfiguration>();
    group.addEditor(ExecutionBundle.message("run.configuration.configuration.tab.title"), new CucumberJavaApplicationConfigurable(getProject()));
    JavaRunConfigurationExtensionManager.getInstance().appendEditors(this, group);
    group.addEditor(ExecutionBundle.message("logs.tab.title"), new LogConfigurationPanel<CucumberJavaRunConfiguration>());
    return group;
  }

  @Override
  public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws ExecutionException {
    final JavaCommandLineState state = new JavaApplicationCommandLineState(this, env) {
      protected JavaParameters createJavaParameters() throws ExecutionException {
        final JavaParameters params = new JavaParameters();
        final JavaRunConfigurationModule module = getConfigurationModule();

        final int classPathType = JavaParameters.JDK_AND_CLASSES_AND_TESTS;
        final String jreHome = CucumberJavaRunConfiguration.this.ALTERNATIVE_JRE_PATH_ENABLED ? ALTERNATIVE_JRE_PATH : null;
        JavaParametersUtil.configureModule(module, params, classPathType, jreHome);
        JavaParametersUtil.configureConfiguration(params, CucumberJavaRunConfiguration.this);

        String path = getSMRunnerPath();
        params.getClassPath().add(path);

        params.setMainClass(MAIN_CLASS_NAME);
        for (RunConfigurationExtension ext : Extensions.getExtensions(RunConfigurationExtension.EP_NAME)) {
          ext.updateJavaParameters(CucumberJavaRunConfiguration.this, params, getRunnerSettings());
        }

        final String glueValue = getGlue();
        if (glueValue != null && !StringUtil.isEmpty(glueValue)) {
          final String[] glues = glueValue.split(" ");
          for (String glue : glues) {
            if (!StringUtil.isEmpty(glue)) {
              params.getProgramParametersList().addParametersString(" --glue " + glue);
            }
          }

        }
        return params;
      }

      @Nullable
      private ConsoleView createConsole(@NotNull final Executor executor, ProcessHandler processHandler) throws ExecutionException {
        // console view
        final ConsoleView testRunnerConsole;

        final String testFrameworkName = "cucumber";
        final CucumberJavaRunConfiguration runConfiguration = CucumberJavaRunConfiguration.this;
        final SMTRunnerConsoleProperties consoleProperties = new SMTRunnerConsoleProperties(runConfiguration, testFrameworkName, executor);

        testRunnerConsole = SMTestRunnerConnectionUtil.createAndAttachConsole(testFrameworkName, processHandler, consoleProperties,
                                                                              getRunnerSettings(),
                                                                              getConfigurationSettings());

        return testRunnerConsole;
      }

      @NotNull
      @Override
      public ExecutionResult execute(@NotNull Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
        final ProcessHandler processHandler = startProcess();
        final ConsoleView console = createConsole(executor, processHandler);
        return new DefaultExecutionResult(console, processHandler, createActions(console, processHandler, executor));
      }
    };
    state.setConsoleBuilder(TextConsoleBuilderFactory.getInstance().createBuilder(getProject()));
    return state;
  }

  private static String getSMRunnerPath() {
    return PathUtil.getJarPathForClass(SMRunnerUtil.class);
  }

  @Override
  public void checkConfiguration() throws RuntimeConfigurationException {
    final String featureFileOrFolder = CucumberUtil.getFeatureFileOrFolderNameFromParameters(getProgramParameters());
    if (featureFileOrFolder == null) {
      throw new RuntimeConfigurationException(CucumberBundle.message("cucumber.run.error.specify.file"));
    } else if (!(new File(featureFileOrFolder)).exists()) {
      throw new RuntimeConfigurationException(CucumberBundle.message("cucumber.run.error.file.doesnt.exist"));
    }
    else if (StringUtil.isEmpty(getGlue())) {
      throw new RuntimeConfigurationException(CucumberJavaBundle.message("cucumber.java.run.configuration.glue.mustnt.be.empty"));
    }

    if (getProgramParameters().contains("--glue")) {
      throw new RuntimeConfigurationException(CucumberJavaBundle.message("cucumber.java.run.configuration.glue.in.program.parameters"));
    }

    super.checkConfiguration();
  }

  @Nullable
  public String getGlue() {
    if (glueInitializer != null) {
      GLUE = glueInitializer.compute();
      glueInitializer = null;
    }

    return GLUE;
  }

  public void setGlue(String value) {
    GLUE = value;
    glueInitializer = null;
  }

  public void setGlue(NullableComputable<String> value) {
    glueInitializer = value;
  }
}
