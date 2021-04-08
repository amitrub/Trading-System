package TradingSystem.Server.ServiceLayer.ServiceApi;



import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
//        Response res = new Response(false, "no implemented");
        Response res = this.tradingSystem.connectSystem();
        tradingSystem.printUsers();
        return res;
    }

    //return connID
    @GetMapping("exit")
    public Response Exit(@RequestHeader("connID") String connID){
//        Response res = new Response(-1, connID, false, "no implemented, TODO shutdown system");
        Response res = this.tradingSystem.Exit(connID);
        tradingSystem.printUsers();
        return res;
    }

    @PostMapping("register")
    public Response Register(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        String userName = (String) obj.get("userName");
        String password = (String) obj.get("password");
//        Response res = new Response(800, "connID",  false, "register not implemented. userName: " + userName + " password: " + password);
        Response res = this.tradingSystem.Register(connID, userName, password);
        tradingSystem.printUsers();
        return res;
    }

    @PostMapping("login")
    public Response Login(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        String userName = (String) obj.get("userName");
        String password = (String) obj.get("password");
//        Response res = new Response(800, "connID",  false, "login not implemented. userName: " + userName + " password: " + password);
        Response res = this.tradingSystem.Login(connID, userName, password);
        tradingSystem.printUsers();
        return res;
    }

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
//        return new ArrayList<DummyStore>();
    }

    @GetMapping("store/{storeID}/products")
    public List<DummyProduct> ShowStoreProducts(@PathVariable int storeID){
        List<DummyProduct> res = this.tradingSystem.ShowStoreProducts(storeID);
        return res;
//        return new ArrayList<DummyProduct>();
    }

    @PostMapping("shopping_cart/add_product")
    public Response AddProductToCart(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        int storeID = (int) obj.get("storeID");
        int productID = (int) obj.get("productID");
        int quantity = (int) obj.get("quantity");
        Response res = tradingSystem.AddProductToCart(connID, storeID, productID, quantity);
        return res;
//        return new Response();
    }

    @GetMapping("shopping_cart")
    public List<DummyProduct> ShowShoppingCart(@RequestHeader("connID") String connID){
//        List<DummyProduct> res = this.tradingSystem.ShowShoppingCart();
//        return res;
        return new ArrayList<>();
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
