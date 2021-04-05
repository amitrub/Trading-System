package TradingSystem.Server.DomainLayer.UserComponent;



import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

import java.util.List;

public  class User {

    private TradingSystem tradingSystem = TradingSystem.getInstance();

    private Integer id;
    private String password;
    private List<Integer> myFoundedStoresIDs;
    private List<Integer> myOwnedStoresIDs;
    private List<Integer> myManagedStoresIDs;


    private ShoppingCart shoppingCart;
    private List<Integer> shoppingHistory;


}
