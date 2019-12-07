package org.jetbrains.plugins.cucumber;

/**
 * User: Andrey.Vokin
 * Date: 1/10/13
 */
public class CucumberTestUtil {
  public static String getTestDataPath() {
    return getPluginPath() + getShortTestPath();
  }

  public static String getPluginPath() {
    return getShortPluginPath();
  }

  public static String getShortPluginPath() {
    return "/contrib/cucumber";
  }

  public static String getShortTestPath() {
    return "/testData";
  }
}
