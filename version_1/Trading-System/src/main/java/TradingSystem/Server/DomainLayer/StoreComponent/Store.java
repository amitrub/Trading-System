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

    public Store(String name, Integer founderID, List<Integer> ownersIDs, List<Integer> managersIDs, DiscountPolicy discountPolicy, BuyingPolicy buyingPolicy, Double rate, Integer numberOfRatings, List<Integer> shoppingHistory) {
        this.id = getNextStoreID();
        this.name = name;
        this.founderID = founderID;
        this.ownersIDs = ownersIDs;
        this.managersIDs = managersIDs;
        this.discountPolicy = discountPolicy;
        this.buyingPolicy = buyingPolicy;
        this.rate = rate;
        this.numberOfRatings = numberOfRatings;
        this.shoppingHistory = shoppingHistory;
        this.inventory=Inventory.getInstance();
    }

    private static synchronized int getNextStoreID() {
        nextStoreID++;
        return nextStoreID;
    }

    public Integer addNewProduct(Integer ownerId, String productName , Double price, String category) {
        if (this.ownersIDs.contains(ownerId)) {
            Product p = new Product(productName, category, price);
            productQuantity.put(p.getProductID(), 1);
            inventory.addProduct(p, this.id);
            return 0;
        }
        String err="Only a store owner is allowed to add new products";
        return -1;
    }

    public Integer addProductToInventory(Integer ownerId, Integer productId, Integer quantity){
        if (this.ownersIDs.contains(ownerId)) {
            Iterator it = this.productQuantity.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                int id = (int)pair.getKey();
                int newQuantity=(int)pair.getValue()+quantity;
                if(id==productId) {
                        this.productQuantity.remove(pair);
                        this.productQuantity.put(id, newQuantity);
                        return 0;
                }
            }
            String err="The product does not exist in the system";
            return -1;
        }
        String err="Only a store owner is allowed to add products to the Inventory";
        return -1;
    }

    public Integer deleteProduct(Integer ownerId, Integer productId){
        if (this.ownersIDs.contains(ownerId)) {
            inventory.deleteProduct(productId, this.id);
            Iterator it = this.productQuantity.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                int id = (int) pair.getKey();
                if (id == productId)
                    this.productQuantity.remove(pair);
                return 0;
            }
            String err="The product does not exist in the system";
            return -1;
        }
        String err="Only a store owner is allowed to remove a product";
        return -1;
    }

    public Integer editProductDetails(Integer ownerId,Integer productId, String productName , Double price, String category) {
        if (this.ownersIDs.contains(ownerId)) {
            Product p=inventory.getProduct(productId);
            if(p!=null) {
                inventory.changeDetails(productId, productName, price, category);
                return 0;
            }
            String err="The product does not exist in the system";
            return -1;
        }
        String err="Only a store owner is allowed to edit the products details";
        return -1;
    }

    public Integer reduceProduct(Integer productId, Integer quantityToReduce){
        Iterator it = this.productQuantity.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int id = (int)pair.getKey();
            int newQuantity=(int)pair.getValue()-quantityToReduce;
            if(id==productId) {
                if (newQuantity < 0) {
                    newQuantity = 1;
                }
                this.productQuantity.remove(pair);
                this.productQuantity.put(id, newQuantity);
                return 0;
            }
        }
        String err="The product does not exist in the system";
        return -1;
    }

    //todo - ensure that the owner/manager is subscriber!
    public Integer addNewOwner(Integer userId, Integer newOwnerId){
         if (this.ownersIDs.contains(userId)){
             if(!this.ownersIDs.contains(newOwnerId)) {
                 this.ownersIDs.add(newOwnerId);
                 this.ownersAppointee.put(newOwnerId,userId);
                 return 0;
             }
             String err="This user is already the owner of this store";
         }
        String err="Only a store owner can appoint another store owner";
        return -1;
    }

    //todo - ensure that the owner/manager is subscriber!
    public Integer addNewManager(Integer userId, Integer newManagerId){
        if (this.ownersIDs.contains(userId)){
            if(!this.ownersIDs.contains(newManagerId)) {
                this.managersIDs.add(newManagerId);
                this.managersAppointee.put(newManagerId,userId);
                return 0;
            }
            String err="This user is already the owner of this store, so he can't be a manager";
        }
        String err="Only a store owner is allowed to appoint store's manager";
        return -1;
    }

    public Integer removeManager(Integer userId, Integer managerId){
        if (this.ownersIDs.contains(userId)){
            if(this.managersIDs.contains(managerId) && managersAppointee.get(managerId)!=null) {
                if(this.managersAppointee.get(managerId)==userId) {
                    Iterator it = this.managersAppointee.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        int id = (int) pair.getKey();
                        if (id == managerId)
                            this.managersAppointee.remove(pair);
                            this.managersIDs.remove(managerId);
                            return 0;
                    }
                    String err="something ia wrong";
                    return -1;
                }
                String err="Only the store owner who appointed the store manager can remove him";
                return -1;
            }
            String err="This user is not the manager of this store, so it impossible to remove him";
            return -1;
        }
        String err="Only a store owner is allowed to remove store's manager";
        return -1;
    }

    //todo - ensure that only the Trading Administrator can access this function.
    public List<Integer> GetShoppingHistory(){
        return this.shoppingHistory;
    }

    //todo - here??
    public Integer addProductToShopingBag(Integer userId, Integer productId, Integer quantity ){
        return 0;
    }
}
