package TradingSystem.Server.DomainLayer.ExternalServices;

public class SupplySystem implements ExternalServices {

    //Singleton
    private static SupplySystem supplySystem = null;
    private SupplySystem() {
    }
    public static SupplySystem getInstance() {
        if (supplySystem == null) {
            supplySystem = new SupplySystem();
        }
        return supplySystem;
    }

    public boolean canSupply(String address){return true;}
}
