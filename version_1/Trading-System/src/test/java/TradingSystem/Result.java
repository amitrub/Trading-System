package TradingSystem;

class Result
{
    private String name;
    private String timestamp;

    public Result(String name, String timestamp) {
        super();
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Result [name=" + name + ", value=" + timestamp + "]";
    }
}