package TradingSystem.Server.DomainLayer.UserComponent;

import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataPermission;

public class PermissionEnum {
    public enum Permission {
        AddProduct,
        ReduceProduct,
        DeleteProduct,
        EditProduct,
        AppointmentOwner,
        AppointmentManager,
        EditManagerPermission,
        RemoveManager,
        GetInfoOfficials,
        GetInfoRequests,
        ResponseRequests,
        GetHistoryPurchasing,
        GetStoreHistory,
        GetDailyIncomeForStore,
        GetDailyIncomeForSystem,
        RequestBidding,
        EditDiscountPolicy,
        EditBuyingPolicy

        }

    public static PermissionEnum.Permission dataToPermission(DataPermission.Permission permission){
        switch (permission){
            case AddProduct: return PermissionEnum.Permission.AddProduct;
            case ReduceProduct: return PermissionEnum.Permission.ReduceProduct;
            case DeleteProduct: return PermissionEnum.Permission.DeleteProduct;
            case EditProduct: return PermissionEnum.Permission.EditProduct;
            case AppointmentOwner: return PermissionEnum.Permission.AppointmentOwner;
            case AppointmentManager: return PermissionEnum.Permission.AppointmentManager;
            case EditManagerPermission: return PermissionEnum.Permission.EditManagerPermission;
            case RemoveManager: return PermissionEnum.Permission.RemoveManager;
            case GetInfoOfficials: return PermissionEnum.Permission.GetInfoOfficials;
            case GetInfoRequests: return PermissionEnum.Permission.GetInfoRequests;
            case ResponseRequests: return PermissionEnum.Permission.ResponseRequests;
            case GetHistoryPurchasing: return PermissionEnum.Permission.GetHistoryPurchasing;
            case GetStoreHistory: return PermissionEnum.Permission.GetStoreHistory;
            case GetDailyIncomeForStore: return PermissionEnum.Permission.GetDailyIncomeForStore;
            case GetDailyIncomeForSystem: return PermissionEnum.Permission.GetDailyIncomeForSystem;
            case RequestBidding: return PermissionEnum.Permission.RequestBidding;
            case EditDiscountPolicy: return PermissionEnum.Permission.EditDiscountPolicy;
            case EditBuyingPolicy: return PermissionEnum.Permission.EditBuyingPolicy;
        }
        return null;
    }
}
