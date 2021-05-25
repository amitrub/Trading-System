package TradingSystem.Acceptence_test;

import TradingSystem.Client.Client_Driver;
import TradingSystem.Client.Client_Interface;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OwnerTests {

    Client_Interface client = Client_Driver.getClient();
    Integer storeID;
    Integer newUserID;

    @BeforeEach
    void setUp() {
        client.clearSystem();
        client.connectSystem();
        newUserID = client.Register("Nofet", "123");
        client.Logout();

        client.connectSystem();
        client.Register("Elinor", "123");
        client.Login("Elinor", "123");
        client.openStore("Adidas");
        storeID = getStoreID(client.showAllStores().getStores(),"Adidas");
        //client.addProduct(storeID, "Dress", "Dress", 80.0, 25);
    }

    @AfterEach
    void tearDown() {
        client.exitSystem();
        client.clearSystem();
    }

    //region other functions
    Integer getStoreID(List<DummyStore> stores, String storeName)
    {
        for (int i=0; i<stores.size(); i++)
        {
            if(stores.get(i).getName().equals(storeName))
                return stores.get(i).getId();
        }
        return -1;
    }

    Integer getProductID(List<DummyProduct> storeProducts, String productName)
    {
        for (int i=0; i<storeProducts.size(); i++)
        {
            if(storeProducts.get(i).getProductName().equals(productName))
                return storeProducts.get(i).getProductID();
        }
        return -1;
    }

    private List<User.Permission> makePermissions (HashMap<String, Boolean> permissionToGive){
        List<User.Permission> Permissions=new LinkedList<>();
        try {
            if((boolean) permissionToGive.get("AddProduct"))
                Permissions.add(User.Permission.AddProduct);
            if((boolean) permissionToGive.get("ReduceProduct"))
                Permissions.add(User.Permission.ReduceProduct);
            if((boolean) permissionToGive.get("DeleteProduct"))
                Permissions.add(User.Permission.DeleteProduct);
            if((boolean) permissionToGive.get("EditProduct"))
                Permissions.add(User.Permission.EditProduct);
            if((boolean) permissionToGive.get("AppointmentOwner"))
                Permissions.add(User.Permission.AppointmentOwner);
            if((boolean) permissionToGive.get("AppointmentManager"))
                Permissions.add(User.Permission.AppointmentManager);
            if((boolean) permissionToGive.get("EditManagerPermission"))
                Permissions.add(User.Permission.EditManagerPermission);
            if((boolean) permissionToGive.get("RemoveManager"))
                Permissions.add(User.Permission.RemoveManager);
            if((boolean) permissionToGive.get("GetInfoOfficials"))
                Permissions.add(User.Permission.GetInfoOfficials);
            if((boolean) permissionToGive.get("GetInfoRequests"))
                Permissions.add(User.Permission.GetInfoRequests);
            if((boolean) permissionToGive.get("ResponseRequests"))
                Permissions.add(User.Permission.ResponseRequests);
            if((boolean) permissionToGive.get("GetStoreHistory"))
                Permissions.add(User.Permission.GetStoreHistory);
            if((boolean) permissionToGive.get("GetDailyIncomeForStore"))
                Permissions.add(User.Permission.GetDailyIncomeForStore);
            if((boolean) permissionToGive.get("GetDailyIncomeForSystem"))
                Permissions.add(User.Permission.GetDailyIncomeForSystem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Permissions;
    }
    //endregion



    //region requirement 4.1.1: Add Product Tests
    @Test
    void HappyAddProduct() {
        //happy add
        Response response = client.addProduct(storeID, "T-Shirt", "Tops", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts1.size(), 1);
        assertFalse(response.getIsErr());
    }

    @Test
    void SadAddPrice() {
        //sad add - product price illegal
        Response response = client.addProduct(storeID, "T-Shirt", "Tops", -50.0, 25);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts2.size(), 0);
        assertTrue(response.getIsErr());
    }

    @Test
    void SadAddNameTaken() {
        client.addProduct(storeID, "T-Shirt", "Tops", 60.0, 25);

        Response response = client.addProduct(storeID, "T-Shirt", "Tops", 200.0, 25);
        List<DummyProduct> storeProducts3 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts3.size(), 1);
        assertTrue(response.getIsErr());
    }

    @Test
    void SadAddQuantity() {
        //sad add - product quantity is illegal
        Response response = client.addProduct(storeID, "T-Shirt", "Tops", 60.0, -10);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts1.size(), 0);
        assertTrue(response.getIsErr());
    }

    //endregion
    //region requirement 4.1.2: Remove Product Tests
    @Test
    void HappyRemove() {
        //Prepare
        client.addProduct(storeID, "T-Shirt", "Tops", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(storeProducts1,"T-Shirt");
        Integer preSize = storeProducts1.size();

        //happy remove
        boolean b1 = client.removeProduct(storeID, productID).getIsErr();
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts2.size(), preSize-1);
        assertFalse(b1);
    }

    @Test
    void BadRemove() {
        client.addProduct(storeID, "T-Shirt", "Tops", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(storeProducts1,"Arma Heels");
        client.removeProduct(storeID, productID);
        Integer preSize = client.showStoreProducts(storeID).returnProductList().size();

        //bad remove - the product doesn't exist
        boolean b2 = client.removeProduct(storeID, productID).getIsErr();
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID).returnProductList();
        Integer newSize = storeProducts2.size();
        assertEquals(newSize, preSize);
        assertTrue(b2);
    }

    //endregion
    //region requirement 4.1.3: Edit Product Tests
    @Test
    void HappyEditPrice() {
        client.addProduct(storeID, "T-Shirt", "Tops", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(storeProducts1,"T-Shirt");

        //happy edit price
        Response response = client.editProduct(storeID, productID, "T-Shirt", "Tops", 100.0,25);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts2.get(0).getPrice(), 100.0, 0);
        assertEquals(storeProducts2.size(), 1);
        assertFalse(response.getIsErr());
    }

    @Test
    void HappyEditQuantity() {
        client.addProduct(storeID, "T-Shirt", "Tops", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(storeProducts1,"T-Shirt");

        //happy edit quantity
        Response response = client.editProduct(storeID, productID, "T-Shirt", "Tops", 80.0,35);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts2.get(0).getQuantity(), 35);
        assertEquals(storeProducts2.size(), 1);
        assertFalse(response.getIsErr());
    }

    @Test
    void SadEditPrice() {
        client.addProduct(storeID, "T-Shirt", "Tops", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(storeProducts1,"T-Shirt");

        //sad edit
        Response response = client.editProduct(storeID, productID, "T-Shirt", "Tops", -120.0,25);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts2.get(0).getPrice(), 80.0, 0);
        assertEquals(storeProducts2.size(), 1);
        assertTrue(response.getIsErr());
    }

    @Test
    void SadEditQuantity() {
        client.addProduct(storeID, "T-Shirt", "Tops", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(storeProducts1,"T-Shirt");
        Integer preSize = client.showStoreProducts(storeID).returnProductList().size();

        //sad edit
        Response response = client.editProduct(storeID, productID, "T-Shirt", "Tops", 120.0,-25);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID).returnProductList();
        Integer newSize = storeProducts2.size();
        assertEquals(storeProducts2.get(0).getQuantity(), 25);
        assertEquals(newSize, preSize);
        assertTrue(response.getIsErr());
    }

    @Test
    void SadEditNonExist() {
        client.addProduct(storeID, "T-Shirt", "Tops", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(storeProducts1,"T-Shirt");
        client.removeProduct(storeID, productID);
        Integer preSize = client.showStoreProducts(storeID).returnProductList().size();

        //sad edit
        Response response = client.editProduct(storeID, productID, "T-Shirt", "Tops", 120.0,25);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID).returnProductList();
        Integer newSize = storeProducts2.size();
        assertEquals(newSize, preSize);
        assertTrue(response.getIsErr());
    }

    //endregion

    //region requirement 4.2 - the tests in PoliciesTests
    //endregion

    //region requirement 4.3: Add owner tests
    @Test
    void HappyAddOwner() {
        boolean b1 = client.addOwner(storeID, newUserID).getIsErr();
        client.Logout();
        client.Login("Nofet", "123");
        List<DummyStore> owners = client.showOwnerStores().getStores();
        assertFalse(b1);
        assertEquals(owners.size(), 1);
    }

    @Test
    void SadAddIsNotOwner() {
        client.connectSystem();
        client.Register("roee", "123");
        client.Login("roee", "123");

        boolean b1 = client.addOwner(storeID, newUserID).getIsErr();
        client.Logout();

        client.Login("Nofet", "123");
        List<DummyStore> owners = client.showOwnerStores().getStores();
        assertTrue(b1);
        assertEquals(owners.size(), 0);
    }

    @Test
    void SadAddIllegalIDOwner() {
        boolean b1 = client.addOwner(storeID, newUserID+5).getIsErr();
        List<DummyStore> owners = client.showOwnerStores().getStores();
        assertTrue(b1);
        assertEquals(owners.size(), 1);
    }

    @Test
    void SadAddDoubleAppointmentOwner() {
        client.addOwner(storeID, newUserID);
        boolean b1 = client.addOwner(storeID, newUserID).getIsErr();
        client.Logout();
        client.Login("Nofet", "123");
        List<DummyStore> owners = client.showOwnerStores().getStores();
        assertTrue(b1);
        assertEquals(owners.size(), 1);
    }
    //endregion

    //region requirement 4.4: Remove owner tests
    @Test
    void HappyRemoveOwner() {
        client.addOwner(storeID, newUserID);

        Response res = client.removeOwner(storeID, newUserID);
        client.Logout();

        client.Login("Nofet", "123");
        List<DummyStore> owners = client.showOwnerStores().getStores();
        assertFalse(res.getIsErr());
        assertEquals(owners.size(), 0);
    }

    @Test
    void SadRemoveNotAppointment() {
        Integer newOwnerID2 = client.Register("Roee", "123");
        client.Logout();

        client.Login("Elinor", "123");
        client.addOwner(storeID, newUserID);
        client.addOwner(storeID, newOwnerID2);
        client.Logout();

        client.Login("Nofet", "123");
        Response res = client.removeOwner(storeID, newOwnerID2);
        client.Logout();
        client.Login("Roee", "123");
        List<DummyStore> owners = client.showOwnerStores().getStores();
        assertTrue(res.getIsErr());
        assertEquals(owners.size(), 1);
    }

    @Test
    void SadRemoveNotOwner1() {
        client.addOwner(storeID, newUserID);
        client.Logout();

        client.Register("Roee", "123");
        client.Login("Roee", "123");

        Response res = client.removeOwner(storeID, newUserID);
        client.Logout();
        client.Login("Nofet", "123");
        List<DummyStore> owners = client.showOwnerStores().getStores();
        assertTrue(res.getIsErr());
        assertEquals(owners.size(), 1);
    }

    @Test
    void SadRemoveNotOwner2() {
        Response res = client.removeOwner(storeID, newUserID);
        assertTrue(res.getIsErr());
    }

    //endregion

    //region requirement 4.5: Add manager tests
    @Test
    void HappyAddManager() {
        boolean b1 = client.addManager(storeID, newUserID).getIsErr();
        client.Logout();
        client.Login("Nofet", "123");
        List<DummyStore> managers = client.showManagerStores().getStores();
        assertFalse(b1);
        assertEquals(managers.size(), 1);
       }

    @Test
    void SadAddIsNotManager() {
        client.connectSystem();
        client.Register("Roee", "123");
        client.Login("Roee", "123");

        boolean b1 = client.addManager(storeID, newUserID).getIsErr();
        List<DummyStore> managers = client.showManagerStores().getStores();
        assertTrue(b1);
        assertEquals(managers.size(), 0);
    }

    @Test
    void SadAddIllegalIDManager() {
        boolean b1 = client.addManager(storeID, newUserID+5).getIsErr();
        assertTrue(b1);
    }

    @Test
    void SadAddDoubleAppointmentManager() {
        client.addManager(storeID, newUserID);
        boolean b1 = client.addManager(storeID, newUserID).getIsErr();
        client.Logout();
        client.Login("Nofet", "123");
        List<DummyStore> managers = client.showManagerStores().getStores();
        assertTrue(b1);
        assertEquals(managers.size(), 1);
    }
    
    //endregion

    //region requirement 4.6: Edit manager Permissions tests
    @Test
    void HappyAddPermissions() {
        client.addManager(storeID, newUserID);

        Response optionalPermissionsForManager = client.GetPossiblePermissionsToManager(storeID);

        //this test give all permissions to manager
        HashMap<String, Boolean> permissionToGive = new HashMap<>();
        for (String per : optionalPermissionsForManager.returnPermissionList()) {
            permissionToGive.put(per, true);
        }
        List<User.Permission> Permissions = makePermissions(permissionToGive);
        Response responseEditPer = client.editManagerPermissions(storeID, newUserID, Permissions);
        client.Logout();
        assertFalse(responseEditPer.getIsErr());
    }

    //case 4.6.2 sad edit permissions, manager id not ok
    @Test
    void SadAddPermissions() {
        Response optionalPermissionsForMannager = client.GetPossiblePermissionsToManager(storeID);

        //this test give all permissions to manager
        HashMap<String, Boolean> permissionToGive = new HashMap<>();
        for (String per : optionalPermissionsForMannager.returnPermissionList()) {
            permissionToGive.put(per, true);
        }
        List<User.Permission> Permissions = makePermissions(permissionToGive);
        Response responseEditPer = client.editManagerPermissions(storeID, 6, Permissions);
        client.Logout();
        assertTrue(responseEditPer.getIsErr());
    }

    //case 4.6.3 sad edit permissions, manager is not manage the store
    @Test
    void SadAddPermissionsNotManage() {
        Response optionalPermissionsForManager = client.GetPossiblePermissionsToManager(storeID);

        //this test give all permissions to manager
        HashMap<String, Boolean> permissionToGive = new HashMap<>();
        for (String per : optionalPermissionsForManager.returnPermissionList()) {
            permissionToGive.put(per, true);
        }
        List<User.Permission> Permissions = makePermissions(permissionToGive);
        Response responseEditPer = client.editManagerPermissions(storeID, newUserID, Permissions);
        client.Logout();
        assertTrue(responseEditPer.getIsErr());
    }

    //case 4.6.4 sad edit permissions, manager is not manage the store
    @Test
    void SadAddPermissionsNotAppointment() {
        client.connectSystem();
        Integer managerId = client.Register("manager", "123");
        client.Login("manager", "123");
        client.Logout();

        client.Login("Elinor", "123");
        client.addOwner(storeID,newUserID);
        client.addManager(storeID, managerId);
        client.Logout();


        client.Login("Nofet", "123");
        Response optionalPermissionsForManager = client.GetPossiblePermissionsToManager(storeID);

        //this test give all permissions to manager
        HashMap<String, Boolean> permissionToGive = new HashMap<>();
        for (String per : optionalPermissionsForManager.returnPermissionList()) {
            permissionToGive.put(per, true);
        }
        List<User.Permission> Permissions = makePermissions(permissionToGive);
        Response responseEditPer = client.editManagerPermissions(storeID, managerId, Permissions);
        client.Logout();
        assertTrue(responseEditPer.getIsErr());
    }

    //endregion

    //region requirement 4.7: Remove manager tests
    @Test
    void HappyRemoveManager() {
        client.addManager(storeID, newUserID);

        boolean b1 = client.removeManager(storeID, newUserID).getIsErr();
        client.Logout();
        client.Login("Nofet", "123");
        List<DummyStore> managers = client.showManagerStores().getStores();
        assertFalse(b1);
        assertEquals(managers.size(), 0);
    }

    @Test
    void SadRemoveManagerNoPermission() {
        client.connectSystem();
        Integer newManagerID2 = client.Register("Roee", "123");

        client.Login("Elinor", "123");
        client.addManager(storeID, newUserID);
        client.addManager(storeID, newManagerID2);
        client.Logout();

        client.Login("Roee", "123");
        boolean b1 = client.removeManager(storeID, newUserID).getIsErr();
        client.Logout();
        client.Login("Nofet", "123");
        List<DummyStore> managers = client.showManagerStores().getStores();
        assertEquals(managers.size(), 1);
        assertTrue(b1);
    }

    @Test
    void SadRemoveManagerNoManager() {
        boolean b1 = client.removeManager(storeID, newUserID).getIsErr();
        assertTrue(b1);
    }
    //endregion

    //region requirement 4.9: Information on officials tests
    @Test
    void HappyShowOfficialsInfo() {
        client.connectSystem();
        Integer newManagerID = client.Register("hadas", "123");

        client.Login("Elinor", "123");
        client.addOwner(storeID, newUserID);
        client.addManager(storeID, newManagerID);
        Response res = client.showStoreWorkers(storeID);
        assertFalse(res.getIsErr());
    }

    @Test
    void SadNotOwner() {
        client.connectSystem();
        client.Register("nofet", "123");
        client.Login("nofet", "123");
        Response res = client.showStoreWorkers(storeID);
        assertTrue(res.getIsErr());
    }

    @Test
    void SadWrongStoreID() {
        Response res = client.showStoreWorkers(storeID+1);
        assertTrue(res.getIsErr());
    }
    //endregion

    //region requirement 4.11: Store history tests
    @Test
    void HappyShowStoreHistory() {
        client.addProduct(storeID, "Sneakers", "Shoes", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(storeProducts1,"Sneakers");
        client.Logout();

        client.Login("Nofet", "123");
        client.addProductToCart(storeID, productID, 2);
        client.subscriberPurchase("123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        client.Logout();

        client.Login("Elinor", "123");
        Response response = client.ownerStoreHistory(storeID);
        List<DummyShoppingHistory> history = response.returnHistoryList();
        assertFalse(response.getIsErr());
        assertEquals(history.size(), 1);
    }

    @Test
    void SadEmptyStoreHistory() {
        client.addProduct(storeID, "Sneakers", "Shoes", 80.0, 25);

        Response response = client.ownerStoreHistory(storeID);
        List<DummyShoppingHistory> history = response.returnHistoryList();
        assertFalse(response.getIsErr());
        assertEquals(history.size(), 0);
    }

    @Test
    void SadStoreID() {
        Response response = client.ownerStoreHistory(storeID+1);
        List<DummyShoppingHistory> history = response.returnHistoryList();
        assertTrue(response.getIsErr());
        assertEquals(history.size(), 0);
    }
    //endregion

/*
    //region requirement 4.12 : get daily Income for store tests
    @Test
    void HappyDailyIncomeForStore(){
        client.
        Response res= tradingSystemImpl.getDailyIncomeForStore(NofetID,NofetStore,NconnID);
        Double DailyIncome=(Double) res.getReturnObject().get("DailyIncome");
        assertEquals(DailyIncome, 50.0);
    }

    @Test
    void HappyDailyIncomeForStore_NotExistPurchase(){
        Store New=new Store("New",NofetID);
        tradingSystemImpl.stores.put(New.getId(),New);
        tradingSystemImpl.subscribers.get(NofetID).AddStore(New.getId());
        Response res= tradingSystemImpl.getDailyIncomeForStore(NofetID,New.getId(),NconnID);
        Double DailyIncome=(Double) res.getReturnObject().get("DailyIncome");
        assertEquals(DailyIncome, 0);
    }

    @Test
    void SadDailyIncomeForStore_UserNotOwnerOfTheStore(){
        Store New=new Store("New",NofetID);
        tradingSystemImpl.stores.put(New.getId(),New);
        tradingSystemImpl.subscribers.get(NofetID).AddStore(New.getId());
        Response res= tradingSystemImpl.getDailyIncomeForStore(ElinorID,New.getId(),EconnID);
        assertEquals(res.getMessage(),"getDailyIncomeForStore: The user " + ElinorID + " is not the owner of the store");
    }

    // end region
*/
}
