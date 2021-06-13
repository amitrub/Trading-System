package TradingSystem.Server.DataLayer.Data_Modules.Permissions;

import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;

public class DataPermission {
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

    public static Permission toDataPermission(PermissionEnum.Permission permission){
        switch (permission){
            case AddProduct: return Permission.AddProduct;
            case ReduceProduct: return Permission.ReduceProduct;
            case DeleteProduct: return Permission.DeleteProduct;
            case EditProduct: return Permission.EditProduct;
            case AppointmentOwner: return Permission.AppointmentOwner;
            case AppointmentManager: return Permission.AppointmentManager;
            case EditManagerPermission: return Permission.EditManagerPermission;
            case RemoveManager: return Permission.RemoveManager;
            case GetInfoOfficials: return Permission.GetInfoOfficials;
            case GetInfoRequests: return Permission.GetInfoRequests;
            case ResponseRequests: return Permission.ResponseRequests;
            case GetHistoryPurchasing: return Permission.GetHistoryPurchasing;
            case GetStoreHistory: return Permission.GetStoreHistory;
            case GetDailyIncomeForStore: return Permission.GetDailyIncomeForStore;
            case GetDailyIncomeForSystem: return Permission.GetDailyIncomeForSystem;
            case RequestBidding: return Permission.RequestBidding;
            case EditDiscountPolicy: return Permission.EditDiscountPolicy;
            case EditBuyingPolicy: return Permission.EditBuyingPolicy;
        }
        return null;
    }


}
