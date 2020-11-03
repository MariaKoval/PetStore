package test.java;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import main.java.data.responses.Responses;
import main.java.entities.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static io.restassured.RestAssured.given;

public class APITestsUser {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @After
    public void tearDown() {
    }


    @Test
    public void addUserToTheStore() {


        User newUser = new User(
                100000 + (long) (Math.random() * 999999),
                "Mike1990",
                "Mike",
                "Smith",
                "test@gmail.com",
                "qwerty100100",
                "+380502233456",
                1);

//        Tests
        Response responseAddUser = given()
                .basePath("/user")
                .contentType(ContentType.JSON)
                .body(newUser)
                .post();

        Assert.assertEquals("Wrong status code", 200, responseAddUser.getStatusCode());
        System.out.println("Response for adding a new user: \n" + responseAddUser.asString() + "\n"); // log info
//        Responses addedUser = responseAddUser.as(Responses.class);

        User foundUserByUsername = given()
                .pathParam("username", newUser.getUsername())
                .basePath("/user/{username}")
                .accept("application/json")
                .when()
                .get()
                .as(User.class);
        System.out.println("Response for getting user by username: \n" + foundUserByUsername.toString()); // log info

        // final assert
        Assert.assertEquals("Wrong email", newUser.getEmail(), foundUserByUsername.getEmail());

        // удалить user в конце теста, чтобы не засорять базу
        Response deleteResponseUser =
                given()
                        .pathParam("username", newUser.getUsername())
                        .basePath("/user/{username}")
                        .accept("application/json")
                        .when()
                        .delete();
        System.out.println(deleteResponseUser.asString());

        Responses deleteResponseUserAsClass = deleteResponseUser.as(Responses.class);

        Assert.assertEquals("Code is wrong", 200, deleteResponseUserAsClass.getCode());
        Assert.assertNotNull("Field is null", deleteResponseUserAsClass.getType());
        Assert.assertNotNull("Message is wrong", deleteResponseUserAsClass.getMessage());
    }

    @Test
    public void loginUser() {

        User newUser = new User(
                100000 + (long) (Math.random() * 999999),
                "Senior",
                "Petr",
                "Lucky",
                "test22@gmail.com",
                "qwerty",
                "+380967564534",
                1);

//        Tests
        Response responseAddUser = given()
                .basePath("/user")
                .contentType(ContentType.JSON)
                .body(newUser)
                .post();

        Assert.assertEquals("Wrong status code", 200, responseAddUser.getStatusCode());
        System.out.println("Response for adding a new user: \n" + responseAddUser.asString() + "\n"); // log info
//        Responses addedUser = responseAddUser.as(Responses.class);

        Responses loginUser = given()
                .queryParam("username", newUser.getUsername())
                .queryParam("password", newUser.getPassword())
                .basePath("/user/login?username=Senior&password=qwerty")
                .accept("application/json")
                .when()
                .get()
                .as(Responses.class);
        System.out.println("Response for getting user by username: \n" + loginUser.toString()); // log info

        // final assert
        Assert.assertEquals("Wrong status code", 200, loginUser.getCode());
        Assert.assertEquals("Message has problem", true, loginUser.getMessage().startsWith("logged"));

        // удалить user в конце теста, чтобы не засорять базу
        Response deleteResponseUser =
                given()
                        .pathParam("username", newUser.getUsername())
                        .basePath("/user/{username}")
                        .accept("application/json")
                        .when()
                        .delete();
        System.out.println(deleteResponseUser.asString());

        Responses deleteResponseUserAsClass = deleteResponseUser.as(Responses.class);

        Assert.assertEquals("Code is wrong", 200, deleteResponseUserAsClass.getCode());
        Assert.assertNotNull("Field is null", deleteResponseUserAsClass.getType());
        Assert.assertNotNull("Message is wrong", deleteResponseUserAsClass.getMessage());
    }

    @Test
    public void validationJSONSchemaOfResponseGetUser() {

        User newUser = new User(
                100000 + (long) (Math.random() * 999999),
                "Mike1990",
                "Mike",
                "Smith",
                "test@gmail.com",
                "qwerty100100",
                "+380502233456",
                1);

//        Tests
        Responses responseAddUser = given()
                .basePath("/user")
                .contentType(ContentType.JSON)
                .body(newUser)
                .post()
                .as(Responses.class);

        Assert.assertEquals("Code is wrong", 200, responseAddUser.getCode());

        given()
                .basePath("/user" + responseAddUser.getCode())
                .accept("application/json")
                .get()
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("main/java/data/json/schemaUser.json"));
    }
}