package TradingSystem.Server.ServiceLayer.Bridge;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

public abstract class Trading_Driver {

    public static TradingSystem getTradingSystem() {
        ProxyTrading bridge = new ProxyTrading();
     //   bridge.setRealBridge(TradingSystemImpl.getInstance());  //Remove after implement
        return bridge;
    }
}
