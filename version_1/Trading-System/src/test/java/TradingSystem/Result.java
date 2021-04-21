package TradingSystem;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;

class Result
{
    private String name;
    private String timestamp;
    private Response response;

    public Result(String name, String timestamp) {
        super();
        this.name = name;
        this.timestamp = timestamp;
    }
    public Result(String name, String timestamp, Response response) {
        super();
        this.name = name;
        this.timestamp = timestamp;
        this.response = response;
    }

    public String getName() {
        return name;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public Response getResponse() {
        return response;
    }
    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "Result [name=" + name + ", value=" + timestamp + "]";
    }
}