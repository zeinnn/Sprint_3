import courier.Courier;
import courier.CourierClient;
import courier.CourierCredentials;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class CourierLoginTest {

    private CourierClient courierClient;
    private int courierId;
    private String errorMessage = "Недостаточно данных для создания учетной записи";


    @Before
    public void setup(){
        courierClient =  new CourierClient();
    }


    @Test
    @DisplayName("Логин курьера с валидным логином и паролем")
    public void loginWithValidData(){
        Courier courier = Courier.getRandom();
        courierClient.create(courier);
        CourierCredentials creds = CourierCredentials.from(courier);
        ValidatableResponse loginResponse = courierClient.login(creds);
        courierId = loginResponse.extract().path("id");

        assertNotNull("Courier ID is incorrect ",courierId);
        courierClient.delete(courierId);


    }


    @Test
    @DisplayName("Попытка логина с неправильным логином")
    public void tryLoginWithIncorrectLogin(){
        Courier courier = Courier.getRandom();
        courierClient.create(courier);

        CourierCredentials creds = new CourierCredentials("wrongLogin",courier.getPassword());
        ValidatableResponse loginResponse = courierClient.login(creds);
        int statusCode = loginResponse.extract().statusCode();
        assertThat("Код ответа другой", statusCode, equalTo(404));
        String message = loginResponse.extract().path("message");
        assertEquals(message, "Учетная запись не найдена");

        //удалить курьера
        CourierCredentials trueCreds = CourierCredentials.from(courier);
        ValidatableResponse trueLoginResponse =  courierClient.login(trueCreds);
        courierId = trueLoginResponse.extract().path("id");
        courierClient.delete(courierId);

    }
    @Test
    @DisplayName("Курьер не мождет залогиниться с неправильным паролем")
    public void courierCanLogin(){
        Courier courier =  Courier.getRandom();
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(),"wrongPassword"));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        assertThat("Код ответа другой", statusCode, equalTo(404));
        assertEquals(message, "Учетная запись не найдена");
        //удалить курьера
        CourierCredentials trueCreds = CourierCredentials.from(courier);
        ValidatableResponse trueLoginResponse =  courierClient.login(trueCreds);
        courierId = trueLoginResponse.extract().path("id");
        courierClient.delete(courierId);

    }

    @Test(timeout = 1000)
    @DisplayName("Курьер не может залогиниться без пароля")
    public void courierCantLoginWithNullPassword() {
        Courier courier = Courier.getRandom();
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(),null));
        int statusCode =  loginResponse.extract().statusCode();
        assertThat("Код ответа другой", statusCode, equalTo(400));
        String message = loginResponse.extract().path("message");
        int codeMessage = loginResponse.extract().path("code");
        Assert.assertNull(codeMessage);
        assertEquals(message, "Недостаточно данных для входа");
        //удалить курьера
        CourierCredentials trueCreds = CourierCredentials.from(courier);
        ValidatableResponse trueLoginResponse =  courierClient.login(trueCreds);
        courierId = trueLoginResponse.extract().path("id");
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Курьер не может залогиниться без логина")
    public void courierCantLoginWithNullLogin() {
        Courier courier = Courier.getRandom();
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(null,courier.getPassword()));
        int statusCode =  loginResponse.extract().statusCode();
        assertThat("Код ответа другой", statusCode, equalTo(400));
        String message = loginResponse.extract().path("message");
        int codeMessage = loginResponse.extract().path("code");
        Assert.assertNull(codeMessage);
        assertEquals(message, "Недостаточно данных для входа");
        //удалить курьера
        CourierCredentials creds = CourierCredentials.from(courier);
        ValidatableResponse trueLoginResponse =  courierClient.login(creds);
        courierId = trueLoginResponse.extract().path("id");
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Нельзя залогиниться несуществующим курьером")
    public void cantLoginNonExistingCourier(){
        Courier courier = Courier.getRandom();
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(),courier.getPassword()));
        int statusCode =  loginResponse.extract().statusCode();
        assertThat("Код ответа другой", statusCode, equalTo(404));
        String message = loginResponse.extract().path("message");
        assertEquals(message, "Учетная запись не найдена");
    }
}

