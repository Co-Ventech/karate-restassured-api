Feature: Cart CRUD chained

  Background:
    * def auth = call read('classpath:features/auth.feature')
    * def token = auth.token
    * def headers = { Authorization: 'Bearer #(token)', Content-Type: 'application/json' }

    Given url baseUrl + '/products'
    And param page = 1
    When method get
    Then status 200
    * def product = karate.jsonPath(response, "$.data[?(@.in_stock == true)]")[0]
    * def productId = product.id

  Scenario: create, read, update, delete cart line then cart
    Given url baseUrl + '/carts'
    And headers headers
    And request {}
    When method post
    Then status 201
    And match response.id == '#string'
    * def cartId = response.id

    Given url baseUrl + '/carts/' + cartId
    And headers headers
    And request { product_id: '#(productId)', quantity: 1 }
    When method post
    Then status 200

    Given url baseUrl + '/carts/' + cartId
    And headers headers
    When method get
    Then status 200
    And match response.id == cartId
    And match response.cart_items[0].product_id == productId
    And match response.cart_items[0].quantity == 1

    Given url baseUrl + '/carts/' + cartId + '/product/quantity'
    And headers headers
    And request { product_id: '#(productId)', quantity: 2 }
    When method put
    Then status 200

    Given url baseUrl + '/carts/' + cartId
    And headers headers
    When method get
    Then status 200
    And match response.cart_items[0].quantity == 2

    Given url baseUrl + '/carts/' + cartId + '/product/' + productId
    And headers headers
    When method delete
    * match [200, 204] contains responseStatus

    Given url baseUrl + '/carts/' + cartId
    And headers headers
    When method delete
    * match [200, 204] contains responseStatus
