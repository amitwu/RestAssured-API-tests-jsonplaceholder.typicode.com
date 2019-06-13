package config;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;

import io.restassured.specification.RequestSpecification;
import io.restassured.builder.RequestSpecBuilder;

public class TestConfig {


    public static RequestSpecification baseRequestSpec;
    public static ResponseSpecification successResponse;
    public static ResponseSpecification notFoundResponse;
  //  public static ResponseSpecification createdResponse;

    @BeforeClass
    public static void setup(){


      //Define default base request specification parameters e.g.
        // 1. server name
        // 2. header that
        // This should match all the test cases
        baseRequestSpec = new RequestSpecBuilder()
               .setBaseUri("https://jsonplaceholder.typicode.com/")
               .addHeader("Content-Type","application/json")
              .addHeader("Server ", "cloudflare")
               .build();
        RestAssured.requestSpecification = baseRequestSpec;

        //Success Response
        successResponse = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectHeader("Content-Type","application/json; charset=utf-8")
                .build();
        RestAssured.responseSpecification = successResponse;

     /*   //Created Response
        createdResponse = new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectHeader("Content-Type","application/json; charset=utf-8")
                .build();
        RestAssured.responseSpecification = createdResponse;*/

        //Not found response
       notFoundResponse = new ResponseSpecBuilder()
                .expectStatusCode(404)
                .expectHeader("Content-Type","application/json; charset=utf-8")
                .build();
        RestAssured.responseSpecification = notFoundResponse;




    }

}
