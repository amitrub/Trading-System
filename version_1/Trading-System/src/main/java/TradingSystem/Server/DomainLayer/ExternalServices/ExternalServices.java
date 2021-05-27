package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;

public interface ExternalServices {

    public Response purchase(PaymentInfo paymentInfo, AddressInfo addressInfo);

    public Response Cancel(String transactionId);
}
