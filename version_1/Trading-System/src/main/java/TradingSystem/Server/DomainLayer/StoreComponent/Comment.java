package TradingSystem.Server.DomainLayer.StoreComponent;

public class Comment {
   private Integer userID;
   private String comment;
   private Double rate;

    public Comment(Integer userID, String comment) {
        this.userID = userID;
        this.comment = comment;
        this.rate=0.0;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
