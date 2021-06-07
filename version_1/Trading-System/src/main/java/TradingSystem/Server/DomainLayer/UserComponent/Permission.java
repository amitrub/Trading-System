package TradingSystem.Server.DomainLayer.UserComponent;

public interface Permission {
   public boolean hasPermission(PermissionEnum.Permission p);

   public String toString();
}
