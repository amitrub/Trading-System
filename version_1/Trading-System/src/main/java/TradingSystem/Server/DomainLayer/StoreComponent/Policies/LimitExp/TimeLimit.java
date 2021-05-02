package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ConcurrentHashMap;

public class TimeLimit extends SimpleExpression {

    Date date;

    public TimeLimit(Integer expID,Date date)
    {
       // super(expID);
        this.date = date;
    }

    //ToDo check!
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID){
        if(products.isEmpty()){
            return true;
        }
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        Calendar rightNow = Calendar.getInstance();
        return rightNow.after(date)||rightNow.before(date);
    }
}
