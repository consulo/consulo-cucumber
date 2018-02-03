package org.jetbrains.plugins.cucumber.java.run;

import java.io.File;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.cucumber.CucumberBundle;
import org.jetbrains.plugins.cucumber.CucumberUtil;
import org.jetbrains.plugins.cucumber.java.CucumberJavaBundle;
import com.intellij.diagnostic.logging.LogConfigurationPanel;
import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionBundle;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.JavaRunConfigurationExtensionManager;
import com.intellij.execution.RunConfigurationExtension;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.JavaCommandLineState;
import com.intellij.execution.configurations.JavaRunConfigurationModule;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
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
import consulo.java.execution.configurations.OwnJavaParameters;

/**
 * User: Andrey.Vokin
 * Date: 8/6/12
 */

public class CucumberJavaRunConfiguration extends ApplicationConfiguration {
  private NullableComputable<String> glueInitializer = null;

  public String GLUE;

  protected CucumberJavaRunConfiguration(String name, Project project, ConfigurationFactory factory) {
    super(name, project, factory);
  }

  @NotNull
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
      protected OwnJavaParameters createJavaParameters() throws ExecutionException {
        final OwnJavaParameters params = new OwnJavaParameters();
        final JavaRunConfigurationModule module = getConfigurationModule();

        final int classPathType = OwnJavaParameters.JDK_AND_CLASSES_AND_TESTS;
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
            getEnvironment());

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
    return state;
  }

  private static String getSMRunnerPath() {
    return PathUtil.getJarPathForClass(CucumberJvmSMFormatter.class);
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