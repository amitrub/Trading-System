package TradingSystem.Server.DomainLayer.TradingSystemComponent;


import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import java.util.List;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertTrue;


import static org.junit.jupiter.api.Assertions.*;

class TradingSystemTest {

    TradingSystem tradingSystem=TradingSystem.getInstance();

    String connID;
    int userID, storeid,productId;

    @BeforeEach
    void setUp() {
        connID= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID,"reutlevy","8119");
        userID= response.returnUserID();
        connID= tradingSystem.Login(connID,"reutlevy","8119").returnConnID();


    }

    @Test
    void connectSystem() {
        Response response= tradingSystem.ConnectSystem();
        assertTrue(response.returnConnID()!="" && response.getIsErr()==false);
    }

    @Test
    void exitGood() {
        String connId= tradingSystem.ConnectSystem().returnConnID();
        Response response=tradingSystem.Exit(connId);
        assertFalse(response.getIsErr());
    }

    @Test
    void exiBad() {
        String connId= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Exit(connId);
        Response response=tradingSystem.Exit(connId);
        assertTrue(response.getIsErr());
    }

    @Test
    void registerGood() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID,"reutlevy30","8111996");
        assertFalse(response.getIsErr() && response.returnUserID()<0);
    }
    @Test
    void registerExistUser() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID,"reutlevy30","8111996");
        response= tradingSystem.Register(connID,"reutlevy30","reut");
        assertTrue(response.getIsErr());
    }
    //TODO- implement this
    void registerInvalidPassword() {

    }

    @Test
    void loginSucess() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(connID,"reutlevy30","811199");
        Response response=tradingSystem.Login(connID,"reutlevy30","811199");
        assertFalse(response.getIsErr() && response.returnUserID()<0);
    }
    @Test
    void loginWrongUserName() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        Response response=tradingSystem.Login(connID,"reutlevy3","811199");
        assertTrue(response.getIsErr());
    }
    @Test
    void loginWrongPassword() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(connID,"reutlevy30","811199");
        Response response=tradingSystem.Login(connID,"reutlevy30","8111996");
        assertTrue(response.getIsErr());
    }

    @Test
    void showAllStoresGood() {
        tradingSystem.AddStore(userID,connID,"Store");
        Response res = tradingSystem.ShowAllStores();
        List<DummyStore> list = res.returnStoreList();
        System.out.println(list.size());
        assertEquals(list.size(),1);
    }

    //TODO- figure out how to test
    @Test
    void showAllStoresBad() {

    }

    @Test

    void AddStoreSuccess() {
        Response response= tradingSystem.AddStore(userID,connID,"Store3");
        assertFalse(response.getIsErr());
    }

    @Test
    void AddStoreNotSubscriber() {
        connID= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.AddStore(11,connID,"Store3");
        assertTrue(response.getIsErr());
    }

    @Test
    void AddStoreSameName() {
        tradingSystem.AddStore(userID,connID,"Store3");
        Response response= tradingSystem.AddStore(userID,connID,"Store3");
        assertTrue(response.getIsErr());
    }



    void Logout() {
        Response response= tradingSystem.Logout(connID);
        assertFalse(response.getIsErr());
    }

    @Test
    void RemoveProductSuccess(){
        Store store= new Store("store11", userID);
        tradingSystem.AddStore(userID,connID,"store11");
        for(Store store1: tradingSystem.stores.values()){
            if(store1.getName().equals("store11")){
                storeid=store1.getId();
                tradingSystem.AddProductToStore(userID,connID,storeid,"prod3","food",11.0,9);
                productId= store1.getProductID("prod3");
            }
        }
        System.out.println(productId);
        Response response=tradingSystem.RemoveProduct(userID,storeid,productId,connID);
        assertFalse(response.getIsErr());
    }
    @Test
    void RemoveProductInvalidPermmison(){
        Store store= new Store("store11", userID);
        tradingSystem.AddStore(userID,connID,"store11");
        for(Store store1: tradingSystem.stores.values()){
            if(store1.getName().equals("store11")){
                storeid=store1.getId();
                tradingSystem.AddProductToStore(userID,connID,storeid,"prod3","food",11.0,9);
                productId= store1.getProductID("prod3");
            }
        }
        String connID1= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID1,"reutlevy30","8119");
        int userID1= response.returnUserID();
        connID1= tradingSystem.Login(connID1,"reutlevy30","8119").returnConnID();
        response=tradingSystem.RemoveProduct(userID1,storeid,productId,connID1);
        assertTrue(response.getIsErr());
    }

    @Test
    void RemoveProductNotExist(){
        Store store= new Store("store11", userID);
        tradingSystem.AddStore(userID,connID,"store11");
        Product product=new Product(4,"prod4","food",7.0,11);
        Response response=tradingSystem.RemoveProduct(userID,storeid,product.getProductID(),connID);
        assertTrue(response.getIsErr());
    }

    @Test
    void EditProductSuccess(){
        Store store= new Store("store11", userID);
        tradingSystem.AddStore(userID,connID,"store11");
        for(Store store1: tradingSystem.stores.values()){
            if(store1.getName().equals("store11")){
                storeid=store1.getId();
                tradingSystem.AddProductToStore(userID,connID,storeid,"prod3","food",11.0,9);
                productId= store1.getProductID("prod3");
            }
        }
        System.out.println(productId);
        Response response=tradingSystem.EditProduct(userID,connID,storeid,productId,"prod4","food",12.0,9);
        assertFalse(response.getIsErr());
    }

    @Test
    void EditProductInvalidPremmision(){
        Store store= new Store("store11", userID);
        tradingSystem.AddStore(userID,connID,"store11");
        for(Store store1: tradingSystem.stores.values()){
            if(store1.getName().equals("store11")){
                storeid=store1.getId();
                tradingSystem.AddProductToStore(userID,connID,storeid,"prod3","food",11.0,9);
                productId= store1.getProductID("prod3");
            }
        }
        String connID1= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID1,"reutlevy30","8119");
        int userID1= response.returnUserID();
        connID1= tradingSystem.Login(connID1,"reutlevy30","8119").returnConnID();
        response=tradingSystem.EditProduct(userID1,connID1,storeid,productId,"prod4","food",12.0,9);
        assertTrue(response.getIsErr());
    }

    @Test
    void EditProductnotexist(){
        Store store= new Store("store11", userID);
        tradingSystem.AddStore(userID,connID,"store11");
        Product product=new Product(4,"prod4","food",7.0,11);
        Response response=tradingSystem.EditProduct(userID,connID,storeid,product.getProductID(),"prod4","food",12.0,9);
        assertTrue(response.getIsErr());
    }

    @Test
    void EditProductWrongPrice(){
        Store store= new Store("store11", userID);
        tradingSystem.AddStore(userID,connID,"store11");
        for(Store store1: tradingSystem.stores.values()){
            if(store1.getName().equals("store11")){
                storeid=store1.getId();
                tradingSystem.AddProductToStore(userID,connID,storeid,"prod3","food",11.0,9);
                productId= store1.getProductID("prod3");
            }
        }
        System.out.println(productId);
        Response response=tradingSystem.EditProduct(userID,connID,storeid,productId,"prod4","food",-12.0,9);
        assertTrue(response.getIsErr());
    }

    @Test
    void EditProductWrongQuantity(){
        Store store= new Store("store11", userID);
        tradingSystem.AddStore(userID,connID,"store11");
        for(Store store1: tradingSystem.stores.values()){
            if(store1.getName().equals("store11")){
                storeid=store1.getId();
                tradingSystem.AddProductToStore(userID,connID,storeid,"prod3","food",11.0,9);
                productId= store1.getProductID("prod3");
            }
        }
        System.out.println(productId);
        Response response=tradingSystem.EditProduct(userID,connID,storeid,productId,"prod4","food",12.0,-9);
        assertTrue(response.getIsErr());
    }

    @Test
    void ChangeProductQuantitySuccess(){
        Store store= new Store("store11", userID);
        tradingSystem.AddStore(userID,connID,"store11");
        for(Store store1: tradingSystem.stores.values()){
            if(store1.getName().equals("store11")){
                storeid=store1.getId();
                tradingSystem.AddProductToStore(userID,connID,storeid,"prod3","food",11.0,9);
                productId= store1.getProductID("prod3");
            }
        }
        System.out.println(productId);
        Response response=tradingSystem.ChangeQuantityProduct(userID,connID,storeid,productId,10);
        assertFalse(response.getIsErr());
    }

    @Test
    void ChangeProductQunatityInvalidPremmision(){
        Store store= new Store("store11", userID);
        tradingSystem.AddStore(userID,connID,"store11");
        for(Store store1: tradingSystem.stores.values()){
            if(store1.getName().equals("store11")){
                storeid=store1.getId();
                tradingSystem.AddProductToStore(userID,connID,storeid,"prod3","food",11.0,9);
                productId= store1.getProductID("prod3");
            }
        }
        String connID1= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID1,"reutlevy30","8119");
        int userID1= response.returnUserID();
        connID1= tradingSystem.Login(connID1,"reutlevy30","8119").returnConnID();
        response=tradingSystem.ChangeQuantityProduct(userID1,connID1,storeid,productId,10);
        assertTrue(response.getIsErr());
    }

    @Test
    void ChangeProductWrongQuantity(){
        Store store= new Store("store11", userID);
        tradingSystem.AddStore(userID,connID,"store11");
        for(Store store1: tradingSystem.stores.values()){
            if(store1.getName().equals("store11")){
                storeid=store1.getId();
                tradingSystem.AddProductToStore(userID,connID,storeid,"prod3","food",11.0,9);
                productId= store1.getProductID("prod3");
            }
        }
        System.out.println(productId);
        Response response=tradingSystem.ChangeQuantityProduct(userID,connID,storeid,productId,-10);
        assertTrue(response.getIsErr());
    }


}