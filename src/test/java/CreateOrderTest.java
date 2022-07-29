import com.github.javafaker.Faker;
import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.Order;
import order.OrderClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;


public class CreateOrderTest {

    private CourierClient courierClient;
    private Faker faker;

    private OrderClient orderClient;
    private int courierId;
    private String errorMessage = "Недостаточно данных для создания учетной записи";
    @Before
    public void setup(){

        courierClient =  new CourierClient();
        orderClient = new OrderClient();
        faker = new Faker(new Locale("ru"));

    }


    @Test
    @DisplayName("Проверяем возврат заказа")
    public void returnOrder(){
        //создать тело заказа
        Order order = new Order(
               "Николай",
                "Кравченко",
                "Москва",
                "Сокольники",
                "+88005553535",
                4,
                "2022-05-01",
                "Lets get started",
                null);
        //ссздать заказ

        ValidatableResponse createOrderResponse = orderClient.createOrder(order);


        int orderNumber = createOrderResponse.extract().path("track");
        System.out.println(orderNumber);
        //сформировать курьера
        Courier courier = Courier.getRandom();
        //создать курьера
        courierClient.create(courier);
        // логинимся курьером и получаем его id
        CourierCredentials creds = CourierCredentials.from(courier);
        ValidatableResponse loginResponse =  courierClient.login(creds);
        courierId = loginResponse.extract().path("id");
        //получаем заказ по его номеру и берём оттуда id
        ValidatableResponse orderByTrackNumberResponse = orderClient.getOrderByTrackNumber(orderNumber);
        int orderId = orderByTrackNumberResponse.extract().path("order.id");
        System.out.println(orderId);


        //принять заказ(ы)
        orderClient.acceptOrder(orderId,courierId);

        // получить заказ курьера по id
        ValidatableResponse courierOrderByIdResponse = orderClient.getCourierOrderById(courierId);
        int total = courierOrderByIdResponse.extract().path("pageInfo.total");
        Assert.assertEquals(1,total);
        Assert.assertEquals(200,courierOrderByIdResponse.extract().statusCode());
        courierOrderByIdResponse.assertThat().body("orders",notNullValue());

        //завершить заказ
       orderClient.finishOrder(orderId);

        //удалить курьера
        courierClient.delete(courierId);
    }
}
