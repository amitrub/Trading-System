package TradingSystem.Server.Unit_tests.ExternalServices;

import TradingSystem.Server.DomainLayer.ExternalServices.AddressInfo;
import TradingSystem.Server.DomainLayer.ExternalServices.PaymentInfo;
import TradingSystem.Server.DomainLayer.ExternalServices.SupplySystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(args = {"src/main/resources/initialization_System.json","src/main/resources/External_State.json"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SupplySystemTest {

    @Autowired
    TradingSystemImpl tradingSystem;

    SupplySystem supplySystem = SupplySystem.getInstance();
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
        Response response = supplySystem.purchase(paymentInfo, addressInfo);
        assertFalse(response.getIsErr());
    }

    @Test
    void SupplyTestSad() {
    }
}