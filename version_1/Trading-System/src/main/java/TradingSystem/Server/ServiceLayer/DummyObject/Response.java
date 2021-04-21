package TradingSystem.Server.ServiceLayer.DummyObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.util.*;

import static TradingSystem.Server.ServiceLayer.Configuration.errMsgGenerator;

public class Response {
    private boolean isErr = false;
    private String message = "";
    Map<String, Object> returnObject = new HashMap<>();


    public Response() {
    }
    public Response(String message) {
        this.message = message;
    }
    public Response(boolean isErr, String message) {
        this.isErr = isErr;
        this.message = message;
    }

    public void AddPair(String key, Object value){
        this.returnObject.put(key, value);
    }
    public void AddConnID(String value){
        this.returnObject.put("connID", value);
    }
    public void AddUserID(int value){
        this.returnObject.put("userID", value);
    }

    public Integer returnUserID(){
        if(!this.isErr){
            Integer userID = (Integer) this.returnObject.get("userID");
            return userID;
        }
        else
            return -1;
    }
    public String returnConnID(){
        if(!this.isErr){
            String connID = (String) this.returnObject.get("connID");
            return connID;
        }
        else
            return "";
    }
    public List<DummyStore> returnStoreList(){
        if(!this.isErr){
            List<DummyStore> storeList = (List<DummyStore>) this.returnObject.get("stores");
//            for (DummyStore dummyStore: storeList){
//                String s = dummyStore.getName();
//            }
            return storeList;
        }
        else
            return new ArrayList<>();
    }
    public List<DummyProduct> returnProductList(){
        if(!this.isErr){
            List<DummyProduct> productList = (List<DummyProduct>) this.returnObject.get("products");
            return productList;
        }
        else
            return new ArrayList<>();
    }
    public List<DummyShoppingHistory> returnHistoryList(){
        if(!this.isErr){
            List<DummyShoppingHistory> historyList = (List<DummyShoppingHistory>) this.returnObject.get("history");
            return historyList;
        }
        else
            return new ArrayList<>();
    }


    public boolean getIsErr() {
        return isErr;
    }
    public void setErr(boolean err) {
        isErr = err;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getReturnObject() {
        return returnObject;
    }
    private void setReturnObject(Map<String, Object> returnObject) {
        this.returnObject = returnObject;
    }

    public static Response makeResponseFromJSON(JSONObject jsonResponse) {
        try{
            boolean isErr = jsonResponse.getBoolean("isErr");
            String message = jsonResponse.getString("message");
            JSONObject jsonObject = jsonResponse.getJSONObject("returnObject");
            HashMap<String,Object> returnObject = new ObjectMapper().readValue(jsonObject.toString(), HashMap.class);
            Response res = new Response(isErr, message);
            res.setReturnObject(returnObject);
            return res;
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Service", "Response", "60", "error in making response from JSON object"));
        }
        return new Response(true, "Error in convert json");
    }

    @Override
    public String toString() {
        return "Response{" +
                "isErr=" + isErr +
                ", massege='" + message + '\'' +
                ", returnObject=" + returnObject +
                '}';
    }
}
