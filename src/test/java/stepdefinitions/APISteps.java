package stepdefinitions;

import Utils.ReusableMethods;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.Assert;
import pojos.HubAddReqBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static Hooks.API.spec;
import static Hooks.API.token;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class APISteps {

    HashMap<String,Object> reqBody;
    Response response;
    JSONObject expData;

    public static String fullPath;
    String idBoard;

    @Given("Token create etmek icin gerekli olan pathparametresi set edilir.")
    public void token_create_etmek_icin_gerekli_olan_pathparametresi_set_edilir() {

        spec.pathParam("pp1","gettoken");

    }
    @Then("getToken Post methodu icin gerekli olan ReqBody olusturulur")
    public void get_token_post_methodu_icin_gerekli_olan_req_body_olusturulur() {

        /*
        {
            "email": "banuyurdakul.admin@agileswiftcargo.com",
            "password":"123123123"
        }
         */

        reqBody = new HashMap<>();

        reqBody.put("email", "banuyurdakul.admin@agileswiftcargo.com");
        reqBody.put("password","123123123");

    }
    @Then("getToken icin gerekli olan Request gonderilir")
    public void get_token_icin_gerekli_olan_request_gonderilir() {

        response = given().spec(spec).contentType(ContentType.JSON).when().body(reqBody).post("/{pp1}");
        // response.prettyPeek();
    }
    @Then("GetToken requesti icin donen Response bodysinden Token degeri alinir")
    public void get_token_requesti_icin_donen_response_bodysinden_token_degeri_alinir() {

        JsonPath respJP =response.jsonPath();

        String token = respJP.get("data.token");

        System.out.println("token = " + token);
    }


    @Given("Get Hub id api icin gerekli path parametreleri set edilir")
    public void get_hub_id_api_icin_gerekli_path_parametreleri_set_edilir() {
        spec.pathParams("pp1","hub","pp2",1);
    }
    @Then("Get Hub id api icin Expected Data hazirlanir")
    public void get_hub_id_api_icin_expected_data_hazirlanir() {

        /*
        {
            "id": 1,
            "name": "New York City",
            "phone": "01000000001",
            "address": "New York City, New York",
            "current_balance": "0.00",
            "status": 1,
            "created_at": "2023-08-01T14:12:21.000000Z",
            "updated_at": "2023-08-01T14:12:21.000000Z"
        }
         */

        expData =new JSONObject();
        expData.put("id",1);
        expData.put("name","New York City");
        expData.put("phone","01000000001");
        expData.put("address","New York City, New York");
        expData.put("current_balance","0.00");
        expData.put("status",1);
        expData.put("created_at","2023-08-01T14:12:21.000000Z");
        expData.put("updated_at","2023-08-01T14:12:21.000000Z");


    }
    @Then("Get Hub id icin get Request yapilir")
    public void get_hub_id_icin_get_request_yapilir() {

        response = given().spec(spec).header("Authorization","Bearer " + token).when().get("/{pp1}/{pp2}");
        response.prettyPrint();

    }
    @Then("Get Hub id icin donen response'in dogrulamasi yapilir")
    public void get_hub_id_icin_donen_response_in_dogrulamasi_yapilir() {

        JsonPath respJP = response.jsonPath();

        assertEquals( expData.get("id")    , respJP.get("id")    );
        assertEquals( expData.get("name")    , respJP.get("name")    );
        assertEquals( expData.get("phone")    , respJP.get("phone")    );
        assertEquals( expData.get("address")    , respJP.get("address")    );
        assertEquals( expData.get("current_balance")    , respJP.get("current_balance")    );
        assertEquals( expData.get("status")    , respJP.get("status")    );
        assertEquals( expData.get("created_at")    , respJP.get("created_at")    );
        assertEquals( expData.get("updated_at")    , respJP.get("updated_at")    );

    }
    @Given("Api {string} Path Parametreleri set edilir")   //     hub/list
    public void api_path_parametreleri_set_edilir(String rawpaths) {

       // spec.pathParams("pp1","hub","pp2","list");
       // spec.pathParam("pp1","hub").pathParam("pp2","list");   => Bu iki ifade ayni isi yapar!

       // spec.pathParam("pp1","hub");
       // spec.pathParam("pp2","list");   => Bu iki ifade ayni isi yapar!

        // /{pp1}/{pp2}/{.}....

        String [] paths = rawpaths.split("/");

        StringBuilder tempPath = new StringBuilder("/{");

        for (int i = 0; i < paths.length ; i++) {

            String key = "pp" + (i + 1);
            String value = paths[i];

            spec.pathParam(key,value);

            tempPath.append( key + "}/{" );  // /{pp1}/{pp2}/{pp3}/{
        }

        tempPath.deleteCharAt(tempPath.lastIndexOf("{"));
        tempPath.deleteCharAt(tempPath.lastIndexOf("/"));

        fullPath = tempPath.toString();

    }
    @Then("Get request gonderilir")
    public void get_request_gonderilir() {

        response = ReusableMethods.getRequest();
        response.prettyPrint();
    }
    @Then("Donen Response'in status code'unun {int} oldugu dogrulanir")
    public void donen_response_in_status_code_unun_oldugu_dogrulanir(Integer statusCode) {

        response.then().assertThat().statusCode(statusCode);

    }

    @Then("Donen Response Body'sinin success degerinin {string} , message bilgisinin de {string} oldugu dogrulanir")
    public void donenResponseBodySininSuccessDegerininMessageBilgisininDeOlduguDogrulanir(String success, String message) {

        boolean successbo;

        if(success.contains("true") )
            {successbo = Boolean.parseBoolean(success.trim());}
        else
            {successbo = false;}

        response
                .then()
                .assertThat()
                .body("success", Matchers.equalTo(successbo),
                              "message",Matchers.equalTo(message));
    }


    @Then("Hub Add Api icin name {string}, phone {string}, address {string} bilgileriyle Post request gonderilir")
    public void hubAddApiIcinNamePhoneAddressBilgileriylePostRequestGonderilir(String name1, String phone1, String address1) {

    // Request Body hazirlama

        HubAddReqBody reqBody = new HubAddReqBody(name1,phone1,address1);

        response = ReusableMethods.postRequest(reqBody);

        response.prettyPrint();

    }

    @Then("Hub list Response Body icin dogrulama yapilir")
    public void hubListResponseBodyIcinDogrulamaYapilir() {

        response
                .then()
                .assertThat()
                .body("data.id",Matchers.hasItem(1),
                        "data.name",Matchers.hasItems("New York City","Los Angeles"),
                        "data.address",Matchers.hasSize(Matchers.greaterThan(100)),
                        "data[0].id",Matchers.equalTo(1),
                        "data[0].name",Matchers.equalTo("New York City"));
    }

    @Then("Api {string} Query Parametreleri set edilir")
    public void apiQueryParametreleriSetEdilir(String rawQuery) {

        // https://api.trello.com/1/boards/?name={name}&key=APIKey&token=APIToken

        String [] query = rawQuery.split("&");  // ["name={name}","key=APIKey","token=APIToken"]

        Map<String, String> queryMap = new HashMap<>();

        for (int i = 0; i < query.length ; i++) {

            String [] ikili = query[i].split("="); // 1 - ["name","{name}"]

            String key = ikili[0];
            String value = ikili[1];

            queryMap.put(key,value);
        }
        System.out.println(queryMap);

        // spec.queryParam().queryParam().queryParam();
        // spec.queryParams();
        // spec.queryParam();

        spec.queryParams(queryMap);
    }


    @Then("Post request gonderilir")
    public void postRequestGonderilir() {

        response = ReusableMethods.postRequest();

        response.prettyPrint();

    }

    @Then("Donen response body'den idBoard kaydedilir.")
    public void donenResponseBodyDenIdBoardKaydedilir() {

        JsonPath resJP = response.jsonPath();

        idBoard = resJP.get("id");

        System.out.println("idBoard = " + idBoard);
    }

    @Then("Create a new List Api icin idBoard query parametresi hazirlanir")
    public void createANewListApiIcinIdBoardQueryParametresiHazirlanir() {

        spec.queryParam("idBoard","656b99189ed74b0ea7d954e9");

    }

    // https://api.trello.com/1/lists?name={name}&idBoard=5abbe4b7ddc1b351ef961414&key=APIKey&token=APIToken
}
