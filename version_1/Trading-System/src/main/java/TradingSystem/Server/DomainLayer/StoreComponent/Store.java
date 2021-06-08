package TradingSystem.Server.DomainLayer.StoreComponent;



import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Services.Data_Controller;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.States.approveState;
import TradingSystem.Server.DomainLayer.StoreComponent.States.baseState;
import TradingSystem.Server.DomainLayer.StoreComponent.States.initState;
import TradingSystem.Server.DomainLayer.StoreComponent.States.refusalState;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.DomainLayer.UserComponent.ManagerPermission;
import TradingSystem.Server.DomainLayer.UserComponent.OwnerPermission;
import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

//import javafx.util.Pair;

public class Store extends Observable {

    @Autowired
    public static Data_Controller data_controller;
    public static void setData_controller(Data_Controller data_controller) {
        Store.data_controller = data_controller;
    }

    private static TradingSystemImplRubin tradingSystem;
    public static void setTradingSystem(TradingSystemImplRubin tradingSystem) {
        Store.tradingSystem = tradingSystem;
    }

    private static int nextStoreID=0;

    private static int nextExpressionID=0;

    private Integer id;
    private String name;
    private Double rate;

    private final Integer founderID;
    private List<Integer> ownersIDs = new ArrayList<>();
    private List<Integer> managersIDs = new ArrayList<>();

    //ownerID_Permission
    private ConcurrentHashMap<Integer, OwnerPermission> ownersPermission = new ConcurrentHashMap<>();
    //managersID_Permission
    private ConcurrentHashMap<Integer, ManagerPermission> managersPermission = new ConcurrentHashMap<>();;

    private List<ShoppingHistory> shoppingHistory = new ArrayList<>();
    private Inventory inventory;

    private DiscountPolicy discountPolicy;
    private BuyingPolicy buyingPolicy;

    //userID_Bid
    private ConcurrentLinkedDeque<Bid> Bids = new ConcurrentLinkedDeque<Bid>();

    //userID_Bidding
    private ConcurrentHashMap<Integer, Double> usersBidding = new ConcurrentHashMap<>();;



    public Store(String name, Integer founderID,  DiscountPolicy discountPolicy, BuyingPolicy buyingPolicy) {
        this.id = getNextStoreID();
        this.name = name;
        this.founderID = founderID;
        this.ownersIDs.add(founderID);
        this.discountPolicy = discountPolicy;
        this.buyingPolicy = buyingPolicy;
        this.rate =5.0; //todo- add rating!
        this.inventory=new Inventory(this.id,name);
    }

    public Store(String name, Integer founderID) {
        this.id = getNextStoreID();
        this.name = name;
        this.founderID = founderID;
        this.ownersIDs.add(founderID);
        this.rate =5.0; //todo- add rating!
        this.inventory=new Inventory(this.id,name);
        this.discountPolicy=new DiscountPolicy(this.id,null);
        this.buyingPolicy=new BuyingPolicy(this.id,null);
    }
    public Store(int id, String name, Integer founderID) {
        this.id = id;
        this.name = name;
        this.founderID = founderID;
        this.ownersIDs.add(founderID);
        this.rate =5.0; //todo- add rating!
        this.inventory=new Inventory(this.id,name);
        this.discountPolicy=new DiscountPolicy(this.id,null);
        this.buyingPolicy=new BuyingPolicy(this.id,null);
    }

    public Store(DataStore store){
        this.id=store.getStoreID();
        this.name=store.getStoreName();
        this.founderID=store.getFounder().getUserID();
        this.ownersIDs=store.getOwners().stream().map(DataSubscriber::getUserID).collect(Collectors.toList());
        this.ownersIDs.add(founderID);
        this.managersIDs=store.getManagers().stream().map(DataSubscriber::getUserID).collect(Collectors.toList());
        this.rate =store.getStoreRate();
        this.inventory=new Inventory(this.id,name, store.getProducts());


        this.discountPolicy=new DiscountPolicy(this.id,null);
        this.buyingPolicy=new BuyingPolicy(this.id,null);
    }

