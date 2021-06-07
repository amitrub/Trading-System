package TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataCompositeExpression;
import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataExpression;

import javax.persistence.*;
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

}
