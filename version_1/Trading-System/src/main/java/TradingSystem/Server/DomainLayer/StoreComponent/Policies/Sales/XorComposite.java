package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.CompositeExpression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.XorDecision.Cheaper;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.XorDecision.Decission;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class XorComposite extends CompositeSale{

    private Decission des;
    public XorComposite(List<Sale> c) {
        super(c);
    }

    public XorComposite(){
        super(new LinkedList<Sale>());
        this.des=new Cheaper();
    }

    @Override
    public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID) {
         LinkedList<Sale> sales=new LinkedList<>();
        for (Sale s:children
             ) {
            if(s.calculateSale(products,finalSale,userID,storeID)>0){
                sales.add(s);
            }
        }
        if(sales.isEmpty())
           return 0.0;
        else
           return des.chooseSale(sales,products,finalSale,userID,storeID);
    }

    public void setDes(Decission des) {
        this.des = des;
    }
}
