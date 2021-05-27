package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import TradingSystem.Server.ServiceLayer.LoggerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(path = "api")
@RestController
@CrossOrigin("*")
public class Publisher {
    @Autowired
    public SimpMessagingTemplate template;

    @Autowired
    private final TradingSystem tradingSystem;
    private static final LoggerController loggerController=LoggerController.getInstance();

    public Publisher(TradingSystem tradingSystem) {
        this.tradingSystem = tradingSystem;
    }


    /**
     * @requirement 2.4
     *
     * @param connID: String (Header)
     * @param obj:{
     *  "userName": String
     *  "password": String
     * }
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "userID": int
     * }
     */
    @PostMapping("login")
    public Response Login(@RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        System.out.println("1111111");
        String userName, password;
        try {
            userName = (String) obj.get("userName");
            password = (String) obj.get("password");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : Login");
            res.AddConnID(connID);
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        Response res = this.tradingSystem.LoginPublisher(connID, userName, password, this);
        System.out.println("33333\n" + res);
        tradingSystem.printUsers();
        System.out.println(res);
        WriteToLogger(res);
        return res;
    }


    public void SendMessage(String topic, Response res){
        if(template!=null){
            template.convertAndSend(String.format("/topic/%s",topic), res);
        }
    }

//    @SendTo("/topic/test")
//    public Response notify(String connID){
//        System.out.println("Publisher");
////        template.convertAndSend(String.format("/topic/%s",connID), res);
//        return new Response(false, "Test Work!!! Publisher");
//    }

    private void WriteToLogger(Response res){
        if(res.getIsErr()) {
            loggerController.WriteErrorMsg("Guest Error: " + res.getMessage());
        }
        else{
            loggerController.WriteLogMsg("Guest: " + res.getMessage());
        }
    }
}
