package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;

public interface ExternalServices {

    public Response purchase(String topic, PaymentInfo paymentInfo, AddressInfo addressInfo);

    public Response Cancel(String transactionId);
}
