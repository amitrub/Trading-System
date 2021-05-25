package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplySystemTest {

    SupplySystem supplySystem = SupplySystem.getInstance();
    TradingSystem tradingSystem = TradingSystemImpl.getInstance();
    String connID;


    @BeforeEach
    void setUp() {
        connID = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(connID, "Elinor", "123");
        connID= tradingSystem.Login(connID, "Elinor", "123").returnConnID();
    }


    @Test
    void SupplyTestHappy() {
        PaymentInfo paymentInfo = new PaymentInfo();
        AddressInfo addressInfo = new AddressInfo("Elinor", "Israel", "Kiryat Gat", "Flower", "8458527");
        Response response = supplySystem.purchase(connID, paymentInfo, addressInfo);
        assertFalse(response.getIsErr());
    }
}