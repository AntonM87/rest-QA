package in.reqres;


import data.People;
import data.PeopleCreated;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;

public class APITests {
    @Test
    public void test() {
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .body("page",notNullValue())
                .body("per_page", notNullValue())
                .body("total", notNullValue())
                .body("total_pages", notNullValue())
                .body("data.id",not(hasItem(nullValue())))
                .body("data.first_name", hasItem("Lindsay"))
                .statusCode(200);
    }
    @Test
    public void secondTest(){
        Map<String,String> requestData = new HashMap<>();
        requestData.put("name","Kirill");
        requestData.put("job","teacher");
        Response response = given()
                .contentType("application/json")
                .body(requestData)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().body()
                .statusCode(201)
                .extract().response();
        JsonPath jsonResponse = response.jsonPath();
        Assert.assertEquals(jsonResponse.get("name"),requestData.get("name"),
                "Ожидали создание пользователя с именем " + requestData.get("name") +
                        ", создался с именем " + jsonResponse.get("name"));
        Assert.assertEquals(jsonResponse.get("job"),requestData.get("job"),
                "Ожидали создание пользователя с именем " + requestData.get("job") +
                        ", создался с именем " + jsonResponse.get("job"));
    }
    @Test
    public void apiTest(){
        People people = new People("Anton","enginer");
        PeopleCreated peopleCreated = given()
                .contentType("application/json")
                .body(people)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().body()
                .statusCode(201)
                .extract().body().as(PeopleCreated.class);
        System.out.println("-----------------------------");
        System.out.println(peopleCreated.getCreatedAt());
    }
}
