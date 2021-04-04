package Domain_Layer.User_Component;

import Domain_Layer.Shopping_Component.Shopping_Cart;
import Domain_Layer.Trading_System_Component.Trading_System;

import java.util.List;

public  class User {

    private static Trading_System trading_system;

    private Integer id;
    private List<Integer> my_founded_storesiDs;
    private List<Integer> my_owned_storesiDs;
    private List<Integer> my_managed_storesiDs;


    private Shopping_Cart shopping_cart;
    private List<Integer> shopping_history;


}
