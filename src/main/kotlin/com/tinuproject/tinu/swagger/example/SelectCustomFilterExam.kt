package com.tinuproject.tinu.swagger.example

class SelectCustomFilterExam {
    companion object {
        const val EXAMPLE_CUSTOM_FILTER_RESPONSE = """
        {
          "success": true,
          "stateCode": 200,
          "result": [
            {
              "filterId": 0,
              "filterName": "전자제품",
              "category": [1,2],
              "maxPrice": 100000,
              "minPrice": 1000,
              "onlySell": true
            },
            {
              "filterId": 1,
              "filterName": "의류",
              "category": [3],
              "maxPrice": 50000,
              "minPrice": 5000,
              "onlySell": false
            }
          ]
        }
    """
    }
}