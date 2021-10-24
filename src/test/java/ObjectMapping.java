import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojos.CreateUserRequest;
import pojos.CreateUserResponse;
import pojos.RegisterFailPojo;
import pojos.RegisterSuccessPojo;

import static io.restassured.RestAssured.*;

public class ObjectMapping {

    private static final RequestSpecification REQ_SPEC =
            new RequestSpecBuilder()
                    .setBaseUri("https://reqres.in/api")
                    .setContentType(ContentType.JSON)
                    .build();


    @Test
    public void registerSuccess() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "eve.holt@reqres.in");
        requestParams.put("password", "pistol");

        basePath = "/register";

        Response response = given()
                .spec(REQ_SPEC)
                .body(requestParams.toJSONString())
                .post()
                .then()
                .extract().response();

        //Deserialize JSON based on status code
        if (response.statusCode() == 200) {
            RegisterSuccessPojo registerSuccessPojo = response.as(RegisterSuccessPojo.class);
            //Validate id and token values
            Assert.assertEquals(registerSuccessPojo.getId(),4);
            Assert.assertEquals(registerSuccessPojo.getToken(),"QpwL5tke4Pnpja7X4");
        }
    }

    @Test
    public void registerFail() {
        JSONObject requestParams = new JSONObject();
        requestParams.put("email", "sydney@fife");


        basePath = "/register";
        Response response = given()
                .spec(REQ_SPEC)
                .body(requestParams.toJSONString())
                .post()
                .then()
                .extract().response();

        //Deserialize JSON based on status code
        if (response.statusCode() == 400){
            RegisterFailPojo registerFailPojo = response.as(RegisterFailPojo.class);
            //Validate error
            Assert.assertEquals(registerFailPojo.getError(),"Missing password");
        }
    }

    @Test
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


        System.out.println(createUserResponse.getName() + "\n" + createUserResponse.getJob() + "\n" + createUserResponse.getId() + "\n" + createUserResponse.getCreatedAt());

    }
}
