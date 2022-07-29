package order;

import com.github.javafaker.Faker;
import lombok.Data;

import java.util.List;
import java.util.Locale;


@Data
public class Order {

    private String firstName = "Николай";  //я пытался подключить фейкер но у меня появлялась ошибка No serializer found for class com.github.javafaker.Faker and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: order.Order["faker"])
    private String lastName = "Кравченко";
    private String address = "Москва";
    private String metroStation = "Сокольники";
    private String phone = "+88005553535";
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
