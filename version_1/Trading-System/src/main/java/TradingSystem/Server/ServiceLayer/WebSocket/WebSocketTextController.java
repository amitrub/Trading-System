package TradingSystem.Server.ServiceLayer.WebSocket;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin("*")
public class WebSocketTextController {

    @Autowired
    SimpMessagingTemplate template;

//    @PostMapping("/send")
//    public ResponseEntity<Void> sendMessage(@RequestBody Map<String, Object> obj) {
//        System.out.println(obj);
//        template.convertAndSend("/topic/message", obj);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/message")
        public Response receiveMessage(@Payload Map<String, Object> obj) {
        // receive message from client
//        System.out.println(userID);
        System.out.println(obj);
        return new Response(false, "success");
    }


    @SendTo("/topic/message")
    public Response broadcastMessage(@Payload Response response) {
        return response;
    }
}