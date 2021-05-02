package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/admin")
public class AdminServiceHttp {

    private final TradingSystem tradingSystem = TradingSystem.getInstance();

    /**
     * @requirement 6.4
     *
     * @param adminID: int (Path)
     * @param connID: String (Header)
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
    @GetMapping("{adminID}/users")
    public Response AdminAllUsers(@PathVariable int adminID, @RequestHeader("connID") String connID){
        Response res = tradingSystem.AllUsersHistoryAdmin(adminID, connID);
        return res;
    }

    /**
     * @requirement 6.4
     *
     * @param adminID: int (Path)
     * @param connID: String (Header)
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
    @GetMapping("{adminID}/stores")
    public Response AdminAllStores(@PathVariable int adminID, @RequestHeader("connID") String connID){
        Response res = tradingSystem.AllStoresHistoryAdmin(adminID, connID);
        return res;
    }


    /**
     * @requirement 6.4
     *
     * @param adminID: int (Path)
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
    @GetMapping("{adminID}/user_history_admin/{userID}")
    public Response AdminUserHistory(@PathVariable int adminID, @PathVariable int userID, @RequestHeader("connID") String connID){
        Response res = tradingSystem.UserHistoryAdmin(adminID, userID ,connID);
        return res;
    }


    /**
     * @requirement 6.4
     *
     * @param adminID: int (Path)
     * @param storeID: int (Path)
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
    @GetMapping("{adminID}/store_history_admin/{storeID}")
    public Response AdminStoreHistory(@PathVariable int adminID, @PathVariable int storeID, @RequestHeader("connID") String connID){
        return tradingSystem.StoreHistoryAdmin(adminID,storeID,connID);
    }




}