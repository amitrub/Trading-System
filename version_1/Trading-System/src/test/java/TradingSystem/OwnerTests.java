package TradingSystem;

import TradingSystem.Client.Client;
import TradingSystem.Server.DomainLayer.StoreComponent.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class OwnerTests {

    Client client;
    TradingSystem tradingSystem = TradingSystem.getInstance();

    @BeforeEach
    void setUp() {
        this.client = new Client();
        client.connectSystem();
    }
    @AfterEach
    void tearDown() {
        client.exitSystem();
        tradingSystem.Initialization();
    }


    //region requirement 4.1: add Product Tests
    @Test
    void HappyAdd() {
        client.Register("Gal", "123");
        client.Login("Gal", "123");
        client.openStore("Scoop");
        Integer storeID = getStoreID(client.showAllStores(),"Scoop");

        //happy add
        boolean b1 = client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        ArrayList<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        assertEquals(storeProducts1.size(), 1);
        assertFalse(b1);
    }

    @Test
    void SadAddPrice() {
        client.Register("Lior", "123");
        client.Login("Lior", "123");
        client.openStore("Shoes");
        Integer storeID =  getStoreID(client.showAllStores(),"Shoes");

        //sad add - product price illegal
        boolean b2 = client.addProduct(storeID, "Classic Heels", "Heels", -50.0, 25);
        ArrayList<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        assertEquals(storeProducts2.size(), 0);
        assertTrue(b2);
    }

    @Test
    void SadAddNameTaken() {
        client.Register("Ori", "123");
        client.Login("Ori", "123");
        client.openStore("Ice Cube");
        Integer storeID = getStoreID(client.showAllStores(),"Ice Cube");
        client.addProduct(storeID, "Arma Heels", "Heels", 60.0, 25);

        boolean b3 = client.addProduct(storeID, "Arma Heels", "Heels", 200.0, 25);
        ArrayList<DummyProduct> storeProducts3 = client.showStoreProducts(storeID);
        assertEquals(storeProducts3.size(), 1);
        assertTrue(b3);
    }

    @Test
    void SadAddQuantity() {
        client.Register("Sapir", "123");
        client.Login("Sapir", "123");
        client.openStore("To-Go");
        Integer storeID = getStoreID(client.showAllStores(),"To-Go");

        //sad add - product quantity is illegal
        boolean b4 = client.addProduct(storeID, "Short Heels", "Heels", 60.0, -10);
        ArrayList<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        assertEquals(storeProducts1.size(), 0);
        assertTrue(b4);
    }

    //endregion
    //region requirement 4.1: Remove Product Tests
    @Test
    void HappyRemove() {
        client.Register("Oriya", "123");
        client.Login("Oriya", "123");
        client.openStore("Ran Sport");
        Integer storeID = getStoreID(client.showAllStores(), "Ran Sport");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        ArrayList<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = getProductID(storeProducts1,"Arma Heels");
        Integer preSize = client.showStoreProducts(storeID).size();

        //happy remove
        boolean b1 = client.removeProduct(storeID, productID);
        ArrayList<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        assertEquals(storeProducts2.size(), preSize-1);
        assertFalse(b1);
    }

    @Test
    void BadRemove() {
        client.Register("Oriyan", "123");
        client.Login("Oriyaמ", "123");
        client.openStore("Mega Sport");
        Integer storeID = getStoreID(client.showAllStores(), "Mega Sport");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        ArrayList<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = getProductID(storeProducts1,"Arma Heels");
        client.removeProduct(storeID, productID);
        Integer preSize = client.showStoreProducts(storeID).size();

        //bad remove - the product doesn't exist
        boolean b2 = client.removeProduct(storeID, productID);
        ArrayList<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        Integer newSize = storeProducts2.size();
        assertEquals(newSize, preSize);
        assertTrue(b2);
    }
    //endregion
    //region requirement 4.1: edit Product Tests
    @Test
    void HappyEditPrice() {
        client.Register("Shani", "123");
        client.Login("Shani", "123");
        client.openStore("WeShoes");
        Integer storeID = getStoreID(client.showAllStores(), "WeShoes");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        ArrayList<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = getProductID(storeProducts1,"Arma Heels");

        //happy edit price
        boolean b1 = client.editProduct(storeID, productID, "Arma Heels", "Heels", 100.0,25);
        ArrayList<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        assertEquals(storeProducts2.get(0).getPrice(), 100.0, 0);
        assertEquals(storeProducts2.size(), 1);
        assertFalse(b1);
    }

    @Test
    void HappyEditQuantity() {
        client.Register("Sha", "123");
        client.Login("Sha", "123");
        client.openStore("WeShoes");
        Integer storeID = getStoreID(client.showAllStores(), "WeShoes");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        ArrayList<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = getProductID(storeProducts1,"Arma Heels");

        //happy edit quantity
        boolean b1 = client.editProduct(storeID, productID, "Arma Heels", "Heels", 100.0,35);
        ArrayList<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        assertEquals(storeProducts2.get(0).getQuantity(), 35);
        assertEquals(storeProducts2.size(), 1);
        assertFalse(b1);
    }

    @Test
    void SadEditPrice() {
        client.Register("Shira", "123");
        client.Login("Shira", "123");
        client.openStore("Gali");
        Integer storeID = getStoreID(client.showAllStores(), "Gali");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        ArrayList<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = getProductID(storeProducts1,"Arma Heels");

        //sad edit
        boolean b2 = client.editProduct(storeID, productID, "Arma Heels", "Heels", -120.0,25);
        ArrayList<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        assertEquals(storeProducts2.get(0).getPrice(), 80.0, 0);
        assertEquals(storeProducts2.size(), 1);
        assertTrue(b2);
    }

    @Test
    void SadEditQuantity() {
        client.Register("Lin", "123");
        client.Login("Lin", "123");
        client.openStore("Gali");
        Integer storeID = getStoreID(client.showAllStores(), "Gali");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        ArrayList<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = getProductID(storeProducts1,"Arma Heels");
        Integer preSize = client.showStoreProducts(storeID).size();

        //sad edit
        boolean b2 = client.editProduct(storeID, productID, "Arma Heels", "Heels", 120.0,-25);
        ArrayList<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        Integer newSize = storeProducts2.size();
        assertEquals(storeProducts2.get(0).getQuantity(), 25);
        assertEquals(newSize, preSize);
        assertTrue(b2);
    }

    @Test
    void SadEditNonExist() {
        client.Register("Din", "123");
        client.Login("Din", "123");
        client.openStore("Gali");
        Integer storeID = getStoreID(client.showAllStores(), "Gali");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        ArrayList<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = getProductID(storeProducts1,"Arma Heels");
        client.removeProduct(storeID, productID);
        Integer preSize = client.showStoreProducts(storeID).size();

        //sad edit
        boolean b2 = client.editProduct(storeID, productID, "Arma Heels", "Heels", 120.0,25);
        ArrayList<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        Integer newSize = storeProducts2.size();
        assertEquals(newSize, preSize);
        assertTrue(b2);
    }

    //endregion

    //region add Policies tests
    @Test
    void HappyAddBuyingPolicy() {
    }

    @Test
    void SadAddBuyingPolicy() {
    }

    @Test
    void HappyAddDiscountPolicy() {
    }

    @Test
    void SadAddDiscountPolicy() {
    }
    //endregion

    //region add owner/manager tests

    //TODO -add to client AddOwner, AddManager, RemoveManager
    @Test
    void addNewOwner_Happy() {
        Integer newOwnerID = client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.Logout();

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores(), "Store");

        boolean b1 = client.addOwner(storeID, newOwnerID);
        assertFalse(b1);
    }

    @Test
    void addNewOwner_Sad_AppointmentIsNotOwner() {
        client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores(), "Store");
        client.Logout();

        Integer id1 = client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.Logout();

        client.Register("roee", "123");
        client.Login("roee", "123");

        boolean b1 = client.addOwner(storeID, id1);
        assertTrue(b1);
    }

    /*
    @Test
    void addNewOwner_Sad_DoubleAppointmentOfOwner() {
        Integer newOwnerID = client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.Logout();

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores(), "Store");
        client.addOwner(storeID, newOwnerID);
        boolean b1 = client.addOwner(storeID, newOwnerID);
        assertTrue(b1);
    }
    */

    /*
    @Test
    void addNewManager_Happy() {
        Integer newOwnerID = client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.Logout();

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store2");
        Integer storeID = getStoreID(client.showAllStores(), "Store2");

        boolean b1 = client.addManager(storeID, newOwnerID);
        assertFalse(b1);
       }
     */

    @Test
    void addNewManager_Sad_AppointmentIsNotOwner() {
        client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores(), "Store");
        client.Logout();

        Integer id1 = client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.Logout();

        client.Register("roee", "123");
        client.Login("roee", "123");

        boolean b1 = client.addManager(storeID, id1);
        assertTrue(b1);
    }

    /*
    @Test
    void addNewManager_Sad_DoubleAppointmentOfOwner() {
        String gust1 = tradingSystem.connectSystem().getConnID();
        int NofetId=tradingSystem.Register(gust1, "nofet", "123").getUserID();
        String NofetConnID=tradingSystem.Login(gust1, "nofet", "123").getConnID();
        tradingSystem.AddStore(NofetId, NofetConnID, "NofetStore");

        String gust2 = tradingSystem.connectSystem().getConnID();
        int ElinorId=tradingSystem.Register(gust2, "elinor", "123").getUserID();
        String ElinorConnID = tradingSystem.Login(gust2, "elinor", "123").getConnID();

        String gust3 = tradingSystem.connectSystem().getConnID();
        int RoeeId=tradingSystem.Register(gust3, "Roee", "123").getUserID();
        String RoeeConnID = tradingSystem.Login(gust3, "Roee", "123").getConnID();

        tradingSystem.AddNewOwner(NofetId,NofetConnID,2,ElinorId);
        tradingSystem.AddNewManager(NofetId,NofetConnID,2,RoeeId);

        Response res1= tradingSystem.AddNewManager(NofetId,NofetConnID,2,ElinorId);
        assertEquals(res1.getMessage(), "The user "+ElinorId+" is owner the store, so he can not appoint to Manager");

        Response res2= tradingSystem.AddNewManager(NofetId,NofetConnID,2,RoeeId);
        assertEquals(res2.getMessage(), "The user "+RoeeId+" is manages the store, so he can not appoint to Manager again");
    }
     */
