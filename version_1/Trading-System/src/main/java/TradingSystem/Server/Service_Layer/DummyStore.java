package TradingSystem.Server.Service_Layer;

import TradingSystem.Server.DomainLayer.StoreComponent.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.DiscountPolicy;

import java.util.List;

public class DummyStore {

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

    public DummyStore(Integer id, String name, Integer founderID, List<Integer> ownersIDs, List<Integer> managersIDs, DiscountPolicy discountPolicy, BuyingPolicy buyingPolicy, Double rate, Integer numberOfRatings, List<Integer> shoppingHistory) {
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

}
