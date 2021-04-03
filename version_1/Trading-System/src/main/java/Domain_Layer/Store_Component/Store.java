package Domain_Layer.Store_Component;

import Domain_Layer.User_Component.User;

import java.util.List;

public class Store {

    private Integer id;
    private String name;

    private Integer founderID;
    private List<Integer> ownersIDs;
    private List<Integer> managersIDs;

    private Discount_Policy discount_policy;
    private Buying_Policy buying_policy;

    private List<Integer> shopping_history;

}
