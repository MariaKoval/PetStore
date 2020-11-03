package test.java;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import main.java.data.Statuses;

import main.java.data.responses.Responses;
import main.java.entities.Category;
import main.java.entities.Pet;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static java.lang.Long.valueOf;
import static main.java.data.Statuses.SOLD;
import static org.hamcrest.Matchers.equalTo;

public class APITestsPet {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @After
    public void tearDown() {
    }

//    @Test
//    public void getInfoAboutDartVader() {
//        given().baseUri("https://swapi.dev/api")
//                .basePath("people")
//                .when()
//                .get("4")
//                .then()
//                .statusCode(200)
//                .and()
//                .contentType(ContentType.JSON)
//                .and()
//                .log()
//                .all()
//                .and()
//                .body("name", equalTo("Darth Vader"));
//    }

    @Test
    public void addPetToTheStore() {
//        Preparing test data
        Category dogs = new Category(1, "dogs");
        Category patrol = new Category(43, "patrol");

        Pet newPet = new Pet(
                1 + (int) (Math.random() * 10),
                dogs,
                "Crazy " + RandomStringUtils.randomAlphabetic(5),
                Collections.singletonList("urls"),
                Arrays.asList(dogs, patrol),
                Statuses.AVAILABLE.name());

//        Tests
        Response responseAddPet = given()
                .basePath("/pet")
                .contentType(ContentType.JSON)
                .body(newPet)
                .post();

        Assert.assertEquals("Wrong status code", 200, responseAddPet.getStatusCode());
        System.out.println("Response for adding a new pet: \n" + responseAddPet.asString() + "\n"); // log info
        Pet addedPet = responseAddPet.as(Pet.class);

        Pet foundPetById = given()
                .pathParam("Id", addedPet.getId())
                .basePath("/pet/{Id}")
                .accept("application/json")
                .when()
                .get()
                .as(Pet.class);
        System.out.println("Response for getting pet by Id: \n" + foundPetById.toString()); // log info

        // final assert
        Assert.assertEquals("Wrong name", addedPet.getName(), foundPetById.getName());

        // удалить своего питомца в конце теста, чтобы не засорять базу
        Response deleteResponsePet =
                given()
                        .pathParam("Id", addedPet.getId())
                        .basePath("/pet/{Id}")
                        .accept("application/json")
                        .when()
                        .delete();
        System.out.println(deleteResponsePet.asString());

        Responses deleteResponsePetAsClass = deleteResponsePet.as(Responses.class);

        Assert.assertEquals("Code is wrong", 200, deleteResponsePetAsClass.getCode());
        Assert.assertNotNull("Field is null", deleteResponsePetAsClass.getType());
        Assert.assertEquals("Message is wrong", addedPet.getId(), Long.parseLong(deleteResponsePetAsClass.getMessage()));
    }


    @Test
    public void updatePetNameAndStatus() {
//        Preparing test data
        Category fishes = new Category(3, "fishes");
        Category goldFish = new Category(50, "gold fish");


        Pet newPet = new Pet(
                500 + (int) (Math.random() * 1000),
                fishes,
                "Little " + RandomStringUtils.randomAlphabetic(4),
                Collections.singletonList("urls"),
                Arrays.asList(fishes, goldFish),
                Statuses.AVAILABLE.name());

//        Tests
        Response responseAddPet = given()
                .basePath("/pet")
                .contentType(ContentType.JSON)
                .body(newPet)
                .post();

        Assert.assertEquals("Wrong status code", 200, responseAddPet.getStatusCode());
        System.out.println("Response for adding a new pet: \n" + responseAddPet.asString() + "\n"); // log info
        Pet addedPet = responseAddPet.as(Pet.class);

        Pet updatedPet = new Pet(
                newPet.getId(),
                fishes,
                "Big " + RandomStringUtils.randomAlphabetic(4),
                Collections.singletonList("urls"),
                Arrays.asList(fishes, goldFish),
                SOLD.name());

        Response updatePet = given()
                .basePath("/pet")
                .contentType(ContentType.JSON)
                .body(updatedPet)
                .put();

        Pet foundUpdatedPetById = given()
                .pathParam("petId", updatedPet.getId())
                .basePath("/pet/{petId}")
                .accept("application/json")
                .when()
                .get()
                .as(Pet.class);
        System.out.println("Response for getting pet by Id: \n" + foundUpdatedPetById.toString()); // log info

        // final assert
        Assert.assertNotEquals("Something wrong with name", newPet.getName(), foundUpdatedPetById.getName());
        Assert.assertNotEquals("Something wrong with status", newPet.getStatus(), foundUpdatedPetById.getStatus());

        // удалить своего питомца в конце теста, чтобы не засорять базу
        Response deleteResponsePet =
                given()
                        .pathParam("Id", addedPet.getId())
                        .basePath("/pet/{Id}")
                        .accept("application/json")
                        .when()
                        .delete();
        System.out.println(deleteResponsePet.asString());

        Responses deleteResponsePetAsClass = deleteResponsePet.as(Responses.class);

        Assert.assertEquals("Code is wrong", 200, deleteResponsePetAsClass.getCode());
        Assert.assertNotNull("Field is null", deleteResponsePetAsClass.getType());
        Assert.assertEquals("Message is wrong", addedPet.getId(), Long.parseLong(deleteResponsePetAsClass.getMessage()));
    }


