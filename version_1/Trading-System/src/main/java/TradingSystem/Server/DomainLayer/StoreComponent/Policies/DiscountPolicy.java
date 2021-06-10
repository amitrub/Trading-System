package TradingSystem.Server.DomainLayer.StoreComponent.Policies;

import TradingSystem.Server.DataLayer.Data_Modules.Sales.DBSale;
import TradingSystem.Server.DataLayer.Data_Modules.Sales.DBSaleExpression;
import TradingSystem.Server.DataLayer.Data_Modules.Sales.DataDiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.AndComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.OrComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.NumOfProductsForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.PriceForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.QuantityForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.*;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiscountPolicy {

    private Integer storeID;
   // private static int nextSaleID=0;
    private Sale sale;

    @Autowired
    TradingSystemImpl tradingSystem;

    public DiscountPolicy(Integer storeID,Sale s){
     this.storeID=storeID;
     this.sale=s;
    }
/*
    public static synchronized int getNextSaleID() {
        nextSaleID++;
        return nextSaleID;
    }

    public void RemoveSale(Integer saleID){
        this.Sales.remove(saleID);
    }
*/
    public int getStoreId(){
        return storeID;
    }

    public Sale getSale() {
        return sale;
    }

    public void AddSale(Sale sale){
        this.sale=sale;
    }
   //TODO check
    public Double calculatePrice(ConcurrentHashMap<Integer,Integer> products, Integer userID, Double priceBeforeSale){
       if(sale!=null) {
           Double sale = this.sale.calculateSale(products, priceBeforeSale, userID, storeID);
           return priceBeforeSale - sale;
       }
       return 0.0;
    }

    public DiscountPolicy(DataDiscountPolicy dataDiscountPolicy){
        this.storeID=dataDiscountPolicy.getStore_id();
        this.sale=createSale(dataDiscountPolicy.getSale());
    }

    public Sale createSale(DBSale sale){
        switch (sale.getType()) {
            case "ADD":
                AddComposite addComposite = new AddComposite();
                for (DBSale dbSale : sale.getSubdomains()) {
                    addComposite.add(createSale(dbSale));
                }
                return addComposite;
            case "XOR":
                XorComposite xorComposite = new XorComposite();
                for (DBSale dbSale : sale.getSubdomains()) {
                    xorComposite.add(createSale(dbSale));
                }
                return xorComposite;
            case "MAX":
                MaxComposite maxComposite = new MaxComposite();
                for (DBSale dbSale : sale.getSubdomains()) {
                    maxComposite.add(createSale(dbSale));
                }
                return maxComposite;
            case "Product":
                ProductSale productSale;
                if (sale.getExpression() != null) {
                    Expression expression = buildExpForSale(sale.getExpression());
                    productSale = new ProductSale(expression, sale.getProductID(), sale.getDiscountPercentage());
                } else {
                    productSale = new ProductSale(sale.getProductID(), sale.getDiscountPercentage());
                }
                return productSale;
            case "Store":
                StoreSale storeSale;
                if (sale.getExpression() != null) {
                    Expression expression = buildExpForSale(sale.getExpression());
                    storeSale = new StoreSale(expression, sale.getStoreID(), sale.getDiscountPercentage());
                } else {
                    storeSale = new StoreSale(sale.getStoreID(), sale.getDiscountPercentage());
                }
                return storeSale;
            case "Category":
                CategorySale categorySale;
                if (sale.getExpression() != null) {
                    Expression expression = buildExpForSale(sale.getExpression());
                    categorySale = new CategorySale(expression, sale.getCategory(), sale.getDiscountPercentage());
                } else {
                    categorySale = new CategorySale(sale.getCategory(), sale.getDiscountPercentage());
                }
                return categorySale;
        }
        return null;
    }

    public Expression buildExpForSale(DBSaleExpression expression){
        switch (expression.getType()) {
            case "Number":
                return new NumOfProductsForGetSale(expression.getNumOfProductsForSale());
            case "Price":
                return new PriceForGetSale(expression.getPriceForSale());
            case "Quantity":
                return new QuantityForGetSale(expression.getProductId(), expression.getQuantityForSale());
            case "OR":
                OrComposite orComposite = new OrComposite();
                for (DBSaleExpression exp : expression.getSubdomains()) {
                    orComposite.add(buildExpForSale(exp));
                }
                return orComposite;
            case "AND":
                AndComposite andComposite = new AndComposite();
                for (DBSaleExpression exp : expression.getSubdomains()) {
                    andComposite.add(buildExpForSale(exp));
                }
                return andComposite;
        }
        return null;
    }

    @Override
    public String toString() {
        return "DiscountPolicy{" +
                "storeID=" + storeID +
                ", sale=" + sale.toString()+"}";
    }
}
