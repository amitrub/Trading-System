package TradingSystem.Server.Unit_tests.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ManagerOperationTest {

    @Autowired
    TradingSystemImpl tradingSystem;

    String EconnID;
    String NconnID;
    int ElinorID;
    int NofetID;
    int NofetStore;
    int ElinorStore;

    @Before
    public void setUp() {
        NconnID = tradingSystem.ConnectSystem().returnConnID();
        Response r1= tradingSystem.Register(NconnID, "nofet", "123");
        NofetID= r1.returnUserID();
        NconnID= tradingSystem.Login(NconnID, "nofet", "123").returnConnID();

        EconnID = tradingSystem.ConnectSystem().returnConnID();
        Response r2= tradingSystem.Register(EconnID, "elinor", "123");
        ElinorID= r2.returnUserID();
        EconnID= tradingSystem.Login(EconnID, "elinor", "123").returnConnID();

        tradingSystem.AddStore(NofetID,NconnID,"NofetStore");
        tradingSystem.AddStore(ElinorID,EconnID,"ElinorStore");
        NofetStore = tradingSystem.getStoreIDByName("NofetStore");
        ElinorStore = tradingSystem.getStoreIDByName("ElinorStore");

    }

    @Test
    public void ManagerOperationsSuccess() {
        tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, NofetID);
        LinkedList<PermissionEnum.Permission> p=new LinkedList<>();
        p.add(PermissionEnum.Permission.AddProduct);
        tradingSystem.EditManagerPermissions(ElinorID,EconnID,ElinorStore,NofetID,p);
        Response response = tradingSystem.AddProductToStore(NofetID, NconnID, ElinorStore, "Watermelon", "Fruits", 20.0, 10);
        Assertions.assertFalse(response.getIsErr());

        //check that add product was successful
        Integer productID = tradingSystem.stores.get(ElinorStore).getProductID("Watermelon");
        assertTrue(productID != -1);
    }

    @Test
    public void ManagerOperationsFailed() {
        String newConnID = tradingSystem.ConnectSystem().returnConnID();
        Response r2= tradingSystem.Register(newConnID, "Reut", "123");
        Integer newUSerID= r2.returnUserID();

        tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, NofetID);
        LinkedList<PermissionEnum.Permission> p=new LinkedList<>();
        p.add(PermissionEnum.Permission.AddProduct);
        tradingSystem.EditManagerPermissions(ElinorID,EconnID,ElinorStore,NofetID,p);
        Response response = tradingSystem.AddNewOwner(NofetID, NconnID, ElinorStore, newUSerID);
        assertTrue(response.getIsErr());

        //check that add owner was failed
        Integer size = tradingSystem.stores.get(ElinorStore).OwnersID().size();
        Assertions.assertEquals(size, 1);
    }

    @Test
    public void ManagerOperations_SadNotManager() {
        LinkedList<PermissionEnum.Permission> p=new LinkedList<>();
        p.add(PermissionEnum.Permission.AddProduct);
        Response res = tradingSystem.EditManagerPermissions(ElinorID,EconnID,ElinorStore,NofetID,p);
        assertTrue(res.getIsErr());
    }
}