    public void AddOwnerIfNotExist(int ownerID){
        if (!ownersIDs.contains(ownerID)){
            ownersIDs.add(ownerID);
        }
    }

    public void AddManagerIfNotExist(int managerID){
        if (!ownersIDs.contains(managerID)){
            ownersIDs.add(managerID);
        }
    }

    public void AddStoreProductIfNotExist(Product product){
        inventory.AddStoreProductIfNotExist(product);

    }


    public Integer getId() {
        return id;
    }

    private static synchronized int getNextStoreID() {
        nextStoreID++;
        return nextStoreID;
    }

    private static synchronized int getNextExpressionID() {
        nextExpressionID++;
        return nextExpressionID;
    }

    public void setInventory(Inventory inventory){
        this.inventory=inventory;
    }

    public static void ClearSystem() {
        nextStoreID = 0;
    }

    public boolean checkFounder(int userID){
        return this.founderID == userID;
    }

    public boolean checkOwner(int userID){
        return this.ownersIDs.contains(userID);
    }

    public boolean checkManager(int newOwner) {
        return this.managersIDs.contains(newOwner);
    }

    public List<DummyProduct> ShowStoreProducts(){
        return inventory.ShowStoreProducts();
    }

    public Response AddProductToStore(String productName , Double price, String category, int quantity){
        return inventory.addProduct(productName, category, price, quantity);
    }

    public Response addProductToInventory(Integer productId, Integer quantity){
        return inventory.addQuantityProduct(productId, quantity);
    }

    public Response deleteProduct(Integer productId){
        return inventory.deleteProduct(productId);
    }

    public Response editProductDetails(Integer ownerId,Integer productId, String productName , Double price, String category, Integer quantity) {
        return inventory.editProductDetails(productId,productName,price,category,quantity);
    }

    public String addNewOwner(Integer userId, Integer newOwnerId) {
        this.ownersIDs.add(newOwnerId);
       // this.ownersPermission.put(newOwnerId, OP);
        return "";
    }

    public String addNewManager(Integer userId, Integer newManagerId){
                this.managersIDs.add(newManagerId);
                //this.managersPermission.put(newManagerId,om);
        return "";
    }

    public void setManagersIDs(List<Integer> managersIDs){
        this.managersIDs=managersIDs;
    }
    public void setOwnersIDs(List<Integer> ownersIDs){
        this.ownersIDs=ownersIDs;
    }

    public void setShoppingHistory(List<ShoppingHistory> shoppingHistories){
        this.shoppingHistory=shoppingHistories;
    }

    public String removeManager(Integer managerId) {
        this.managersIDs.remove(managerId);
        this.managersPermission.remove(managerId);
        return "The Manager removed";
    }

    public String removeOwner(Integer ownerId) {
        this.ownersIDs.remove(ownerId);
        this.ownersPermission.remove(ownerId);
        return "The Manager removed";
    }

