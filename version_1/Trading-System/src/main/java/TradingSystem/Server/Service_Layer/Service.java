package TradingSystem.Server.Service_Layer;



import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api")
public class Service {
    private final TradingSystem tradingSystem = TradingSystem.getInstance();

//    @RequestMapping(path = "api/try")
    @ExceptionHandler
    @GetMapping("try/{userID}")
    public Response try1(@PathVariable int userID, @RequestHeader("connID") String connID){
        System.out.println(userID);
        System.out.println(connID);
        return new Response(userID, "aaa","good2");
    }


    @PostMapping("try2/{id}")
    public Response try2(@RequestBody DummyUser dummyUser){
        System.out.println(dummyUser);
        return new Response(1,"");
    }

    @RequestMapping(path = "register")
    @GetMapping
    public Response Register(@RequestBody DummyUser dummyUser){
        return tradingSystem.Register(dummyUser);
    }

    @RequestMapping(path = "login")
    @PostMapping
    public Response Login(@RequestBody DummyUser dummyUser){
        return tradingSystem.Login(dummyUser);
    }

    //todo- only subscriber
    @RequestMapping(path = "logout")
    @GetMapping
    public Response Logout(@RequestHeader("connID") int connID){
        System.out.println(connID);
        return tradingSystem.Logout(connID);
    }

    public List<Object> Search(String objectToSearch){
        return null;
    }

    public List<Store> ShowAllStores(){
        return null;
    }

    public ShoppingCart ShowShoppingCart(int userId){
        return null;
    }

    public List<Object> Sort(Integer Category){ //todo getting list?
        return null;
    }

    public List<Product> ShowAllProducts(){
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
