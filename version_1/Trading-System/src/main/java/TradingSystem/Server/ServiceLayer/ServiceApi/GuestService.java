
package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "app")
//server localhost is 8080 and react localhost is 3000 - we need to crossOrigin to communicate between the two.
//but, if we think security its a problem because everybody can control and get our info.
//todo: define who we want to cross origin
@CrossOrigin("*") 
public class GuestService {
    
  @Autowired
  SimpMessagingTemplate template;

    private final TradingSystem tradingSystem = TradingSystem.getInstance();
    // 2.1 test
    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody Map<String, Object> obj) {
        template.convertAndSend("/topic/message", obj);
        System.out.println("-------------------------------------------");
        return new ResponseEntity<>(HttpStatus.OK);
    }
  
    @MessageMapping("/test")
    @SendTo("/topic/message")
    public void test(@Payload Map<String, Object> obj){
        System.out.println("testtttt");
        String connID = (String) obj.get("connID");
//        String path = String.format("/topic/%s", connID);
        String path = "/topic/greetings";
        System.out.println(path);
        template.convertAndSend(path, new Response(false, "Hello Welcome to Trading System"));
//        return new Response(false, "Hello Welcome to Trading System");
    }

    @GetMapping("clear_system")
    public Response ClearSystem(){
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
    @GetMapping("/home")
    public Response ConnectSystem(){
        System.out.println("eeeeeeee");
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
     * @param obj:{
     *  "connID": String
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
    @MessageMapping("register")
    public Response Register(@Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        String userName = (String) obj.get("userName");
        String password = (String) obj.get("password");
        Response res = this.tradingSystem.Register(connID, userName, password);
        tradingSystem.printUsers();
        template.convertAndSend(String.format("/topic/%s", connID), res);
        return res;
    }

    /**
     * @requirement 2.4
     *
     * @param obj:{
     *  "connID": String
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
    @MessageMapping("login")
    public Response Login(@Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        String userName = (String) obj.get("userName");
        String password = (String) obj.get("password");
        Response res = this.tradingSystem.Login(connID, userName, password);
        template.convertAndSend(String.format("/topic/%s", connID), res);
        tradingSystem.printUsers();
        return res;
    }

    /**
     * @requirement 2.5
     *
     * @param obj:{
     *  "connID": String
     * }
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
    @MessageMapping("stores")
    public Response ShowAllStores(@Payload Map<String, Object> obj) {
        String connID = (String) obj.get("connID");
        Response res = this.tradingSystem.ShowAllStores();
        template.convertAndSend(String.format("/topic/%s", connID), res);
        return res;
    }

    /**
     * @requirement 2.5
     *
     * @param storeID: int (path)
     * @param obj:{
     *  "connID": String
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
    @MessageMapping("store/{storeID}/products")
    public Response ShowStoreProducts(@DestinationVariable int storeID, @Payload Map<String, Object> obj) {
        Response res = this.tradingSystem.ShowStoreProducts(storeID);
        String connID = (String) obj.get("connID");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        return res;
    }


    /**
     * @requirement 2.6
     *
     * @param obj:{
     *  "connID": String
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
    @MessageMapping("search")
    public Response Search(@Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        String name = (String) obj.get("name");
        boolean productNameMode = (boolean) obj.get("ProductName");
        boolean productCategoryMode = (boolean) obj.get("ProductCategory");
        int minPrice = (int) obj.get("minPrice");
        int maxPrice = (int) obj.get("maxPrice");
        int pRank = (int) obj.get("pRank");
        int sRank = (int) obj.get("sRank");
        Response res;
        if(productNameMode & !productCategoryMode)
            res = tradingSystem.SearchProduct(name, null, minPrice, maxPrice);
        else if(!productNameMode & productCategoryMode)
            res = tradingSystem.SearchProduct(null, name, minPrice, maxPrice);
        else
            res = new Response(true, "Input Error");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        return res;
    }


    /**
     * @requirement 2.7
     *
     * @param obj:{
     *  "connID": String
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
    @MessageMapping("shopping_cart/add_product")
    public Response AddProductToCart(@Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        int storeID = (int) obj.get("storeID");
        int productID = (int) obj.get("productID");
        int quantity = (int) obj.get("quantity");
        Response res = tradingSystem.AddProductToCart(connID, storeID, productID, quantity);
        res.AddConnID(connID);
        tradingSystem.printUsers();
        template.convertAndSend(String.format("/topic/%s", connID), res);
        return res;
    }

    /**
     * @requirement 2.8
     *
     * @param obj:{
     *  "connID": String
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
    @MessageMapping("shopping_cart")
    public Response ShowShoppingCart(@Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        Response res = this.tradingSystem.ShowShoppingCart(connID);
        res.AddConnID(connID);
        template.convertAndSend(String.format("/topic/%s", connID), res);
        return res;
    }

    /**
     * @requirement 2.8
     *
     * @param obj:{
     *  "connID": String
     *  "storeID": int
     *  "productID": int
     * }
     * @return NewResponse{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @MessageMapping("shopping_cart/remove_product")
    public Response RemoveProductFromCart(@Payload Map<String, Object> obj){
       String connID = (String) obj.get("connID");
       int storeID = (int) obj.get("storeID");
       int productID = (int) obj.get("productID");
       Response res = tradingSystem.RemoveProductFromCart(connID, storeID, productID);
       res.AddConnID(connID);
       template.convertAndSend(String.format("/topic/%s", connID), res);
       return res;
    }

    /**
     * @requirement 2.8
     *
     * @param obj:{
     *  "connID": String
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
    @MessageMapping("shopping_cart/edit_product")
    public Response EditProductQuantityFromCart(@Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        int storeID = (int) obj.get("storeID");
        int productID = (int) obj.get("productID");
        int quantity = (int) obj.get("quantity");
        Response res = tradingSystem.editProductQuantityFromCart(connID, storeID, productID, quantity);
        res.AddConnID(connID);
        template.convertAndSend(String.format("/topic/%s", connID), res);
        return res;
    }

    /**
     * @requirement 2.9
     *
     * @param obj:{
     *  "connID": String
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
    @MessageMapping("shopping_cart/purchase")
    public Response guestPurchase(@Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        String name = (String) obj.get("name");
        String credit_number = (String) obj.get("credit_number");
        String phone_number = (String) obj.get("phone_number");
        String address = (String) obj.get("address");
        Response res = tradingSystem.guestPurchase(connID, name, credit_number, phone_number, address);
        template.convertAndSend(String.format("/topic/%s", connID), res);
        return res;
    }


}
