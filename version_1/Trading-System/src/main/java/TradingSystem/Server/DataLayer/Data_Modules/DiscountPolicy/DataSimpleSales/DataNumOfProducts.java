package TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy.DataSimpleSales;

import TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy.DataSimpleSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.NumOfProductsForGetSale;

import javax.persistence.Entity;

@Entity
public class DataNumOfProducts extends DataSimpleSale {

    private Integer numOfProductsForSale;

    public DataNumOfProducts(){

    }

    public DataNumOfProducts(int numOfProductsForSale){
        super();
        this.numOfProductsForSale=numOfProductsForSale;
    }


}
