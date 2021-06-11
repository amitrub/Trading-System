package TradingSystem.Server.ServiceLayer.DummyObject.DummySales;

import java.util.HashMap;
import java.util.Map;

public class DummyCategorySale extends DummySimpleSale{

    private String category;
    private Integer  discountPercentage;

    public DummyCategorySale(Integer nodeID, String category, Integer discountPercentage) {
        super(nodeID);
        this.category = category;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public Map<String,Object> createMap() {
        Map<String,Object> mapElements=new HashMap<>();
        mapElements.put("NodeId",NodeID);
        mapElements.put("category",category);
        mapElements.put("discountPercentage",discountPercentage);
        mapElements.put("expression",expression.createMap());
        //Map<String,Object> mapType=new HashMap<>();
        //mapType.put("CategorySale",mapElements);
        return mapElements;
    }

    @Override
    public String getName() {
        return "CategorySale";
    }

    public String getCategory() {
        return category;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }
}
