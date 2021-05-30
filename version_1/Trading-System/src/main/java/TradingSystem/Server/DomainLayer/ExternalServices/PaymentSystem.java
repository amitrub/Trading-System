package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import static TradingSystem.Server.ServiceLayer.Configuration.*;

public class PaymentSystem implements ExternalServices {

    //Singleton
    private static PaymentSystem paymentSystem = null;
    public static PaymentSystem getInstance() {
        if (paymentSystem == null) {
            paymentSystem = new PaymentSystem();
        }
        return paymentSystem;
    }

    @Override
    public Response purchase(PaymentInfo paymentInfo, AddressInfo addressInfo) {
        Response responseHandShake = handshake();
        if (!responseHandShake.getMessage().equals("OK")) {
            return new Response(true, "The connection to Payment System Failed");
        }
        Response responsePay = Pay(paymentInfo);
        try {
            Integer transactionId = Integer.parseInt(responsePay.getMessage());
            if (transactionId == -1) {
                return new Response(true, "The Payment Failed");
            }
        }
        catch (Exception e){
            return new Response(true, "The connection to Payment System Failed");
        }
        return new Response(false, "The connection to Payment System Failed");
        }

    private Response handshake(){
        HttpResponse<String> response = null;
        try {
            Unirest.setTimeouts(20000,20000);
            response = Unirest.post(urlbaseExternalSystems)
                    .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                    .header("cache-control", "no-cache")
                    .header("postman-token", "66ab157e-327a-05b6-1df2-a10698d4c6da")
                    .body("------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"action_type\"\r\n\r\nhandshake\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--")
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
            return new Response(true, "The connection to Payment System Failed");
        }

        String message = response.getBody();
        return new Response(false, message);
    }

    private Response Pay(PaymentInfo paymentInfo){
        String action_type = "pay";
        HttpResponse<String> response = null;
        try {
            Unirest.setTimeouts(20000,20000);
            response = Unirest.post("https://cs-bgu-wsep.herokuapp.com/")
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
        } catch (UnirestException e) {
            e.printStackTrace();
            return new Response(true, "The connection to Payment System Failed");
        }

        String message = response.getBody();
        return new Response(false, message);
    }

    @Override
    public Response Cancel(String transactionId) {
        String action_type = "cancel_pay";
        HttpResponse<String> response = null;
        try {
            Unirest.setTimeouts(20000,20000);
            response = Unirest.post(urlbaseExternalSystems)
                    .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                    .header("cache-control", "no-cache")
                    .header("postman-token", "66ab157e-327a-05b6-1df2-a10698d4c6da")
                    .body(String.format( "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"action_type\"\r\n\r\n%s\r\n" +
                            "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"transaction_id\"\r\n\r\n%s\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--",action_type, transactionId))
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
            return new Response(true, "The connection to Payment System Failed");
        }

        String message = response.getBody();
        return new Response(false, message);
    }
}
