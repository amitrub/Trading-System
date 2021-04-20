package TradingSystem.Server.DomainLayer.TradingSystemComponent;


import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import java.util.List;
import TradingSystem.Client.Client;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertTrue;


import static org.junit.jupiter.api.Assertions.*;

class TradingSystemTest {

    TradingSystem tradingSystem=TradingSystem.getInstance();
    String connID;
    int userID;

    @BeforeEach
    void setUp() {
        connID= tradingSystem.connectSystem().getConnID();
        Response response= tradingSystem.Register(connID,"reutlevy","8119");
        userID= response.getUserID();
        tradingSystem.Login(connID,"reutlevy","8119");
    }
  
  @Test
    void connectSystem() {
        Response response= tradingSystem.connectSystem();
        assertTrue(response.getUserID()==-1 && response.isErr()==false);
    }
  
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

    @Test
    void showAllStoresGood() {
        tradingSystem.AddStore(userID,connID,"Store");
        List<DummyStore> res= tradingSystem.ShowAllStores();
        System.out.println(res.size());
        assertEquals(res.size(),1);
    }

    //TODO- figure out how to test
    @Test
    void showAllStoresBad() {

    }

}