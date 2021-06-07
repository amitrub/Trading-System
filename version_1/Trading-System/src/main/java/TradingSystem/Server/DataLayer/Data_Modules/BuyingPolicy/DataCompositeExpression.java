package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy;

import javax.persistence.*;
import java.util.List;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DataCompositeExpression extends DataExpression {
    @OneToMany(
            mappedBy = "composite_data_expression",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    List<DataExpression> expressionDataList;
}