    public Integer getProductID(String computer)
    {
        return inventory.getProductID(this.id,computer);
    }
/*
    public void addRatingToStore(Integer userID, Double Rating)
    {
        this.Ratings.put(userID,Rating);
        this.rate=CalculateRate();
    }

    public void removeRatingFromStore(Integer userID)
    {
        this.Ratings.remove(userID);
        this.rate=CalculateRate();
    }

    public Double CalculateRate() {
        Integer NumOfUsaers = 0;
        Double SumOfRating = 0.0;
        Set<Integer> RatingSet = this.Ratings.keySet();
        for (Integer id : RatingSet) {
            Double Rate = this.Ratings.get(id);
            NumOfUsaers++;
            SumOfRating = SumOfRating + Rate;
        }
        return SumOfRating / NumOfUsaers;
    }

    public void addRatingToProduct(Integer productID, Integer userID, Double Rating)
    {
        inventory.addRatingToProduct(productID,userID,Rating);
    }

    public void removeRatingFromProduct(Integer productID, Integer userID, Double Rating)
    {
        inventory.removeRatingFromProduct(productID,userID);
    }

    public Double CalculateRateForProduct(Integer productID)
    {
        return inventory.CalculateRateForProduct(productID);
    }
*/
    public List<DummyProduct> SearchProduct(String name, String category, int minprice, int maxprice) {
        List<Integer> FinalID=inventory.SearchProduct(name, category,minprice, maxprice);
        return inventory.getDummySearchForList(FinalID);
    }
    /*

    public List<DummyProduct> SearchProduct(String name, String category, int minprice, int maxprice) {
        List<Integer> FinalID = new ArrayList<>();
        if (name != null) {
            FinalID = inventory.getDummySearchByName(FinalID, name);
        }
        if (category != null) {
            FinalID = inventory.getDummySearchByCategory(FinalID, category);
        }
        if (minprice != -1 && maxprice != -1) {
            FinalID = inventory.getDummySearchByPrice(FinalID, minprice, maxprice);
        }
        return inventory.getDummySearchForList(FinalID);
    }

    public List<DummyProduct> SearchByName(String name, int minprice, int maxprice, int prank){
       List<Integer> FinalID=new ArrayList<>();
       if(name!=null){
           FinalID=inventory.getDummySearchByName(FinalID,name);
       }
        if(minprice!=-1&&maxprice!=-1){
            FinalID=inventory.getDummySearchByPrice( FinalID,minprice,maxprice);
        }
        if(prank!=-1){
            FinalID=inventory.getDummySearchByRate(FinalID,prank);
        }
        return  inventory.getDummySearchForList(FinalID);
    }

    public List<DummyProduct> SearchByCategory(String category, int minprice, int maxprice, int prank){
        List<Integer> FinalID = new ArrayList<>();
        if (category != null) {
            FinalID = inventory.getDummySearchByCategory(FinalID, category);
        }
        if (minprice != -1 && maxprice != -1) {
            FinalID = inventory.getDummySearchByPrice(FinalID, minprice, maxprice);
        }
        if (prank != -1) {
            FinalID = inventory.getDummySearchByRate(FinalID, prank);
        }
        return inventory.getDummySearchForList(FinalID);
    }
*/
    public Double getRate() {
        return rate;
    }

    public boolean checkProductsExistInTheStore(Integer productID, Integer quantity){
        return this.inventory.checkProductsExistInTheStore(productID,quantity);
    }

    public boolean checkBuyingPolicy(Integer userID,ConcurrentHashMap<Integer,Integer> productsInTheBug){
        if(this.buyingPolicy!=null)
        return this.buyingPolicy.checkEntitlement(productsInTheBug,userID,this.CalculatePriceBeforeSale(productsInTheBug));
        else
        return true;
    }

    public Double calculateBugPrice(Integer userId, ConcurrentHashMap<Integer,Integer> productsInTheBug){
       if(this.discountPolicy.getSale()!=null) {
           return this.discountPolicy.calculatePrice(productsInTheBug, userId, this.CalculatePriceBeforeSale(productsInTheBug));
       }
       return this.CalculatePriceBeforeSale(productsInTheBug);
       }


    public Double CalculatePriceBeforeSale (ConcurrentHashMap<Integer,Integer> productsInTheBug){
        Double priceBefore = 0.0;
        for (Integer pId : productsInTheBug.keySet()) {
            Integer quantity=productsInTheBug.get(pId);
            Double price=inventory.getProduct(pId).getPrice();
            priceBefore = priceBefore + price*quantity;
        }
        return priceBefore;
    }



    public boolean productIsLock(Integer productID){
        return inventory.productIsLock(productID);
    }

