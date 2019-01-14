package runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import testng.BaseReRunFeaturesTest;

@RunWith(Cucumber.class)
@CucumberOptions(
        monochrome  =   true,
        strict      =   true
)

public class ReRunTestRunner extends BaseReRunFeaturesTest {
}
