package TradingSystem.Server.Notification;

import javax.management.Notification;

public class Alert extends Notification {
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
}
