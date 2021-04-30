package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CompositeSale implements Sale{

    public List<Sale> children;
    private Integer ID;

    public CompositeSale()
    { this.children = new LinkedList<>(); }

    public Sale add(Sale sale){
        children.add(sale); return this;
    }

    public abstract Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID);

    @Override
    public Sale setSale(Sale sale) {
        this.children.add(sale);
        return this;
    }

    @Override
    public Sale getSale() {
        return this;
    }

    @Override
    public Expression setExpression(Integer saleID, Integer expID, Expression exp) {
        if(exp!=null) {
            for (Sale sale : children
            ) {
                Expression e = sale.setExpression(saleID, expID, exp);
                if (e != null) {
                    return e;
                }
            }
        }
        return null;
    }

    @Override
    public Sale setSale(Integer saleID, Sale sale) {
        if(this.ID==saleID){
            children.add(sale);
        }
        else {
            for (Sale s : children
            ) {
                sale.setSale(saleID, sale);
            }
        }
        return this;
    }

    @Override
    public Integer getID() {
        return this.ID;
    }

    @Override
    public Sale getSale(Integer saleID) {
        if(this.ID==saleID){
           return this;
        }
        else {
            for (Sale s : children
            ) {
                Sale sale=s.getSale(saleID);
                if(sale!=null){
                    return s;
                }
            }
        }
        return this;
    }

    public Boolean hasChildren()
    { return !children.isEmpty(); }

    public List<Sale> getChildren() {
        return children;
    }

    private void setChildren(List<Sale> children) {
        this.children = children;
    }
}

/*
@Override
    public Expression setExpression(Integer expID, Expression exp) {
        return null;
    }

@Override
    public Expression setExpression(Expression exp) {
        return null;
    }

    @Override
    public Expression getExpression() {
        return null;
    }

    @Override
    public Expression getExpression(Integer ID) {
        return null;
    }
 */

