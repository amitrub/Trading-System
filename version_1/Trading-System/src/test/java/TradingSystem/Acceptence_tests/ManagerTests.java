package TradingSystem.Acceptence_tests;

import TradingSystem.Client.Client;
import TradingSystem.Client.Client_Driver;
import TradingSystem.Client.Client_Interface;
import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ManagerTests {

    //Client_Interface client = Client_Driver.getClient();
    Client client;
    Integer storeID;
    Integer newUserID;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.clearSystem();
        client.connectSystem();
        newUserID = client.Register("Reut", "123");
        client.Logout();

        client.connectSystem();
        client.Register("Elinor", "123");
        client.Login("Elinor", "123");
        client.openStore("Adidas");
        storeID = client.getStoreIDByName("Adidas").returnStoreID();
        client.addManager(storeID, newUserID);
    }

    @AfterEach
    void tearDown() {
        client.exitSystem();
        client.clearSystem();
    }


    @Test
    void PermissionsHappy() {
        //prepare
        LinkedList<PermissionEnum.Permission> p=new LinkedList<>();
        p.add(PermissionEnum.Permission.AddProduct);
        client.editManagerPermissions(storeID, newUserID, p);
        client.Logout();

        //Issue
        client.Login("Reut", "123");
        Response response = client.addProduct(storeID, "T-Shirt", "Tops", 100.0, 20);

        //Assert
        assertFalse(response.getIsErr());
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts1.size(), 1);
    }

    @Test
    void PermissionsSad() {
        LinkedList<PermissionEnum.Permission> p=new LinkedList<>();
        p.add(PermissionEnum.Permission.AppointmentOwner);
        client.editManagerPermissions(storeID, newUserID, p);
        client.Logout();

        client.Login("Reut", "123");
        Response response = client.addProduct(storeID, "T-Shirt", "Tops", 100.0, 20);
        assertTrue(response.getIsErr());

        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        assertEquals(storeProducts1.size(), 0);
    }

    //endregion
}
