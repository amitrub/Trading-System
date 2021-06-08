package TradingSystem.Client;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class Client_Driver {

//    @Autowired
//    private static ClientProxy bridge;

    public static ClientProxy getClient() {
        ClientProxy bridge = new ClientProxy();
        bridge.setRealBridge(new Client());  //Remove after implement
        return bridge;
    }
}
