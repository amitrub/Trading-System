package TradingSystem.Server.ServiceLayer.Bridge;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Trading_Driver {

    @Autowired
    TradingSystemImplRubin tradingSystem;

    public static TradingSystem getTradingSystem() {
        ProxyTrading bridge = new ProxyTrading();
        bridge.setRealBridge(TradingSystemImpl.getInstance());  //Remove after implement
        return bridge;
    }
}
