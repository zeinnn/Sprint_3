package order;
import courier.RestAssuredClient;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import lombok.Data;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;


public class OrderClient extends RestAssuredClient {

    private final String ROOT  = "/orders";
    private final String TRACKNUMBER = ROOT + "/track?t=";
    private final String ACCEPTORDER = ROOT + "/accept/";

    private final String GETCOURIERORDERBYID = ROOT + "{courierId}";

    private final String FINISHORDER = ROOT + "/finish/";


    @Step("Создать заказ")
    public ValidatableResponse createOrder(Order order) {
        return reqSpec  .body(order)
                .when()
                .post(ROOT)
                .then().log().all();
    }


    public ValidatableResponse cancel(int track) {
        return  reqSpec .put("/api/v1/orders/cancel" + "?track="+ track)
                .then().log().all();
    }

    @Step("Получить заказ по трек номеру")
    public ValidatableResponse getOrderByTrackNumber(int trackNumber){
            return reqSpec .when()
                    .get(TRACKNUMBER + trackNumber)
                    .then().log().all();
    }

    @Step("Принять заказ")
    public ValidatableResponse acceptOrder(int orderId, int courierId){
        return reqSpec .when()
                .put(ACCEPTORDER + orderId + "?courierId=" + courierId)
                .then().log().all();
    }

    @Step("Получить заказ курьера по id")
    public ValidatableResponse getCourierOrderById(int courierId){
        return reqSpec .when()
                .get(ROOT + "?courierId=" + courierId)
                .then().log().all()
                .assertThat()
                .and();
    }

    @Step("Завершить заказ")
    public ValidatableResponse finishOrder(int OrderId){
        return reqSpec .when()
                .put(FINISHORDER + OrderId)
                .then().log().all();
    }

}
