Feature: Data-driven add to cart

  Background:
    * def auth = call read('classpath:features/auth.feature')
    * def token = auth.token
    * def headers = { Authorization: 'Bearer #(token)', Content-Type: 'application/json' }

    Given url baseUrl + '/products'
    And param page = 1
    When method get
    Then status 200
    * def productId = karate.jsonPath(response, "$.data[?(@.in_stock == true)]")[0].id

    Given url baseUrl + '/carts'
    And headers headers
    And request {}
    When method post
    Then status 201
    * def cartId = response.id

  Scenario Outline: add quantity from CSV
    * def qty = quantity * 1
    Given url baseUrl + '/carts/' + cartId
    And headers headers
    And request { product_id: '#(productId)', quantity: #(qty) }
    When method post
    Then status 200

    Examples:
      | read('classpath:data/quantities.csv') |

  Scenario: add quantities from JSON
    * def rows = read('classpath:data/quantities.json')
    * def add =
      """
      function(row) {
        var result = karate.call('classpath:features/helpers/add-item.feature', { cartId: cartId, productId: productId, quantity: row.quantity, headers: headers, baseUrl: baseUrl });
        return result;
      }
      """
    * def results = karate.map(rows, add)
