package courier;


import io.qameta.allure.Step;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;
public class CourierClient extends RestAssuredClient {
    private final String ROOT  = "/courier";
    private final String LOGIN = ROOT + "/login";
    private final String COURIER = ROOT + "/{courierId}";
    @Step("Создание курьера")
    public ValidatableResponse create(Courier courier) {
        return reqSpec .body(courier)
                .when()
                .post("/courier")
                .then().log().all();
    }

    @Step("Залогироваться созданным курьером")
    public ValidatableResponse login(CourierCredentials creds) {

        return reqSpec .body(creds)
                .when()
                .post("/courier/login")
                .then().log().all()
                .defaultParser(Parser.JSON);
    }

    @Step
    public ValidatableResponse tryCreateSameCourier(Courier courier) {
        return reqSpec .body(courier)
                .when()
                .post("/courier")
                .then().log().all();


    }
    @Step("Попытаться создать курьера без необходимого параметра")
    public ValidatableResponse tryCreateCourierWithoutNecessaryParameter(CourierCredentials courierCredentials){
        return reqSpec.body(courierCredentials).log().all()
                .when().log().all()
                .post(ROOT)
                .then().log().all();
    }
    @Step("Удалить курьера по Id")
    public void delete(int courierId){
        reqSpec .pathParam("courierId", courierId)
                .delete(COURIER)
                .then().log().all()
                .assertThat();

    }
}





