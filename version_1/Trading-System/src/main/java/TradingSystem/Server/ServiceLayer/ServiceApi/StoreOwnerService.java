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
        int quantity  = (int) obj.get("quantity");
        int price_int;
        Double price;
        try {
            price = (Double) obj.get("price");
        } catch (Exception e) {
            price_int = (int) obj.get("price");
            price = new Double(price_int);
        }
        Response res = tradingSystem.AddProductToStore(userID, connID, storeID, productName, category, price, quantity);
        return res;
    }

    @PostMapping("{userID}/store/{storeID}/add_quantity_product/{productID}")
    public Response AddQuantityProduct(@PathVariable int userID, @PathVariable int storeID, @PathVariable int productID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        int quantity  = (int) obj.get("quantity");
        return tradingSystem.AddQuantityProduct(userID,connID,storeID,productID,quantity);
    }

    @GetMapping("{userID}/store/{storeID}/remove_product/{productID}")
    public Response RemoveProduct(@PathVariable int userID, @PathVariable int storeID, @PathVariable int productID, @RequestHeader("connID") String connID){
        System.out.println("Test!!!!");
        Response res = this.tradingSystem.RemoveProduct(userID,storeID,productID,connID);
        System.out.println(res);
        tradingSystem.printProducts();
        return res;
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
    @GetMapping("{userID}/store/{storeID}/add_new_owner/{newOwnerID}")
    public Response AddNewOwner(@PathVariable int userID, @PathVariable int storeID, @PathVariable int newOwnerID, @RequestHeader("connID") String connID)  {
        return tradingSystem.AddNewOwner(userID, connID, storeID, newOwnerID);
    }

    //TODO: not check yet
    @GetMapping("{userID}/store/{storeID}/add_new_manager/{newManagerID}")
    public Response AddNewManager(@PathVariable int userID, @PathVariable int storeID, @PathVariable int newManagerID, @RequestHeader("connID") String connID)  {
        return tradingSystem.AddNewManager(userID, connID, storeID,newManagerID);
    }

    //TODO: not check yet
    @GetMapping("{userID}/store/{storeID}/remove_manager/{ManagerToRemoveID}")
    public Response RemoveManager(@PathVariable int userID, @PathVariable int storeID, @PathVariable int ManagerToRemoveID, @RequestHeader("connID") String connID)  {
        return tradingSystem.RemoveManager(userID, connID, storeID,ManagerToRemoveID);
    }
    
}
