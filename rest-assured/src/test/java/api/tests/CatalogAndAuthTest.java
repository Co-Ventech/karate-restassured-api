package api.tests;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertTrue;

import api.client.AuthSupport;
import api.client.ShopClient;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class CatalogAndAuthTest extends ApiTestBase {

  @Test
  void productsMatchSchema() {
    var response = new ShopClient().get("/products", Map.of("page", 1));
    response
        .then()
        .statusCode(200)
        .body("data.size()", greaterThan(0))
        .body(matchesJsonSchemaInClasspath("schemas/products.json"));
  }

  @Test
  void registerThenLoginReturnsToken() {
    var account = AuthSupport.registerAndLogin();
    assertTrue(account.token() != null && !account.token().isBlank());
  }

  @Test
  void cartCrudChained() {
    var token = AuthSupport.registerAndLogin().token();
    var api = new ShopClient(token);

    var products = api.get("/products", Map.of("page", 1));
    products.then().statusCode(200);
    var inStock =
        products.jsonPath().getList("data").stream()
            .map(item -> (Map<?, ?>) item)
            .filter(item -> Boolean.TRUE.equals(item.get("in_stock")))
            .findFirst()
            .orElseThrow();
    var productId = String.valueOf(inStock.get("id"));

    var created = api.post("/carts", Map.of());
    created.then().statusCode(201);
    var cartId = created.jsonPath().getString("id");

    api.post("/carts/" + cartId, Map.of("product_id", productId, "quantity", 1)).then().statusCode(200);

    api.get("/carts/" + cartId, null)
        .then()
        .statusCode(200)
        .body(matchesJsonSchemaInClasspath("schemas/cart.json"))
        .body("cart_items[0].product_id", equalTo(productId))
        .body("cart_items[0].quantity", equalTo(1));

    api.put("/carts/" + cartId + "/product/quantity", Map.of("product_id", productId, "quantity", 2))
        .then()
        .statusCode(200);

    api.get("/carts/" + cartId, null).then().statusCode(200).body("cart_items[0].quantity", equalTo(2));

    var deletedItem = api.delete("/carts/" + cartId + "/product/" + productId);
    assertTrue(List.of(200, 204).contains(deletedItem.statusCode()));

    var deletedCart = api.delete("/carts/" + cartId);
    assertTrue(List.of(200, 204).contains(deletedCart.statusCode()));
  }
}
