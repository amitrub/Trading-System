package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DataAccessLayer.StoreService;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import TradingSystem.Server.ServiceLayer.LoggerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(path = "api/subscriber")
@CrossOrigin("*")
@Transactional
public class SubscriberServiceHttp {
    private final TradingSystem tradingSystem = TradingSystemImpl.getInstance();
    private static final LoggerController loggerController=LoggerController.getInstance();
    @Autowired
    private StoreService storeService;


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

    @GetMapping("{storeId}/findstore")
    public Response findStore(@PathVariable int storeId){
        if(storeService.findDummyStoreById(storeId)!=null)
            return new Response("success");
        else
            return new Response(true,"failed to find store");
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
    @PostMapping("test")
    public Response testAddStore(){
        System.out.println("adding store");
//        MongoDatabase database = mongoClient.getDatabase("test");
//        database.createCollection("students");
        DummyStore store=new DummyStore(5,"test",5.0);
//        Document document = new Document();
//        document.append("student1",store);
//        database.getCollection("students").insertOne(store);
        DummyStore store1= storeService.addStore(store);
        if(store1!=null){
            System.out.println(store1.getId());
            return new Response("success adding store");
        }
        return new Response(true,"error");
    }
    @GetMapping("findstore")
    public Response testfindStore(){
        System.out.println("helllo");
        DummyStore dummyStore= storeService.findDummyStoreById(5);
        if(dummyStore!=null)
        {
            Response response=new Response();
            response.AddPair("store",dummyStore);
            return response;
        }
        return new Response(true,"error");
    }

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
    //TODO: not check yet
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
        return tradingSystem.ShowSubscriberHistory(userID, connID);
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
        String credit_number, phone_number, address;
        try {
            credit_number = (String) obj.get("credit_number");
            phone_number = (String) obj.get("phone_number");
            address = (String) obj.get("address");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : subscriberPurchase");
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        Response res = tradingSystem.subscriberPurchase(userID, connID, credit_number, phone_number, address);
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
