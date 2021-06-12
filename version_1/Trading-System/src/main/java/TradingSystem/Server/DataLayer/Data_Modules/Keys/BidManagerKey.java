package TradingSystem.Server.DataLayer.Data_Modules.Keys;

import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataPermission;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BidManagerKey implements Serializable {

    private UserProductKey key;

    @Column(name = "manager_id")
    private int managerID;

    public BidManagerKey() {
        // DO NOT DELETE
    }

    public BidManagerKey(UserProductKey key, int managerID) {
        this.key = key;
        this.managerID = managerID;
    }

    public UserProductKey getKey() {
        return key;
    }

    public void setKey(UserProductKey key) {
        this.key = key;
    }

    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BidManagerKey that = (BidManagerKey) o;
        return managerID == that.managerID && Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, managerID);
    }

    @Override
    public String toString() {
        return "BidManagerKey{" +
                "key=" + key +
                ", managerID=" + managerID +
                '}';
    }
}
