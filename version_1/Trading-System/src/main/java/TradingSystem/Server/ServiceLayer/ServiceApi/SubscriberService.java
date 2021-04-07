package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "api")
public class SubscriberService {
    private final TradingSystem tradingSystem = TradingSystem.getInstance();

    @GetMapping("logout")
    public Response Logout(@RequestHeader("connID") String connID){
        System.out.println(connID);
        return tradingSystem.Logout(connID);
    }
}
