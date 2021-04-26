package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CompositeSale implements Sale{
    public List<Sale> children;

    public CompositeSale()
    { this.children = new LinkedList<>(); }

    public Sale add(Sale sale){
        children.add(sale); return this;
    }

    public Sale set(String name, Boolean value){
        return this;
    }

    public abstract Boolean checkEntitlement(ConcurrentHashMap<Integer,Integer> products,Double finalPrice);
    public abstract Double calculateSale(ConcurrentHashMap<Integer,Integer> products,Double finalSale);

    public Boolean hasChildren()
    { return !children.isEmpty(); }

    public List<Sale> getChildren() {
        return children;
    }

    private void setChildren(List<Sale> children) {
        this.children = children;
    }
}

