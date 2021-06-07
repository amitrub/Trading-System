package TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy.DataSimpleSales;

import TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy.DataSimpleSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.NumOfProductsForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.PriceForGetSale;

import javax.persistence.Entity;

@Entity
public class DataPriceForGetSale extends DataSimpleSale {

    Integer priceForSale;

    public DataPriceForGetSale(){

    }
    public DataPriceForGetSale(int priceForSale){
        super();
        this.priceForSale=priceForSale;
    }

    public DataPriceForGetSale(PriceForGetSale getSale){
        super();
        this.priceForSale=getSale.getPriceForSale();
    }

}
