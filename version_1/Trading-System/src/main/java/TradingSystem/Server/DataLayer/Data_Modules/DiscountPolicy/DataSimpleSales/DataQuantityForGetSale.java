package TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy.DataSimpleSales;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy.DataDiscountPolicy;
import TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy.DataSimpleSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.QuantityForGetSale;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DataQuantityForGetSale extends DataSimpleSale {
    @ManyToOne
    @JoinColumn(
            name = "product_id",
            nullable = false,
            referencedColumnName = "productID",
            foreignKey = @ForeignKey(
                    name = "prodcut_id_fk"
            )
    )
    private DataProduct productId;
    private Integer quantityForSale;

    public DataQuantityForGetSale(){

    }

    public DataQuantityForGetSale(DataProduct product, int quantityForSale){
        super();
        this.productId=product;
        this.quantityForSale=quantityForSale;
    }

    public DataQuantityForGetSale(QuantityForGetSale quantityForGetSale){
        this.quantityForSale=quantityForGetSale.getQuantityForSale();
    }
}
