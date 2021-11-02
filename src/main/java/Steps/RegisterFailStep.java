package Steps;

import Models.responses.RegisterFailResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.given;

public class RegisterFailStep {
    public void registerFail(RequestSpecification REQ_SPEC){
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
            RegisterFailResponse registerFailResponse = response.as(RegisterFailResponse.class);
            //Validate error
            Assert.assertEquals(registerFailResponse.getError(),"Missing password");
        }
    }
}
