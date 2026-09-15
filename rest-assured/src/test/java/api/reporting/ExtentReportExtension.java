package api.reporting;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class ExtentReportExtension implements BeforeEachCallback, AfterAllCallback, TestWatcher {

  @Override
  public void beforeEach(ExtensionContext context) {
    var className = context.getRequiredTestClass().getSimpleName();
    ExtentReportManager.createTest(className + " · " + context.getDisplayName());
  }

  @Override
  public void testSuccessful(ExtensionContext context) {
    var test = ExtentReportManager.current();
    if (test != null) {
      test.pass("Passed");
    }
  }

  @Override
  public void testFailed(ExtensionContext context, Throwable cause) {
    var test = ExtentReportManager.current();
    if (test != null) {
      test.fail(cause);
    }
  }

  @Override
  public void testAborted(ExtensionContext context, Throwable cause) {
    var test = ExtentReportManager.current();
    if (test != null) {
      test.skip(cause);
    }
  }

  @Override
  public void afterAll(ExtensionContext context) {
    ExtentReportManager.flush();
  }
}
