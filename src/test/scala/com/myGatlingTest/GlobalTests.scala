package com.myGatlingTest


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class GlobalTests extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8091")
    .header("Content-Type", "application/json")


  val scn = scenario("Post Product")
    .exec(
      http("Create Product")
        .post("/api/products")
        .body(StringBody(
          """{
            |"matricule":"PROD001",
            |"name":"Sample Product",
            |"description":"This is a sample product.",
            |"image":"https://example.com/sample-product-image.jpg",
            |"discount":10,
            |"price":99.99,
            |"stock":100,
            |"vendingType":"AVENDRE",
            |"active":true
            |}""".stripMargin)).asJson
        .check(status.is(200))
    ).pause(1)
    .exec(
      http("Create Command")
        .post("/api/commands")
        .body(StringBody(
          """{
            |  "shippingAddress": "123 Main St",
            |  "method": "Express",
            |  "confirmed": true,
            |  "customerFirstName": "John",
            |  "customerLastName": "Doe",
            |  "customerEmail": "john.doe@example.com",
            |  "phoneNumber": "123-456-7890",
            |  "productCommands": [
            |    {
            |      "product": {
            |        "id": 1,
            |        "matricule": "PROD001",
            |        "name": "Sample Product",
            |        "description": "This is a sample product.",
            |        "image": "https://example.com/sample-product-image.jpg",
            |        "discount": 10,
            |        "price": 99.99,
            |        "stock": 100,
            |        "vendingType": "AVENDRE",
            |        "active": true,
            |        "createdAt": "2024-05-17T11:26:26.876222100Z",
            |        "modifiedAt": "2024-05-17T11:26:26.876222100Z"
            |      },
            |      "quantity": 1,
            |      "priceTotal": 99.99
            |    }
            |  ]
            |}""".stripMargin)).asJson
        .check(status.is(201))
    )



  setUp(
    scn.inject(atOnceUsers(500))
  ).protocols(httpProtocol)
}
