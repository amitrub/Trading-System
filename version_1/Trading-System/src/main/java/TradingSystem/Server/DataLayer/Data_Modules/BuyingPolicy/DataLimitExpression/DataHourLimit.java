package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataLimitExpression;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataSimpleExpression;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class DataHourLimit extends DataSimpleExpression {

    Date date;

    public DataHourLimit(){

    }

    public DataHourLimit(Date date){
        super();
        this.date=date;
    }
}
