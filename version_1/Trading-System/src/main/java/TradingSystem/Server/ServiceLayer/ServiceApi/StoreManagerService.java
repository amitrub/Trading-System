package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import TradingSystem.Server.ServiceLayer.LoggerController;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/manager")
@CrossOrigin("*")
public class StoreManagerService {
    private static final LoggerController loggerController=LoggerController.getInstance();

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
//        Response res = tradingSystemImpl.ManagementOperations(userID, connID, storeID);
        Response res = new Response(true, "not implemented");
        WriteToLogger(res);
        return res;
    }

    private void WriteToLogger(Response res){
        if(res.getIsErr()) {
            loggerController.WriteErrorMsg("Store Manager Error: " + res.getMessage());
        }
        else{
            loggerController.WriteLogMsg("Store Manager: " + res.getMessage());
        }
    }
}
