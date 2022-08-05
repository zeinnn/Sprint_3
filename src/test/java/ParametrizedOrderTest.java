import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.Order;
import order.OrderClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;


import java.util.List;

@RunWith(Parameterized.class)
public class ParametrizedOrderTest {

    private ValidatableResponse created;
    private OrderClient orderClient;
    private  List<String> color;

    int track;

    public ParametrizedOrderTest(List<String> color) {
        this.color = color;

    }


    @Parameterized.Parameters
    public static Object[][] createOrderWithColor() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {null},
        };


    }

    @Before
    public void setup(){
        orderClient =  new OrderClient();
    }

    @Test
    @DisplayName("Параметризированный тест поля Цвет при создании заказа")
    public void paramOrderTest() {
        Order defaultOrder = Order.getDefaultOrder();
        defaultOrder.setColor(color);
        created = orderClient.createOrder(defaultOrder);
        track = created.extract().path("track");

        assertNotEquals(0,track);
        Assert.assertEquals(201,created.extract().statusCode());

        orderClient.cancel(track);

    }
}