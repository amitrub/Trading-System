package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.concurrent.ConcurrentHashMap;

public interface Sale {
     public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID);
     public Sale setSale(Sale sale);
     public Sale getSale();


     /**
      *option with Id
      */
     /*
     public Expression setExpression(Integer saleID, Integer expID, Expression exp);
     public Sale setSale(Integer saleID, Sale sale);
     public Sale getSale(Integer saleID);
     public Integer getID();
     */
    // public Expression setExpression(Expression exp);
    // public Expression getExpression();
    // public Expression setExpression(Integer expID, Expression exp);
    // public Expression getExpression(Integer ID);

}
