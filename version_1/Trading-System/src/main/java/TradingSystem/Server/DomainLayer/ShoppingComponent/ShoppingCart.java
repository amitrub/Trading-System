package TradingSystem.Server.DomainLayer.ShoppingComponent;

import java.util.List;

public class ShoppingCart {

    private Integer userID;
    private List<ShoppingBag> shoppingCart;

    private Object payment;//?

    //todo -userId?
    public List<ShoppingBag> GetInfo(){
        return null;
    }

    public Integer Purchase(){
        return 0;
    }

    public Integer Purchase(Object Payment){
        return 0;
    }
}
