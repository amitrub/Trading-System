package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;

public class SupplySystemMock implements ExternalServices {

    SupplySystem real;

    public SupplySystemMock() {
        this.real = null;
    }

    public void setRealBridge(SupplySystem implementation) {
        if (real == null)
            real = implementation;
    }

    @Override
    public Response purchase(PaymentInfo paymentInfo, AddressInfo addressInfo) {
        if(addressInfo.getCity().equals("Eilat")){
            return new Response(true, "The service is not available");
        }
        return new Response(false, "Can Supply");
    }

    @Override
    public Response Cancel(String transactionId) {
        return new Response("Supply with transaction ID: " + transactionId + " canceled successfully");
    }
}
