Feature: Card on file Test

  Scenario Outline: BNAK transaction with different amount
    Given id "id= " key "key= " card_number "card_number= " card_exp_date "card_exp_date= "
    When transaction_amount "<tamount>" transaction_type "transaction_type= "
    Then transaction_id should not be null
    And error_code should be "000"
    And auth_response_text should be "Approval"
    And auth_code should be present
    And card_id should be alphanumeric
    And database values should be matched with api response

    Examples: 
      | tamount                  |
      | transaction_amount=20.24 |
      | transaction_amount=21.24 |
      | transaction_amount=22.24 |
      | transaction_amount=23.24 |
      | transaction_amount=24.24 |
