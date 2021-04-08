package TradingSystem.Server.DomainLayer.UserComponent;



import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;

import java.util.LinkedList;
import java.util.List;

public  class User {

    private final TradingSystem tradingSystem = TradingSystem.getInstance();
    private static int nextUserID = 0;

    private final Integer id;
    private String userName;
    private String password;
    private List<Integer> myFoundedStoresIDs = new LinkedList<>();;
    private List<Integer> myOwnedStoresIDs = new LinkedList<>();;
    private List<Integer> myManagedStoresIDs = new LinkedList<>();;


    private ShoppingCart shoppingCart;
    private List<ShoppingHistory> shoppingHistory;

    public User() {
        this.id = -1;
        this.userName = "guest";
        this.password = "";
        this.shoppingCart = new ShoppingCart(this.id);
    }

    public User(String userName, String password) {
        this.id = getNextUserID();
        this.userName = userName;
        this.password = password;
        this.shoppingCart = new ShoppingCart(this.id);
    }

    public User(String userName, String password, ShoppingCart shoppingCart) {
        this.id = getNextUserID();
        this.userName = userName;
        this.password = password;
        this.shoppingCart = shoppingCart;
    }

    private static synchronized int getNextUserID() {
        nextUserID++;
        return nextUserID;
    }

    public void AddStore(int storeID){
        this.myFoundedStoresIDs.add(storeID);
    }

    public void mergeToMyCart(ShoppingCart shoppingCartToMerge){
        this.shoppingCart.mergeToMyCart(shoppingCartToMerge);
    }
    
    public void AddShoppingCartTohistory(ShoppingHistory shoppingHistory){
        this.shoppingHistory.add(shoppingHistory);
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

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void AddProductToCart(int StoreId,int productId, int quantity){
        shoppingCart.addProductToBag(productId,StoreId,quantity);
    }

    public List<DummyProduct> ShowShoppingCart(){
        return shoppingCart.ShowShoppingCart();
    }

    public List<DummyShoppingHistory> ShowStoreHistory(){
        List<DummyShoppingHistory> shoppingHistories=new LinkedList<>();
        for(ShoppingHistory shoppingHistory:shoppingHistory){
            shoppingHistories.add(shoppingHistory.createDummy());
        }
        return shoppingHistories;
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
