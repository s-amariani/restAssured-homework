package Steps;

import Models.requests.CreateUserRequest;
import Models.responses.CreateUserResponse;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class CreateUserStep {
    public void createUser(){
        baseURI = "https://reqres.in/api/users";
        CreateUserRequest request = new CreateUserRequest();
        request.setName("morpheus");
        request.setJob("leader");

        CreateUserResponse createUserResponse = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post()
                .then()
                .statusCode(201)
                .extract().response().as(CreateUserResponse.class);
    }
}
