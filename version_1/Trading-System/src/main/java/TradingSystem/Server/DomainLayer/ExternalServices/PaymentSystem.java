package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;
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
        Response responseHandShake = handshake();
        if(!responseHandShake.getMessage().equals("OK")){
            return new Response(true, "The connection to Payment System Failed");
        }
        Response responsePay = Pay(paymentInfo);
        Integer transactionId = Integer.parseInt(responsePay.getMessage());
        if(transactionId == -1){
            return new Response(true, "The Payment Failed");
        }
        return new Response(false, "Payment confirmed");
    }

    private Response handshake(){
        JSONObject post_data = new JSONObject();
        String message = "Failed";
        String actionType = "handshake";
        try {
            post_data.put("action_type", actionType);
        } catch (Exception e){
            System.out.println(e);
        }
        JSONObject jsonResponse = ExternalHttpRequest.sendPOSTRequest(urlbaseExternalSystems, post_data.toString());
        try{
            message = jsonResponse.toString();
        }catch (Exception e){
            System.out.println(e);
        }
        Response response = new Response(false, message);
        return response;
    }

    private Response Pay(PaymentInfo paymentInfo) {
        JSONObject post_data = new JSONObject();
        String message = "Failed";
        String actionType = "pay";
        try {
            post_data.put("action_type", actionType);
            post_data.put("card_number", paymentInfo.getCardNumber());
            post_data.put("month", paymentInfo.getMonth());
            post_data.put("year", paymentInfo.getYear());
            post_data.put("holder", paymentInfo.getFullName());
            post_data.put("ccv", paymentInfo.getCvv());
            post_data.put("id", paymentInfo.getID());

        } catch (Exception e){
            System.out.println(e);
        }
        JSONObject jsonResponse = ExternalHttpRequest.sendPOSTRequest(urlbaseExternalSystems, post_data.toString());
        try{
            String str = jsonResponse.toString();
            return new Response(false, str);
        }catch (Exception e){
            System.out.println(e);
        }

        return new Response(true, message);
    }

    @Override
    public Response Cancel(Integer transactionId) {
        JSONObject post_data = new JSONObject();
        String message = "Failed";
        String actionType = "cancel_pay";
        try {
            post_data.put("action_type", actionType);
            post_data.put("transaction_id", transactionId);
        } catch (Exception e){
            System.out.println(e);
        }
        JSONObject jsonResponse = ExternalHttpRequest.sendPOSTRequest(urlbaseExternalSystems, post_data.toString());
        try{
            String str = jsonResponse.toString();
            return new Response(false, str);
        }catch (Exception e){
            System.out.println(e);
        }

        return new Response(true, message);
    }
}
