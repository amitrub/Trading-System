package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.json.JSONObject;

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
            return new Response(true, "The Supply Failed");
        }
        return new Response(false, "Supply confirmed");
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

    private Response Supply(AddressInfo addressInfo) {
        JSONObject post_data = new JSONObject();
        String message = "Failed";
        String actionType = "supply";
        try {
            post_data.put("action_type", actionType);
            post_data.put("name", addressInfo.getName());
            post_data.put("address", addressInfo.getAddress());
            post_data.put("city", addressInfo.getCity());
            post_data.put("country", addressInfo.getCountry());
            post_data.put("zip", addressInfo.getZip());

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
