package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplySystemTest {

    SupplySystem supplySystem = SupplySystem.getInstance();


    @Test
    void SupplyTestHappy() {
        PaymentInfo paymentInfo = new PaymentInfo();
        AddressInfo addressInfo = new AddressInfo("Elinor", "Israel", "Kiryat Gat", "Flower", "8458527");
        Response response = supplySystem.purchase(paymentInfo, addressInfo);
        assertFalse(response.getIsErr());
    }
}