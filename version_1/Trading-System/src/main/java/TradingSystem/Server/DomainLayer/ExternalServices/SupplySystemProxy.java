package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;

public class SupplySystemProxy implements ExternalServices {

    SupplySystem real;

    public SupplySystemProxy() {
        this.real = null;
    }

    public void setRealBridge(SupplySystem implementation) {
        if (real == null)
            real = implementation;
    }

    @Override
    public Response purchase(String topic, PaymentInfo paymentInfo, AddressInfo addressInfo) {
        if(addressInfo.getCity().equals("Eilat")){
            return new Response(true, "Can't Deliver to Eilat");
        }
        return new Response(false, "Can Supply");
    }

    @Override
    public Response Cancel(String transactionId) {
        return new Response("Supply with transaction ID: " + transactionId + " canceled successfully");
    }
}
