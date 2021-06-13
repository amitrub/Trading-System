package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ConcurrentHashMap;

public class HourLimit extends SimpleExpression {

    Date date;

    public HourLimit(Integer expID, Date date) {
      //  super(expID);
        this.date = date;
    }


    //ToDo check!
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID, int mode) {
       if(products.isEmpty()){
           return true;
       }
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        return calendar.get(Calendar.HOUR_OF_DAY)>=hour;
    }
}
