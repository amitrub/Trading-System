package TradingSystem.Server.DataLayer.Data_Modules.Sales;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;

import javax.persistence.*;
import javax.xml.crypto.Data;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "Discount_Policy")
public class DataDiscountPolicy {
    @Id
    @SequenceGenerator(
            name = "Discount_SEQUENCE",
            sequenceName = "Discount_SEQUENCE",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "Discount_SEQUENCE"
    )
    @Column(
            name = "DiscountID"
    )
    private Integer DiscountID;
    @OneToOne
    @JoinColumn(
            name = "store_id",
            nullable = false,
            referencedColumnName = "storeID",
            foreignKey = @ForeignKey(
                    name = "store_id_fk"
            )
    )
    DataStore store_id;
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

//    public DataDiscountPolicy(DiscountPolicy policy){
//        this.store_id=policy.getStoreId();
//    }

    public DBSale getSale(){
        return sale;
    }

    public DataDiscountPolicy(DataStore store_id, DBSale dataDiscountPolicy){
        this.store_id=store_id;
        this.sale= dataDiscountPolicy;
    }

    public DataStore getStore(){
        return store_id;
    }
}
