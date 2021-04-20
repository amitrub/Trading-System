package TradingSystem.Server.DomainLayer.TradingSystemComponent;


import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import java.util.List;
import TradingSystem.Client.Client;
import TradingSystem.Server.ServiceLayer.DummyObject.NewResponse;
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
        connID= tradingSystem.ConnectSystem().getConnID();
        NewResponse response= tradingSystem.Register(connID,"reutlevy","8119");
        userID= response.getUserID();
        connID= tradingSystem.Login(connID,"reutlevy","8119").getConnID();
    }
  
  @Test
    void connectSystem() {
        NewResponse response= tradingSystem.ConnectSystem();
        assertTrue(response.getConnID()!="" && response.getIsErr()==false);
    }
  
    @Test
    void exitGood() {
        String connId= tradingSystem.ConnectSystem().getConnID();
        NewResponse response=tradingSystem.Exit(connId);
        assertFalse(response.getIsErr());
    }

    @Test
    void exiBad() {
        String connId= tradingSystem.ConnectSystem().getConnID();
        tradingSystem.Exit(connId);
        NewResponse response=tradingSystem.Exit(connId);
        assertTrue(response.getIsErr());
    }
    
    @Test
    void registerGood() {
        String connID= tradingSystem.ConnectSystem().getConnID();
        NewResponse response= tradingSystem.Register(connID,"reutlevy30","8111996");
        assertFalse(response.getIsErr() && response.getUserID()<0);
    }
    @Test
    void registerExistUser() {
        String connID= tradingSystem.ConnectSystem().getConnID();
        NewResponse response= tradingSystem.Register(connID,"reutlevy30","8111996");
        response= tradingSystem.Register(connID,"reutlevy30","reut");
        assertTrue(response.getIsErr());
    }
    //TODO- implement this
    void registerInvalidPassword() {

    }

    @Test
    void loginSucess() {
        String connID= tradingSystem.ConnectSystem().getConnID();
        tradingSystem.Register(connID,"reutlevy30","811199");
        NewResponse response=tradingSystem.Login(connID,"reutlevy30","811199");
        assertFalse(response.getIsErr() && response.getUserID()<0);
    }
    @Test
    void loginWrongUserName() {
        String connID= tradingSystem.ConnectSystem().getConnID();
        NewResponse response=tradingSystem.Login(connID,"reutlevy3","811199");
        assertTrue(response.getIsErr());
    }
    @Test
    void loginWrongPassword() {
        String connID= tradingSystem.ConnectSystem().getConnID();
        tradingSystem.Register(connID,"reutlevy30","811199");
        NewResponse response=tradingSystem.Login(connID,"reutlevy30","8111996");
        assertTrue(response.getIsErr());
    }

    @Test
    void showAllStoresGood() {
        tradingSystem.AddStore(userID,connID,"Store");
        NewResponse res = tradingSystem.ShowAllStores();
        List<DummyStore> list = res.getStoreList();
        System.out.println(list.size());
        assertEquals(list.size(),1);
    }

    //TODO- figure out how to test
    @Test
    void showAllStoresBad() {

    }

    @Test

    void AddStoreSuccess() {
        NewResponse response= tradingSystem.AddStore(userID,connID,"Store3");
        assertFalse(response.getIsErr());
    }

    @Test
    void AddStoreNotSubscriber() {
        connID= tradingSystem.ConnectSystem().getConnID();
        NewResponse response= tradingSystem.AddStore(11,connID,"Store3");
        assertTrue(response.getIsErr());
    }

    @Test
    void AddStoreSameName() {
        tradingSystem.AddStore(userID,connID,"Store3");
        NewResponse response= tradingSystem.AddStore(userID,connID,"Store3");
        assertTrue(response.getIsErr());
    }



    void Logout() {
        NewResponse response= tradingSystem.Logout(connID);
        assertFalse(response.getIsErr());
    }


}