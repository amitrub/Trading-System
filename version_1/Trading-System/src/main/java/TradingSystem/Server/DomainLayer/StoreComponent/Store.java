package TradingSystem.Server.DomainLayer.StoreComponent;



import TradingSystem.Server.DomainLayer.UserComponent.User;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static int nextStoreID=0;

    private Integer id;
    private String name;

    private Integer founderID;
    private List<Integer> ownersIDs;
    private List<Integer> managersIDs;

    //ownerID_hisAppointeeID
    private ConcurrentHashMap<Integer, Integer> ownersAppointee;
    //managersID_hisAppointeeID
    private ConcurrentHashMap<Integer, Integer> managersAppointee;

    private DiscountPolicy discountPolicy;
    private BuyingPolicy buyingPolicy;

    private Double rate;
    private Integer numberOfRatings;
    private List<Integer> shoppingHistory;

    //poductID_quantity
    private ConcurrentHashMap<Integer, Integer> productQuantity;

    public Inventory inventory;

    public Store(String name, Integer founderID,  DiscountPolicy discountPolicy, BuyingPolicy buyingPolicy) {
        this.id = getNextStoreID();
        this.name = name;
        this.founderID = founderID;
        this.ownersIDs = new LinkedList<Integer>();
        this.ownersIDs.add(founderID);
        this.managersIDs = new LinkedList<Integer>();
        this.ownersAppointee=new ConcurrentHashMap<>();
        this.managersAppointee=new ConcurrentHashMap<>();
        this.discountPolicy = discountPolicy;
        this.buyingPolicy = buyingPolicy;
        this.rate =1.0; //todo- add rating!
        this.numberOfRatings =0;
        this.shoppingHistory = new LinkedList<Integer>();
        this.productQuantity=new ConcurrentHashMap<>();
        this.inventory=Inventory.getInstance();
    }

    private static synchronized int getNextStoreID() {
        nextStoreID++;
        return nextStoreID;
    }

    public  String addNewProduct(Integer ownerId, String productName , Double price, String category) {
        if (this.ownersIDs.contains(ownerId)) {
            Product p = new Product(productName, category, price);
            productQuantity.put(p.getProductID(), 1);
            inventory.addProduct(p, this.id);
            return "The product added";
            //return 0;
        }
        return "Only a store owner is allowed to add new products";
        //return -1;
    }

    public  String addProductToInventory(Integer ownerId, Integer productId, Integer quantity){
        if (this.ownersIDs.contains(ownerId)) {
            Iterator it = this.productQuantity.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                int id = (int)pair.getKey();
                int newQuantity=(int)pair.getValue()+quantity;
                if(id==productId) {
                        this.productQuantity.remove(productId);
                        this.productQuantity.put(id, newQuantity);
                        return "The Inventory update";
                   //     return 0;
                }
            }
            return "The product does not exist in the system";
          //  return -1;
        }
        return "Only a store owner is allowed to add products to the Inventory";
        //return -1;
    }

    public  String deleteProduct(Integer ownerId, Integer productId){
        if (this.ownersIDs.contains(ownerId)) {
            inventory.deleteProduct(productId, this.id);
           if(this.productQuantity.containsKey(productId)){
                this.productQuantity.remove(id);
                return "The product delete";
                //return 0;
            }
            return "The product does not exist in the system";
            //return -1;
        }
        return "Only a store owner is allowed to remove a product";
        //return -1;
    }

    public String editProductDetails(Integer ownerId,Integer productId, String productName , Double price, String category) {
        if (this.ownersIDs.contains(ownerId)) {
            Product p=inventory.getProduct(productId);
            if(p!=null) {
                inventory.changeDetails(productId, productName, price, category);
                return "The product update";
            }
            return "The product does not exist in the system";
        }
        return "Only a store owner is allowed to edit the products details";
    }

    public String reduceProduct(Integer productId, Integer quantityToReduce){
        if(productQuantity.containsKey(productId)) {
            Iterator it = this.productQuantity.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                int id = (int) pair.getKey();
                int newQuantity = (int) pair.getValue() - quantityToReduce;
                if (id == productId) {
                    if (newQuantity < 0) {
                        newQuantity = 1;
                    }
                    this.productQuantity.remove(productId);
                    this.productQuantity.put(id, newQuantity);
                    return "The quantity update";
                }
            }
        }
        return "The product does not exist in the system";

    }

    //todo - ensure that the owner/manager is subscriber!
    public String addNewOwner(Integer userId, Integer newOwnerId){
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
    public String addNewManager(Integer userId, Integer newManagerId) {
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

    public String removeManager(Integer userId, Integer managerId){
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
    public List<Integer> GetShoppingHistory(){
        return this.shoppingHistory;
    }

    //todo - here??
    public Integer addProductToShopingBag(Integer userId, Integer productId, Integer quantity ){
        return 0;
    }

    public Integer getProductID(String computer) {
        return inventory.getProductID(this.id,computer);
    }
}
