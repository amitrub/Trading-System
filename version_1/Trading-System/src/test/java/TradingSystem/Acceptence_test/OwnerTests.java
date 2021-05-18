package TradingSystem.Acceptence_test;

import TradingSystem.Client.Client_Driver;
import TradingSystem.Client.Client_Interface;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.Assert.*;

public class OwnerTests {

    Client_Interface client = Client_Driver.getClient();

    @BeforeEach
    void setUp() {
        client.clearSystem();
        client.connectSystem();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Permissions;
    }
    //endregion



    //region requirement 4.1.1: Add Product Tests
    @Test
    void HappyAddProduct() {
        client.Register("Gal", "123");
        client.Login("Gal", "123");
        client.openStore("Scoop");
        Integer storeID = getStoreID(client.showAllStores().getStores(),"Scoop");

        //happy add
        Response response = client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts1.size(), 1);
        assertFalse(response.getIsErr());
    }

    @Test
    void SadAddPrice() {
        client.Register("Lior", "123");
        client.Login("Lior", "123");
        client.openStore("Shoes");
        Integer storeID =  getStoreID(client.showAllStores().getStores(),"Shoes");

        //sad add - product price illegal
        Response response = client.addProduct(storeID, "Classic Heels", "Heels", -50.0, 25);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts2.size(), 0);
        assertTrue(response.getIsErr());
    }

    @Test
    void SadAddNameTaken() {
        client.Register("Ori", "123");
        client.Login("Ori", "123");
        client.openStore("Ice Cube");
        Integer storeID = getStoreID(client.showAllStores().getStores(),"Ice Cube");
        client.addProduct(storeID, "Arma Heels", "Heels", 60.0, 25);

        Response response = client.addProduct(storeID, "Arma Heels", "Heels", 200.0, 25);
        List<DummyProduct> storeProducts3 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts3.size(), 1);
        assertTrue(response.getIsErr());
    }

    @Test
    void SadAddQuantity() {
        client.Register("Sapir", "123");
        client.Login("Sapir", "123");
        client.openStore("To-Go");
        Integer storeID = getStoreID(client.showAllStores().getStores(),"To-Go");

        //sad add - product quantity is illegal
        Response response = client.addProduct(storeID, "Short Heels", "Heels", 60.0, -10);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts1.size(), 0);
        assertTrue(response.getIsErr());
    }

    //endregion
    //region requirement 4.1.2: Remove Product Tests
    @Test
    void HappyRemove() {
        //Prepare
        client.Register("Oriya", "123");
        client.Login("Oriya", "123");
        client.openStore("Ran Sport");
        List<DummyStore> stores=client.showAllStores().getStores();
        Integer storeID = getStoreID(stores, "Ran Sport");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(storeProducts1,"Arma Heels");
        Integer preSize = storeProducts1.size();

        //happy remove
        boolean b1 = client.removeProduct(storeID, productID).getIsErr();
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts2.size(), preSize-1);
        assertFalse(b1);
    }

    @Test
    void BadRemove() {
        client.Register("Oriyan", "123");
        client.Login("Oriyan", "123");
        client.openStore("Mega Sport");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Mega Sport");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
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
        client.Register("Shani", "123");
        client.Login("Shani", "123");
        client.openStore("WeShoes");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "WeShoes");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(storeProducts1,"Arma Heels");

        //happy edit price
        Response response = client.editProduct(storeID, productID, "Arma Heels", "Heels", 100.0,25);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts2.get(0).getPrice(), 100.0, 0);
        assertEquals(storeProducts2.size(), 1);
        assertFalse(response.getIsErr());
    }

    @Test
    void HappyEditQuantity() {
        client.Register("Sha", "123");
        client.Login("Sha", "123");
        client.openStore("WeShoes");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "WeShoes");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(storeProducts1,"Arma Heels");

        //happy edit quantity
        Response response = client.editProduct(storeID, productID, "Arma Heels", "Heels", 80.0,35);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts2.get(0).getQuantity(), 35);
        assertEquals(storeProducts2.size(), 1);
        assertFalse(response.getIsErr());
    }

    @Test
    void SadEditPrice() {
        client.Register("Shira", "123");
        client.Login("Shira", "123");
        client.openStore("Gali");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Gali");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(storeProducts1,"Arma Heels");

        //sad edit
        Response response = client.editProduct(storeID, productID, "Arma Heels", "Heels", -120.0,25);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts2.get(0).getPrice(), 80.0, 0);
        assertEquals(storeProducts2.size(), 1);
        assertTrue(response.getIsErr());
    }

    @Test
    void SadEditQuantity() {
        client.Register("Lin", "123");
        client.Login("Lin", "123");
        client.openStore("Gali");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Gali");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(storeProducts1,"Arma Heels");
        Integer preSize = client.showStoreProducts(storeID).returnProductList().size();

        //sad edit
        Response response = client.editProduct(storeID, productID, "Arma Heels", "Heels", 120.0,-25);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID).returnProductList();
        Integer newSize = storeProducts2.size();
        assertEquals(storeProducts2.get(0).getQuantity(), 25);
        assertEquals(newSize, preSize);
        assertTrue(response.getIsErr());
    }

    @Test
    void SadEditNonExist() {
        client.Register("Din", "123");
        client.Login("Din", "123");
        client.openStore("Gali");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Gali");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(storeProducts1,"Arma Heels");
        client.removeProduct(storeID, productID);
        Integer preSize = client.showStoreProducts(storeID).returnProductList().size();

        //sad edit
        Response response = client.editProduct(storeID, productID, "Arma Heels", "Heels", 120.0,25);
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
        Integer newOwnerID = client.Register("nofet", "123");

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");

        boolean b1 = client.addOwner(storeID, newOwnerID).getIsErr();
        client.Logout();
        client.connectSystem();
        client.Login("nofet", "123");
        List<DummyStore> owners = client.showOwnerStores().getStores();
        assertFalse(b1);
        assertEquals(owners.size(), 1);
    }

    @Test
    void SadAddIsNotOwner() {
        client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");
        client.Logout();

        Integer id1 = client.Register("elinor", "123");

        client.Register("roee", "123");
        client.Login("roee", "123");

        boolean b1 = client.addOwner(storeID, id1).getIsErr();
        client.Logout();
        client.Login("elinor", "123");
        List<DummyStore> owners = client.showOwnerStores().getStores();
        assertTrue(b1);
        assertEquals(owners.size(), 0);
    }

    @Test
    void SadAddIllegalIDOwner() {
        Integer id = client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");

        boolean b1 = client.addOwner(storeID, id+1).getIsErr();
        List<DummyStore> owners = client.showOwnerStores().getStores();
        assertTrue(b1);
        assertEquals(owners.size(), 1);
    }

    @Test
    void SadAddDoubleAppointmentOwner() {
        Integer newOwnerID = client.Register("nofet", "123");

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");

        client.addOwner(storeID, newOwnerID);
        boolean b1 = client.addOwner(storeID, newOwnerID).getIsErr();
        client.Logout();
        client.Login("nofet", "123");
        List<DummyStore> owners = client.showOwnerStores().getStores();
        assertTrue(b1);
        assertEquals(owners.size(), 1);
    }
    //endregion

    //region requirement 4.4: Remove owner tests
    @Test
    void HappyRemoveOwner() {
        Integer newOwnerID = client.Register("nofet", "123");

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");
        client.addOwner(storeID, newOwnerID);

        Response res = client.removeOwner(storeID, newOwnerID);
        client.Logout();
        client.connectSystem();
        client.Login("nofet", "123");
        List<DummyStore> owners = client.showOwnerStores().getStores();
        assertFalse(res.getIsErr());
        assertEquals(owners.size(), 0);
    }

    @Test
    void SadRemoveNotAppointment() {
        Integer newOwnerID1 = client.Register("nofet", "123");
        Integer newOwnerID2 = client.Register("roee", "123");

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");
        client.addOwner(storeID, newOwnerID1);
        client.addOwner(storeID, newOwnerID2);
        client.Logout();

        client.Login("nofet", "123");
        Response res = client.removeOwner(storeID, newOwnerID2);
        client.Logout();
        client.Login("roee", "123");
        List<DummyStore> owners = client.showOwnerStores().getStores();
        assertTrue(res.getIsErr());
        assertEquals(owners.size(), 1);
    }

    @Test
    void SadRemoveNotOwner1() {
        Integer newOwnerID = client.Register("nofet", "123");

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");
        client.addOwner(storeID, newOwnerID);
        client.Logout();

        client.Register("roee", "123");
        client.Login("roee", "123");

        Response res = client.removeOwner(storeID, newOwnerID);
        client.Logout();
        client.Login("nofet", "123");
        List<DummyStore> owners = client.showOwnerStores().getStores();
        assertTrue(res.getIsErr());
        assertEquals(owners.size(), 1);
    }

    @Test
    void SadRemoveNotOwner2() {
        Integer newOwnerID = client.Register("nofet", "123");

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");

        Response res = client.removeOwner(storeID, newOwnerID);
        assertTrue(res.getIsErr());
    }

    //endregion

    //region requirement 4.5: Add manager tests
    @Test
    void HappyAddManager() {
        Integer newManagerID = client.Register("nofet", "123");

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");

        boolean b1 = client.addManager(storeID, newManagerID).getIsErr();
        client.Logout();
        client.Login("nofet", "123");
        List<DummyStore> managers = client.showManagerStores().getStores();
        assertFalse(b1);
        assertEquals(managers.size(), 1);
       }

    @Test
    void SadAddIsNotManager() {
        client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");
        client.Logout();

        Integer id1 = client.Register("elinor", "123");

        client.Register("roee", "123");
        client.Login("roee", "123");

        boolean b1 = client.addManager(storeID, id1).getIsErr();
        List<DummyStore> managers = client.showManagerStores().getStores();
        assertTrue(b1);
        assertEquals(managers.size(), 0);
    }

    @Test
    void SadAddIllegalIDManager() {
        Integer id = client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");

        boolean b1 = client.addManager(storeID, id+1).getIsErr();
        assertTrue(b1);
    }

    @Test
    void SadAddDoubleAppointmentManager() {
        Integer id1 = client.Register("nofet", "123");

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");

        client.addManager(storeID, id1);
        boolean b1 = client.addManager(storeID, id1).getIsErr();
        assertTrue(b1);
    }
    
    //endregion

    //region requirement 4.6: Edit manager Permissions tests
    @Test
    void HappyAddPermissions() {
        Integer managerId = client.Register("manager", "123");

        client.Register("owner", "123");
        client.Login("owner", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");
        client.addManager(storeID, managerId);

        Response optionalPermissionsForManager = client.GetPossiblePermissionsToManager(storeID);

        //this test give all permissions to manager
        HashMap<String, Boolean> permissionToGive = new HashMap<>();
        for (String per : optionalPermissionsForManager.returnPermissionList()) {
            permissionToGive.put(per, true);
        }
        List<User.Permission> Permissions = makePermissions(permissionToGive);
        Response responseEditPer = client.editManagerPermissions(storeID, managerId, Permissions);
        client.Logout();
        assertFalse(responseEditPer.getIsErr());
    }

    //case 4.6.2 sad edit permissions, manager id not ok
    @Test
    void SadAddPermissions() {
        client.Register("owner", "123");
        client.Login("owner", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");

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

    //case 4.6.3 sad edit permissions, cant give this permissions to manager
    @Test
    void SadAddPermissionsToManager() {

    }
    //case 4.6.4 sad edit permissions, manager is not manage the store
    @Test
    void SadAddPermissionsNotManage() {
        Integer managerId = client.Register("manager", "123");
        client.Login("manager", "123");
        client.Logout();

        client.Register("owner", "123");
        client.Login("owner", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");

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

    //case 4.6.5 sad edit permissions, manager is not manage the store
    @Test
    void SadAddPermissionsNotAppointment() {
        Integer managerId = client.Register("manager", "123");
        client.Login("manager", "123");
        client.Logout();

        Integer NewOwnerId = client.Register("NewOwnerId", "123");
        client.Login("NewOwnerId", "123");
        client.Logout();

        client.Register("owner", "123");
        client.Login("owner", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");
        client.addOwner(storeID,NewOwnerId);
        client.addManager(storeID, managerId);
        client.Logout();

        client.Login("NewOwnerId", "123");
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
        Integer newManagerID = client.Register("nofet", "123");

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");
        client.addManager(storeID, newManagerID);

        boolean b1 = client.removeManager(storeID, newManagerID).getIsErr();
        assertFalse(b1);
    }

    @Test
    void SadRemoveManagerNoPermission() {
        Integer newManagerID1 = client.Register("nofet", "123");
        Integer newManagerID2 = client.Register("roee", "123");

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");
        client.addManager(storeID, newManagerID1);
        client.addManager(storeID, newManagerID2);
        client.Logout();
        client.connectSystem();

        client.Login("roee", "123");
        boolean b1 = client.removeManager(storeID, newManagerID1).getIsErr();
        assertTrue(b1);
    }

    @Test
    void SadRemoveManagerNoManager() {
        Integer newManagerID = client.Register("nofet", "123");

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");

        boolean b1 = client.removeManager(storeID, newManagerID).getIsErr();
        assertTrue(b1);
    }
    //endregion

    //region requirement 4.9: Information on officials tests
    @Test
    void HappyShowOfficialsInfo() {
        Integer newOwnerID = client.Register("nofet", "123");
        Integer newManagerID = client.Register("hadas", "123");

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        System.out.println("enter to client" +client.showAllStores());
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");

        client.addOwner(storeID, newOwnerID);
        client.addManager(storeID, newManagerID);
        Response res = client.showStoreWorkers(storeID);
        assertFalse(res.getIsErr());
    }

    @Test
    void SadNotOwner() {
        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");
        client.Logout();

        Integer newOwnerID = client.Register("nofet", "123");
        client.Login("nofet", "123");
        Response res = client.showStoreWorkers(storeID);
        assertTrue(res.getIsErr());
    }

    @Test
    void SadWrongStoreID() {
        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");

        Response res = client.showStoreWorkers(storeID+1);
        assertTrue(res.getIsErr());
    }
    //endregion

    //region requirement 4.11: Store history tests
    @Test
    void HappyShowStoreHistory() {
        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Asos");
        Integer storeID = getStoreID(client.showAllStores().getStores(),"Asos");
        client.addProduct(storeID, "Sneakers", "Shoes", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(storeProducts1,"Sneakers");
        client.Logout();

        client.Register("Yasmin", "123");
        client.Login("Yasmin", "123");
        client.addProductToCart(storeID, productID, 2);
        client.subscriberPurchase("123456789", "0521234567", "Kiryat Gat");
        client.Logout();

        client.Login("elinor", "123");
        List<DummyShoppingHistory> history = client.ownerStoreHistory(storeID).returnHistoryList();
        assertEquals(history.size(), 1);
    }

    @Test
    void SadEmptyStoreHistory() {
        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Asos");
        Integer storeID = getStoreID(client.showAllStores().getStores(),"Asos");
        client.addProduct(storeID, "Sneakers", "Shoes", 80.0, 25);

        List<DummyShoppingHistory> history = client.ownerStoreHistory(storeID).returnHistoryList();
        assertEquals(history.size(), 0);
    }

    @Test
    void SadStoreID() {
        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Asos");
        Integer storeID = getStoreID(client.showAllStores().getStores(),"Asos");
        client.addProduct(storeID, "Sneakers", "Shoes", 80.0, 25);

        List<DummyShoppingHistory> history = client.ownerStoreHistory(storeID+1).returnHistoryList();
        assertEquals(history.size(), 0);
    }
    //endregion

    //region requirement 9.1 - Real time alert
    @Test
    void PurchaseAlert() {
        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");
        client.addProduct(storeID, "Sneakers", "Shoes", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(storeProducts1,"Sneakers");
        client.Logout();

        Integer newClient = client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.addProductToCart(storeID, productID, 2);
        client.subscriberPurchase("123456789", "0521234567","Tel Aviv");

        //need to check that elinor got a message of buying a product from her store

    }

    @Test
    void RemoveAlert() {
        Integer newOwnerID = client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.Logout();

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores().getStores(), "Store");
        client.addOwner(storeID, newOwnerID);
        client.removeOwner(storeID, newOwnerID);

        //need to check that nofet got a message of remove her from owning the store
        //maybe add to response - list of messages
    }

    @Test
    void EditPolicesAlert() {
    }
    //endregion

}
