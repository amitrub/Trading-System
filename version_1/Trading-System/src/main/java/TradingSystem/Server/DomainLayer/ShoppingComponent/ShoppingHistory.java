package TradingSystem.Server.DomainLayer.ShoppingComponent;

import javax.xml.crypto.Data;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingHistory {

    private Integer userID;
    private Integer storeID;

    //productID_quantity
    private ConcurrentHashMap<Integer,Integer> products;

    private Data data;
    private Double finalPrice;


}
