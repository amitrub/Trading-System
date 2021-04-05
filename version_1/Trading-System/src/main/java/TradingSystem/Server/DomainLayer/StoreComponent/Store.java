package TradingSystem.Server.DomainLayer.StoreComponent;



import java.util.LinkedList;
import java.util.List;

public class Store {

    private Integer id;
    private String name;

    private Integer founderID;
    private List<Integer> ownersIDs;
    private List<Integer> managersIDs;

    private DiscountPolicy discountPolicy;
    private BuyingPolicy buyingPolicy;

    private Double rate;
    private Integer numberOfRatings;
    private List<Integer> shoppingHistory;

    public Store(Integer id, String name, Integer founderID, List<Integer> ownersIDs, List<Integer> managersIDs, DiscountPolicy discountPolicy, BuyingPolicy buyingPolicy, Double rate, Integer numberOfRatings, List<Integer> shoppingHistory) {
        this.id = id;
        this.name = name;
        this.founderID = founderID;
        this.ownersIDs = ownersIDs;
        this.managersIDs = managersIDs;
        this.discountPolicy = discountPolicy;
        this.buyingPolicy = buyingPolicy;
        this.rate = rate;
        this.numberOfRatings = numberOfRatings;
        this.shoppingHistory = shoppingHistory;
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
