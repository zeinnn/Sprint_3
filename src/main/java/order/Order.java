package order;

import lombok.Data;

import java.util.List;

@Data
public class Order {
    private String firstName = "Николай";
    private String lastName = "Геращенко";
    private String address = "Москва";
    private String metroStation = "Сокольники";
    private String phone = "+79198901212";
    private int rentTime = 4;
    private String deliveryDate = "2022-05-01";
    private String comment = "Lets get started";
    private List<String> color;

    public static Order getDefaultOrder(){
        return new Order();
    }

    public Order(){

    }

    public Order(String firstName,String lastName,String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

}
