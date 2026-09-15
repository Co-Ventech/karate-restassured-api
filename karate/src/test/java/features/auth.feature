Feature: Auth

  Scenario: register a unique user then login
    * def stamp = java.lang.System.currentTimeMillis() + '-' + Math.floor(Math.random() * 1000000)
    * def email = 'qa.api.' + stamp + '@example.com'
    * def password = 'Hyb!' + stamp + 'Qa#9'

    Given url baseUrl + '/users/register'
    And header Content-Type = 'application/json'
    And request
      """
      {
        "first_name": "Api",
        "last_name": "Tester",
        "email": "#(email)",
        "password": "#(password)",
        "dob": "1990-01-15",
        "phone": "5550100123",
        "address": {
          "street": "Test Street",
          "house_number": "12",
          "city": "Utrecht",
          "state": "UT",
          "country": "NL",
          "postal_code": "3511AB"
        }
      }
      """
    When method post
    Then status 201

    Given url baseUrl + '/users/login'
    And header Content-Type = 'application/json'
    And request { email: '#(email)', password: '#(password)' }
    When method post
    Then status 200
    And match response.access_token == '#string'
    * def token = response.access_token
