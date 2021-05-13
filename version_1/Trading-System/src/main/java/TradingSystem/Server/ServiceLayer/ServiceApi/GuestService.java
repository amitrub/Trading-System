
package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import TradingSystem.Server.ServiceLayer.LoggerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(path = "app")
@RestController
//server localhost is 8080 and react localhost is 3000 - we need to crossOrigin to communicate between the two.
//but, if we think security its a problem because everybody can control and get our info.
//todo: define who we want to cross origin
@CrossOrigin("*") 
public class GuestService {
    
  @Autowired
  SimpMessagingTemplate template;

    private final TradingSystem tradingSystem = TradingSystemImpl.getInstance();
    private static final LoggerController loggerController=LoggerController.getInstance();

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
        template.convertAndSend(String.format("/topic/%s","store1"), new Response(false, "Hello Welcome to Trading System"));
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
        System.out.println("--------------HELLO--------------");
        Response res = this.tradingSystem.ConnectSystem();
        tradingSystem.printUsers();
        res.AddTag("ConnectSystem");
        WriteToLogger(res);
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
    @GetMapping("exit")
    public Response Exit(@RequestHeader("connID") String connID){
        Response res = this.tradingSystem.Exit(connID);
        tradingSystem.printUsers();
        res.AddTag("Exit");
        WriteToLogger(res);
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
        String connID, userName, password;
        try {
            connID = (String) obj.get("connID");
            userName = (String) obj.get("userName");
            password = (String) obj.get("password");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : Register");
            System.out.println(res);
            return res;
        }
        Response res = this.tradingSystem.Register(connID, userName, password);
        tradingSystem.printUsers();
        res.AddTag("Register");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
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
        String connID, userName, password;
        try {
            connID = (String) obj.get("connID");
            userName = (String) obj.get("userName");
            password = (String) obj.get("password");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : Login");
            System.out.println(res);
            return res;
        }
        Response res = this.tradingSystem.Login(connID, userName, password);
        res.AddTag("Login");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
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
        String connID;
        try {
            connID = (String) obj.get("connID");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : ShowAllStores");
            System.out.println(res);
            return res;
        }
        Response res = this.tradingSystem.ShowAllStores();
        System.out.println(res);
        res.AddTag("ShowAllStores");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
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
        String connID;
        try {
            connID = (String) obj.get("connID");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : ShowStoreProducts");
            System.out.println(res);
            return res;
        }
        Response res = this.tradingSystem.ShowStoreProducts(storeID);
        res.AddTag("ShowStoreProducts");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 2.6
     *
     * @param obj:{
     *  "connID": String
     *  "name": String
     *  "ProductName": boolean
     *  "ProductCategory": boolean
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
    @MessageMapping("search")
    public Response Search(@Payload Map<String, Object> obj){
        String connID, name;
        boolean productNameMode, productCategoryMode;
        int minPrice, maxPrice, pRank, sRank;
        try {
            connID = (String) obj.get("connID");
            name = (String) obj.get("name");
            productNameMode = (boolean) obj.get("ProductName");
            productCategoryMode = (boolean) obj.get("ProductCategory");
            minPrice = (int) obj.get("minPrice");
            maxPrice = (int) obj.get("maxPrice");
            pRank = (int) obj.get("pRank");
            sRank = (int) obj.get("sRank");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : Search");
            System.out.println(res);
            return res;
        }
        Response res;
        System.out.println("minPrice" + minPrice);
        System.out.println("maxPrice" + maxPrice);
        System.out.println("pRank" + pRank);
        System.out.println("sRank" + sRank);
        if (productNameMode & !productCategoryMode)
            res = tradingSystem.SearchProduct(name, null, minPrice, maxPrice);
        else if (!productNameMode & productCategoryMode)
            res = tradingSystem.SearchProduct(null, name, minPrice, maxPrice);
        else
            res = new Response(true, "Input Error");
        res.AddTag("Search");
        System.out.println("Search!!!\n " + res);
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
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
        String connID;
        int storeID, productID, quantity;
        try {
            connID = (String) obj.get("connID");
            storeID = (int) obj.get("storeID");
            productID = (int) obj.get("productID");
            quantity = (int) obj.get("quantity");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : AddProductToCart");
            System.out.println(res);
            return res;
        }
        Response res = tradingSystem.AddProductToCart(connID, storeID, productID, quantity);
        res.AddConnID(connID);
        tradingSystem.printUsers();
        res.AddTag("AddProductToCart");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
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
        String connID;
        try {
            connID = (String) obj.get("connID");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : AddProductToCart");
            System.out.println(res);
            return res;
        }
        Response res = this.tradingSystem.ShowShoppingCart(connID);
        res.AddConnID(connID);
        res.AddTag("ShowShoppingCart");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
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
        String connID;
        int storeID, productID;
        try {
            connID = (String) obj.get("connID");
            storeID = (int) obj.get("storeID");
            productID = (int) obj.get("productID");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : RemoveProductFromCart");
            System.out.println(res);
            return res;
        }
        Response res = tradingSystem.RemoveProductFromCart(connID, storeID, productID);
        res.AddConnID(connID);
        res.AddTag("RemoveProductFromCart");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
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
        String connID;
        int storeID, productID, quantity;
        try {
            connID = (String) obj.get("connID");
            storeID = (int) obj.get("storeID");
            productID = (int) obj.get("productID");
            quantity = (int) obj.get("quantity");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : EditProductQuantityFromCart");
            System.out.println(res);
            return res;
        }
        Response res = tradingSystem.editProductQuantityFromCart(connID, storeID, productID, quantity);
        res.AddConnID(connID);
        res.AddTag("EditProductQuantityFromCart");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
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
        String connID, name, credit_number, phone_number, address;
        try {
            connID = (String) obj.get("connID");
            name = (String) obj.get("name");
            credit_number = (String) obj.get("credit_number");
            phone_number = (String) obj.get("phone_number");
            address = (String) obj.get("address");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : guestPurchase");
            System.out.println(res);
            return res;
        }
        Response res = tradingSystem.guestPurchase(connID, name, credit_number, phone_number, address);
        res.AddTag("guestPurchase");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    private void WriteToLogger(Response res){
        if(res.getIsErr()) {
            loggerController.WriteErrorMsg("Guest Error: " + res.getMessage());
        }
        else{
            loggerController.WriteLogMsg("Guest: " + res.getMessage());
        }
    }
}
