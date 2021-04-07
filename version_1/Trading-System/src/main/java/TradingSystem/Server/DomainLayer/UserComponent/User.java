package TradingSystem.Server.DomainLayer.UserComponent;



import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummySearch;

import java.util.LinkedList;
import java.util.List;

public  class User {

    private final TradingSystem tradingSystem = TradingSystem.getInstance();
    private static int nextUserID = 0;

    private final Integer id;
    private String userName;
    private String password;
    private List<Integer> myFoundedStoresIDs;
    private List<Integer> myOwnedStoresIDs;
    private List<Integer> myManagedStoresIDs;


    private ShoppingCart shoppingCart;
    private List<Integer> shoppingHistory;

    public User(String userName, String password) {
        this.id = getNextUserID();
        this.userName = userName;
        this.password = password;
        this.myFoundedStoresIDs = new LinkedList<>();
        this.myOwnedStoresIDs = new LinkedList<>();
        this.myManagedStoresIDs = new LinkedList<>();
    }

    public User(Integer id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.myFoundedStoresIDs = new LinkedList<>();
        this.myOwnedStoresIDs = new LinkedList<>();
        this.myManagedStoresIDs = new LinkedList<>();
    }

    private static synchronized int getNextUserID() {
        nextUserID++;
        return nextUserID;
    }

    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

//    public List<DummySearch> getShoppingCart(){
//        List<DummySearch> shoppingBags=new LinkedList<>();
//        for(ShoppingBag shoppingBag: shoppingCart.shoppingBags()){
//            List<Integer> products=shoppingBag.getProductsList();
//            for(Integer i:products){
//                List<DummySearch> dummySearches= this.tradingSystem.getStores().get(shoppingBag.getStoreID()).getAllProducts();
//                shoppingBags.add(new DummySearch(shoppingBag.getStoreID(), this.tradingSystem.getStores().get(shoppingBag.getStoreID()).getName(),i,this.tradingSystem.ge,))
//            }
//        }
//    }
}
