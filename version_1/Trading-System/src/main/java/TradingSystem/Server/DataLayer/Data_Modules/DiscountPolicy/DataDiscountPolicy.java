package TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;

import javax.persistence.*;

@Entity
public class DataDiscountPolicy {

    @Id
    @JoinColumn(
            name = "store_id",
            nullable = false,
            referencedColumnName = "storeID",
            foreignKey = @ForeignKey(
                    name = "store_id_fk"
            )
    )
    int store_id;
    @ManyToOne
    @JoinColumn(
            name = "sale",
            nullable = false,
            referencedColumnName = "SaleID",
            foreignKey = @ForeignKey(
                    name = "sale_id_fk"
            )
    )
    DataSale sale;

    public DataDiscountPolicy(){

    }

    public DataDiscountPolicy(int store_id, DataSale sale){
        this.store_id = store_id;
        this.sale=sale;
    }
}
