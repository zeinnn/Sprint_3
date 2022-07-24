package courier;

import lombok.Data;
@Data
public class CourierCredentials {
    private String login;
    private String password;

    private String firstname;
//    public CourierCredentials(Courier courier) {
//        this.login = courier.getLogin();
//        this.password = courier.getPassword();
//    }

    public CourierCredentials(Courier courier) {
        this.login = courier.getLogin();
        this.password = courier.getPassword();

    }
    public CourierCredentials(String login,String password, String firstname){
        this.login = login;
        this.password = password;
        this.firstname = firstname;
    }

    public CourierCredentials(String login,String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierCredentials from(Courier courier){
        return new CourierCredentials(courier);
    }
}
