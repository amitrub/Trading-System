package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentSystemTest {

    PaymentSystem paymentSystem = PaymentSystem.getInstance();
    TradingSystem tradingSystem = TradingSystemImpl.getInstance();
    String connID;

    @BeforeEach
    void setUp() {
        connID = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(connID, "Elinor", "123");
        connID= tradingSystem.Login(connID, "Elinor", "123").returnConnID();

    }

    @Test
    void PaymentTestHappy() {
        PaymentInfo paymentInfo = new PaymentInfo("123456789","4","2021","Elinor","123","123456789");
        AddressInfo addressInfo = new AddressInfo();
        Response response = paymentSystem.purchase(connID, paymentInfo, addressInfo);
        assertFalse(response.getIsErr());
    }
}