package TradingSystem.Server.DomainLayer.TradingSystemComponent;


import TradingSystem.Server.DomainLayer.StoreComponent.Store;
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


    @Test
    void NewOwnerSuccess() {
        tradingSystem.AddStore(userID,connID,"store");
        String gust1 = tradingSystem.ConnectSystem().getConnID();
        tradingSystem.Register(gust1, "elinor", "123");
        Response r=tradingSystem.Login(gust1, "elinor", "123");
        Response r1 = tradingSystem.AddNewOwner(userID, connID, 1, r.getUserID());
        System.out.println(r1.getMessage());
        assertFalse(r1.getIsErr());
    }

    @Test
    void NewOwnerNotConnected() {
        tradingSystem.AddStore(userID,connID,"store");
        Response r = tradingSystem.AddNewManager(-1, "--", 1, 2);
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    @Test
    void NewOwnerNotSubscriber() {
        tradingSystem.AddStore(userID,connID,"store");
        Response r1 = tradingSystem.AddNewOwner(-1, connID, 1, 2);
        System.out.println(r1.getMessage());
        assertTrue(r1.getIsErr());
        Response r2 = tradingSystem.AddNewOwner(userID, connID, 1, 7);
        System.out.println(r2.getMessage());
        assertTrue(r2.getIsErr());
    }

    @Test
    void AddNewOwnerIsNotTheOwner() {
        tradingSystem.AddStore(userID,connID,"store");
        String gust1 = tradingSystem.ConnectSystem().getConnID();
        tradingSystem.Register(gust1, "elinor", "123");
        Response res = tradingSystem.Login(gust1, "elinor", "123");
        String gust2 = tradingSystem.ConnectSystem().getConnID();
        tradingSystem.Register(gust2, "roee", "123");
        tradingSystem.Login(gust2, "roee", "123").getConnID();
        Response r1 = tradingSystem.AddNewOwner(res.getUserID(), res.getConnID(), 1, 3);
        System.out.println(r1.getMessage());
        assertTrue(r1.getIsErr());
    }

    @Test
    void NewOwnerDouble() {
        tradingSystem.AddStore(userID,connID,"store");
        String gust1 = tradingSystem.ConnectSystem().getConnID();
        tradingSystem.Register(gust1, "elinor", "123");
        Response res = tradingSystem.Login(gust1, "elinor", "123");
        tradingSystem.AddNewOwner(userID, connID, 1, res.getUserID());
        Response r = tradingSystem.AddNewOwner(userID, connID, 1, res.getUserID());
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    @Test
    void NewOwnerAlreadyManager() {
        // String gust1 = tradingSystem.ConnectSystem().getConnID();
        //tradingSystem.Register(gust1, "elinor", "123");
        //Response res = tradingSystem.Login(gust1, "elinor", "123");
        //tradingSystem.AddNewManager(userID, connID, 1, res.getUserID());
        //Response r = tradingSystem.AddNewOwner(userID, connID, 1, res.getUserID());
        //assertTrue(!r.getIsErr());
    }

}