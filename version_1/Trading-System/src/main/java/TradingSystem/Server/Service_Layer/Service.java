package TradingSystem.Server.Service_Layer;



import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api")
public class Service {
    private final TradingSystem tradingSystem = TradingSystem.getInstance();

    @GetMapping("buy/{userID}")
    public Response try1(@PathVariable int userID, @RequestHeader("connID") String connID){
        System.out.println(userID);
        System.out.println(connID);
        return new Response(userID, "aaa","good2");
    }

    @PostMapping("try2")
    public String try2(@RequestBody Map<String, Object> dummyUser){
        System.out.println(dummyUser.toString());
        String name = (String) dummyUser.get("userName");
//        try {
//            name = obj.getString("name");
//        }
//        catch (Exception e){
//            name = "Error";
////        }

        return name;
    }

    @GetMapping("try3")
    public Integer try3(){
        return 8;
    }

    @PostMapping("register")
    public Response Register(@RequestBody DummyUser dummyUser){
        return tradingSystem.Register(dummyUser);
    }

    @PostMapping("login")
    public Response Login(@RequestBody DummyUser dummyUser){
        return tradingSystem.Login(dummyUser);
    }

    @GetMapping("logout")
    public Response Logout(@RequestHeader("connID") String connID){
        System.out.println(connID);
        return tradingSystem.Logout(connID);
    }

    @RequestMapping(path = "search")
    @GetMapping
    public List<DummySearch> Search(@RequestBody Map<String, Object> obj){
        return null;
    }

    @RequestMapping(path = "stores")
    @GetMapping
    public List<Store> ShowAllStores(){
        return null;
    }

    @RequestMapping(path = "store/{storeID}")
    @GetMapping
    public List<Product> ShowAllProducts(){
        return null;
    }

    @RequestMapping(path = "cart")
    @GetMapping
    public ShoppingCart ShowShoppingCart(@RequestHeader("connID") String connID){
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
