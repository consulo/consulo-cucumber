package cucumber.examples.java.calculator;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShoppingStepdefs {
  @When(timeout=100, value="^I subtract (\\d+)" + " from (\\d+)$")
  public void I_subtract_from(int arg1, int arg2) throws Throwable {
  }
}
