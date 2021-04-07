package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummySearch;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/owner")
public class StoreOwnerService {
    private final TradingSystem tradingSystem = TradingSystem.getInstance();

    @PostMapping("{userID}/store/{storeID}/add_product")
    public Response AddProduct(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        String productName = (String) obj.get("productName");
        String category = (String) obj.get("category");
        double price = (double) obj.get("price");
        int quantity  = (int) obj.get("quantity");
        return null;
    }
    @GetMapping("{userID}/store/{storeID}/remove_product/{productID}")
    public Response RemoveProduct(@PathVariable int userID, @PathVariable int storeID, @PathVariable int productID, @RequestHeader("connID") String connID){
        return null;
    }

    @PostMapping("{userID}/store/{storeID}/edit_product/{productID}")
    public Response EditProduct(@PathVariable int userID, @PathVariable int storeID, @PathVariable int productID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        String productName = (String) obj.get("productName");
        String category = (String) obj.get("category");
        double price = (double) obj.get("price");
        int quantity  = (int) obj.get("quantity");
        return null;
    }

    @GetMapping("{userID}/store_history/{storeID}")
    public List<DummySearch> StoreHistory(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID){
        return null;
    }
}
