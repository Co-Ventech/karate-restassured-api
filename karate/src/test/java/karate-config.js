function fn() {
  var env = karate.env || java.lang.System.getenv('TEST_ENV') || 'demo';
  var config = {
    env: env,
    baseUrl: 'https://api.practicesoftwaretesting.com'
  };
  var override = java.lang.System.getenv('API_BASE_URL');
  if (override) {
    config.baseUrl = override;
  }
  karate.log('Running Karate against', config.baseUrl, 'env=', config.env);
  return config;
}
