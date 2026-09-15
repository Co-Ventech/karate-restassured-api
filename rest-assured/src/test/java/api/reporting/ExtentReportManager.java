package api.reporting;

import api.config.Env;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

final class ExtentReportManager {
  private static final ExtentReports EXTENT = create();
  private static final ThreadLocal<ExtentTest> CURRENT = new ThreadLocal<>();

  static {
    Runtime.getRuntime().addShutdownHook(new Thread(EXTENT::flush));
  }

  private ExtentReportManager() {}

  private static ExtentReports create() {
    var spark = new ExtentSparkReporter("target/extent-report/index.html");
    spark.config().setDocumentTitle("Rest Assured API");
    spark.config().setReportName("Toolshop API — Rest Assured");
    spark.config().setTheme(Theme.STANDARD);
    var reports = new ExtentReports();
    reports.attachReporter(spark);
    reports.setSystemInfo("Environment", Env.ENV_NAME);
    reports.setSystemInfo("Base URL", Env.BASE_URL);
    return reports;
  }

  static ExtentTest createTest(String name) {
    var test = EXTENT.createTest(name);
    CURRENT.set(test);
    return test;
  }

  static ExtentTest current() {
    return CURRENT.get();
  }

  static void flush() {
    EXTENT.flush();
  }
}
