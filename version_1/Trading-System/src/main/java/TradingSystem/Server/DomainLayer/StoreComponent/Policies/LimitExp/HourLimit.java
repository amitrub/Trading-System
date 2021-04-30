package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ConcurrentHashMap;

public class HourLimit implements Expression {

    Date date;

    public HourLimit(Integer productID, Date date) {
        this.date = date;
    }

    @Override
    public Expression add(Expression expr) {
        return null;
    }

    @Override
    //ToDo check!
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
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
