import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
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

        String circuitIdIndex0 = jsonPath.get("MRData.CircuitTable.Circuits[0].circuitId");
        String circuitIdIndex5 = jsonPath.get("MRData.CircuitTable.Circuits[5].circuitId");
        //get country value to validate country in "validateCountry()" test
        String countryIndex0 = jsonPath.get("MRData.CircuitTable.Circuits[0].Location.country");
        String countryIndex5 = jsonPath.get("MRData.CircuitTable.Circuits[5].Location.country");

        return new Object[][]{
                new Object[] {circuitIdIndex0, countryIndex0},
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
                .body("MRData.CircuitTable.Circuit.Location.Country",equalTo(country))
                .extract()
                .response();

    }
}
