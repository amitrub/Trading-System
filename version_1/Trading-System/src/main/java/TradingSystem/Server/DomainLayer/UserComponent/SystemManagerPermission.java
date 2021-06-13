package TradingSystem.Server.DomainLayer.UserComponent;

import java.util.ArrayList;
import java.util.List;

public class SystemManagerPermission implements Permission{

    List<PermissionEnum.Permission> permissions;

    public SystemManagerPermission() {
        this.permissions=new ArrayList<PermissionEnum.Permission>();
        permissions.add(PermissionEnum.Permission.GetHistoryPurchasing);
        permissions.add(PermissionEnum.Permission.GetDailyIncomeForSystem);
    }



    @Override
    public boolean hasPermission(PermissionEnum.Permission p) {
        return this.permissions.contains(p);
    }
}
