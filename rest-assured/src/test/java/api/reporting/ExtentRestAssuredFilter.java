package api.reporting;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class ExtentRestAssuredFilter implements Filter {

  @Override
  public Response filter(
      FilterableRequestSpecification request, FilterableResponseSpecification responseSpec, FilterContext ctx) {
    var response = ctx.next(request, responseSpec);
    var test = ExtentReportManager.current();
    if (test == null) {
      return response;
    }
    test.info(request.getMethod() + " " + request.getURI() + " → HTTP " + response.statusCode());
    var body = request.getBody();
    if (body != null) {
      test.info("Request body: " + body);
    }
    var responseBody = response.getBody();
    if (responseBody != null) {
      test.info("Response body: " + responseBody.asPrettyString());
    }
    return response;
  }
}
