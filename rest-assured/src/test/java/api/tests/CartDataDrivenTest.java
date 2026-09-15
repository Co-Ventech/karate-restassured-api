package api.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import api.client.AuthSupport;
import api.client.ShopClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class CartDataDrivenTest extends ApiTestBase {

  static Stream<Integer> quantitiesFromCsv() throws Exception {
    var path = Path.of("src/test/resources/data/quantities.csv");
    return Files.readAllLines(path).stream().skip(1).map(String::trim).filter(s -> !s.isBlank()).map(Integer::valueOf);
  }

  static Stream<Integer> quantitiesFromJson() {
    return Stream.of(1, 3);
  }

  @ParameterizedTest
  @MethodSource("quantitiesFromCsv")
  void addQuantityFromCsv(int quantity) {
    addToFreshCart(quantity);
  }

  @ParameterizedTest
  @MethodSource("quantitiesFromJson")
  void addQuantityFromJson(int quantity) {
    addToFreshCart(quantity);
  }

  private void addToFreshCart(int quantity) {
    var api = new ShopClient(AuthSupport.registerAndLogin().token());
    var products = api.get("/products", Map.of("page", 1));
    products.then().statusCode(200);
    var productId =
        products.jsonPath().getList("data").stream()
            .map(item -> (Map<?, ?>) item)
            .filter(item -> Boolean.TRUE.equals(item.get("in_stock")))
            .map(item -> String.valueOf(item.get("id")))
            .findFirst()
            .orElseThrow();

    var cartId = api.post("/carts", Map.of()).then().statusCode(201).extract().jsonPath().getString("id");
    var added = api.post("/carts/" + cartId, Map.of("product_id", productId, "quantity", quantity));
    assertTrue(List.of(200, 201).contains(added.statusCode()), "add to cart failed: " + added.asString());
  }
}
