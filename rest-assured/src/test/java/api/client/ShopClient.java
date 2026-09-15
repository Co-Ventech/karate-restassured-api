package api.client;

import static io.restassured.RestAssured.given;

import api.config.Env;
import api.reporting.ExtentRestAssuredFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class ShopClient {
  private final String token;

  public ShopClient() {
    this(null);
  }

  public ShopClient(String token) {
    this.token = token;
  }

  public Response get(String path, Map<String, ?> query) {
    var spec = spec(false);
    if (query != null) {
      spec = spec.queryParams(query);
    }
    return spec.get(path);
  }

  public Response post(String path, Object body) {
    var spec = spec(true);
    if (body != null) {
      spec = spec.body(body);
    }
    return spec.post(path);
  }

  public Response put(String path, Object body) {
    return spec(true).body(body).put(path);
  }

  public Response delete(String path) {
    return spec(false).delete(path);
  }

  private RequestSpecification spec(boolean jsonBody) {
    var spec = given().baseUri(Env.BASE_URL).filter(new ExtentRestAssuredFilter()).accept(ContentType.JSON);
    if (jsonBody) {
      spec = spec.contentType(ContentType.JSON);
    }
    if (token != null) {
      spec = spec.auth().oauth2(token);
    }
    return spec;
  }
}
