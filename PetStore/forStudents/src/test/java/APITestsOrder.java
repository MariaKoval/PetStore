package test.java;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import main.java.data.Statuses;
import main.java.data.responses.Responses;
import main.java.entities.Category;
import main.java.entities.Order;
import main.java.entities.Pet;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static io.restassured.RestAssured.given;

public class APITestsOrder {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @After
    public void tearDown() {
    }


    @Test
    public void addOrderToTheStore() {
//        Preparing test data
        Category cats = new Category(2, "cats");
        Category siamese = new Category(1, "siamese");

        Pet newPet = new Pet(
                1 + (int) (Math.random() * 10),
                cats,
                "Fluffy " + RandomStringUtils.randomAlphabetic(5),
                Collections.singletonList("urls"),
                Arrays.asList(cats, siamese),
                Statuses.SOLD.name());
        Response responseAddPet = given()
                .basePath("/pet")
                .contentType(ContentType.JSON)
                .body(newPet)
                .post();

        Assert.assertEquals("Wrong status code", 200, responseAddPet.getStatusCode());

        Order newOrder = new Order(
                 1 + (int) (Math.random() * 10),
                newPet.getId(),
                1,
                LocalDateTime.now().toString(),
                "placed",
                true);

//        Tests
        Order responseNewOrder = given()
                .basePath("/store/order")
                .contentType(ContentType.JSON)
                .body(newOrder)
                .post()
                .as(Order.class);

        Assert.assertEquals("Wrong complete value:", true, responseNewOrder.getComplete());
        System.out.println("Response for adding an order: \n" + responseNewOrder.toString() + "\n"); // log info
//        Responses addedOrder = responseNewOrder.as(Responses.class);

        Order foundOrderById = given()
                .pathParam("orderId", newOrder.getId())
                .basePath("/store/order/{orderId}")
                .accept("application/json")
                .when()
                .get()
                .as(Order.class);
        System.out.println("Response for getting order by Id: \n" + foundOrderById.toString()); // log info

        // final assert
        Assert.assertEquals("Wrong petId", responseNewOrder.getPetId(), foundOrderById.getPetId());

        // удалить свой order в конце теста, чтобы не засорять базу
        Response deleteResponseOrder =
                given()
                        .pathParam("orderId", newOrder.getId())
                        .basePath("/store/order/{orderId}")
                        .accept("application/json")
                        .when()
                        .delete();
        System.out.println(deleteResponseOrder.asString());

        Responses responsesAsClass = deleteResponseOrder.as(Responses.class);

        Assert.assertEquals("Code is wrong", 200, responsesAsClass.getCode());
        Assert.assertNotNull("Field is null", responsesAsClass.getType());
        Assert.assertEquals("Message is wrong", newOrder.getId(), Integer.parseInt(responsesAsClass.getMessage()));
    }

    @Test
    public void validationJSONSchemaOfResponseGetPet() {
        //        Preparing test data
        Category cats = new Category(129, "cats");
        Category siamese = new Category(111, "siamese");

        Pet newPet = new Pet(
                1 + (int) (Math.random() * 10),
                cats,
                "Little " + RandomStringUtils.randomAlphabetic(5),
                Collections.singletonList("urls"),
                Arrays.asList(cats, siamese),
                Statuses.SOLD.name());

        Order newOrder = new Order(
                1 + (int) (Math.random() * 10),
                newPet.getId(),
                1,
                LocalDateTime.now().toString(),
                "placed",
                false);

//        Tests
        Order responseAddOrder = given()
                .basePath("/store/order")
                .contentType(ContentType.JSON)
                .body(newOrder)
                .post()
                .as(Order.class);

        given()
                .basePath("/store/order/" + responseAddOrder.getId())
                .accept("application/json")
                .get()
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("main/java/data/json/schemaOrder.json"));
    }

}