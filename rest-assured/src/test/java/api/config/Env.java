package api.config;

public final class Env {
  public static final String BASE_URL =
      firstNonBlank(System.getenv("API_BASE_URL"), "https://api.practicesoftwaretesting.com");
  public static final String ENV_NAME = firstNonBlank(System.getenv("TEST_ENV"), "demo");

  private Env() {}

  private static String firstNonBlank(String value, String fallback) {
    return value == null || value.isBlank() ? fallback : value;
  }
}
