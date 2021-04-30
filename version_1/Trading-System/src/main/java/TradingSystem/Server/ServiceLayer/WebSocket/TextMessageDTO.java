package TradingSystem.Server.ServiceLayer.WebSocket;


public class TextMessageDTO {

    private String message;

    public TextMessageDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "TextMessageDTO{" +
                "message='" + message + '\'' +
                '}';
    }
}
