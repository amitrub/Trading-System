package TradingSystem.Server.Service_Layer;



import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Service {
    private TradingSystem trading_system = TradingSystem.getInstance();

    @RequestMapping(path = "api/register")
    @GetMapping
    public Integer Register(String userName, String password){
        return trading_system.Register(userName, password);
    }

    @RequestMapping(path = "api/login")
    @GetMapping
    public Integer Login(String userName, String password){ //todo id?
        return 0;
    }

    public List<Object> Search(String objectToSearch){
        return null;
    }

    public List<Object> Sort(Integer Category){ //todo getting list?
        return null;
    }

    //todo- only subscriber
    public void Logout(int userId){ //todo void?

    }

     public List<Store> ShowAllStores(){
        return null;
     }

    public List<Product> ShowAllProducts(){
        return null;
    }

    public ShoppingCart ShowShoppingCart(int userId){
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
