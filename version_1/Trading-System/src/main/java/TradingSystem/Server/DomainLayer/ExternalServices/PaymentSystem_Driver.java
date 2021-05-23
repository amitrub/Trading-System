package TradingSystem.Server.DomainLayer.ExternalServices;

public abstract class PaymentSystem_Driver {

    public static PaymentSystemProxy getPaymentSystem() {
        PaymentSystemProxy bridge = new PaymentSystemProxy();
        bridge.setRealBridge(PaymentSystem.getInstance());  //Remove after implement
        return bridge;
    }
}
