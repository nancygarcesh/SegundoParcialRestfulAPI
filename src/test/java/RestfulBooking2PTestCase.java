import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.not;

public class RestfulBooking2PTestCase {


    //RB1 Este test verifica que al hacer un GET de un id especifico, este lo retorna correctamente junto a un status code 200
    @Test
    public void GET_id_especifico()
    {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        Response response = RestAssured
                .given().pathParam("id", "1")
                .when().get("/booking/{id}");
        response.then().log().body();
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("size()", not(0));
    }


    //RB2 Este test verifica que al hacer un POST de un nuevo record, este es creado y se devuelve un status code 200
    @Test
    public void POST_nuevo() throws JsonProcessingException {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        Bookingdates bookingdates = new Bookingdates();
        bookingdates.setCheckin("2015-01-01");
        bookingdates.setCheckout("1985-01-01");

        Booking booking = new Booking();
        booking.setFirstname("Billie");
        booking.setLastname("Eilish");
        booking.setTotalprice(143);
        booking.setDepositpaid(true);
        booking.setBookingdates(bookingdates);
        booking.setAdditionalneeds("Breakfast");

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(booking);
        System.out.println(payload);

        Response response = RestAssured
                .given().contentType(ContentType.JSON).body(payload)
                .when().post("/booking");

        response.then().log().body();
        response.then().assertThat().statusCode(200);
        response.then().assertThat().body("size()", not(0));
        response.then().assertThat().body("booking.firstname", Matchers.equalTo("Billie"));
        response.then().assertThat().body("booking.lastname", Matchers.equalTo("Eilish"));
        response.then().assertThat().body("booking.totalprice", Matchers.equalTo(143));
        response.then().assertThat().body("booking.depositpaid", Matchers.equalTo(true));
        response.then().assertThat().body("booking.additionalneeds", Matchers.equalTo("Breakfast"));
    }


    //RB3 Este test verifica que al hacer un GET de un id invalido, el servidor devuelve un status code 404 not found
    @Test
    public void GET_id_invalido()
    {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        Response response = RestAssured
                .given().pathParam("id", "32472097")
                .when().get("/booking/{id}");
        response.then().log().body();
        response.then().assertThat().statusCode(404);
    }

    //RB6 Este test verifica que al hacer un POST con letras en lugar de numeros y nuemros en lugar de letras,
    // este devuelve un status code 400 bad request

    //NOTA, ESTE TEST CASE DEBERIA FALLAR DEBIDO A UN CODIGO 500
    @Test
    public void POST_numeros_letras() throws JsonProcessingException {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";
        Bookingdates bookingdates = new Bookingdates();
        bookingdates.setCheckin("2015-01-01");
        bookingdates.setCheckout("198a-01-01");
        AlternativeBookingOne booking = new AlternativeBookingOne();
        booking.setFirstname("Billie");
        booking.setLastname(5467);
        booking.setTotalprice(143);
        booking.setDepositpaid(true);
        booking.setBookingdates(bookingdates);
        booking.setAdditionalneeds("Breakfast");

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(booking);
        System.out.println(payload);

        Response response = RestAssured
                .given().contentType(ContentType.JSON).body(payload)
                .when().post("/booking");

        response.then().log().body();
        response.then().assertThat().statusCode(400);
    }

    //RB5 Este test verifica que al hacer un POST de un record con campos faltantes,
    // el servidor devuelve un status code 400 bad request

    //NOTA, ESTE TEST CASE DEBERIA FALLAR DEBIDO A UN CODIGO 500
    @Test
    public void POST_campos_faltantes() throws JsonProcessingException {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        Bookingdates bookingdates = new Bookingdates();
        bookingdates.setCheckin("2015-01-01");
        bookingdates.setCheckout("1985-01-01");

        AlternativeBookingOne booking = new AlternativeBookingOne();
        // booking.setFirstname("Billie");  // Campo faltante
        // booking.setLastname("Eilish");   // Campo faltante
        // booking.setTotalprice(143);      // Campo faltante
        booking.setDepositpaid(true);       // Este campo es opcional, pero lo dejamos
        booking.setBookingdates(bookingdates);
        booking.setAdditionalneeds("Breakfast"); // Este también es opcional

        // Convertir el objeto en un payload JSON
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(booking);
        System.out.println(payload);

        Response response = RestAssured
                .given().contentType(ContentType.JSON).body(payload)
                .when().post("/booking");

        response.then().log().body();

        response.then().assertThat().statusCode(400);
    }


}
