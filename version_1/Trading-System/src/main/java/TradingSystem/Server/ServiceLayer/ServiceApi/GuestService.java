
package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
//@RequestMapping(path = "app")
//server localhost is 8080 and react localhost is 3000 - we need to crossOrigin to communicate between the two.
//but, if we think security its a problem because everybody can control and get our info.
//todo: define who we want to cross origin
@CrossOrigin("*") public class GuestService {
    private final TradingSystem tradingSystem = TradingSystem.getInstance();
    // 2.1 test

    @MessageMapping("/test")
    @SendTo("/topic/{connID}")
    public Response test(@DestinationVariable("connID") String connID){
        System.out.println("testtttt");
        return new Response(false, "Hello Welcome to Trading System");
    }

    @GetMapping("clear_system")
    public Response ClearSystem(){
        System.out.println("777777777777777777777777777777");
        this.tradingSystem.ClearSystem();
        return new Response();
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
    public Response ConnectSystem(){
        Response res = this.tradingSystem.ConnectSystem();
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
    public Response Exit(@RequestHeader("connID") String connID){
        Response res = this.tradingSystem.Exit(connID);
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
    public Response Register(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        String userName = (String) obj.get("userName");
        String password = (String) obj.get("password");
        Response res = this.tradingSystem.Register(connID, userName, password);
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
    public Response Login(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        String userName = (String) obj.get("userName");
        String password = (String) obj.get("password");
        Response res = this.tradingSystem.Login(connID, userName, password);
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
    public Response ShowAllStores() {
        Response res = this.tradingSystem.ShowAllStores();
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
    public Response ShowStoreProducts(@PathVariable int storeID) {
        Response res = this.tradingSystem.ShowStoreProducts(storeID);
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
    public Response Search(@RequestBody Map<String, Object> obj){
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
            return new Response(true, "Input Error");
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
    public Response AddProductToCart(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        int storeID = (int) obj.get("storeID");
        int productID = (int) obj.get("productID");
        int quantity = (int) obj.get("quantity");
        Response res = tradingSystem.AddProductToCart(connID, storeID, productID, quantity);
        res.AddConnID(connID);
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
    public Response ShowShoppingCart(@RequestHeader("connID") String connID){
        Response res = this.tradingSystem.ShowShoppingCart(connID);
        res.AddConnID(connID);
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
     * @return NewResponse{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @PostMapping("shopping_cart/remove_product")
    public Response RemoveProductFromCart(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj)
    {
       int storeID = (int) obj.get("storeID");
       int productID = (int) obj.get("productID");
       Response res = tradingSystem.RemoveProductFromCart(connID, storeID, productID);
       res.AddConnID(connID);
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
    public Response EditProductQuantityFromCart(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        int storeID = (int) obj.get("storeID");
        int productID = (int) obj.get("productID");
        int quantity = (int) obj.get("quantity");
        Response res = tradingSystem.editProductQuantityFromCart(connID, storeID, productID, quantity);
        res.AddConnID(connID);
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
    public Response guestPurchase(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        String name = (String) obj.get("name");
        String credit_number = (String) obj.get("credit_number");
        String phone_number = (String) obj.get("phone_number");
        String address = (String) obj.get("address");
        Response res = tradingSystem.guestPurchase(connID, name, credit_number, phone_number, address);
        return res;
    }


}
