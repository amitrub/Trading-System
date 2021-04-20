package TradingSystem.Server.ServiceLayer.DummyObject;

import java.util.HashMap;
import java.util.Map;

public class NewResponse {
    private boolean isErr = false;
    private String message = "";
    Map<String, Object> returnObject = new HashMap<>();

    public NewResponse() {
    }

    public void addPair(String key, Object value){
        this.returnObject.put(key, value);
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

    public boolean isErr() {
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

    public void setReturnObject(Map<String, Object> returnObject) {
        this.returnObject = returnObject;
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
