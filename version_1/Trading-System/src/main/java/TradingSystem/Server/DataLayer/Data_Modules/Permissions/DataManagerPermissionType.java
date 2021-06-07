package TradingSystem.Server.DataLayer.Data_Modules.Permissions;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.PermissionKey;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreKey;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagProduct;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ManagerPermissionType")
@Table(
        name = "manager_permission_type"
)
public class DataManagerPermissionType {

    @EmbeddedId
    private PermissionKey key;

    @ManyToOne
    @MapsId("userStoreKey")
    @JoinColumns({@JoinColumn(
            name = "subscriber_id",
            nullable = false,
            referencedColumnName = "subscriber_id",
            foreignKey = @ForeignKey(
                    name = "bag_product_subscriber_fk"
            )),
            @JoinColumn(
                    name = "store_id",
                    nullable = false,
                    referencedColumnName = "store_id",
                    foreignKey = @ForeignKey(
                            name = "bag_product_store_fk"
                    )
            )
    })
    private DataManagerPermissions managerPermissions;


    public DataManagerPermissionType(){
        // DO NOT DELETE
    }

    public DataManagerPermissionType(DataManagerPermissions managerPermissions, DataPermission.Permission permission) {
        this.key = new PermissionKey(managerPermissions.getKey(), permission);
        this.managerPermissions = managerPermissions;
    }

    public PermissionKey getKey() {
        return key;
    }

    public void setKey(PermissionKey key) {
        this.key = key;
    }

    public DataManagerPermissions getManagerPermissions() {
        return managerPermissions;
    }

    public void setManagerPermissions(DataManagerPermissions managerPermissions) {
        this.managerPermissions = managerPermissions;
    }

    @Override
    public String toString() {
        return "DataManagerPermissionType{" +
                "key=" + key +
                '}';
    }
}
