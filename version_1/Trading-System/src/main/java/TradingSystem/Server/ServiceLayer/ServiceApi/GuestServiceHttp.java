
package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import TradingSystem.Server.ServiceLayer.LoggerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api")
@CrossOrigin("*")
public class GuestServiceHttp {
    private static final LoggerController loggerController=LoggerController.getInstance();

    @Autowired
    public final TradingSystem tradingSystem;

    public GuestServiceHttp(TradingSystem tradingSystem) {
        this.tradingSystem = tradingSystem;
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
     *
     *  "connID": String
     * }
     */
    @GetMapping("home")
    public Response ConnectSystem(){
        System.out.println("--------------HELLO--------------");
        Response res = this.tradingSystem.ConnectSystem();
        tradingSystem.printUsers();
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
    //return connID
    @GetMapping("exit")
    public Response Exit(@RequestHeader("connID") String connID){
        Response res = this.tradingSystem.Exit(connID);
        tradingSystem.printUsers();
        WriteToLogger(res);
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
        String userName, password;
        try {
            userName = (String) obj.get("userName");
            password = (String) obj.get("password");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : Register");
            res.AddConnID(connID);
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        Response res = this.tradingSystem.Register(connID, userName, password);
        tradingSystem.printUsers();
        WriteToLogger(res);
        return res;

    }

//    /**
//     * @requirement 2.4
//     *
//     * @param connID: String (Header)
//     * @param obj:{
//     *  "userName": String
//     *  "password": String
//     * }
//     * @return Response {
//     *  "isErr: boolean
//     *  "message": String
//     *  "connID: String
//     *  "userID": int
//     * }
//     */
//    @PostMapping("login")
//    public Response Login(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
//        String userName, password;
//        try {
//            userName = (String) obj.get("userName");
//            password = (String) obj.get("password");
//        }
//        catch (Exception e){
//            System.out.println(e);
//            Response res = new Response(true, "Error in parse body : Login");
//            res.AddConnID(connID);
//            System.out.println(res);
//            WriteToLogger(res);
//            return res;
//        }
//        Response res = this.tradingSystem.Login(connID, userName, password);
//        tradingSystem.printUsers();
//        WriteToLogger(res);
//        return res;
//
//    }

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
        WriteToLogger(res);
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
        WriteToLogger(res);
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
    @PostMapping("search")
    public Response Search(@RequestBody Map<String, Object> obj){
        String name;
        boolean productNameMode, productCategoryMode;
        int minPrice, maxPrice, pRank, sRank;
        try {
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
            WriteToLogger(res);
            return res;
        }
        if (productNameMode & !productCategoryMode)
            return tradingSystem.SearchProduct(name, null, minPrice, maxPrice);
        else if (!productNameMode & productCategoryMode)
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
        int storeID, productID, quantity;
        try {
            storeID = (int) obj.get("storeID");
            productID = (int) obj.get("productID");
            quantity = (int) obj.get("quantity");
        } catch (Exception e) {
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : AddProductToCart");
            res.AddConnID(connID);
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        Response res = tradingSystem.AddProductToCart(connID, storeID, productID, quantity);
        res.AddConnID(connID);
        tradingSystem.printUsers();
        WriteToLogger(res);
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
    //TODO figureout how to implement
    @GetMapping("shopping_cart")
    public Response ShowShoppingCart(@RequestHeader("connID") String connID){
        Response res = this.tradingSystem.ShowShoppingCart(connID);
        res.AddConnID(connID);
        WriteToLogger(res);
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
    public Response RemoveProductFromCart(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj) {
        int storeID, productID;
        try {
            storeID = (int) obj.get("storeID");
            productID = (int) obj.get("productID");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : RemoveProductFromCart");
            res.AddConnID(connID);
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        Response res = tradingSystem.RemoveProductFromCart(connID, storeID, productID);
        res.AddConnID(connID);
        WriteToLogger(res);
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
        int storeID, productID, quantity;
        try {
            storeID = (int) obj.get("storeID");
            productID = (int) obj.get("productID");
            quantity = (int) obj.get("quantity");
        } catch (Exception e) {
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : EditProductQuantityFromCart");
            res.AddConnID(connID);
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        Response res = tradingSystem.editProductQuantityFromCart(connID, storeID, productID, quantity);
        res.AddConnID(connID);
        WriteToLogger(res);
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
        String name, credit_number, month, year, cvv, ID, address, city, country, zip;
        try {
            name = (String) obj.get("name");
            credit_number = (String) obj.get("credit_number");
            month = (String) obj.get("month");
            year = (String) obj.get("year");
            cvv = (String) obj.get("cvv");
            ID = (String) obj.get("ID");
            address = (String) obj.get("address");
            city = (String) obj.get("city");
            country = (String) obj.get("country");
            zip = (String) obj.get("zip");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : guestPurchase");
            res.AddConnID(connID);
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        Response res = tradingSystem.guestPurchase(connID, name, credit_number, month, year, cvv, ID, address,city,country,zip);
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
