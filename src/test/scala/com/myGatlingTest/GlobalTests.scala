package com.myGatlingTest

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class GlobalTests extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8091")
    .header("Content-Type", "application/json")

  var counter = 1

  val scn = scenario("Global Test")
    .exec(session => {
      println(s" Run jenkins 3")
      val matricule = java.util.UUID.randomUUID().toString.take(8)
      session.set("matricule", matricule)


    })
    .exec(session => {
      counter += 1
      session.set("id", counter.toString);

    })
    .exec(
      http("Create Product")
        .post("/products")
        .body(StringBody(
          session =>
            s"""{
               |"matricule":"${session("matricule").as[String]}",
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
        .post("/commands")
        .body(StringBody(
          session =>
            s"""{
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
               |        "id": ${session("id").as[String]},
               |        "matricule": "${session("matricule").as[String]}",
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
