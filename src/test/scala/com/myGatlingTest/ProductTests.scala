package com.myGatlingTest


import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class ProductTests extends Simulation {

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
    )


  setUp(
    scn.inject(atOnceUsers(1000))
  ).protocols(httpProtocol)
}