    @Test
    public void getDeletedPet() {
//        Preparing test data
        Category parrots = new Category(66, "parrots");
        Category ara = new Category(65, "ara");

        Pet newPet = new Pet(
                500 + (int) (Math.random() * 1000),
                parrots,
                "Little " + RandomStringUtils.randomAlphabetic(4),
                Collections.singletonList("urls"),
                Arrays.asList(parrots, ara),
                Statuses.AVAILABLE.name());

//        Tests
        Response responseAddPet = given()
                .basePath("/pet")
                .contentType(ContentType.JSON)
                .body(newPet)
                .post();

        Assert.assertEquals("Wrong status code", 200, responseAddPet.getStatusCode());
        System.out.println("Response for adding a new pet: \n" + responseAddPet.asString() + "\n"); // log info
        Pet addedPet = responseAddPet.as(Pet.class);

        // удалить своего питомца
        Response deleteResponsePet =
                given()
                        .pathParam("Id", addedPet.getId())
                        .basePath("/pet/{Id}")
                        .accept("application/json")
                        .when()
                        .delete();
        System.out.println(deleteResponsePet.asString());

        Responses deleteResponsePetAsClass = deleteResponsePet.as(Responses.class);

        Assert.assertEquals("Code is wrong", 200, deleteResponsePetAsClass.getCode());
        Assert.assertNotNull("Field is null", deleteResponsePetAsClass.getType());
        Assert.assertEquals("Message is wrong", addedPet.getId(), Long.parseLong(deleteResponsePetAsClass.getMessage()));


        Responses getDeletedPetById = given()
                .pathParam("Id", newPet.getId())
                .basePath("/pet/{Id}")
                .accept("application/json")
                .when()
                .get()
                .as(Responses.class);
        System.out.println("Response for getting deleted pet by Id: \n" + getDeletedPetById.toString()); // log info


        // final assert
        Assert.assertNotEquals("Wrong status code", 500, getDeletedPetById.getCode());
    }

    @Test
    public void findPetByStatus() {
//        Preparing test data
        Category snakes = new Category(66, "snakes");
        Category anaconda = new Category(65, "anaconda");

        Pet newPet = new Pet(
                500 + (int) (Math.random() * 1000),
                snakes,
                "Dangerous " + RandomStringUtils.randomAlphabetic(10),
                Collections.singletonList("urls"),
                Arrays.asList(snakes, anaconda),
                SOLD.name());

//        Tests
        Response responseAddPet = given()
                .basePath("/pet")
                .contentType(ContentType.JSON)
                .body(newPet)
                .post();

        Assert.assertEquals("Wrong status code", 200, responseAddPet.getStatusCode());
        System.out.println("Response for adding a new pet: \n" + responseAddPet.asString() + "\n"); // log info
        Pet addedPet = responseAddPet.as(Pet.class);

        // получить всех питомцев со статусом SOLD
        Pet getSoldPets = (Pet) given()
                .queryParam("status ", newPet.getStatus())
                .basePath("/pet/findByStatus")
                .accept("application/json")
                .when()
                .get()
                .as(Pet.class);


        System.out.println("Response for getting all SOLD pet by status: \n" + getSoldPets.toString()); // log info


        // final assert
        Assert.assertEquals("Something wrong with pet id", newPet.getId(), getSoldPets.getId());
        Assert.assertEquals("Something wrong with pet name", newPet.getName(), getSoldPets.getName());

    }

    @Test
    public void validationJSONSchemaOfResponseGetPet() {
        //        Preparing test data
        Category dogs = new Category(1, "dogs");
        Category patrol = new Category(43, "patrol");

        Pet newPet = new Pet(
                1 + (int) (Math.random() * 10),
                dogs,
                "Fluffy " + RandomStringUtils.randomAlphabetic(5),
                Collections.singletonList("urls"),
                Arrays.asList(dogs, patrol),
                Statuses.AVAILABLE.name());

//        Tests
        Pet responseAddPet = given()
                .basePath("/pet")
                .contentType(ContentType.JSON)
                .body(newPet)
                .post()
                .as(Pet.class);

        given()
                .basePath("/pet/" + responseAddPet.getId())
                .accept("application/json")
                .get()
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("main/java/data/json/schemaPet.json"));
    }

}