/*
    @Test
    void addNewManager_Sad_ThereIsNoPermission() {
        String gust1 = tradingSystem.connectSystem().getConnID();
        int NofetId=tradingSystem.Register(gust1, "nofet", "123").getUserID();
        String NofetConnID=tradingSystem.Login(gust1, "nofet", "123").getConnID();
        tradingSystem.AddStore(NofetId, NofetConnID, "NofetStore");

        String gust2 = tradingSystem.connectSystem().getConnID();
        int ElinorId=tradingSystem.Register(gust2, "elinor", "123").getUserID();
        String ElinorConnID = tradingSystem.Login(gust2, "elinor", "123").getConnID();

        String gust3 = tradingSystem.connectSystem().getConnID();
        int RoeeId=tradingSystem.Register(gust3, "Roee", "123").getUserID();
        String RoeeConnID = tradingSystem.Login(gust3, "Roee", "123").getConnID();

        tradingSystem.AddNewManager(NofetId,NofetConnID,2,ElinorId);

        Response res= tradingSystem.AddNewManager(ElinorId,ElinorConnID,2,RoeeId);
        assertEquals(res.getMessage(), "The user "+ElinorId+" is not allowed to add manager to the store");
    }

 */

    //endregion

    /*
    //region remove manager tests
    @Test
    void removeManager_Happy() {
        String gust1 = tradingSystem.connectSystem().getConnID();
        int NofetId=tradingSystem.Register(gust1, "nofet", "123").getUserID();
        String NofetConnID=tradingSystem.Login(gust1, "nofet", "123").getConnID();
        tradingSystem.AddStore(NofetId, NofetConnID, "NofetStore");

        String gust2 = tradingSystem.connectSystem().getConnID();
        int ElinorId=tradingSystem.Register(gust2, "elinor", "123").getUserID();
        String ElinorConnID = tradingSystem.Login(gust2, "elinor", "123").getConnID();

        tradingSystem.AddNewManager(NofetId,NofetConnID,2,ElinorId);
        Response res= tradingSystem.RemoveManager(NofetId,NofetConnID,2,ElinorId);
        assertEquals(res.getMessage(), "The manager removed successfully");
    }

    @Test
    void removeManager_Sad_RemovingManagerAppointmentBySomeoneElse() {
        String gust1 = tradingSystem.connectSystem().getConnID();
        int NofetId=tradingSystem.Register(gust1, "nofet", "123").getUserID();
        String NofetConnID=tradingSystem.Login(gust1, "nofet", "123").getConnID();
        tradingSystem.AddStore(NofetId, NofetConnID, "NofetStore");

        String gust2 = tradingSystem.connectSystem().getConnID();
        int ElinorId=tradingSystem.Register(gust2, "elinor", "123").getUserID();
        String ElinorConnID = tradingSystem.Login(gust2, "elinor", "123").getConnID();

        String gust3 = tradingSystem.connectSystem().getConnID();
        int RoeeId=tradingSystem.Register(gust3, "Roee", "123").getUserID();
        String RoeeConnID = tradingSystem.Login(gust3, "Roee", "123").getConnID();

        tradingSystem.AddNewOwner(NofetId,NofetConnID,2,ElinorId);
        tradingSystem.AddNewManager(NofetId,NofetConnID,2,RoeeId);

        Response res= tradingSystem.RemoveManager(ElinorId,ElinorConnID,2,RoeeId);
        assertEquals(res.getMessage(), "The user " + ElinorId + " is not the one who appointed the manager");
    }
*/
    //endregion
    //region Information on officials tests
    @Test
    void showOfficialsInfo_Happy() {
    }

    @Test
    void showOfficialsInfo_Sad() {
    }
    //endregion
    //region Store history tests
    @Test
    void showStoreHistory_Happy() {
    }

    @Test
    void showStoreHistory_Sad() {
    }
    //endregion



    /*
    @Test
    void deleteProduct() {
        store.AddProductToStore("Jogger Shorts", 75.0, "Pants");
        Integer productID1 = store.getProductID("Jogger Shorts");
        Response ans1 = store.deleteProduct(productID1);
        assertEquals(ans1.getMessage(), "Remove Product from the Inventory was successful");

        Response ans2 = store.deleteProduct(productID1);
        assertEquals(ans2.getMessage(), "The product does not exist in the system");

        //sad add - not store owner is trying to delete product
        //store.AddProductToStore("Jogger Shorts", 75.0, "Pants");
        //Integer productID2 = store.getProductID("Jogger Shorts");
        //client.Login("Lior", "123");
        //Response ans3 = store.deleteProduct(productID2);
        //assertEquals(ans3, "Only a store owner is allowed to remove a product");
    }

     */


    /*
    @Test
    void editProductDetails() {
        //happy edit
        store.AddProductToStore("Print Legging", 149.9, "Pants");
        Integer productID1 = store.getProductID("Print Legging");
        //String ans1 = store.editProductDetails(ownerID, productID1, "Print Legging", 200.0, "Pants");
        //assertEquals(ans1,"The product update");

        //sad edit - edit non exist product
        //String ans2 = store.editProductDetails(ownerID, productID1 , "Jeans Pants", 100.0, "Pants");
        //assertEquals(ans2, "The product does not exist in the system");
    }
    //endregion
    //region doesnt have service yet
    */


    /*
     @Test
    void addRatingToStore() {
    }

    @Test
    void removeRatingFromStore() {
    }

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
    //endregion

     */

    //region other functions
    Integer getStoreID(ArrayList<DummyStore> stores, String storename)
    {
        for (int i=0; i<stores.size(); i++)
        {
            if(stores.get(i).getName().equals(storename))
                return stores.get(i).getId();
        }
        return -1;
    }

    Integer getProductID(ArrayList<DummyProduct> storeProducts, String productName)
    {
        for (int i=0; i<storeProducts.size(); i++)
        {
            if(storeProducts.get(i).getProductName().equals(productName))
                return storeProducts.get(i).getProductID();
        }
        return -1;
    }
    //endregion

}
