package TradingSystem.Server.ServiceLayer.DummyObject;

import TradingSystem.Server.DomainLayer.StoreComponent.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static TradingSystem.Server.ServiceLayer.Configuration.errMsgGenerator;

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

    public String getName() {
        return name;
    }

    public Double getStoreRate() {
        return storeRate;
    }

    public static ArrayList<DummyStore> makeDummyStoreFromJSON(JSONArray jsonArray) {
        ArrayList dummyStoreArr = new ArrayList();
        try {
            for (int i=0;i<jsonArray.length();i++) {
                JSONObject jsonResponse = jsonArray.getJSONObject(i);
                int id = jsonResponse.getInt("id");
                String name = jsonResponse.getString("name");
                double storeRate = jsonResponse.getDouble("storeRate");
                DummyStore dummyStore = new DummyStore(id, name, storeRate);
                dummyStoreArr.add(dummyStore);
            }

            return dummyStoreArr;
        } catch (Exception e) {
            System.out.println(errMsgGenerator("Service", "DummyStore", "43", "error in making dummyStore from JSON object"));
        }
        return dummyStoreArr;
    }

    @Override
    public String toString() {
        return "Store {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", storeRate=" + storeRate +
                '}';
    }
}
