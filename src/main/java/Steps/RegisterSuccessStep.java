package Steps;

import Models.responses.RegisterSuccessResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.given;

public class RegisterSuccessStep {
    public void registerSuccess(RequestSpecification REQ_SPEC) {
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
            RegisterSuccessResponse registerSuccessResponse = response.as(RegisterSuccessResponse.class);
            //Validate id and token values
            Assert.assertEquals(registerSuccessResponse.getId(),4);
            Assert.assertEquals(registerSuccessResponse.getToken(),"QpwL5tke4Pnpja7X4");
        }
    }
}
