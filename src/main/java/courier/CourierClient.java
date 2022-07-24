package courier;

import courier.POJO.Message;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.put;

public class CourierClient extends RestAssuredClient {


    private final String ROOT  = "/courier";
    private final String LOGIN = ROOT + "/login";
    private final String COURIER = ROOT + "/{courierId}";
    @Step("Создание курьера")
    public boolean create(Courier courier) {
        return given().log().all()
                .header("Content-Type","application/json")
                .baseUri("http://qa-scooter.praktikum-services.ru/")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().log().all()
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");
    }

    @Step("Залогироваться созданным курьером")
    public ValidatableResponse login(CourierCredentials creds) {

        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri("http://qa-scooter.praktikum-services.ru/")
                .body(creds)
                .when()
                .post("/api/v1/courier/login")
                .then().log().all()
                .defaultParser(Parser.JSON);

//                .assertThat()
//                .extract()
//                .path("id");
    }

//    @Step("Залогироваться созданным курьером")
//    public Response based(CourierCredentials creds) {
//
//        return given().log().all()
//                .contentType(ContentType.JSON)
//                .baseUri("http://qa-scooter.praktikum-services.ru/")
//                .body(creds)
//                .when()
//                .post("/api/v1/courier/login")
//                .then().log().all()
//                .assertThat()
//                .extract()
//                .path("id");
//    }

    @Step
    public String tryCreateSameCourier(Courier courier) {
        return given()
                .header("Content-Type", "application/json")
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().log().all()
                .assertThat()
                .statusCode(409)
                .extract()
                .path("message");


    }

    @Step("Попытаться создать курьера без необходимого параметра")
    public String tryCreateCourierWithoutNecessaryParameter(CourierCredentials courierCredentials){
        return reqSpec.body(courierCredentials).log().all()
                .when().log().all()
                .post(ROOT)
                .then().log().all()
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");
    }
    @Step("Удалить курьера по Id")
    public void delete(int courierId){
        reqSpec .pathParam("courierId", courierId)
                .delete(COURIER)
                .then().log().all()
                .assertThat()
                .statusCode(200);

    }
    @Step("огин курбером без пароля")
    public ValidatableResponse withoutpassword(CourierCredentials courierCredentials){
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri("http://qa-scooter.praktikum-services.ru/")
                .body(courierCredentials)
                .when()
                .post("/api/v1/courier/login")
                .then().log().all();

    }
    public void b(CourierCredentials creds){
    Message message = given()
            .contentType(ContentType.JSON)
            .baseUri("http://qa-scooter.praktikum-services.ru/")
            .body(creds)
            .when()
            .post("/api/v1/courier/login")
            .then().log().all()
            .extract()
            .body()
            .as(Message.class);
}

}





