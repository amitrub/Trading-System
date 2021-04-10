package TradingSystem.Server.DomainLayer.StoreComponent;



import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static int nextStoreID=0;

    private Integer id;
    private String name;

    private final Integer founderID;
    private List<Integer> ownersIDs = new LinkedList<>();
    private List<Integer> managersIDs = new LinkedList<>();

    //ownerID_hisAppointeeID
    private ConcurrentHashMap<Integer, Integer> ownersAppointee = new ConcurrentHashMap<>();
    //managersID_hisAppointeeID
    private ConcurrentHashMap<Integer, Integer> managersAppointee = new ConcurrentHashMap<>();;

    private DiscountPolicy discountPolicy;
    private BuyingPolicy buyingPolicy;

    private Double rate;
    //userID_rating
    private ConcurrentHashMap<Integer, Double> Ratings = new ConcurrentHashMap<>();;

    private List<ShoppingHistory> shoppingHistory = new LinkedList<>();

    private Inventory inventory;

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
    }

    public Integer getId() {
        return id;
    }

    private static synchronized int getNextStoreID() {
        nextStoreID++;
        return nextStoreID;
    }

    public boolean checkFounder(int userID){
        return this.founderID == userID;
    }

    public boolean checkOwner(int userID){
        return this.ownersIDs.contains(userID);
    }

    public List<DummyProduct> ShowStoreProducts(){
        return inventory.ShowStoreProducts();
    }

    public Response AddProductToStore(String productName , Double price, String category){
        return inventory.addProduct(productName, category, price);
    }

    public Response addProductToInventory(Integer productId, Integer quantity){
        return inventory.addQuantityProduct(productId, quantity);
    }

    public Response deleteProduct(Integer productId){
        return inventory.deleteProduct(productId);
    }

    public void editProductDetails(Integer ownerId,Integer productId, String productName , Double price, String category)
    {
        inventory.editProductDetails(productId,productName,price,category);
    }

    //todo - ensure that the owner/manager is subscriber!
    public String addNewOwner(Integer userId, Integer newOwnerId)
    {
         if (this.ownersIDs.contains(userId)){
             if(!this.ownersIDs.contains(newOwnerId)) {
                 this.ownersIDs.add(newOwnerId);
                 this.ownersAppointee.put(newOwnerId,userId);
                 return "The owner added";
                 //return 0;
             }
             return "This user is already the owner of this store";
         }
        return "Only a store owner can appoint another store owner";
        //return -1;
    }

    //todo - ensure that the owner/manager is subscriber!
    public String addNewManager(Integer userId, Integer newManagerId)
    {
        if (this.ownersIDs.contains(userId)){
            if(!this.ownersIDs.contains(newManagerId)) {
                this.managersIDs.add(newManagerId);
                this.managersAppointee.put(newManagerId,userId);
               return "The manager added";
               // return 0;
            }
            return "This user is already the owner of this store, so he can't be a manager";
        }
        return "Only a store owner is allowed to appoint store's manager";
        //return -1;
    }

    public String removeManager(Integer userId, Integer managerId)
    {
        if (this.ownersIDs.contains(userId)){
            if(this.managersIDs.contains(managerId) && managersAppointee.get(managerId)!=null) {
                if(this.managersAppointee.get(managerId)==userId) {
                    Iterator it = this.managersAppointee.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        int id = (int) pair.getKey();
                        if (id == managerId)
                            this.managersAppointee.remove(managerId);
                            this.managersIDs.remove(managerId);
                            return "The Manager removed";
                            //return 0;
                    }
                    return "something ia wrong";
                    //return -1;
                }
                return "Only the store owner who appointed the store manager can remove him";
                //return -1;
            }
            return "This user is not the manager of this store, so it impossible to remove him";
            //return -1;
        }
        return "Only a store owner is allowed to remove store's manager";
        //return -1;
    }

    //todo - ensure that only the Trading Administrator can access this function.
    public List<ShoppingHistory> GetShoppingHistory()
    {
        return this.shoppingHistory;
    }

    public Integer getProductID(String computer)
    {
        return inventory.getProductID(this.id,computer);
    }

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

    public List<DummyProduct> SearchByName(String name, int minprice, int maxprice, int prank){
       LinkedList<Integer> FinalID=new LinkedList<>();
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
        LinkedList<Integer> FinalID = new LinkedList<>();
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

    public Double getRate() {
        return rate;
    }

    public boolean checkProductsExistInTheStore(Integer productID, Integer quantity){
        return this.inventory.checkProductsExistInTheStore(productID,quantity);
    }

    public boolean checkBuyingPolicy(Integer productID, Integer quantity, ConcurrentHashMap<Integer,Integer> productsInTheBug){
        return true;
    }

    public Double calculateBugPrice(Integer productID, Integer quantity,ConcurrentHashMap<Integer,Integer> productsInTheBug){
        return 1.0;
    }

    public boolean productIsLock(Integer productID){
        return inventory.productIsLock(productID);
    }

    public void lockProduct(Integer productID){
        this.inventory.lockProduct(productID);
    }

    public void  unLockProducts(Collection<Integer> productID){
        this.inventory.unlockProduct(productID);
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


    public List<DummyShoppingHistory> getHistory(){
        List<DummyShoppingHistory> shoppingHistories=new LinkedList<>();
        for(ShoppingHistory shoppingHistory:shoppingHistory){
            shoppingHistories.add(shoppingHistory.createDummy());
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

    public LinkedList<Product> getProducts() {
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
}
