package TradingSystem.Server.Service_Layer;

import org.json.JSONObject;

import static TradingSystem.Server.Service_Layer.Configuration.errMsgGenerator;

public class Response {
    private Integer userID = -1;
    private String connID = "";
    private boolean isErr = false;
    private String message = "";

    public Response() {
    }

    public Response(JSONObject jsonObject){
        try {
            this.userID = jsonObject.getInt("userID");
            this.connID = jsonObject.getString("connID");
            this.isErr = jsonObject.getBoolean("isErr");
            this.message = jsonObject.getString("message");
        }
        catch (Exception e)
        {
            this.isErr = true;
            this.message = "There is some Error!";
        }

    }

    public Response(Integer id, String message) {
        this.userID = id;
        this.message = message;
    }

    public Response(Integer userID, String connID, String message) {
        this.userID = userID;
        this.connID = connID;
        this.message = message;
    }

    public Response(Integer userID, String connID, boolean isErr, String message) {
        this.userID = userID;
        this.connID = connID;
        this.isErr = isErr;
        this.message = message;
    }

    public Response(boolean isErr, String message) {
        this.isErr = isErr;
        this.message = message;
    }

    public static Response makeResponseFromJSON(JSONObject jsonResponse) {
        try{
            int userID = jsonResponse.getInt("userID");
            String connID = jsonResponse.getString("connID");
            boolean isErr = jsonResponse.getBoolean("err");
            String message = jsonResponse.getString("message");
            Response response = new Response(userID, connID, isErr, message);
            return response;
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Service", "Response", "60", "error in making response from JSON object"));
        }
        return new Response();
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getConnID() {
        return connID;
    }

    public void setConnID(String connID) {
        this.connID = connID;
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

    @Override
    public String toString() {
        return "Response{" +
                "userID=" + userID +
                ", connID='" + connID + '\'' +
                ", isErr=" + isErr +
                ", Message='" + message + '\'' +
                '}';
    }
}
