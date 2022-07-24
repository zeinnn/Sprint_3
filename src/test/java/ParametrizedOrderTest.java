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
    private int expectedCode;

    int track;

    public ParametrizedOrderTest(List<String> color, int expectedCode) {
        this.color = color;
        this.expectedCode = expectedCode;
    }


    @Parameterized.Parameters
    public static Object[][] createOrderWithColor() {
        return new Object[][]{
                {List.of("BLACK"), 201},
                {List.of("GREY"), 201},
                {List.of("BLACK", "GREY"), 201},
                {null, 201},
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
        Assert.assertEquals(201,expectedCode);


        assertNotEquals(0,track);

        orderClient.cancel(track);

    }
}