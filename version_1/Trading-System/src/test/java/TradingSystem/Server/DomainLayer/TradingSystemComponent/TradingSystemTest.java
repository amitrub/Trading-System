package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Client.Client;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TradingSystemTest {

    TradingSystem tradingSystem=TradingSystem.getInstance();

    @Test
    void loginSucess() {
        String connID= tradingSystem.connectSystem().getConnID();
        tradingSystem.Register(connID,"reutlevy30","811199");
        Response response=tradingSystem.Login(connID,"reutlevy30","811199");
        assertFalse(response.isErr() && response.getUserID()<0);
    }
    @Test
    void loginWrongUserName() {
        String connID= tradingSystem.connectSystem().getConnID();
        Response response=tradingSystem.Login(connID,"reutlevy3","811199");
        assertTrue(response.isErr());
    }
    @Test
    void loginWrongPassword() {
        String connID= tradingSystem.connectSystem().getConnID();
        tradingSystem.Register(connID,"reutlevy30","811199");
        Response response=tradingSystem.Login(connID,"reutlevy30","8111996");
        assertTrue(response.isErr());
    }
}