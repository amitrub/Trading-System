package TradingSystem.Server.ServiceLayer.ServiceApi;



import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api")
public class GuestService {
    private final TradingSystem tradingSystem = TradingSystem.getInstance();

    //return connID
    @GetMapping("try")
    public Map<String, Object> try1(){
        Map<String, Object> map = new HashMap<>();
        map.put("try", 7);
        tradingSystem.printUsers();
        return map;
    }

    //return connID
    @GetMapping("home")
    public Response connectSystem(){
        Response res = this.tradingSystem.connectSystem();
        tradingSystem.printUsers();
        return res;
    }

    //return connID
    @GetMapping("exit")
    public Response Exit(@RequestHeader("connID") String connID){
        Response res = this.tradingSystem.Exit(connID);
        tradingSystem.printUsers();
        return res;
    }

    @PostMapping("register")
    public Response Register(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        String userName = (String) obj.get("userName");
        String password = (String) obj.get("password");
        Response res = this.tradingSystem.Register(connID, userName, password);
        tradingSystem.printUsers();
        return res;
    }

    @PostMapping("login")
    public Response Login(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        String userName = (String) obj.get("userName");
        String password = (String) obj.get("password");
        Response res = this.tradingSystem.Login(connID, userName, password);
        tradingSystem.printUsers();
        return res;
    }

    //TODO: not check yet
    @PostMapping("search")
    public List<DummyProduct> Search(@RequestBody Map<String, Object> obj){
        String name = (String) obj.get("name");;
        boolean productNameMode = (boolean) obj.get("Product Name");
        boolean productCategoryMode = (boolean) obj.get("Product Category");
        int minPrice = (int) obj.get("minPrice");
        int maxPrice = (int) obj.get("maxPrice");
        int pRank = (int) obj.get("pRank");
        int sRank = (int) obj.get("sRank");
        if(productNameMode & !productCategoryMode)
            return tradingSystem.SearchProductByName(name, minPrice, maxPrice, pRank, sRank);
        else if(!productNameMode & productCategoryMode)
            return tradingSystem.SearchProductByCategory(name, minPrice, maxPrice, pRank, sRank);
        else
            return new ArrayList<>();
    }

    @GetMapping("stores")
    public List<DummyStore> ShowAllStores() {
        List<DummyStore> res = this.tradingSystem.ShowAllStores();
        return res;
    }

    @GetMapping("store/{storeID}/products")
    public List<DummyProduct> ShowStoreProducts(@PathVariable int storeID)
    {
        List<DummyProduct> res = this.tradingSystem.ShowStoreProducts(storeID);
        return res;
    }

    @PostMapping("shopping_cart/add_product")
    public Response AddProductToCart(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        int storeID = (int) obj.get("storeID");
        int productID = (int) obj.get("productID");
        int quantity = (int) obj.get("quantity");
        Response res = tradingSystem.AddProductToCart(connID, storeID, productID, quantity);
        return res;
    }

    @GetMapping("shopping_cart")
    public List<DummyProduct> ShowShoppingCart(@RequestHeader("connID") String connID){
        List<DummyProduct> res = this.tradingSystem.ShowShoppingCart(connID);
        return res;
    }

    public List<Object> Sort(Integer Category){ //todo getting list?
        return null;
    }


}
