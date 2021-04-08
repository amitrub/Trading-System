package TradingSystem.Server.DomainLayer.ShoppingComponent;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingHistory {

    private Integer userID;
    private Integer storeID;

    //productID_quantity
    private ConcurrentHashMap<Integer,Integer> products;

    private Date date;

    private Double finalPrice;


}
