package TradingSystem.Server.DomainLayer.UserComponent;

import java.util.ArrayList;
import java.util.List;

public class SystemManagerPermission implements Permission{

    List<User.Permission> permissions;

    public SystemManagerPermission() {
        this.permissions=new ArrayList<User.Permission>();
        permissions.add(User.Permission.GetHistoryPurchasing);
        permissions.add(User.Permission.GetDailyIncomeForSystem);
    }

    @Override
    public boolean hasPermission(User.Permission p) {
        return this.permissions.contains(p);
    }
}
