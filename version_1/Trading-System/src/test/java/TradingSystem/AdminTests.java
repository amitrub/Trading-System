package TradingSystem;

import TradingSystem.Client.Client;
import TradingSystem.Server.DomainLayer.StoreComponent.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdminTests {

    Client client;


    @BeforeEach
    void setUp() {
        client = new Client();
    }
    @AfterEach
    void tearDown() {
        client.exitSystem();
    }

    //region Purchase history
    @Test
    void allHistory_Happy() {
    }

    @Test
    void allHistory_SadAdminUser() {
    }

    @Test
    void allHistory_SadStoreId() {
    }

    @Test
    void allHistory_SadUserId() {
    }
    //endregion
}
