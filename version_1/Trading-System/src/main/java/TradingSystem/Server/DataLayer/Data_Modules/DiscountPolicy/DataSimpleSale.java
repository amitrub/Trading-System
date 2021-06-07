package TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DataSimpleSale extends DataSale {
}
