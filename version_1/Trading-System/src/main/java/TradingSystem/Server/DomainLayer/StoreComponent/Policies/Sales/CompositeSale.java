package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CompositeSale implements Sale{

    public List<Sale> children;
  //  private Integer ID;

    public CompositeSale(List<Sale> S)
    { this.children= S ;}

    public CompositeSale()
    {this.children=new LinkedList<Sale>();}


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

    public Boolean hasChildren()
    { return !children.isEmpty(); }

    public List<Sale> getChildren() {
        return children;
    }

    private void setChildren(List<Sale> children) {
        this.children = children;
    }
}

