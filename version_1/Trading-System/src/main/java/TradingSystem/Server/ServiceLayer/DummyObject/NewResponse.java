package TradingSystem.Server.ServiceLayer.DummyObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.util.*;

import static TradingSystem.Server.ServiceLayer.Configuration.errMsgGenerator;

public class NewResponse {
    private boolean isErr = false;
    private String message = "";
    Map<String, Object> returnObject = new HashMap<>();


    public NewResponse() {
    }
    public NewResponse(String message) {
        this.message = message;
    }
    public NewResponse(boolean isErr, String message) {
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

    public Integer getUserID(){
        if(!this.isErr){
            Integer userID = (Integer) this.returnObject.get("userID");
            return userID;
        }
        else
            return -1;
    }
    public String getConnID(){
        if(!this.isErr){
            String connID = (String) this.returnObject.get("connID");
            return connID;
        }
        else
            return "";
    }
    public ArrayList<DummyStore> getStoreList(){
        if(!this.isErr){
            ArrayList<DummyStore> storeList = (ArrayList<DummyStore>) this.returnObject.get("stores");
            return storeList;
        }
        else
            return new ArrayList<>();
    }
    public List<DummyProduct> getProductList(){
        if(!this.isErr){
            List<DummyProduct> productList = (List<DummyProduct>) this.returnObject.get("products");
            return productList;
        }
        else
            return new ArrayList<>();
    }
    public List<DummyShoppingHistory> getHistoryList(){
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

    public static NewResponse makeResponseFromJSON(JSONObject jsonResponse) {
        try{
            boolean isErr = jsonResponse.getBoolean("isErr");
            String message = jsonResponse.getString("message");
            JSONObject jsonObject = jsonResponse.getJSONObject("returnObject");
            HashMap<String,Object> returnObject = new ObjectMapper().readValue(jsonObject.toString(), HashMap.class);
            NewResponse res = new NewResponse(isErr, message);
            res.setReturnObject(returnObject);
            return res;
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Service", "Response", "60", "error in making response from JSON object"));
        }
        return new NewResponse(true, "Error in convert json");
    }

    @Override
    public String toString() {
        return "NewResponse{" +
                "isErr=" + isErr +
                ", massege='" + message + '\'' +
                ", returnObject=" + returnObject +
                '}';
    }
}
