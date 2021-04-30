package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.concurrent.ConcurrentHashMap;

public abstract class SimpleSale implements Sale {

    private Integer saleID;
    private Expression expression;

    public SimpleSale(Integer saleID){
        this.saleID=saleID;
    }

    public Integer getID(){
        return this.saleID;
    }

    public abstract Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID);

    public Sale setSale(Sale sale) {
        return this;
    }

    public Sale getSale() {
        return this;
    }

    @Override
    public Expression setExpression(Integer saleID, Integer expID, Expression exp) {
        if(this.saleID==saleID){
            if(this.expression!=null){
                this.expression.setExpression(expID,exp);
            }
            else{
                this.expression=exp;
            }
            return this.expression;
        }
        return null;
    }

    public Sale setSale(Integer saleID, Sale sale) { return this; }

    public Sale getSale(Integer saleID) {
        if(this.saleID==saleID){
            return this;
         }
        return null;
    }

    public Expression setExpression(Expression exp){
        if(this.expression!=null){
            this.expression.add(exp);
        }
        else{
            this.expression=exp;
        }
        return this.expression;
    }

    public Expression getExpression(){
        return this.expression;
    }

    public Expression setExpression(Integer expID, Expression exp){
        if(this.expression!=null) {
            this.expression.setExpression(expID, exp);
        }
        else{
            this.expression=exp;
        }
        return this.expression;
    }

    public Expression getExpression(Integer ID){
        if(this.expression!=null){
            return this.expression.getExpression(ID);
        }
        return this.expression;
    }

}
