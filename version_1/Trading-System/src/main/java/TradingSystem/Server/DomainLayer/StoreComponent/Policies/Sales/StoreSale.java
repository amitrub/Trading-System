package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.concurrent.ConcurrentHashMap;

public class StoreSale extends SimpleSale {

   // private Integer saleID;
    private Integer storeID;
    private Double  discountPercentage;
    //private Expression expression;

    public StoreSale(Integer saleID, Integer storeID, Double discountPercentage) {
        super(saleID);
        this.storeID = storeID;
        this.discountPercentage = discountPercentage;
       // this.saleID=saleID;
    }

    @Override
    public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID) {
        if(this.getExpression().evaluate(products,finalSale, userID, storeID)){
            return  (discountPercentage/100)*finalSale;
        }
        return 0.0;
    }

}


/*
    @Override
    public Expression setExpression(Expression exp) {
        if(this.expression!=null){
         this.expression.add(exp);
        }
        else{
            this.expression=exp;
        }
        return this.expression;
    }

    @Override
    public Expression getExpression() {
        return this.expression;
    }

    @Override
    public Sale setSale(Sale sale) {
        return this;
    }

    @Override
    public Sale getSale() {
        return this;
    }



    @Override
    public Sale setSale(Integer saleID, Sale sale) {
        return this;
    }

    @Override
    public Expression getExpression(Integer ID) {
        if(this.expression!=null){
            return this.expression.getExpression(ID);
        }
        return this.expression;
    }

    @Override
    public Integer getID() {
        return this.saleID;
    }

    @Override
    public Sale getSale(Integer saleID) {
        return this;
    }


    @Override
    public Expression setExpression(Integer expID, Expression exp) {
        if(this.expression!=null) {
            this.expression.setExpression(expID, exp);
        }
        else{
            this.expression=exp;
        }
        return this.expression;
    }
 */