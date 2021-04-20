package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/manager")
public class StoreManagerService {
    private final TradingSystem tradingSystem = TradingSystem.getInstance();

    /**
     * @requirement 5.1
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param connID: String (Header)
     * @param obj:{
     *      TODO: Think what values should be here
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    //TODO: not implemented Think how to implement Management Operations
    @PostMapping("{userID}/store/{storeID}/management_operations")
    public Response ManagementOperations(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj) {
//        Response res = tradingSystem.ManagementOperations(userID, connID, storeID);
        Response res = new Response(true, "not implemented");
        return res;
    }
}
