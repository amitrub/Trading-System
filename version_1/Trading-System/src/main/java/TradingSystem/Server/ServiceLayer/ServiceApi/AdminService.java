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
@MessageMapping("admin")
@CrossOrigin("*") public class AdminService {
    @Autowired
    SimpMessagingTemplate template;

    private final TradingSystem tradingSystem = TradingSystemImpl.getInstance();
    private static final LoggerController loggerController=LoggerController.getInstance();

    /**
     * @requirement 6.4
     *
     * @param adminID: int (Path)
     * @param obj:{
     *  "connID": String
     * }
     * @return @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     *  "users": List [{
     *      "userID": int
     *      "userName": String
     *      "password": "XXXXXXX"
     *  }]
     * }
     */
    @MessageMapping("{adminID}/users")
    public Response AdminAllUsers(@DestinationVariable int adminID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        Response res = tradingSystem.AllUsersHistoryAdmin(adminID, connID);
        res.AddTag("AdminAllUsers");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 6.4
     *
     * @param adminID: int (Path)
     * @param obj:{
     *  "connID": String
     * }
     * @return @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     *  "stores": List [{
     *      "storeID": int
     *      "storeName": String
     *  }]
     * }
     */
    @MessageMapping("{adminID}/stores")
    public Response AdminAllStores(@DestinationVariable int adminID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        Response res = tradingSystem.AllStoresHistoryAdmin(adminID, connID);
        res.AddTag("AdminAllStores");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }


    /**
     * @requirement 6.4
     *
     * @param adminID: int (Path)
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
    @MessageMapping("{adminID}/user_history_admin/{userID}")
    public Response AdminUserHistory(@DestinationVariable int adminID, @DestinationVariable int userID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        Response res = tradingSystem.UserHistoryAdmin(adminID, userID ,connID);
        res.AddTag("AdminUserHistory");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }


    /**
     * @requirement 6.4
     *
     * @param adminID: int (Path)
     * @param storeID: int (Path)
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
    @MessageMapping("{adminID}/store_history_admin/{storeID}")
    public Response AdminStoreHistory(@DestinationVariable int adminID, @DestinationVariable int storeID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        Response res = tradingSystem.StoreHistoryAdmin(adminID,storeID,connID);
        res.AddTag("AdminStoreHistory");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    private void WriteToLogger(Response res){
        if(res.getIsErr()) {
            loggerController.WriteErrorMsg("Admin Error: " + res.getMessage());
        }
        else{
            loggerController.WriteLogMsg("Admin: " + res.getMessage());
        }
    }


}
