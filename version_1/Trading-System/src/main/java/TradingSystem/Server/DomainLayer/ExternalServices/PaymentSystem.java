package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.Validation;
import TradingSystem.Server.ServiceLayer.LoggerController;

import java.util.concurrent.ConcurrentHashMap;

public class PaymentSystem implements ExternalServices {

    //Singleton
    private static PaymentSystem paymentSystem = null;
    private PaymentSystem() {
    }
    public static PaymentSystem getInstance() {
        if (paymentSystem == null) {
            paymentSystem = new PaymentSystem();
        }
        return paymentSystem;
    }

    public boolean checkCredit(String name, String credit_number, String phone_number){
//        TODO: Roee did stupid check for the tests!!!
        try {
            System.out.println("check credit: " + credit_number + " phone credit: " + phone_number);
            int credit = Integer.parseInt(credit_number);
        } catch(Exception e){
            System.out.println("check credit details wrong credit");
            return false;
        }

        if(!phone_number.substring(0, 3).equals("052")) {
            System.out.println("check credit details wrong phone");
            return false;
        }

        return true;
    }
}
