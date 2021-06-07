package TradingSystem.Server.DomainLayer.UserComponent;

import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataOwnerPermissionType;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataOwnerPermissions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OwnerPermission implements Permission {

    private Integer userId;
    private Integer storeId;
    private Integer appointmentId;

    private List<PermissionEnum.Permission> permissions = new ArrayList<>();

    public OwnerPermission(Integer userId, Integer storeId) {
        this.userId = userId;
        this.storeId = storeId;
        this.appointmentId=null;
        this.permissions.add(PermissionEnum.Permission.AddProduct);
        this.permissions.add(PermissionEnum.Permission.ReduceProduct);
        this.permissions.add(PermissionEnum.Permission.DeleteProduct);
        this.permissions.add(PermissionEnum.Permission.EditProduct);
        this.permissions.add(PermissionEnum.Permission.AppointmentOwner);
        this.permissions.add(PermissionEnum.Permission.AppointmentManager);
        this.permissions.add(PermissionEnum.Permission.EditManagerPermission);
        this.permissions.add(PermissionEnum.Permission.RemoveManager);
        this.permissions.add(PermissionEnum.Permission.GetInfoOfficials);
        this.permissions.add(PermissionEnum.Permission.GetInfoRequests);
        this.permissions.add(PermissionEnum.Permission.ResponseRequests);
        this.permissions.add(PermissionEnum.Permission.GetStoreHistory);
        this.permissions.add(PermissionEnum.Permission.GetDailyIncomeForStore);
        this.permissions.add(PermissionEnum.Permission.RequestBidding);
        this.permissions.add(PermissionEnum.Permission.EditBuyingPolicy);
        this.permissions.add(PermissionEnum.Permission.EditDiscountPolicy);
    }

    public OwnerPermission(DataOwnerPermissions dataOwnerPermission) {
        this.userId = dataOwnerPermission.getSubscriber().getUserID();
        this.storeId = dataOwnerPermission.getStore().getStoreID();
        this.appointmentId = dataOwnerPermission.getAppointment().getUserID();
        for (DataOwnerPermissionType permissionType: dataOwnerPermission.getPermissions()){
            this.permissions.add(PermissionEnum.dataToPermission(permissionType.getKey().getPermission()));
        }
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public synchronized void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public List<PermissionEnum.Permission> getPermissions() {
      if(permissions==null){
          return new LinkedList<PermissionEnum.Permission>();
      }
        return permissions;
    }

    public void setPermissions(List<PermissionEnum.Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean hasPermission(PermissionEnum.Permission p) {
       if(this.permissions.contains(p))
        return true;
       else
       return false;
    }

    @Override
    public String toString(){
        String res="";
        for(int i=0;i<permissions.size();i++){
            res+= permissions.get(i).toString()+" ,";
        }
        return res;
    }
}
