package TradingSystem.Server.DomainLayer.UserComponent;


import java.util.ArrayList;
import java.util.List;

public class ManagerPermission implements Permission {

    private Integer userId;
    private Integer storeId;
    private Integer appointmentId;
    private List<User.Permission> permissions;

    public ManagerPermission(Integer userId,Integer storeId) {
        this.userId=userId;
        this.storeId=storeId;
        this.permissions = new ArrayList<User.Permission>();
        this.permissions.add(User.Permission.GetInfoOfficials);
        this.permissions.add(User.Permission.GetInfoRequests);
        this.permissions.add(User.Permission.ResponseRequests);
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
        return permissions;
    }

    public void setPermissions(List<User.Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean hasPermission(User.Permission p) {
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
