package TradingSystem.Server.DomainLayer.UserComponent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OwnerPermission implements Permission {

    private Integer userId;
    private Integer storeId;
    private Integer appointmentId;

    private List<User.Permission> permissions;

    public OwnerPermission(Integer userId, Integer storeId) {
        this.userId = userId;
        this.storeId = storeId;
        this.appointmentId=null;
        this.permissions = new ArrayList<User.Permission>();
        this.permissions.add(User.Permission.AddProduct);
        this.permissions.add(User.Permission.ReduceProduct);
        this.permissions.add(User.Permission.DeleteProduct);
        this.permissions.add(User.Permission.EditProduct);
        this.permissions.add(User.Permission.AppointmentOwner);
        this.permissions.add(User.Permission.AppointmentManager);
        this.permissions.add(User.Permission.EditManagerPermission);
        this.permissions.add(User.Permission.RemoveManager);
        this.permissions.add(User.Permission.GetInfoOfficials);
        this.permissions.add(User.Permission.GetInfoRequests);
        this.permissions.add(User.Permission.ResponseRequests);
        this.permissions.add(User.Permission.GetStoreHistory);
        this.permissions.add(User.Permission.GetDailyIncomeForStore);
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

    public List<User.Permission> getPermissions() {
      if(permissions==null){
          return new LinkedList<User.Permission>();
      }
        return permissions;
    }

    public void setPermissions(List<User.Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean hasPermission(User.Permission p) {
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
