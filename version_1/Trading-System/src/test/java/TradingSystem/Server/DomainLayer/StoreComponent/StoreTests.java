package TradingSystem.Server.DomainLayer.StoreComponent;

import TradingSystem.Client.Client;
//import org.junit.BeforeClass;
import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import static org.junit.jupiter.api.Assertions.*;

class StoreTests {

    Client client;
    Integer ownerID = client.Register("Gal", "1234");
    DiscountPolicy DC = new DiscountPolicy();
    BuyingPolicy BC = new BuyingPolicy();
    Store store = new Store("Factory54", ownerID, DC, BC);
    Integer client1ID = client.Register("Lior", "123");
    Integer client2ID = client.Register("Sapir", "123");
    Integer client3ID = client.Register("Roni", "123");
    Integer client4ID = client.Register("Efrat", "123");
    Integer client5ID = client.Register("Yasmin", "123");
    Integer client6ID = client.Register("Eden", "123");
    Integer client7ID = client.Register("Sharon", "123");


    @BeforeEach
    void setUp() {
        client = new Client();
        //client.Login("Gal", "1234");
        //ownerID = client.Register("Gal", "1234");

        //client1ID = client.Register("Lior", "123");
        //client2ID = client.Register("Sapir", "123");
        //client3ID = client.Register("Roni", "123");
        //client4ID = client.Register("Efrat", "123");
        //client5ID = client.Register("Yasmin", "123");
        //client6ID = client.Register("Eden", "123");
        //client7ID = client.Register("Sharon", "123");
        //DiscountPolicy DC = new DiscountPolicy();
        //BuyingPolicy BC = new BuyingPolicy();
        //store = new Store("Factory54", ownerID, DC, BC);


    }

    @Test
    void addNewProduct() {
//        String ans1 = store.addNewProduct(ownerID, "Classic Shirt", 129.9, "Tops");
//        assertEquals(ans1, "The product added");
        //assertEquals(store.getInventory().getProducts().size(), 1);
        //String ans2 = store.addNewProduct(ownerID, "Classic Shirt", 99.9, "Tops");
        //assertEquals(ans2, "A product with the same name was added to this store");
        //String ans3 = store.addNewProduct(ownerID, "Stripe Shirt", -50.5, "Tops");
        //assertEquals(ans3, "A product's price must be positive");
    }

    /*
    @Test
    void addProductToInventory() {
    }

     */

    @Test
    void deleteProduct() {
//        store.addNewProduct(ownerID, "Jogger Shorts", 75.0, "Pants");
        //todo how can we know the productID??
        //String ans1 = store.deleteProduct(ownerID, );
        //assertEquals(ans1, "The product delete");
        //String ans2 = store.deleteProduct(ownerID, );
        //assertEquals(ans2, "The Product is not exist in the store");
        //client.Login("Lior", "123");
        //String ans3 = store.deleteProduct(client1ID, );
        //assertEquals(ans3, "Only a store owner is allowed to remove a product");
    }

    @Test
    void editProductDetails() {
//        store.addNewProduct(ownerID, "Print Legging", 149.9, "Pants");
        //String ans1 = store.editProductDetails(ownerID, , "Print Legging", 200.0, "Pants");
        //assertEquals(ans1,"The product update");
        //String ans2 = store.editProductDetails(ownerID, , "Jeans Pants", 100.0, "Pants");
        //assertEquals(ans2, "The product does not exist in the system");
    }

    /*
    @Test
    void reduceProduct() {
    }
     */

    @Test
    void addNewOwner() {
        String ans1 = store.addNewOwner(ownerID, client1ID);
        assertEquals(ans1, "The owner added");
        String ans2 = store.addNewOwner(ownerID, client1ID);
        assertEquals(ans2, "This user is already the owner of this store");
        String ans3 = store.addNewOwner(client2ID, client3ID);
        assertEquals(ans3, "Only a store owner can appoint another store owner");
    }

    @Test
    void addNewManager() {
        String ans1 = store.addNewManager(ownerID, client2ID);
        assertEquals(ans1, "The manager added");
        store.addNewOwner(ownerID, client3ID);
        String ans2 = store.addNewManager(ownerID, client3ID);
        assertEquals(ans2, "This user is already the owner of this store, so he can't be a manager");
        String ans3 = store.addNewManager(client4ID, client5ID);
        assertEquals(ans3, "Only a store owner is allowed to appoint store's manager");
    }


    //removeOwner function is missing

    @Test
    void removeManager() {
        store.addNewManager(ownerID, client6ID);
        String ans1 = store.removeManager(ownerID, client6ID);
        assertEquals(ans1, "The Manager removed");
        store.addNewManager(ownerID, client6ID);
        String ans2 = store.removeManager(client7ID, client6ID);
        assertEquals(ans2, "Only a store owner is allowed to remove store's manager");
        String ans3 = store.removeManager(ownerID, client7ID);
        assertEquals(ans3, "This user is not the manager of this store, so it impossible to remove him");
        store.addNewOwner(ownerID, client7ID);
        String ans4 = store.removeManager(client7ID, client6ID);
        assertEquals(ans4, "Only the store owner who appointed the store manager can remove him");
    }

    /*
    @Test
    void addRatingToStore() {
    }

    @Test
    void removeRatingFromStore() {
    }


    @Test
    void searchByName() {
    }

    @Test
    void searchByCategory() {
    }
     */
}