package TradingSystem.Server.DomainLayer.UserComponent;


import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataManagerPermissionType;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataManagerPermissions;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataOwnerPermissionType;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataOwnerPermissions;

import java.util.ArrayList;
import java.util.List;

public class ManagerPermission implements Permission {

    private Integer userId;
    private Integer storeId;
    private Integer appointmentId;
    private List<PermissionEnum.Permission> permissions = new ArrayList<>();;

    public ManagerPermission(Integer userId,Integer storeId) {
        this.userId=userId;
        this.storeId=storeId;
        this.permissions.add(PermissionEnum.Permission.GetInfoOfficials);
        this.permissions.add(PermissionEnum.Permission.GetInfoRequests);
        this.permissions.add(PermissionEnum.Permission.ResponseRequests);
    }

    public ManagerPermission(DataManagerPermissions dataManagerPermission) {
        this.userId = dataManagerPermission.getSubscriber().getUserID();
        this.storeId = dataManagerPermission.getStore().getStoreID();
        this.appointmentId = dataManagerPermission.getAppointment().getUserID();
        for (DataManagerPermissionType permissionType: dataManagerPermission.getPermissions()){
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
        return permissions;
    }

    public void setPermissions(List<PermissionEnum.Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean hasPermission(PermissionEnum.Permission p) {
        return this.permissions.contains(p);
    }

    @Override
    public String toString(){
        String res="";
        for(int i=0;i<permissions.size();i++){
            res+= permissions.get(i).toString()+" ,";
            res+= permissions.get(i).toString()+" ,";
        }
        return res;
    }
}
