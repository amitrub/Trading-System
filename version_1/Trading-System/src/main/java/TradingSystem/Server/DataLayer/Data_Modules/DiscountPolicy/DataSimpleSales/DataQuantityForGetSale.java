package TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy.DataSimpleSales;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy.DataSimpleSale;

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
}
