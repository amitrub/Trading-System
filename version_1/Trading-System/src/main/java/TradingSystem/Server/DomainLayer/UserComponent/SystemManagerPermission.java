package TradingSystem.Server.DomainLayer.UserComponent;

import java.util.LinkedList;
import java.util.List;

public class SystemManagerPermission implements Permission{

    List<User.Permission> permissions;

    public SystemManagerPermission() {
        this.permissions=new LinkedList<User.Permission>();
        permissions.add(User.Permission.GetHistoryPurchasing);
    }

    @Override
    public boolean hasPermission(User.Permission p) {
        return this.permissions.contains(p);
    }
}
