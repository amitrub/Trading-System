package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentSystemTest {

    PaymentSystem paymentSystem = PaymentSystem.getInstance();

    @Test
    void PaymentTestHappy() {
        PaymentInfo paymentInfo = new PaymentInfo("123456789","4","2021","Elinor","123","123456789");
        AddressInfo addressInfo = new AddressInfo();
        Response response = paymentSystem.purchase(paymentInfo, addressInfo);
        assertFalse(response.getIsErr());
    }
}