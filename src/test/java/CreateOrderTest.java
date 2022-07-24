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


public class CreateOrderTest {

    private CourierClient courierClient;

    private OrderClient orderClient;
    private int courierId;
    private String errorMessage = "Недостаточно данных для создания учетной записи";
    @Before
    public void setup(){

        courierClient =  new CourierClient();
        orderClient = new OrderClient();
    }


    @Test
    @DisplayName("Проверяем возврат заказа")
    public void returnOrder(){
        //создать тело заказа
        Order order = new Order("Kostya","Геращенко","Москва","Сокольники","+79198901212", 4, "2022-05-01", "Lets get started", null);
        //ссздать заказ

        ValidatableResponse b = orderClient.createOrder(order);


        int orderNumber = b.extract().path("track");
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
        ValidatableResponse g = orderClient.getOrderByTrackNumber(orderNumber);
        int orderId = g.extract().path("order.id");
        System.out.println(orderId);


        //принять заказ(ы)
        orderClient.acceptOrder(orderId,courierId);

        // получить заказ курьера по id
        ValidatableResponse p = orderClient.getCourierOrderById(courierId);
        int total = p.extract().path("pageInfo.total");
        Assert.assertEquals(1,total);

        //завершить заказ
       orderClient.finishOrder(orderId);

        //удалить курьера
        courierClient.delete(courierId);
    }
}
