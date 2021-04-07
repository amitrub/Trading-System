package TradingSystem.Server.ServiceLayer.DummyObject;

import TradingSystem.Server.DomainLayer.StoreComponent.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;

import java.util.List;

public class DummyStore {

    private Integer id;
    private String name;
    private Double storeRate;

    public DummyStore(Integer id, String name, Double storeRate) {
        this.id = id;
        this.name = name;
        this.storeRate = storeRate;
    }

    public DummyStore(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.storeRate = store.getRate();
    }

    public Integer getId() {
        return id;
    }

    //TODO make dummay store from json
}
