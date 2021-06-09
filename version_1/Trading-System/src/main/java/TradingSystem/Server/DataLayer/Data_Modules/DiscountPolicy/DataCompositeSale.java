package TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.CompositeExpression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.CompositeSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.XorComposite;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DataCompositeSale extends DataSale {
    @OneToMany(
            mappedBy = "composite_data_sale",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    List<DataSale> expressionDataList;

    public DataCompositeSale(){

    }

    public DataCompositeSale(CompositeSale expression){
        this.expressionDataList=new ArrayList<>();
        for(Sale sale:expression.children){
                this.expressionDataList.add(new DataSale(sale));
        }
    }
}
