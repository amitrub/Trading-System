package TradingSystem.Server.ServiceLayer.DummyObject.DummySales;

import java.util.HashMap;
import java.util.Map;

public  class DummyProductSale extends DummySimpleSale {
    private Integer productID;
    private Integer discount;

    public DummyProductSale(Integer nodeID, Integer productID, Integer discountPercentage) {
        super(nodeID);
        this.productID = productID;
        this.discount = discountPercentage;
    }


    @Override
    public Map<String,Object> createMap() {
        Map<String,Object> mapElements=new HashMap<>();
        mapElements.put("NodeId",NodeID);
        mapElements.put("productID",productID);
        mapElements.put("discount",discount);
        mapElements.put("expression",expression.createMap());
        //Map<String,Object> mapType=new HashMap<>();
        //mapType.put("SimpleSale",mapElements);
        return mapElements;
    }

    @Override
    public String getName() {
        return "ProductSale";
    }

    public Integer getProductID() {
        return productID;
    }

    public Integer getDiscountPercentage() {
        return discount;
    }
}
