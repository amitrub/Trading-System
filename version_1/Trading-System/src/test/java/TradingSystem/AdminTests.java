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
        client.ClearSystem();
    }
    @AfterEach
    void tearDown() {
        client.exitSystem();
    }

    //region requirement 6.4: Purchase history
    @Test
    void HappyHistory() {
    }

    @Test
    void SadAdminUser() {
    }

    @Test
    void SadStoreId() {
    }

    @Test
    void SadUserId() {
    }
    //endregion
}