    public void lockProduct(Integer productID){
        this.inventory.lockProduct(productID);
    }

    public Lock getProductLock(int productID) {
        return this.inventory.getProductLock(productID);
    }

    public void  unLockProducts(Collection<Integer> productID){
        this.inventory.unlockProduct(productID);
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public BuyingPolicy getBuyingPolicy() {
        return buyingPolicy;
    }

    public String getName() {
        return name;
    }

    public String getProductName(Integer productID){
        return this.inventory.getProduct(productID).getProductName();
    }

    public Product getProduct(Integer productID)
    {
        return this.inventory.getProduct(productID);
    }

    public Response reduceProducts(ConcurrentHashMap<Integer, Integer> products_quantity) {
        return this.inventory.reduceProducts(products_quantity);
    }

    public void cancelReduceProducts(ConcurrentHashMap<Integer, Integer> products) {
        this.inventory.cancelReduceProducts(products);
    }

    public Response WriteComment(int userId, int productId, String comment) {
        if (this.possibleToAddComment(userId, productId)) {
            return this.inventory.addCommentToProduct(productId, userId, comment);
        }
        else
            return new Response(true, "User may not add a review for product he has not purchased before");
    }

    public void addHistory(ShoppingHistory sh) {
        this.shoppingHistory.add(sh);
    }

    /**
     * @requirement 4.11
     *
     *  @Return List [{
     *      "userID": int
     *      "storeID": int
     *      "products": List [{
     *          "storeID": int
     *          "storeName": String
     *          "productID": int
     *          "productName": String
     *          "price": double
     *          "category": String
     *          "quantity": int
     *      }]
     *  }]
     * }
     */
    public List<DummyShoppingHistory> ShowStoreHistory(){
        List<DummyShoppingHistory> shoppingHistories=new ArrayList<>();
        for(ShoppingHistory shoppingHistory:shoppingHistory){
            shoppingHistories.add(new DummyShoppingHistory(shoppingHistory));
        }
        return shoppingHistories;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", founderID=" + founderID +
                '}';
    }

    public List<Product> getProducts() {
        return this.inventory.getProducts();
    }

    public int getQuantity(Integer productID) {
       return this.inventory.getQuantity(productID);
    }

    public boolean possibleToAddComment(int userId, int productId) {
        return true;
        /*
        for (ShoppingHistory SH:this.shoppingHistory
             ) {
            if(SH.getUserID()==userId){
                if(SH.getProducts().contains(productId)){
                    return true;
                }
            }
        }
        return true;
    }
         */
    }

    public List<String> getCommentsForProduct(Integer productID){
        return this.inventory.getCommentsForProduct(productID);
    }

    public void pay(Double finalPrice) {
    }



    public boolean isProductExist(int id){
        return inventory.checkProductsExistInTheStore(id,1);
    }

    public void addOwnerPermission(int newOwner, OwnerPermission op) {
        this.ownersPermission.put(newOwner,op);
    }

    public void addManagerPermission(ManagerPermission mp) {
        this.managersPermission.put(mp.getUserId(),mp);
    }

    public void editManagerPermissions(int userID, int managerID, List<PermissionEnum.Permission> permissions) {
        ManagerPermission MP=this.managersPermission.get(managerID);
        if(MP==null){
            MP=new ManagerPermission(managerID,this.id);
            MP.setAppointmentId(userID);
            MP.setPermissions(permissions);
            this.managersPermission.put(managerID,MP);
        }
        else{
            MP.setPermissions(permissions);
            this.managersPermission.remove(managerID);
            this.managersPermission.put(managerID,MP);
        }
    }

    public ConcurrentHashMap<Integer,OwnerPermission> getOwnersIDs(){
       return this.ownersPermission;
    }

    public List<Integer> OwnersID (){
        return this.ownersIDs;
    }

    public ConcurrentHashMap<Integer,ManagerPermission> getManagerIDs(){
        return this.managersPermission;
    }

    public void setDiscountPolicy(DiscountPolicy d){
        this.discountPolicy=d;
    }

    public void setBuyingPolicy(BuyingPolicy buyingPolicy) {
        this.buyingPolicy = buyingPolicy;
    }

    //todo syncronize?
    public Response RemoveBuyingPolicy() {
        if(this.getBuyingPolicy()==null||
                this.getBuyingPolicy().getExp()==null){
            return new Response(true,"there is no policy");
        }
        this.buyingPolicy=null;
        return new Response("the buyingPolicy removed successfully");
    }

    public Response RemoveDiscountPolicy() {
        if(this.getDiscountPolicy()==null||
        this.getDiscountPolicy().getSale()==null){
            return new Response(true,"there is no policy");
        }
        this.discountPolicy=null;
        return new Response("the discountPolicy removed successfully");
    }

    public OwnerPermission getPermission(int key){
        return ownersPermission.get(key);
    }

    //Observable pattern
    //send alert to all owners of the store
    public void sendAlertToOwners(Response message){
        for(Integer ID : ownersIDs) {
            User user = tradingSystem.subscribers.get(ID);
            if(user!=null) {
                System.out.println(user);
                this.addObserver(user);
            }
        }
        this.setChanged();
        this.notifyObservers(message);
        this.deleteObservers();
    }

    //TODO Check!
    //Observable pattern
    //send alert to all owners of the store
    public void sendAlertOfBiddingToManager(Response message){
        for(Integer ID : managersIDs) {
            ManagerPermission MP=this.managersPermission.get(ID);
            if(MP!=null) {
                if(MP.hasPermission(PermissionEnum.Permission.RequestBidding)) {
                    User user = tradingSystem.subscribers.get(ID);
                    if (user != null) {
                        System.out.println(user);
                        this.addObserver(user);
                    }
                }
            }
        }
        this.setChanged();
        this.notifyObservers(message);
        this.deleteObservers();
    }

    //send alert to specific owner
    public void sendAlert(Integer ownerID, Response message){
        User user = tradingSystem.subscribers.get(ownerID);
        this.addObserver(user);
        this.setChanged();
        this.notifyObservers(message);
        this.deleteObserver(user);
    }

    public Double getDailyIncome() {
            Double DailyIncome=0.0;
            for (ShoppingHistory SH:this.shoppingHistory
            ) {
                Date today=new Date(System.currentTimeMillis());
                Calendar calForTodey = Calendar.getInstance();
                calForTodey.setTime(today);
                Calendar calForHistory = Calendar.getInstance();
                calForHistory.setTime(SH.getDate());
                if(calForHistory.get(Calendar.DAY_OF_WEEK)==calForTodey.get(Calendar.DAY_OF_WEEK)){
                    DailyIncome=DailyIncome+SH.getFinalPrice();
                }
            }
            return DailyIncome;
        }

    public void AddBidForProduct(int productID, int userID, Integer productPrice,Integer quantity) {
        if(this.Bids==null) {
            this.Bids = new ConcurrentLinkedDeque<>();
        }
        this.Bids.add(new Bid(userID, productID,this.id,productPrice,quantity,createOwnerList()));
    }

    public ConcurrentHashMap<Integer, Boolean> createOwnerList() {
        ConcurrentHashMap<Integer,Boolean> list=new ConcurrentHashMap<>();
        for (Integer key:this.OwnersID()
             ) {
            list.put(key,false);
        }
        for (Integer key: this.managersPermission.keySet()) {
            if(this.managersPermission.get(key).hasPermission(PermissionEnum.Permission.RequestBidding)){
                list.put(key,false);
            }
        }
        return null;
    }

    public boolean CheckBidForProductExist(Integer userID, Integer productID){
        for (Bid bid:this.Bids
             ) {
            if(bid.getUserID()==userID&&bid.getProductID()==productID){
                return true;
            }
        }
        return false;
    }

    public void RemoveProductForPurchaseOffer(int productID, int userWhoOffer) {
        for (Bid bid:this.Bids
        ) {
            if(bid.getUserID()==userWhoOffer&&bid.getProductID()==productID){
                this.Bids.remove(bid);
            }
        }
    }

    public ConcurrentLinkedDeque<Bid> getBids() {
        return Bids;
    }

    public Bid getBid(int userWhoOffer, int productID) {
        for (Bid bid:this.Bids
        ) {
            if(bid.getUserID()==userWhoOffer&&bid.getProductID()==productID){
                return bid;
            }
        }
        return null;
    }

    public Response changeSubmissionBid(int userID,int userWhoOffer, int productID,int productPrice, int quantity) {
        Bid bid = this.getBid(userWhoOffer, productID);
        if (bid == null) {
            return new Response(true, "ResponseForSubmissionBidding: The user " + userID + " try to to response the submission bid for product " + productID + " and user " + userWhoOffer + " but the bidding has already been answered");
        }
        //todo check!
        boolean succeededToLock = false;
        while (!succeededToLock) {
            synchronized (this) {
                succeededToLock = this.getBid(userWhoOffer, productID).bidIsLock();  //todo check!
            }
        }
        if(bid.isFinalState()){
            bid.unlockBid();
            return new Response(true, "ResponseForSubmissionBidding: The user "+userID+" try to to response the submission bid for product " +productID +" and user "+userWhoOffer+" but the bidding has already been answered");
        }
        bid.setPrice(productPrice);
        bid.setQuantity(quantity);
        bid.UpdateOwnerList(this.createOwnerList());
        bid.changeState(new initState());
        return bid.handle(userID);
    }

    public Response approveSubmissionBid(int userID,int userWhoOffer,int productID) {
        Bid bid=this.getBid(userWhoOffer, productID);
        if(bid==null){
            return new Response(true, "ResponseForSubmissionBidding: The user "+userID+" try to to response the submission bid for product " +productID +" and user "+userWhoOffer+" but the bidding has already been answered");
        }
        //todo check!
        boolean succeededToLock = false;
        while (!succeededToLock) {
            synchronized (this) {
                succeededToLock=bid.bidIsLock();  //todo check!
            }
        }
        if(bid.isFinalState()){
            bid.unlockBid();
            return new Response(true, "ResponseForSubmissionBidding: The user "+userID+" try to to response the submission bid for product " +productID +" and user "+userWhoOffer+" but the bidding has already been answered");
        }
        bid.UpdateOwnerList(createOwnerList());
        bid.changeState(new baseState());
        return bid.handle(userID);
    }

    public Response refuseSubmissionBid(int userID,int userWhoOffer,int productID) {
        Bid bid=this.getBid(userWhoOffer, productID);
        if(bid==null){
            return new Response(true, "ResponseForSubmissionBidding: The user "+userID+" try to to response the submission bid for product " +productID +" and user "+userWhoOffer+" but the bidding has already been answered");
        }
        //todo check!
        boolean succeededToLock = false;
        while (!succeededToLock) {
            synchronized (this) {
                succeededToLock=this.getBid(userWhoOffer, productID).bidIsLock();  //todo check!
            }
        }
        if(bid.isFinalState()){
            bid.unlockBid();
            return new Response(true, "ResponseForSubmissionBidding: The user "+userID+" try to to response the submission bid for product " +productID +" and user "+userWhoOffer+" but the bidding has already been answered");
        }
        bid.changeState(new refusalState());
        return bid.handle(userID);
    }

    //todo check the remove function
    public void removeBid(Bid bid) {
        for (Bid b:this.Bids
        ) {
            if(b.getUserID()==bid.getUserID()&&b.getProductID()==bid.getProductID()){
                this.Bids.remove(b);
            }
        }
    }
}
