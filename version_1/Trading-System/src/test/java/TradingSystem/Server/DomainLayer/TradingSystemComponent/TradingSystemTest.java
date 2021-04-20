package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Client.Client;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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