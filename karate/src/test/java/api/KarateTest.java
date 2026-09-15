package api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

class KarateTest {

  @Test
  void runAll() {
    Results results =
        Runner.path("classpath:features")
            .tags("~@ignore")
            .outputCucumberJson(true)
            .outputHtmlReport(true)
            .reportDir("target/karate-reports")
            .parallel(1);
    generateCucumberReport(results.getReportDir());
    assertEquals(0, results.getFailCount(), results.getErrorMessages());
  }

  private static void generateCucumberReport(String karateOutputPath) {
    Collection<File> jsonFiles = FileUtils.listFiles(new File(karateOutputPath), new String[] {"json"}, true);
    if (jsonFiles.isEmpty()) {
      return;
    }
    List<String> jsonPaths = new ArrayList<>();
    jsonFiles.forEach(file -> jsonPaths.add(file.getAbsolutePath()));
    Configuration config = new Configuration(new File("target"), "Karate API");
    config.setBuildNumber("1.0.0");
    config.addClassifications("Project", "karate-restassured-api");
    config.addClassifications("Tool", "Karate");
    new ReportBuilder(jsonPaths, config).generateReports();
  }
}
