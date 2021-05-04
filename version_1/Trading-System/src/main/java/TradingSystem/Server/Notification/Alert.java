package TradingSystem.Server.Notification;

import org.springframework.jmx.export.notification.NotificationPublisher;

import javax.management.Notification;
import java.util.HashMap;
import java.util.Map;

public class Alert extends Notification {
    //TODO - maybe not need to implement, put as a field?
    public Alert(String type, Object source, long sequenceNumber) {
        super(type, source, sequenceNumber);
    }

    public Alert(String type, Object source, long sequenceNumber, String message) {
        super(type, source, sequenceNumber, message);
    }

    public Alert(String type, Object source, long sequenceNumber, long timeStamp) {
        super(type, source, sequenceNumber, timeStamp);
    }

    public Alert(String type, Object source, long sequenceNumber, long timeStamp, String message) {
        super(type, source, sequenceNumber, timeStamp, message);
    }

    public void SendAlertToClient(){
    }

    private static Map buildPacket(Notification note) {
        Map<String, Object> result = new HashMap<>();
        result.put("event", note.getType());
        result.put("source", note.getSource());
        result.put("sequenceNumber", note.getSequenceNumber());
        result.put("timeStamp", note.getTimeStamp());
        result.put("data", note.getUserData());
        return result;
    }


}
