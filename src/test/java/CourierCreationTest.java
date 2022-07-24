import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CourierCreationTest {
    private CourierClient courierClient;
    private int courierId;
    private String errorMessage = "Недостаточно данных для создания учетной записи";
    @Before
    public void setup(){
        courierClient =  new CourierClient();
    }
    @Test
    @DisplayName("Создание курьера с валидными данными")
    public void courierCanBeCreatedWithValidData(){
        Courier courier =  Courier.getRandom();
        boolean created = courierClient.create(courier);

        CourierCredentials creds = CourierCredentials.from(courier);
        ValidatableResponse loginResponse =  courierClient.login(creds);
        courierId = loginResponse.extract().path("id");

        assertTrue("Courier is not created", created);
        courierClient.delete(courierId);

    }
    @Test
    @DisplayName("Попробовать создать двух одинаковых курьеров")
    public void cantCreateTwoSameCouriers(){
        Courier courier =  Courier.getRandom();
        courierClient.create(courier);
        CourierCredentials creds = CourierCredentials.from(courier);
        ValidatableResponse loginResponse =  courierClient.login(creds);
        courierId = loginResponse.extract().path("id");
        String message = courierClient.tryCreateSameCourier(courier);
        assertEquals("Этот логин уже используется",message);
        courierClient.delete(courierId);
    }
    @Test
    @DisplayName("Попробовать создать курьера с пустым паролем")
    public void tryCreateCourierWithEmptyPassword(){
        Courier courier = Courier.getRandom();
        CourierCredentials courierWithoutPassword = new CourierCredentials(courier.getLogin(),null,courier.getFirstName());
        String message = courierClient.tryCreateCourierWithoutNecessaryParameter(courierWithoutPassword);

        assertEquals(errorMessage, message);

    }
    @Test
    @DisplayName("Попробовать создать курьера с пустым логином")
    public void tryCreateCourierWithEmptyLogin(){
        Courier courier =  Courier.getRandom();
        CourierCredentials courierWithoutLogin = new CourierCredentials(null,courier.getPassword(),courier.getFirstName());
        String message = courierClient.tryCreateCourierWithoutNecessaryParameter(courierWithoutLogin);
        assertEquals(errorMessage, message);

    }
    @Test
    @DisplayName("Попробовать создать курьера с пустым полем Имя")
    public void tryCreateCourierWithEmptyFirstName(){
        Courier courier =  Courier.getRandom();
        CourierCredentials courierWithoutName = new CourierCredentials(courier.getLogin(),courier.getPassword(),null);
        String message = courierClient.tryCreateCourierWithoutNecessaryParameter(courierWithoutName);
        assertEquals(errorMessage, message);

    }
}
