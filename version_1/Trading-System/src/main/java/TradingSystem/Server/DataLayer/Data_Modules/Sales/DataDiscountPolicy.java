package TradingSystem.Server.DataLayer.Data_Modules.Sales;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;

import javax.persistence.*;
import javax.xml.crypto.Data;

@Entity
@Table(name = "Discount_Policy")
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
    DBSale sale;

    public DataDiscountPolicy(){

    }

    public DataDiscountPolicy(DiscountPolicy policy){
        this.store_id=policy.getStoreId();
    }

    public DBSale getSale(){
        return sale;
    }

    public DataDiscountPolicy(int store_id, DBSale dataDiscountPolicy){
        this.store_id=store_id;
        this.sale= dataDiscountPolicy;
    }

    public int getStore_id(){
        return store_id;
    }
}
