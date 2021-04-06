package TradingSystem.Server.Service_Layer;

import org.json.JSONObject;

public class Response {
    private Integer userID;
    private String connID;
    private String errMessage;

    public Response() {
        this.userID = -1;
        this.connID = "";
        this.errMessage = "There is some Error!";
    }

    public Response(JSONObject jsonObject){
        try {
            this.userID = jsonObject.getInt("userID");
            this.connID = jsonObject.getString("connID");
            this.errMessage = jsonObject.getString("errMessage");
        }
        catch (Exception e)
        {
            this.userID = -1;
            this.connID = "";
            this.errMessage = "There is some Error!";
        }

    }

    public Response(Integer id, String errMessage) {
        this.userID = id;
        this.connID = "";
        this.errMessage = errMessage;
    }

    public Response(Integer userID, String connID, String errMessage) {
        this.userID = userID;
        this.connID = connID;
        this.errMessage = errMessage;
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

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    @Override
    public String toString() {
        return "Response{" +
                "userID=" + userID +
                ", connID='" + connID + '\'' +
                ", errMessage='" + errMessage + '\'' +
                '}';
    }
}
