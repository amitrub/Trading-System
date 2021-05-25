package TradingSystem.Server.DataLayer.Data_Modules;

import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Map;

import static TradingSystem.Server.ServiceLayer.Configuration.errMsgGenerator;
@Entity(name = "store_data")
@SequenceGenerator(name="STORE_SEQUENCE_GENERATOR", sequenceName="STORE", initialValue=1, allocationSize=10)
public class DataStore {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="STORE_SEQUENCE_GENERATOR")
    private Integer id;
    private String name;
    private Double storeRate;

    public DataStore(){
    }


    public DataStore(String name, Double storeRate) {
        this.name = name;
        this.storeRate = storeRate;
    }

    public DataStore(String name) {
        this.name = name;
        this.storeRate = -1.0;
    }

    public DataStore(Store store) {
        this.id = store.getId();
        this.name = store.getName();
        this.storeRate = store.getRate();
    }

    public DataStore(Map<String, Object> map) {
        this.id = (Integer) map.get("id");
        this.name = (String) map.get("name");
        try {
            this.storeRate = (Double) map.get("storeRate");
        }
        catch (Exception e){
            this.storeRate = new Double((Integer) map.get("storeRate"));
        }
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

    public static ArrayList<TradingSystem.Server.ServiceLayer.DummyObject.DummyStore> makeDummyStoreFromJSON(JSONArray jsonArray) {
        ArrayList dummyStoreArr = new ArrayList();
        try {
            for (int i=0;i<jsonArray.length();i++) {
                JSONObject jsonResponse = jsonArray.getJSONObject(i);
                int id = jsonResponse.getInt("id");
                String name = jsonResponse.getString("name");
                double storeRate = jsonResponse.getDouble("storeRate");
                TradingSystem.Server.ServiceLayer.DummyObject.DummyStore dummyStore = new TradingSystem.Server.ServiceLayer.DummyObject.DummyStore(id, name, storeRate);
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
//        return "Store {" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", storeRate=" + storeRate +
//                '}';
        JSONObject JO = new JSONObject();
        try {
            JO.put("id", id);
            JO.put("name", name);
            JO.put("storeRate", storeRate);
        } catch (Exception e) {
            System.out.println("dummyStore toString error");
        }
        return JO.toString();
    }
}
