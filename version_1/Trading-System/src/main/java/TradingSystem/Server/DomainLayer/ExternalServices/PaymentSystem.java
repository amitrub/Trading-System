package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import static TradingSystem.Server.ServiceLayer.Configuration.*;

public class PaymentSystem implements ExternalServices {

    //Singleton
    private static PaymentSystem paymentSystem = null;
    private PaymentSystem() {}
    public static PaymentSystem getInstance() {
        if (paymentSystem == null) {
            paymentSystem = new PaymentSystem();
        }
        return paymentSystem;
    }

//    public boolean checkCredit(String name, String credit_number, String phone_number){
//        try {
//            System.out.println("check credit: " + credit_number + " phone credit: " + phone_number);
//            int credit = Integer.parseInt(credit_number);
//        } catch(Exception e){
//            System.out.println("check credit details wrong credit");
//            return false;
//        }
//
//        if(!phone_number.substring(0, 3).equals("052")) {
//            System.out.println("check credit details wrong phone");
//            return false;
//        }
//
//        return true;
//    }

    @Override
    public Response purchase(PaymentInfo paymentInfo, AddressInfo addressInfo) {
        try {
            Response responseHandShake = handshake();
            if(!responseHandShake.getMessage().equals("OK")){
                return new Response(true, "The connection to Payment System Failed");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        try {
            Response responsePay = Pay(paymentInfo);
            Integer transactionId = Integer.parseInt(responsePay.getMessage());
            if(transactionId == -1){
                return new Response(true, "The Payment Failed");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return new Response(false, "Payment confirmed");
    }

    private Response handshake() throws UnirestException {
        HttpResponse<String> response = Unirest.post(urlbaseExternalSystems)
                .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                .header("cache-control", "no-cache")
                .header("postman-token", "66ab157e-327a-05b6-1df2-a10698d4c6da")
                .body("------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"action_type\"\r\n\r\nhandshake\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--")
                .asString();

        String message = response.getBody();
        return new Response(false, message);
    }

    private Response Pay(PaymentInfo paymentInfo) throws UnirestException {
        String action_type = "pay";
        HttpResponse<String> response = Unirest.post("https://cs-bgu-wsep.herokuapp.com/")
                .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                .header("cache-control", "no-cache")
                .header("postman-token", "cdc35da7-e014-f8c7-31a6-fe981434f4a2")
                .body(String.format( "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"action_type\"\r\n\r\n%s\r\n" +
                        "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"card_number\"\r\n\r\n%s\r\n" +
                        "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"month\"\r\n\r\n%s\r\n" +
                        "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"year\"\r\n\r\n%s\r\n" +
                        "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"holder\"\r\n\r\n%s\r\n" +
                        "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"ccv\"\r\n\r\n%s\r\n" +
                        "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"id\"\r\n\r\n%s\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--",action_type, paymentInfo.getCardNumber(), paymentInfo.getMonth(), paymentInfo.getYear(), paymentInfo.getFullName(), paymentInfo.getCvv(), paymentInfo.getID()))
                .asString();

        String message = response.getBody();
        return new Response(false, message);
    }

    @Override
    public Response Cancel(String transactionId) throws UnirestException {
        String action_type = "cancel_pay";
        HttpResponse<String> response = Unirest.post(urlbaseExternalSystems)
                .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                .header("cache-control", "no-cache")
                .header("postman-token", "66ab157e-327a-05b6-1df2-a10698d4c6da")
                .body(String.format( "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"action_type\"\r\n\r\n%s\r\n" +
                        "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"transaction_id\"\r\n\r\n%s\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--",action_type, transactionId))
                .asString();

        String message = response.getBody();
        return new Response(false, message);
    }
}
