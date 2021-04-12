package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/owner")
public class StoreOwnerService {
    private final TradingSystem tradingSystem = TradingSystem.getInstance();

    @PostMapping("{userID}/store/{storeID}/add_new_product")
    public Response AddProductToStore(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj) {
        String productName = (String) obj.get("productName");
        String category = (String) obj.get("category");
        double price = (double) obj.get("price");
        return tradingSystem.AddProductToStore(userID, connID, storeID, productName, category, price);
    }

    @PostMapping("{userID}/store/{storeID}/add_quantity_product/{productID}")
    public Response AddQuantityProduct(@PathVariable int userID, @PathVariable int storeID, @PathVariable int productID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        int quantity  = (int) obj.get("quantity");
        return tradingSystem.AddQuantityProduct(userID,connID,storeID,productID,quantity);
    }

    @DeleteMapping("{userID}/store/{storeID}/remove_product/{productID}")
    public Response RemoveProduct(@PathVariable int userID, @PathVariable int storeID, @PathVariable int productID, @RequestHeader("connID") String connID){
        return this.tradingSystem.RemoveProduct(userID,storeID,productID,connID);
    }

    //TODO: not check yet
    @PostMapping("{userID}/store/{storeID}/edit_product/{productID}")
    public Response EditProduct(@PathVariable int userID, @PathVariable int storeID, @PathVariable int productID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        String productName = (String) obj.get("productName");
        String category = (String) obj.get("category");
        double price = (double) obj.get("price");
        int quantity  = (int) obj.get("quantity");
        return tradingSystem.EditProduct(userID, connID, storeID,productID, productName, category, price);
    }

    //TODO: not check yet
    @GetMapping("{userID}/store_history/{storeID}")
    public List<DummyShoppingHistory> ShowStoreHistory(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID){
        return tradingSystem.StoreHistory(userID,storeID,connID);
    }

    //TODO: not check yet
    @PostMapping("{userID}/store/{storeID}/edit_product/{newOwner}")
    public Response AddNewOwner(@PathVariable int userID, @PathVariable int storeID, @PathVariable int newOwner, @RequestHeader("connID") String connID)  {
        return tradingSystem.AddNewOwner(userID, connID, storeID,newOwner);
    }

    //TODO: not check yet
    @PostMapping("{userID}/store/{storeID}/edit_product/{newManager}")
    public Response AddNewManager(@PathVariable int userID, @PathVariable int storeID, @PathVariable int newManager, @RequestHeader("connID") String connID)  {
        return tradingSystem.AddNewManager(userID, connID, storeID,newManager);
    }

    //TODO: not check yet
    @PostMapping("{userID}/store/{storeID}/edit_product/{ManagerToRemove}")
    public Response RemoveManager(@PathVariable int userID, @PathVariable int storeID, @PathVariable int ManagerToRemove, @RequestHeader("connID") String connID) {
        return tradingSystem.RemoveManager(userID, connID, storeID,ManagerToRemove);
    }
    
}
