package TradingSystem.Server.Service_Layer;



import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Service {
    private TradingSystem tradingSystem = TradingSystem.getInstance();

    @RequestMapping(path = "api/try")
    @GetMapping
    public Response try1(@RequestHeader("userName") String userName){
        System.out.println(userName);
        return new Response(1,"");
    }

    @RequestMapping(path = "api/try2")
    @PostMapping
    public Response try2(@RequestBody DummyUser dummyUser){
        System.out.println(dummyUser);
        return new Response(1,"");
    }

    @RequestMapping(path = "api/register")
    @GetMapping
    public Response Register(String userName, String password){
        return tradingSystem.Register(userName, password);
    }

    @RequestMapping(path = "api/login")
    @PostMapping
    public Response Login(@RequestBody DummyUser dummyUser){
        return tradingSystem.Login(dummyUser);
    }

    //todo- only subscriber
    @RequestMapping(path = "api/logout")
    @GetMapping
    public Response Logout(int userId){ //todo void?
        return tradingSystem.Logout(userId);
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
