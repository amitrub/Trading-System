package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/admin")
public class AdminService {
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
    public Response ShowAllUsers(@PathVariable int adminID, @RequestHeader("connID") String connID){
        Response res = tradingSystem.ShowAllUsers(adminID, connID);
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
    public Response ShowAllStores(@PathVariable int adminID, @RequestHeader("connID") String connID){
        Response res = tradingSystem.ShowAllStores();
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
    @GetMapping("{adminID}/user_history/{userID}")
    public Response ShowUserHistory(@PathVariable int adminID, @PathVariable int userID, @RequestHeader("connID") String connID){
        Response res = tradingSystem.ShowUserHistory(adminID,userID ,connID);
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
    @GetMapping("{adminID}/store_history/{storeID}")
    public Response ShowStoreHistory(@PathVariable int adminID, @PathVariable int storeID, @RequestHeader("connID") String connID){
        Response res = tradingSystem.StoreHistoryAdmin(adminID, storeID, connID);
        return res;
    }
}
