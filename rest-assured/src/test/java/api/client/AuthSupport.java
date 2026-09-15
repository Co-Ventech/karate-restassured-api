package api.client;

import java.util.Map;
import java.util.UUID;

public class AuthSupport {
  public record Account(String email, String password, String token) {}

  public static Account registerAndLogin() {
    var stamp = System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8);
    var email = "qa.api." + stamp + "@example.com";
    var password = "Hyb!" + stamp + "Qa#9";
    var guest = new ShopClient();

    var register =
        guest.post(
            "/users/register",
            Map.of(
                "first_name", "Api",
                "last_name", "Tester",
                "email", email,
                "password", password,
                "dob", "1990-01-15",
                "phone", "5550100123",
                "address",
                    Map.of(
                        "street", "Test Street",
                        "house_number", "12",
                        "city", "Utrecht",
                        "state", "UT",
                        "country", "NL",
                        "postal_code", "3511AB")));
    register.then().statusCode(201);

    var login = guest.post("/users/login", Map.of("email", email, "password", password));
    login.then().statusCode(200);
    return new Account(email, password, login.jsonPath().getString("access_token"));
  }
}
