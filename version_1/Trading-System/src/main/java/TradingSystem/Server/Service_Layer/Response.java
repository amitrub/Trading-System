package TradingSystem.Server.Service_Layer;

import org.json.JSONException;
import org.json.JSONObject;

public class Response {
    Integer id;
    String errMessage;

    public Response() {
        this.id = -1;
        this.errMessage = "There is some Error!";
    }

    public Response(JSONObject jsonObject){
        try {
            this.id = jsonObject.getInt("id");
            this.errMessage = jsonObject.getString("errMessage");
        }
        catch (Exception e)
        {
            this.id = -1;
            this.errMessage = "There is some Error!";
        }

    }

    public Response(Integer id, String errMessage) {
        this.id = id;
        this.errMessage = errMessage;
    }

    public Integer getId() {
        return id;
    }

    public String getErrMessage() {
        return errMessage;
    }

    @Override
    public String toString() {
        return "Response{" +
                "id=" + id +
                ", errMessage='" + errMessage + '\'' +
                '}';
    }
}
