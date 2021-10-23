import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RestTest {

    //Extract 2 circuits, with index 1 and 5
    @DataProvider(name = "dataProvider")//Use dataprovider for parametrization of circuitId and country
    public Object[][] getCircuitsID(){
        baseURI = "http://ergast.com/api/f1/2017";
        Response response = given()
                .when().get("/circuits.json")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath jsonPath = response.jsonPath();

        String circuitIdIndex1 = jsonPath.get("MRData.CircuitTable.Circuits[1].circuitId");
        String circuitIdIndex5 = jsonPath.get("MRData.CircuitTable.Circuits[5].circuitId");
        //get country value to validate country in "validateCountry()" test
        String countryIndex1 = jsonPath.get("MRData.CircuitTable.Circuits[1].Location.country");
        String countryIndex5 = jsonPath.get("MRData.CircuitTable.Circuits[5].Location.country");

        return new Object[][]{
                new Object[] {circuitIdIndex1, countryIndex1},
                new Object[] {circuitIdIndex5,countryIndex5}
        };
    }

    //Call http://ergast.com/api/f1/circuits/{circuitId}.json where circuitId is equals to extracted values from the data provider
    @Test(dataProvider = "dataProvider")
    public void validateCountry(String circuitId,String country){
        baseURI = "https://ergast.com/api/f1/circuits/";
        given()
                .contentType(ContentType.JSON)
                .when().get(circuitId)
                .then()
                .log().body()
                .statusCode(200)
                .body("MRData.CircuitTable.Circuit.Location.Country",equalTo(country));
    }

    @Test
    public void validateLastRecordsName(){
        baseURI = "https://chercher.tech/sample/api/product/read";
        given()
                .when()
                .get()
                .then()
                .statusCode(200)
                //Validate last record's name value
                .body("records[-1].name",equalTo("CreateRecord"));

    }

    @Test()
    public void validateAllRecordsCreatedTime() throws ParseException {
        LocalDateTime dateTime = LocalDateTime.now(); // Gets the current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String strCurrentDateTime = dateTime.format(formatter);
        Date currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .parse(strCurrentDateTime);


        System.out.println("current date time :  " + strCurrentDateTime);


        baseURI = "https://chercher.tech/sample/api/product/read";
        Response response = given()
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract().response();

        JsonPath path = response.jsonPath();
        List<String> lstcreatedTime = path.getList("records.created");

        List<Date> lstConvertedCreatedTime = new ArrayList<>();
        for (int i = 0; i < lstcreatedTime.size(); i++) {
                lstConvertedCreatedTime.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .parse(lstcreatedTime.get(i)));
        }

        //Validate that all records 'created' time is less than current time
        for (int i = 0; i < lstConvertedCreatedTime.size(); i++) {
            if (lstConvertedCreatedTime.get(i).compareTo(currentDate) > 0) {
                System.out.println("[" + i + "]Created time is after current time");
                Assert.fail();
            } else if (lstConvertedCreatedTime.get(i).compareTo(currentDate) < 0) {
                System.out.println("[" + i + "]Created time is before current time");
                Assert.assertTrue(true);
            } else if (lstConvertedCreatedTime.get(i).compareTo(currentDate) == 0) {
                System.out.println("[" + i + "]Created time is equal to current time");
                Assert.fail();
            } else {
                System.out.println("[" + i + "]Something weird happened...");
                Assert.fail();
            }
        }
    }

    @Test
    public void postNewUser(){
        //Post new user with any 2 parameters
        String requestBody = "{\n" +
                "    \"name\": \"{{TestAutomation}}\",\n" +
                "    \"job\": \"{{Test Automation Engineer}}\"\n" +
                "}";



        baseURI = "https://reqres.in/api/users";
        given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .post()
                .then()
                .log().ifStatusCodeIsEqualTo(201);//Log response data if status code is equals to 201
    }
}
