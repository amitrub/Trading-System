package TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy.DataSimpleSales;

import TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy.DataSimpleSale;

import javax.persistence.Entity;

@Entity
public class DataPriceForGetSale extends DataSimpleSale {

    Integer priceForSale;
}
