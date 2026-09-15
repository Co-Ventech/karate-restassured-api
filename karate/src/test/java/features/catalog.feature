Feature: Catalog schema

  Scenario: GET /products matches catalog schema
    Given url baseUrl + '/products'
    And param page = 1
    When method get
    Then status 200
    And match response == '#object'
    And match response.data == '#[]'
    And match response.data[0] contains { id: '#string', name: '#string', price: '#number', in_stock: '#boolean' }
