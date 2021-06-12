package TradingSystem.Server.ServiceLayer.DummyObject;

public class DummyDaily {

    private String date;
    private Integer numOfViewers;

    public DummyDaily(String date, Integer numOfViewers) {
        this.date = date;
        this.numOfViewers = numOfViewers;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getNumOfViewers() {
        return numOfViewers;
    }

    public void setNumOfViewers(Integer numOfViewers) {
        this.numOfViewers = numOfViewers;
    }
}
