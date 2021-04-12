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

    public boolean checkCredit(String name, String credit_number, String phone_number){return true;}
}
