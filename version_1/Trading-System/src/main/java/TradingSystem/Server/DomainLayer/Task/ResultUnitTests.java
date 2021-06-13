package TradingSystem.Server.DomainLayer.Task;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;

public class ResultUnitTests {
    private String name;
    private String timestamp;
    private Response response;

    public ResultUnitTests(String name, String timestamp) {
        super();
        this.name = name;
        this.timestamp = timestamp;
    }
    public ResultUnitTests(String name, String timestamp, Response response) {
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
        return "ResultUnitTests [name=" + name + ", value=" + timestamp + "]";
    }
}
