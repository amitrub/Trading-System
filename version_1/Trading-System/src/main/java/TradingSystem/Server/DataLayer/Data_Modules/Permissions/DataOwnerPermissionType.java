package TradingSystem.Server.DataLayer.Data_Modules.Permissions;

import TradingSystem.Server.DataLayer.Data_Modules.Keys.PermissionKey;

import javax.persistence.*;

@Entity(name = "OwnerPermissionType")
@Table(
        name = "owner_permission_type"
)
public class DataOwnerPermissionType {

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
    private DataOwnerPermissions ownerPermissions;


    public DataOwnerPermissionType(){
        // DO NOT DELETE
    }

    public DataOwnerPermissionType(DataOwnerPermissions ownerPermissions, DataPermission.Permission permission) {
        this.key = new PermissionKey(ownerPermissions.getKey(), permission);
        this.ownerPermissions = ownerPermissions;
    }

    public PermissionKey getKey() {
        return key;
    }

    public void setKey(PermissionKey key) {
        this.key = key;
    }

    public DataOwnerPermissions getOwnerPermissions() {
        return ownerPermissions;
    }

    public void setOwnerPermissions(DataOwnerPermissions ownerPermissions) {
        this.ownerPermissions = ownerPermissions;
    }

    @Override
    public String toString() {
        return "DataOwnerPermissionType{" +
                "key=" + key +
                '}';
    }
}
