package TradingSystem.Server.Service_Layer;

public class Response {
    Integer id;
    String errMessage;

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
