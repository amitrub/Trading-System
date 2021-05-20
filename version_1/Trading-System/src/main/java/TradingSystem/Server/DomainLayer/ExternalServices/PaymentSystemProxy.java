package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;

public class PaymentSystemProxy implements ExternalServices {

    PaymentSystem real;

    public PaymentSystemProxy() {
        this.real = null;
    }

    public void setRealBridge(PaymentSystem implementation) {
        if (real == null)
            real = implementation;
    }

    @Override
    public Response purchase(PaymentInfo paymentInfo, AddressInfo addressInfo) {
        try {
            int month = Integer.parseInt(paymentInfo.getMonth());
            int year = Integer.parseInt(paymentInfo.getYear());
            if(month <= 0 || month >= 13 || year <= 2020 || year >= 2028){
                return new Response(true, "Invalid month/year");
            }
        }
        catch(Exception e){
            System.out.println("check credit details wrong credit");
            return new Response(true, "Illegal payment details");
        }

        return new Response(false, "Payment confirmed");
    }

    @Override
    public Response Cancel(Integer transactionId) {
        return new Response(false, "Payment with transaction ID: " + transactionId + " canceled successfully");
    }
}
