package TradingSystem.Server.DomainLayer.ExternalServices;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface ExternalServices {

    public Response purchase(PaymentInfo paymentInfo, AddressInfo addressInfo);

    public Response Cancel(String transactionId)  throws UnirestException;
}
