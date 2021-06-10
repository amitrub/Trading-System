package TradingSystem.Server.ServiceLayer.Bridge;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Trading_Driver {

    @Autowired
    public static TradingSystemImpl tradingSystem;

    public static void setTradingSystem(TradingSystemImpl tradingSystem) {
        Trading_Driver.tradingSystem = tradingSystem;
    }

    public static TradingSystem getTradingSystem() {
        ProxyTrading bridge = new ProxyTrading();
        bridge.setRealBridge(tradingSystem);  //Remove after implement
        return bridge;
    }
}
