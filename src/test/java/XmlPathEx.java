import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

public class XmlPathEx {
    private static final RequestSpecification REQ_SPEC =
            new RequestSpecBuilder()
                    .setBaseUri("http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso/ListOfContinentsByName")
                    .setContentType(ContentType.XML)
                    .build();

    @Test
    public void validateCountOfAllName(){
        Response response = given()
                .spec(REQ_SPEC)
                .when()
                .get()
                .then()
                .extract().response();

        XmlPath xmlPath = response.xmlPath();
        List<String> sNameLst =  xmlPath.getList("ArrayOftContinent.tContinent.sName");

        //validate count of all 'sName' node
        Assert.assertEquals(sNameLst.size(),6);

    }
    @Test
    public void validateListOfNames(){
        Response response = given()
                .spec(REQ_SPEC)
                .when()
                .get()
                .then()
                .extract().response();

        XmlPath xmlPath = response.xmlPath();

        List<String> sNameLst =  xmlPath.getList("ArrayOftContinent.tContinent.sName");
        List<String> expectedSNameLst = new ArrayList<>();
        Collections.addAll(expectedSNameLst,"Africa","Antarctica","Asia","Europe","Ocenania","The Americas");

        //validate list of all 'sName' node's value
        Assert.assertTrue(sNameLst.containsAll(expectedSNameLst));
    }
    @Test
    public void validateNameWithValueOfCode() {
        Response response = given()
                .spec(REQ_SPEC)
                .when()
                .get()
                .then()
                .extract().response();

        // Using XMLPath validate 'sName' node result with value of sCode equals to 'AN'
        XmlPath xmlPath = response.xmlPath();
        String nameWithCodeAN = xmlPath.getString("ArrayOftContinent.tContinent.find {it.sCode == 'AN'}.sName");
        Assert.assertEquals(nameWithCodeAN,"Antarctica");
    }


    @Test
    public void validateLastContinentName(){
        Response response = given()
                .spec(REQ_SPEC)
                .when()
                .get()
                .then()
                .extract().response();

        XmlPath xmlPath = response.xmlPath();
        String lastNodeName =  xmlPath.getString("ArrayOftContinent.tContinent[-1].sName");
        //validate the last tContinent node's sName value
        Assert.assertEquals(lastNodeName,"The Americas");
    }
}
