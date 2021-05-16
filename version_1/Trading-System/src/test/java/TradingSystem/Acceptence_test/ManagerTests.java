package TradingSystem.Acceptence_test;

import TradingSystem.Client.Client_Driver;
import TradingSystem.Client.Client_Interface;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ManagerTests {
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
    //endregion

    //region requirement 5.1

    @Test
    void PermissionsHappy() {
        Integer newUserID = client.Register("Reut", "123");

        client.Register("Elinor", "123");
        client.Login("Elinor", "123");
        client.openStore("Adidas");
        Integer storeID = getStoreID(client.showAllStores().getStores(),"Adidas");
        client.addManager(storeID, newUserID);
        LinkedList<User.Permission> p=new LinkedList<>();
        p.add(User.Permission.AddProduct);
        client.editManagerPermissions(storeID, newUserID, p);
        client.Logout();

        client.Login("Reut", "123");
        Response response = client.addProduct(storeID, "T-Shirt", "Tops", 100.0, 20);
        assertFalse(response.getIsErr());

        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts1.size(), 1);
    }


    @Test
    void PermissionsSad() {
        Integer newUserID = client.Register("Reut", "123");

        client.Register("Elinor", "123");
        client.Login("Elinor", "123");
        client.openStore("Adidas");
        Integer storeID = getStoreID(client.showAllStores().getStores(),"Adidas");
        client.addManager(storeID, newUserID);
        LinkedList<User.Permission> p=new LinkedList<>();
        p.add(User.Permission.AppointmentOwner);
        client.Logout();

        client.Login("Reut", "123");
        Response response = client.addProduct(storeID, "T-Shirt", "Tops", 100.0, 20);
        assertTrue(response.getIsErr());

        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts1.size(), 0);
    }

    //endregion
}
