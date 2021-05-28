package TradingSystem.Server.DomainLayer.ExternalServices;

public abstract class SupplySystem_Driver {

    public static SupplySystemProxy getSupplySystem() {
        SupplySystemProxy bridge = new SupplySystemProxy();
        bridge.setRealBridge(SupplySystem.getInstance());  //Remove after implement
        return bridge;
    }
}
