package TradingSystem.Server.DomainLayer.ExternalServices;

public class PaymentInfo {

    private String cardNumber;
    private String month;
    private String year;
    private String fullName;
    private String cvv;
    private String ID;

    public PaymentInfo(String cardNumber, String month, String year, String fullName, String cvv, String ID) {
        this.cardNumber = cardNumber;
        this.month = month;
        this.year = year;
        this.fullName = fullName;
        this.cvv = cvv;
        this.ID = ID;
    }

    public PaymentInfo() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCvv() {
        return cvv;
    }

    public String getID() {
        return ID;
    }
}
