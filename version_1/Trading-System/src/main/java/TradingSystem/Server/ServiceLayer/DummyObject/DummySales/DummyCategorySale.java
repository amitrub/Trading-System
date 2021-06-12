package TradingSystem.Server.ServiceLayer.DummyObject.DummySales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.CategorySale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.ProductSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;

import java.util.HashMap;
import java.util.Map;

public class DummyCategorySale extends DummySimpleSale{

    private String category;
    private Integer  discount;

    public DummyCategorySale(Integer nodeID, String category, Integer discountPercentage) {
        super(nodeID);
        this.category = category;
        this.discount = discountPercentage;
    }

    @Override
    public Map<String,Object> createMap() {
        Map<String,Object> mapElements=new HashMap<>();
        mapElements.put("NodeId",NodeID);
        mapElements.put("category",category);
        mapElements.put("discount",discount);
        mapElements.put("expression",expression.createMap());
        //Map<String,Object> mapType=new HashMap<>();
        //mapType.put("CategorySale",mapElements);
        return mapElements;
    }

    @Override
    public String getName() {
        return "CategorySale";
    }

    @Override
    public Sale closeSale() {
        CategorySale CS=new CategorySale(category, discount);
        Expression exp=this.expression.closeExp();
        CS.setExpression(exp);
        return CS;
    }

    public String getCategory() {
        return category;
    }

    public Integer getDiscountPercentage() {
        return discount;
    }
}
