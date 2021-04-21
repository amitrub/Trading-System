package TradingSystem.Server.DomainLayer.TradingSystemComponent;


import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import java.util.List;

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
        Response response= tradingSystem.Register(connID,"reutlevy","8119");
        userID= response.getUserID();
        connID= tradingSystem.Login(connID,"reutlevy","8119").getConnID();
    }
  
  @Test
    void connectSystem() {
        Response response= tradingSystem.ConnectSystem();
        assertTrue(response.getConnID()!="" && response.getIsErr()==false);
    }
  
    @Test
    void exitGood() {
        String connId= tradingSystem.ConnectSystem().getConnID();
        Response response=tradingSystem.Exit(connId);
        assertFalse(response.getIsErr());
    }

    @Test
    void exiBad() {
        String connId= tradingSystem.ConnectSystem().getConnID();
        tradingSystem.Exit(connId);
        Response response=tradingSystem.Exit(connId);
        assertTrue(response.getIsErr());
    }
    
    @Test
    void registerGood() {
        String connID= tradingSystem.ConnectSystem().getConnID();
        Response response= tradingSystem.Register(connID,"reutlevy30","8111996");
        assertFalse(response.getIsErr() && response.getUserID()<0);
    }
    @Test
    void registerExistUser() {
        String connID= tradingSystem.ConnectSystem().getConnID();
        Response response= tradingSystem.Register(connID,"reutlevy30","8111996");
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
        Response response=tradingSystem.Login(connID,"reutlevy30","811199");
        assertFalse(response.getIsErr() && response.getUserID()<0);
    }
    @Test
    void loginWrongUserName() {
        String connID= tradingSystem.ConnectSystem().getConnID();
        Response response=tradingSystem.Login(connID,"reutlevy3","811199");
        assertTrue(response.getIsErr());
    }
    @Test
    void loginWrongPassword() {
        String connID= tradingSystem.ConnectSystem().getConnID();
        tradingSystem.Register(connID,"reutlevy30","811199");
        Response response=tradingSystem.Login(connID,"reutlevy30","8111996");
        assertTrue(response.getIsErr());
    }

    @Test
    void showAllStoresGood() {
        tradingSystem.AddStore(userID,connID,"Store");
        Response res = tradingSystem.ShowAllStores();
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
        Response response= tradingSystem.AddStore(userID,connID,"Store3");
        assertFalse(response.getIsErr());
    }

    @Test
    void AddStoreNotSubscriber() {
        connID= tradingSystem.ConnectSystem().getConnID();
        Response response= tradingSystem.AddStore(11,connID,"Store3");
        assertTrue(response.getIsErr());
    }

    @Test
    void AddStoreSameName() {
        tradingSystem.AddStore(userID,connID,"Store3");
        Response response= tradingSystem.AddStore(userID,connID,"Store3");
        assertTrue(response.getIsErr());
    }



    void Logout() {
        Response response= tradingSystem.Logout(connID);
        assertFalse(response.getIsErr());
    }


}