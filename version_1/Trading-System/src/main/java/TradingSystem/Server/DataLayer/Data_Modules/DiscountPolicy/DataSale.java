package TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataCompositeExpression;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DataSale {
    @Id
    @SequenceGenerator(
            name = "Expression_SEQUENCE",
            sequenceName = "Expression_SEQUENCE",
            allocationSize = 1
    )
    @Column(
            name = "SaleID"
    )
    int saleID;
    @ManyToOne
    @JoinColumn(
            name = "composite_data_sale",
            nullable = false,
            referencedColumnName = "SaleID",
            foreignKey = @ForeignKey(
                    name = "sale_id_fk"
            )
    )
    DataCompositeSale composite_data_sale;
}
