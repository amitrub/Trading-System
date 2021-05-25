package TradingSystem.Server.DataLayer.Data_Modules;

import java.io.Serializable;

public class DummyKeyShopping implements Serializable {
    private Integer userID;
    private Integer storeID;

    public DummyKeyShopping() {}

    public DummyKeyShopping(Integer userID, Integer storeID) {
        this.userID = userID;
        this.storeID = storeID;
    }
}
