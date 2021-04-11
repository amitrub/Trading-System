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
    Integer ownerID;
    Store store;
    Integer client1ID;
    Integer client2ID;
    Integer client3ID;
    Integer client4ID;
    Integer client5ID;
    Integer client6ID;
    Integer client7ID;


    @BeforeEach
    void setUp() {
        client = new Client();
        ownerID = client.Register("Gal", "1234");
        //client.openStore("Factory54");
        DiscountPolicy DP = new DiscountPolicy();
        BuyingPolicy BP = new BuyingPolicy();
        store = new Store("Factory54", ownerID, DP, BP);

        client1ID = client.Register("Lior", "123");
        client2ID = client.Register("Sapir", "123");
        client3ID = client.Register("Roni", "123");
        client4ID = client.Register("Efrat", "123");
        client5ID = client.Register("Yasmin", "123");
        client6ID = client.Register("Eden", "123");
        client7ID = client.Register("Sharon", "123");
    }
    @AfterEach
    void tearDown() {
        client.exitSystem();
    }

    @Test
    void allUsersTests() {

    }
}
