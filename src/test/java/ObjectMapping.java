import Steps.CreateUserStep;
import Steps.RegisterFailStep;
import Steps.RegisterSuccessStep;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import Models.requests.CreateUserRequest;
import Models.responses.CreateUserResponse;
import Models.responses.RegisterFailResponse;
import Models.responses.RegisterSuccessResponse;

import static io.restassured.RestAssured.*;

public class ObjectMapping {

    private static final RequestSpecification REQ_SPEC =
            new RequestSpecBuilder()
                    .setBaseUri("https://reqres.in/api")
                    .setContentType(ContentType.JSON)
                    .build();

    @Test
    public void apiTest() {
        RegisterSuccessStep registerSuccessStep = new RegisterSuccessStep();
        registerSuccessStep.registerSuccess(REQ_SPEC);

        RegisterFailStep registerFailStep = new RegisterFailStep();
        registerFailStep.registerFail(REQ_SPEC);

        CreateUserStep createUserStep = new CreateUserStep();
        createUserStep.createUser();
    }
}
