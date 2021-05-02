package TradingSystem.Client;

public abstract class Client_Driver {

    public static ClientProxy getClient() {
        ClientProxy bridge = new ClientProxy();
        bridge.setRealBridge(new Client());  //Remove after implement
        return bridge;
    }
}
