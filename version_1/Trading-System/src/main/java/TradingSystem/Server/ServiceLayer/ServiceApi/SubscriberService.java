package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path = "api/subscriber")
public class SubscriberService {
    private final TradingSystem tradingSystem = TradingSystem.getInstance();

    @GetMapping("{userID}/logout")
    public Response Logout(@PathVariable int userID, @RequestHeader("connID") String connID){
        return tradingSystem.Logout(connID);
    }

    @PostMapping("{userID}/add_store")
    public Response AddStore(@PathVariable int userID, @RequestHeader("connID") String connID, @RequestBody String storeName){
        return null;
    }

    @GetMapping("{userID}/user_history")
    public List<DummyProduct> History(@PathVariable int userID, @RequestHeader("connID") String connID){
        return null;
    }

    @PostMapping("{userID}/writeComment")
    public Response Review(@PathVariable int userID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        int storeID = (int) obj.get("storeID");
        int productID = (int) obj.get("productID");
        String review = (String) obj.get("writeComment");
        double rate = (double) obj.get("rate");
        return null;
    }
}
