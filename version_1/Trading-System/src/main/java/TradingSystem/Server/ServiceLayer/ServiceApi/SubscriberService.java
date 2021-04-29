package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping(path = "api/subscriber")
public class SubscriberService {
    private final TradingSystemImpl tradingSystemImpl = TradingSystemImpl.getInstance();

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
        Response res = tradingSystemImpl.Logout(connID);
        tradingSystemImpl.printUsers();
        return res;
    }

    /**
     * @requirement 3.2
     *
     * @param userID: int (Path)
     * @param connID: String (Header)
     * @param storeName: String (Body)
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @PostMapping("{userID}/add_store")
    public Response AddStore(@PathVariable int userID, @RequestHeader("connID") String connID, @RequestBody String storeName){
        Response res = tradingSystemImpl.AddStore(userID, connID, storeName);
        tradingSystemImpl.printUsers();
        tradingSystemImpl.printStores();
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
        int storeID = (int) obj.get("storeID");
        int productID = (int) obj.get("productID");
        String review = (String) obj.get("comment");
        Response res = tradingSystemImpl.WriteComment(userID,connID,storeID,productID,review);
        tradingSystemImpl.printCommentForProduct(storeID,productID);
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
    //TODO: fix DummyShoppingHistory
    @GetMapping("{userID}/user_history")
    public Response ShowUserHistory(@PathVariable int userID, @RequestHeader("connID") String connID){
        return tradingSystemImpl.ShowSubscriberHistory(userID, connID);
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
        String credit_number = (String) obj.get("credit_number");
        String phone_number = (String) obj.get("phone_number");
        String address = (String) obj.get("address");
        Response res = tradingSystemImpl.subscriberPurchase(userID, connID, credit_number, phone_number, address);
        return res;
    }
}
