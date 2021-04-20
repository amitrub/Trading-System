package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TradingSystemTest {

    TradingSystem tradingSystem=TradingSystem.getInstance();

    @Test
    void exitGood() {
        String connId= tradingSystem.connectSystem().getConnID();
        Response response=tradingSystem.Exit(connId);
        assertFalse(response.isErr());
    }

    @Test
    void exiBad() {
        String connId= tradingSystem.connectSystem().getConnID();
        tradingSystem.Exit(connId);
        Response response=tradingSystem.Exit(connId);
        assertTrue(response.isErr());
    }
}