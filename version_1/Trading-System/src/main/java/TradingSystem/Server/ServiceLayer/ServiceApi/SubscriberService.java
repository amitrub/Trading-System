package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import TradingSystem.Server.ServiceLayer.LoggerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@MessageMapping("api/subscriber")
@CrossOrigin("*") public class SubscriberService {
    @Autowired
    SimpMessagingTemplate template;

    private final TradingSystem tradingSystem = TradingSystemImpl.getInstance();
    private static final LoggerController loggerController=LoggerController.getInstance();


    /**
     * @requirement 3.1
     *
     * @param userID: int (Path)
     * @param obj:{
     *  "connID": String
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @MessageMapping("{userID}/logout")
    public Response Logout(@DestinationVariable int userID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        Response res = tradingSystem.Logout(connID);
        tradingSystem.printUsers();
        res.AddTag("Logout");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 3.2
     *
     * @param userID: int (Path)
     * @param obj:{
     *  "connID": String
     *  "storeName": String
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @MessageMapping("{userID}/add_store")
    public Response AddStore(@DestinationVariable int userID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        String storeName = (String) obj.get("storeName");
        Response res = tradingSystem.AddStore(userID, connID, storeName);
        tradingSystem.printUsers();
        tradingSystem.printStores();
        res.AddTag("AddStore");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 3.3
     *
     * @param userID: int (Path)     
     * @param obj:{
     *  "connID": String
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
    @MessageMapping("{userID}/write_comment")
    public Response WriteComment(@DestinationVariable int userID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        int storeID = (int) obj.get("storeID");
        int productID = (int) obj.get("productID");
        String review = (String) obj.get("comment");
        Response res = tradingSystem.WriteComment(userID,connID,storeID,productID,review);
        tradingSystem.printCommentForProduct(storeID,productID);
        res.AddTag("WriteComment");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 3.7
     *
     * @param userID: int (Path)
     * @param obj:{
     *  "connID": String
     * }
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
    @MessageMapping("{userID}/user_history")
    public Response ShowUserHistory(@DestinationVariable int userID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        Response res = tradingSystem.ShowSubscriberHistory(userID, connID);
        res.AddTag("ShowUserHistory");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 2.9
     *
     * @param userID: int (Path)     
     * @param obj:{
     *  "connID": String
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
    @MessageMapping("{userID}/shopping_cart/purchase")
    public Response subscriberPurchase(@DestinationVariable int userID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        String credit_number = (String) obj.get("credit_number");
        String phone_number = (String) obj.get("phone_number");
        String address = (String) obj.get("address");
        Response res = tradingSystem.subscriberPurchase(userID, connID, credit_number, phone_number, address);
        res.AddTag("subscriberPurchase");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    private void WriteToLogger(Response res){
        if(res.getIsErr()) {
            loggerController.WriteErrorMsg("Subscriber Error: " + res.getMessage());
        }
        else{
            loggerController.WriteLogMsg("Subscriber: " + res.getMessage());
        }
    }
}
