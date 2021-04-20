package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TradingSystemTest {

    TradingSystem tradingSystem= TradingSystem.getInstance();
    @Test
    void registerGood() {
        String connID= tradingSystem.connectSystem().getConnID();
        Response response= tradingSystem.Register(connID,"reutlevy30","8111996");
        assertFalse(response.isErr() && response.getUserID()<0);
    }
    @Test
    void registerExistUser() {
        String connID= tradingSystem.connectSystem().getConnID();
        Response response= tradingSystem.Register(connID,"reutlevy30","8111996");
        response= tradingSystem.Register(connID,"reutlevy30","reut");
        assertTrue(response.isErr());
    }

    //TODO- implement this
    void registerInvalidPassword() {

    }
}