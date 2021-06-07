package TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.XorComposite;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class DataXorSale extends DataCompositeSale {

    public DataXorSale(){
        super();
    }

    public DataXorSale (XorComposite xorComposite){
        this.expressionDataList=new ArrayList<>();
        for(Sale sale:xorComposite.getChildren()){
            this.expressionDataList.add(new DataSale(sale));
        }
    }
}
