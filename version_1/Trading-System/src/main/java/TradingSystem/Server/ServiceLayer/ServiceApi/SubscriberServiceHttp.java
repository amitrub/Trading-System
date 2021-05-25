package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DataLayer.Services.StoreService;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import TradingSystem.Server.ServiceLayer.LoggerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(path = "api/subscriber")
@CrossOrigin("*")
public class SubscriberServiceHttp {
    @Autowired
    StoreService storeService;
    private final TradingSystem tradingSystem = TradingSystemImpl.getInstance();
    private static final LoggerController loggerController=LoggerController.getInstance();


    @GetMapping("{userID}/get_all_subscribers")
    public Response GetAllSubscribers(@PathVariable int userID, @RequestHeader("connID") String connID) {
        Response res = tradingSystem.GetAllSubscribers(connID, userID);
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
    @GetMapping("test")
    public Response Test(){
        DummyStore store=new DummyStore(5,"reut",5.0);
        DummyStore store1=storeService.Addstore(store);
        Response response=new Response();
        response.AddPair("store",store1);
        return response;
    }

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
        int storeID,productID;
        Double productPrice;
        try {
            storeID = (int) obj.get("storeID");
            productID = (int) obj.get("productID");
            productPrice = (Double) obj.get("productPrice");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : submissionBidding");
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        Response res = tradingSystem.subscriberBidding(userID,connID,storeID,productID,productPrice);
        res.AddConnID(connID);
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
