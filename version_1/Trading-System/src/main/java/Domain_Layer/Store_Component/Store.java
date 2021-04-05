package Domain_Layer.Store_Component;

import Domain_Layer.User_Component.User;

import java.util.LinkedList;
import java.util.List;

public class Store {

    private Integer id;
    private String name;

    private Integer founderID;
    private List<Integer> ownersIDs;
    private List<Integer> managersIDs;

    private Discount_Policy discount_policy;
    private Buying_Policy buying_policy;

    private Double Rate;
    private Integer NumberOfRatings;
    private List<Integer> shopping_history;


    public Store(String name, Integer founderID, Discount_Policy discount_policy, Buying_Policy buying_policy) { //todo need policy? id?
        this.name = name;
        this.founderID = founderID;
        this.discount_policy = discount_policy;
        this.buying_policy = buying_policy;
        this.ownersIDs=new LinkedList<Integer>();
        this.managersIDs=new LinkedList<Integer>();
        this.shopping_history=new LinkedList<Integer>();
        this.ownersIDs.add(founderID);
        this.Rate=0.0;
        this.NumberOfRatings=0;
    }

    //todo id??
    public Integer AddNewProduct(Integer productId, String productName , Double price, String Category){ return 0; }

    public Integer AddProductToInventory(Integer productId, Integer quantity){
        return 0;
    }

    public Integer RemoveProduct(Integer productId){
        return 0;
    }

    //todo - here??
    public Integer AddProductToShopingBag(Integer userId, Integer productId, Integer quantity ){
        return 0;
    }

    public Integer AddNewOwner(Integer userId, Integer newOwnerId){ return 0; }

    public Integer AddNewManager(Integer userId, Integer newManagerId){
        return 0;
    }

    public Integer RemoveManager(Integer userId, Integer ManagerId){
        return 0;
    }

    public Integer RemoveOwner(Integer userId, Integer OwnerId){
        return 0;
    }

    public List<Integer> GetOwnerInfo(Integer userId, Integer OwnerId){
        return null;
    }

    public List<Integer> GetShoppingHistory(Integer userId, Integer OwnerId){
        return null;
    }
}
