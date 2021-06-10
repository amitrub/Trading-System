package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import TradingSystem.Server.ServiceLayer.LoggerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Map;


@RestController
@RequestMapping(path = "api/subscriber")
@CrossOrigin("*")
public class SubscriberServiceHttp {

    private static final LoggerController loggerController=LoggerController.getInstance();

    @Autowired
    private final TradingSystem tradingSystem;

    public SubscriberServiceHttp(TradingSystem tradingSystem) {
        this.tradingSystem = tradingSystem;
    }


    @GetMapping("{userID}/get_all_subscribers")
    public Response GetAllSubscribers(@PathVariable int userID, @RequestHeader("connID") String connID) {
        Response res = tradingSystem.GetAllSubscribers(connID, userID);
        tradingSystem.printUsers();
        WriteToLogger(res);
        return res;
    }

    @GetMapping("{storeId}/get_all_manager")
    public Response GetAllManager(@PathVariable int storeId, @RequestHeader("connID") String connID) {
        Response res = tradingSystem.GetAllManager(connID, storeId);
        tradingSystem.printUsers();
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 3.1
     *
     * @param userID: int (Path)
     * @param connID: String (Header)
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */

    @GetMapping("{userID}/logout")
    public Response Logout(@PathVariable int userID, @RequestHeader("connID") String connID){
        Response res = tradingSystem.Logout(connID);
        tradingSystem.printUsers();
        WriteToLogger(res);
        return res;
    }
//    @GetMapping("test")
//    public Response Test(){
//        DummyStore store=new DummyStore(5,"reut",5.0);
//        DummyStore store1=storeService.Addstore(store);
//        Response response=new Response();
//        response.AddPair("store",store1);
//        return response;
//    }

    /**
     * @requirement 3.2
     *
     * @param userID: int (Path)
     * @param connID: String (Header)
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @PostMapping("{userID}/add_store")
    public Response AddStore(@PathVariable int userID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        String storeName;
        try {
            storeName = (String) obj.get("storeName");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : AddStore");
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        Response res = tradingSystem.AddStore(userID, connID, storeName);
        tradingSystem.printUsers();
        tradingSystem.printStores();
        WriteToLogger(res);
        return res;
    }

//    @PostMapping("add_store")
//    public Response AddStore(){
//        DummyStore store;
//        store=new DummyStore(5,"reut",5.0);
//        store= storeService.Addstore(store);
//        Response res = new Response(true, "Error in parse body : AddStore");
//        return res;
//    }


    /**
     * @requirement 3.3
     *
     * @param userID: int (Path)
     * @param connID: String (Header)
     * @param obj:{
     *  "storeID": int
     *  "productID": int
     *  "comment": String
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @PostMapping("{userID}/write_comment")
    public Response WriteComment(@PathVariable int userID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        int storeID,productID;
        try {
            storeID = (int) obj.get("storeID");
            productID = (int) obj.get("productID");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : WriteComment");
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        String review = (String) obj.get("comment");
        Response res = tradingSystem.WriteComment(userID,connID,storeID,productID,review);
        tradingSystem.printCommentForProduct(storeID,productID);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 3.7
     *
     * @param userID: int (Path)
     * @param connID: String (Header)
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "history": List [{
     *      "userID": int
     *      "storeID": int
     *      "finalPrice": int
     *      "date": String
     *      "products": List [{
     *          "storeID": int
     *          "storeName": String
     *          "productID": int
     *          "productName": String
     *          "price": double
     *          "category": String
     *          "quantity": int
     *      }]
     *  }]
     * }
     */
    @GetMapping("{userID}/user_history")
    public Response ShowUserHistory(@PathVariable int userID, @RequestHeader("connID") String connID){
        Response res = tradingSystem.ShowSubscriberHistory(userID, connID);
        WriteToLogger(res);
        return res;
    }


    /**
     * @requirement 2.9
     *
     * @param userID: int (Path)
     * @param connID: String (Header)
     * @param obj:{
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
    @PostMapping("{userID}/shopping_cart/purchase")
    public Response subscriberPurchase(@PathVariable int userID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        String credit_number, month, year, cvv, ID, address, city, country, zip;
        try {
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
            Response res = new Response(true, "Error in parse body : subscriberPurchase");
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        System.out.println("\n\n---------PURCHASE----------------\n\n");
        Response res = tradingSystem.subscriberPurchase(userID, connID, credit_number, month, year, cvv, ID, address,city,country,zip );
        WriteToLogger(res);
        return res;
    }


    /**
     * @requirement 8.3.1
     *
     * @param userID: int (Path)
     * @param connID: String (Header)
     * @param obj:{
     *  "storeID": Integer
     *  "productID": Integer
     *  "productPrice": Integer
     *   "quantity" : Integer
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     *
     */
    @PostMapping("{userID}/submission_bidding")
    public Response submissionBidding(@PathVariable int userID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        int storeID,productID, quantity;
        Integer productPrice;
        try {
            storeID = (int) obj.get("storeID");
            productID = (int) obj.get("productID");
            quantity = (int) obj.get("quantity");
            productPrice = (Integer) obj.get("productPrice");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : submissionBidding");
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        Response res = tradingSystem.subscriberBidding(userID,connID,storeID,productID,productPrice, quantity);
        res.AddConnID(connID);
        WriteToLogger(res);
        return res;
    }
    //________________________________

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
    @GetMapping("stores_subscriber")
    public Response showAllStoresSubscriber() {
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
    @GetMapping("store/{storeID}/products_subscriber")
    public Response showStoreProductsSubscriber(@PathVariable int storeID) {
        Response res = this.tradingSystem.ShowStoreProducts(storeID);
        WriteToLogger(res);
        return res;
    }


    /**
     * @requirement None
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
    //TODO figure out how to implement
    @GetMapping("shopping_cart_special")
    public Response ShowSpecialProductsInShoppingCart(@RequestHeader("connID") String connID){
        Response res = this.tradingSystem.ShowSpecialProductInShoppingCart(connID);
        res.AddConnID(connID);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 8.3.3
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
    @PostMapping("shopping_cart/remove_special_product")
    public Response RemoveSpecialProductProductFromCart(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj) {
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
        Response res = tradingSystem.removeSpecialProductFromCart(connID, storeID, productID);
        res.AddConnID(connID);
        WriteToLogger(res);
        return res;

    }

    @PostMapping("get_store_ID")
    public Response getStoreIDByName(@RequestBody Map<String, Object> obj){
        String storeName;
        try {
            storeName = (String) obj.get("store");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : getStoreIDByName");
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        Integer storeID = tradingSystem.getStoreIDByName(storeName);
        Response res = new Response();
        res.AddPair("storeID", storeID);
        WriteToLogger(res);
        return res;
    }

    @PostMapping("{storeID}/get_product_ID")
    public Response getProductIDByName(@PathVariable int storeID, @RequestBody Map<String, Object> obj){
        String productName;
        try {
            productName = (String) obj.get("productName");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : getProductIDByName");
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        Integer productID = tradingSystem.getProductIDByName(productName, storeID);
        Response res = new Response();
        res.AddPair("productID", productID);
        WriteToLogger(res);
        return res;
    }



    //___________________________________

    private void WriteToLogger(Response res){
        if(res.getIsErr()) {
            loggerController.WriteErrorMsg("Guest Error: " + res.getMessage());
        }
        else{
            loggerController.WriteLogMsg("Guest: " + res.getMessage());
        }
    }
}
