package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/admin")
public class AdminService {
    private final TradingSystem tradingSystem = TradingSystem.getInstance();

    @GetMapping("{userID}/users")
    public Response AllUsers(@PathVariable int userID, @RequestHeader("connID") String connID){
        return null;
    }
}
