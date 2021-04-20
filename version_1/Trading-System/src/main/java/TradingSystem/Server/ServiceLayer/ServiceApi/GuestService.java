
package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.NewResponse;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api")
public class GuestService {
    private final TradingSystem tradingSystem = TradingSystem.getInstance();
    // 2.1 test

    @GetMapping("clear_system")
    public NewResponse ClearSystem(){
        this.tradingSystem.ConnectSystem();
        return new NewResponse();
    }

    /**
     * @requirement 2.1
     *
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @GetMapping("home")
    public NewResponse ConnectSystem(){
        NewResponse res = this.tradingSystem.ConnectSystem();
        tradingSystem.printUsers();
        return res;
    }

    /**
     * @requirement 2.2
     *
     * @param connID: String (Header)
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     */
    //return connID
    @GetMapping("exit")
    public NewResponse Exit(@RequestHeader("connID") String connID){
        NewResponse res = this.tradingSystem.Exit(connID);
        tradingSystem.printUsers();
        return res;
    }

    /**
     * @requirement 2.3
     *
     * @param connID: String (Header)
     * @param obj:{
     *  "userName": String
     *  "password": String
     * }
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "userID": int
     * }
     */
    @PostMapping("register")
    public NewResponse Register(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        String userName = (String) obj.get("userName");
        String password = (String) obj.get("password");
        NewResponse res = this.tradingSystem.Register(connID, userName, password);
        tradingSystem.printUsers();
        return res;
    }

    /**
     * @requirement 2.4
     *
     * @param connID: String (Header)
     * @param obj:{
     *  "userName": String
     *  "password": String
     * }
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "userID": int
     * }
     */
    @PostMapping("login")
    public NewResponse Login(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        String userName = (String) obj.get("userName");
        String password = (String) obj.get("password");
        NewResponse res = this.tradingSystem.Login(connID, userName, password);
        tradingSystem.printUsers();
        return res;
    }

    /**
     * @requirement 2.5
     *
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "stores": [{
     *      "storeID": int
     *      "storeName": String
     *  }]
     * }
     */
    @GetMapping("stores")
    public NewResponse ShowAllStores() {
        NewResponse res = this.tradingSystem.ShowAllStores();
        return res;
    }

    /**
     * @requirement 2.5
     *
     * @param storeID: int (path)
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "products": List [{
     *      "storeID": int
     *      "storeName": String
     *      "productID": int
     *      "productName": String
     *      "price": double
     *      "category": String
     *      "quantity": int
     *  }]
     * }
     */
    @GetMapping("store/{storeID}/products")
    public NewResponse ShowStoreProducts(@PathVariable int storeID) {
        NewResponse res = this.tradingSystem.ShowStoreProducts(storeID);
        return res;
    }


    /**
     * @requirement 2.6
     *
     * @param obj:{
     *  "name": String
     *  "Product Name": boolean
     *  "Product Category": boolean
     *  "minPrice": int
     *  "maxPrice": int
     *  "pRank": int
     *  "sRank": int
     * }
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "products": List [{
     *      "storeID": int
     *      "storeName": String
     *      "productID": int
     *      "productName": String
     *      "price": double
     *      "category": String
     *      "quantity": int
     *  }]
     * }
     */
    //TODO: not check yet
    @PostMapping("search")
    public NewResponse Search(@RequestBody Map<String, Object> obj){
        String name = (String) obj.get("name");
        boolean productNameMode = (boolean) obj.get("Product Name");
        boolean productCategoryMode = (boolean) obj.get("Product Category");
        int minPrice = (int) obj.get("minPrice");
        int maxPrice = (int) obj.get("maxPrice");
        int pRank = (int) obj.get("pRank");
        int sRank = (int) obj.get("sRank");
        if(productNameMode & !productCategoryMode)
            return tradingSystem.SearchProduct(name, null, minPrice, maxPrice);
        else if(!productNameMode & productCategoryMode)
            return tradingSystem.SearchProduct(null, name, minPrice, maxPrice);
        else
            return new NewResponse(true, "Input Error");
    }


    /**
     * @requirement 2.7
     *
     * @param connID: String (Header)
     * @param obj:{
     *  "storeID": int
     *  "productID": int
     *  "quantity": int
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @PostMapping("shopping_cart/add_product")
    public NewResponse AddProductToCart(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        int storeID = (int) obj.get("storeID");
        int productID = (int) obj.get("productID");
        int quantity = (int) obj.get("quantity");
        NewResponse res = tradingSystem.AddProductToCart(connID, storeID, productID, quantity);
        tradingSystem.printUsers();
        return res;
    }

    /**
     * @requirement 2.8
     *
     * @param connID: String (Header)
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "products": List [{
     *      "storeID": int
     *      "storeName": String
     *      "productID": int
     *      "productName": String
     *      "price": double
     *      "category": String
     *      "quantity": int
     *  }]
     * }
     */
    @GetMapping("shopping_cart")
    public NewResponse ShowShoppingCart(@RequestHeader("connID") String connID){
        NewResponse res = this.tradingSystem.ShowShoppingCart(connID);
        return res;
    }

    /**
     * @requirement 2.8
     *
     * @param connID: String (Header)
     * @param obj:{
     *  "storeID": int
     *  "productID": int
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @PostMapping("shopping_cart/remove_product")
    public NewResponse RemoveProductFromCart(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
//        int storeID = (int) obj.get("storeID");
//        int productID = (int) obj.get("productID");
        //TODO: not implemented
//        Response res = tradingSystem.RemoveProductFromCart(connID, storeID, productID);
        NewResponse res = new NewResponse(true, "not implemented");
        return res;
    }

    /**
     * @requirement 2.8
     *
     * @param connID: String (Header)
     * @param obj:{
     *  "storeID": int
     *  "productID": int
     *  "quantity": int
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @PostMapping("shopping_cart/edit_product")
    public NewResponse EditProductQuantityFromCart(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        int storeID = (int) obj.get("storeID");
        int productID = (int) obj.get("productID");
        int quantity = (int) obj.get("quantity");
        //TODO: not implemented
//        Response res = tradingSystem.EditProductQuantityFromCart(connID, storeID, productID, quantity);
        NewResponse res = new NewResponse(true, "not implemented");
        return res;
    }

    /**
     * @requirement 2.9
     *
     * @param connID: String (Header)
     * @param obj:{
     *  "name": String
     *  "credit_number": String
     *  "phone_number": String
     *  "address": String
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @PostMapping("shopping_cart/purchase")
    public NewResponse guestPurchase(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        String name = (String) obj.get("name");
        String credit_number = (String) obj.get("credit_number");
        String phone_number = (String) obj.get("phone_number");
        String address = (String) obj.get("address");
        NewResponse res = tradingSystem.guestPurchase(connID, name, credit_number, phone_number, address);
        return res;
    }


}
