package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import static TradingSystem.Server.ServiceLayer.Configuration.urlbaseExternalSystems;

public class SupplySystem implements ExternalServices {

    //Singleton
    private static SupplySystem supplySystem = null;
    private SupplySystem() {
    }
    public static SupplySystem getInstance() {
        if (supplySystem == null) {
            supplySystem = new SupplySystem();
        }
        return supplySystem;
    }

    @Override
    public Response purchase(PaymentInfo paymentInfo, AddressInfo addressInfo) {
        Response responseHandShake = handshake();
        if(!responseHandShake.getMessage().equals("OK")){
            return new Response(true, "The connection to Supply System Failed");
        }
        Response responseSupply = Supply(addressInfo);
        Integer transactionId = Integer.parseInt(responseSupply.getMessage());
        if(transactionId == -1){
            return new Response(true, "The connection to Supply System Failed");
        }
        return new Response(false, "Supply confirmed");
    }

    private Response handshake(){
        String action_type = "handshake";
        HttpResponse<String> response = null;
        try {
            Unirest.setTimeouts(20000,20000);
            response = Unirest.post(urlbaseExternalSystems)
                    .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                    .header("cache-control", "no-cache")
                    .header("postman-token", "66ab157e-327a-05b6-1df2-a10698d4c6da")
                    .body(String.format( "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"action_type\"\r\n\r\n%s\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--", action_type))
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        String message = response.getBody();
        return new Response(false, message);
    }

    private Response Supply(AddressInfo addressInfo){
        String action_type = "supply";
        HttpResponse<String> response = null;
        try {
            Unirest.setTimeouts(20000,20000);
            response = Unirest.post("https://cs-bgu-wsep.herokuapp.com/")
                    .header("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                    .header("cache-control", "no-cache")
                    .header("postman-token", "cdc35da7-e014-f8c7-31a6-fe981434f4a2")
                    .body(String.format( "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"action_type\"\r\n\r\n%s\r\n" +
                            "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"name\"\r\n\r\n%s\r\n" +
                            "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"address\"\r\n\r\n%s\r\n" +
                            "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"city\"\r\n\r\n%s\r\n" +
                            "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"country\"\r\n\r\n%s\r\n" +
                            "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"zip\"\r\n\r\n%s\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--",action_type, addressInfo.getName(), addressInfo.getAddress(), addressInfo.getCity(), addressInfo.getCountry(), addressInfo.getZip()))
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        String message = response.getBody();
        return new Response(false, message);
    }

    @Override
    public Response Cancel(String transactionId){
        String action_type = "cancel_supply";
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
        }

        String message = response.getBody();
        return new Response(false, message);
    }
}
