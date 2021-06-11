package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;

public class PaymentSystemMock implements ExternalServices {

    private static PaymentSystemMock paymentSystem = null;

    private PaymentSystemMock(){
    }

    public static PaymentSystemMock getInstance() {
        if (paymentSystem == null) {
            paymentSystem = new PaymentSystemMock();
        }
        return paymentSystem;
    }


    @Override
    public Response purchase(PaymentInfo paymentInfo, AddressInfo addressInfo) {
        try {
            int month = Integer.parseInt(paymentInfo.getMonth());
            int year = Integer.parseInt(paymentInfo.getYear());
            if(month <= 0 || month >= 13 || year <= 2020 || year >= 2028){
                return new Response(true, "The service is not available");
            }
        }
        catch(Exception e){
            System.out.println("check credit details wrong credit");
            return new Response(true, "The service is not available");
        }

        return new Response(false, "Payment confirmed");
    }

    @Override
    public Response Cancel(String transactionId) {
        return new Response(false, "Payment with transaction ID: " + transactionId + " canceled successfully");
    }
}
