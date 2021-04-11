package TradingSystem.Server.DomainLayer.UserComponent;

import java.util.List;

public interface Permission {
   public boolean hasPermission(User.Permission p);
}
