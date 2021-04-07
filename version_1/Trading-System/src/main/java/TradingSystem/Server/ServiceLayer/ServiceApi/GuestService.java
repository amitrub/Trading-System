package TradingSystem.Server.ServiceLayer.ServiceApi;



import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummySearch;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyUser;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api")
public class GuestService {
    private final TradingSystem tradingSystem = TradingSystem.getInstance();

//    @GetMapping("buy/{userID}")
//    public Response try1(@PathVariable int userID, @RequestHeader("connID") String connID){
//        System.out.println(userID);
//        System.out.println(connID);
//        return new Response(userID, "aaa","good2");
//    }
//
//    @PostMapping("try2")
//    public String try2(@RequestBody Map<String, Object> dummyUser){
//        System.out.println(dummyUser.toString());
//        String name = (String) dummyUser.get("userName");
////        try {
////            name = obj.getString("name");
////        }
////        catch (Exception e){
////            name = "Error";
//////        }
//
//        return name;
//    }
//
//    @GetMapping("try3")
//    public Integer try3(){
//        return 8;
//    }

    //return connID
    @GetMapping("home")
    public Response connectSystem(){
        return null;
    }

    //return connID
    @GetMapping("exit")
    public Response Exit(@RequestHeader("connID") String connID){
        return null;
    }

    @PostMapping("register")
    public Response Register(@RequestBody Map<String, Object> obj){
        String userName = (String) obj.get("userName");
        String password = (String) obj.get("password");
        return null;
//        return tradingSystem.Register(dummyUser);
    }

    @PostMapping("login")
    public Response Login(@RequestBody Map<String, Object> obj){
        String userName = (String) obj.get("userName");
        String password = (String) obj.get("password");
//        return tradingSystem.Login(dummyUser);
        return null;
    }

    @PostMapping("search")
    public List<DummySearch> Search(@RequestBody Map<String, Object> obj){
        boolean productNameMode = (boolean) obj.get("Product Name");
        boolean productCategoryMode = (boolean) obj.get("Product Category");
        int minPrice = (int) obj.get("minPrice");
        int maxPrice = (int) obj.get("maxPrice");
        int pRank = (int) obj.get("pRank");
        int sRank = (int) obj.get("sRank");
        return null;
    }

    @GetMapping("stores")
    public List<DummyStore> ShowAllStores(){
        return null;
    }

    @GetMapping("store/{storeID}/products")
    public List<DummySearch> ShowStoreProducts(@PathVariable int storeID){
        return null;
    }

    @PostMapping("shopping_cart/add_product")
    public Response AddProductToCart(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        int storeID = (int) obj.get("storeID");
        int productID = (int) obj.get("productID");
        return null;
    }

    @GetMapping("shopping_cart")
    public List<DummySearch> ShowShoppingCart(@RequestHeader("connID") String connID){
        return null;
    }

    public List<Object> Sort(Integer Category){ //todo getting list?
        return null;
    }





    //todo- only subscriber
    public void OpenStore(int id,String storeName){ //todo void? id?

    }

    //todo- only subscriber
    public ShoppingHistory ShowHistory(int userId){ //todo list? id?
        return null;
    }

    //todo- only subscriber
    public void WriteComment(int userId, int productId, String comment){ //todo void? id?

    }

    //todo- only System Manager
    public List<ShoppingHistory> ShowStoreHistory(int userId){ //todo list? id?
        return null;
    }

}
