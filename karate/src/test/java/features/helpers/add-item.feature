@ignore
Feature: add one cart item

  Scenario:
    Given url baseUrl + '/carts/' + cartId
    And headers headers
    And request { product_id: '#(productId)', quantity: #(quantity) }
    When method post
    Then status 200
