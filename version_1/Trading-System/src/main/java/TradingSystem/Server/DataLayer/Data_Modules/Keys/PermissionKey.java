package TradingSystem.Server.DataLayer.Data_Modules.Keys;

import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataPermission;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PermissionKey implements Serializable {

    private UserStoreKey userStoreKey;

    @Enumerated(EnumType.STRING)
    private DataPermission.Permission permission;

    public PermissionKey() {
        // DO NOT DELETE
    }

    public PermissionKey(UserStoreKey key, DataPermission.Permission permission) {
        this.userStoreKey = key;
        this.permission = permission;
    }

    public UserStoreKey getUserStoreKey() {
        return userStoreKey;
    }

    public void setUserStoreKey(UserStoreKey userStoreKey) {
        this.userStoreKey = userStoreKey;
    }

    public DataPermission.Permission getPermission() {
        return permission;
    }

    public void setPermission(DataPermission.Permission permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermissionKey that = (PermissionKey) o;
        return Objects.equals(userStoreKey, that.userStoreKey) && permission == that.permission;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userStoreKey, permission);
    }

    @Override
    public String toString() {
        return "PermissionKey{" +
                ", permission=" + permission +
                '}';
    }
}
