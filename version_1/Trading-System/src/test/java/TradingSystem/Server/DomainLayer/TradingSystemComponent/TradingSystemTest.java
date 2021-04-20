package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Client.Client;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class TradingSystemTest {

    TradingSystem tradingSystem=TradingSystem.getInstance();

    @BeforeEach
    void setUp() {

    }

    @Test
    void connectSystem() {
        Response response= tradingSystem.connectSystem();
        assertTrue(response.getUserID()==-1 && response.isErr()==false);
    }
}