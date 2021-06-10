package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import TradingSystem.Server.ServiceLayer.LoggerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path = "api/owner")
@CrossOrigin("*")
public class StoreOwnerServiceHttp {

    private static final LoggerController loggerController=LoggerController.getInstance();

    @Autowired
    private final TradingSystem tradingSystem;

    public StoreOwnerServiceHttp(TradingSystem tradingSystem) {
        this.tradingSystem = tradingSystem;
    }

    /**
     * @requirement
     *
     * @param userID: int (Path)
     * @param connID: String (Header)
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "stores": [{
     *      "storeID": int
     *      "storeName": String
     *  }]
     * }
     */
    @GetMapping("{userID}/founded_stores")
    public Response ShowAllFoundedStores(@PathVariable int userID, @RequestHeader("connID") String connID) {
        Response res = this.tradingSystem.ShowAllMyStores(connID, userID, true, false, false);
        System.out.println(res);
        res.AddTag("ShowAllFoundedStores");
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement
     *
     * @param userID: int (Path)
     * @param connID: String (Header)
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "stores": [{
     *      "storeID": int
     *      "storeName": String
     *  }]
     * }
     */
    @GetMapping("{userID}/owned_stores")
    public Response ShowAllOwnedStores(@PathVariable int userID, @RequestHeader("connID") String connID) {
        Response res = this.tradingSystem.ShowAllMyStores(connID, userID, false, true, false);
        System.out.println(res);
        res.AddTag("ShowAllOwnedStores");
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement
     *
     * @param userID: int (Path)
     * @param connID: String (Header)
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "stores": [{
     *      "storeID": int
     *      "storeName": String
     *  }]
     * }
     */
    @GetMapping("{userID}/managed_stores")
    public Response ShowAllManagedStores(@PathVariable int userID, @RequestHeader("connID") String connID) {
        Response res = this.tradingSystem.ShowAllMyStores(connID, userID, false, false, true);
        System.out.println(res);
        res.AddTag("ShowAllManagedStores");
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.1
     * 
     * @param userID: int (Path) 
     * @param storeID: int (Path)
     * @param connID: String (Header)
     * @param obj:{
     *  "productName": String
     *  "category": String
     *  "quantity": int
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @PostMapping("{userID}/store/{storeID}/add_new_product")
    public Response AddProductToStore(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj) {
        String productName, category;
        int quantity, price_int;
        Double price;
        try {
            productName = (String) obj.get("productName");
            category = (String) obj.get("category");
            quantity = (int) obj.get("quantity");
            try {
                price = (Double) obj.get("price");
            } catch (Exception e) {
                price_int = (int) obj.get("price");
                price = new Double(price_int);
            }
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : AddProductToStore");
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        Response res = tradingSystem.AddProductToStore(userID, connID, storeID, productName, category, price, quantity);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.1
     *
     * @param userID : int (Path)
     * @param storeID: int (Path)
     * @param productID: int (Path)
     * @param connID: String (Header)
     * @param obj:{
     *  "quantity": int
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @PostMapping("{userID}/store/{storeID}/change_quantity_product/{productID}")
    public Response ChangeQuantityProduct(@PathVariable int userID, @PathVariable int storeID, @PathVariable int productID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        int quantity;
        try {
            quantity = (int) obj.get("quantity");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : ChangeQuantityProduct");
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        return tradingSystem.ChangeQuantityProduct(userID,connID,storeID,productID,quantity);
    }

    /**
     * @requirement 4.1
     *
     * @param userID : int (Path)
     * @param storeID: int (Path)
     * @param productID: int (Path)
     * @param connID: String (Header)
     * @param obj:{
     *  "productName": String
     *  "category": String
     *  "price": String
     *  "double": int
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @PostMapping("{userID}/store/{storeID}/edit_product/{productID}")
    public Response EditProduct(@PathVariable int userID, @PathVariable int storeID, @PathVariable int productID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        String productName, category;
        int price_int;
        Double price;
        try {
            productName = (String) obj.get("productName");
            category = (String) obj.get("category");
            try {
                price = (Double) obj.get("price");
            } catch (Exception e) {
                price_int = (int) obj.get("price");
                price = new Double(price_int);
            }
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : EditProduct");
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        int quantity  = (int) obj.get("quantity");
        return tradingSystem.EditProduct(userID, connID, storeID,productID, productName, category, price,quantity);
    }

    /**
     * @requirement 4.1
     *
     * @param userID : int (Path)
     * @param storeID: int (Path)
     * @param productID: int (Path)
     * @param connID: String (Header)
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @GetMapping("{userID}/store/{storeID}/remove_product/{productID}")
    public Response RemoveProduct(@PathVariable int userID, @PathVariable int storeID, @PathVariable int productID, @RequestHeader("connID") String connID){
        Response res = this.tradingSystem.RemoveProduct(userID,storeID,productID,connID);
        System.out.println(res);
        tradingSystem.printProducts();
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.2.1
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param connID: String (Header)
     * @param obj:{
     *  TODO: Think what values should be in Buying Policy
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @PostMapping("{userID}/store/{storeID}/add_buying_policy")
    public Response AddBuyingPolicy(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){

        Map<String,Object> map=(Map<String,Object>)obj.get("expression");
        Expression exp=tradingSystem.CreateExpForBuy(storeID,map);
        Response res = this.tradingSystem.addBuyingPolicy(userID,connID,storeID,exp);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.2.2
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param connID: String (Header)
     * @param obj:{
     *  TODO: Think what values should be in Discount Policy
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @PostMapping("{userID}/store/{storeID}/add_discount_policy")
    public Response AddDiscountPolicy(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        System.out.println("\n\n-----------------------------------------------------------------\n\n\n");
       Map<String, Object> map=(Map<String, Object> )obj.get("expression");
        System.out.println(map);

        Sale sale=this.tradingSystem.createSaleForDiscount(storeID,map);
        Response res = this.tradingSystem.addDiscountPolicy(userID,connID,storeID,sale);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.2.3
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param buyingPolicyID : int (Path)
     * @param connID: String (Header)

     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @GetMapping("{userID}/store/{storeID}/remove_buying_policy")
    public Response RemoveBuyingPolicy(@PathVariable int userID, @PathVariable int storeID, @PathVariable int buyingPolicyID, @RequestHeader("connID") String connID){
        Response res = this.tradingSystem.RemoveBuyingPolicy(userID,storeID,connID);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.2.4
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param discountPolicyID : int (Path)
     * @param connID: String (Header)

     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @GetMapping("{userID}/store/{storeID}/remove_discount_policy")
    public Response RemoveDiscountPolicy(@PathVariable int userID, @PathVariable int storeID, @PathVariable int discountPolicyID, @RequestHeader("connID") String connID){
        Response res = this.tradingSystem.RemoveDiscountPolicy(userID,storeID,connID);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.2.5
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param discountPolicyID : int (Path)
     * @param connID: String (Header)

     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @GetMapping("{userID}/store/{storeID}/get_policies_info")
    public Response GetPoliciesInfo(@PathVariable int userID, @PathVariable int storeID, @PathVariable int discountPolicyID, @RequestHeader("connID") String connID){
        Response res = this.tradingSystem.GetPoliciesInfo(userID,storeID,connID);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.3
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param newOwnerID: int (Path)
     * @param connID: String (Header)
     * @return Response{
     *      *  "isErr: boolean
     *      *  "message": String
     *      *  "connID": String
     *      * }
     */
    @GetMapping("{userID}/store/{storeID}/add_new_owner/{newOwnerID}")
    public Response AddNewOwner(@PathVariable int userID, @PathVariable int storeID, @PathVariable int newOwnerID, @RequestHeader("connID") String connID)  {
        Response res = tradingSystem.AddNewOwner(userID, connID, storeID, newOwnerID);
        res.AddConnID(connID);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.4
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param OwnerID: int (Path)
     * @param connID: String (Header)
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @GetMapping("{userID}/store/{storeID}/remove_owner/{OwnerID}")
    public Response RemoveOwner(@PathVariable int userID, @PathVariable int storeID, @PathVariable int OwnerID, @RequestHeader("connID") String connID)  {
        Response res = tradingSystem.RemoveOwnerByOwner(userID, connID,OwnerID,storeID);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.5
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param newManagerID: int (Path)
     * @param connID: String (Header)
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @GetMapping("{userID}/store/{storeID}/add_new_manager/{newManagerID}")
    public Response AddNewManager(@PathVariable int userID, @PathVariable int storeID, @PathVariable int newManagerID, @RequestHeader("connID") String connID)  {
        Response res=tradingSystem.AddNewManager(userID, connID, storeID,newManagerID);
        res.AddConnID(connID);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.6
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param managerID: int (Path)
     * @param connID: String (Header)
     * @param obj:{
     *  TODO: Think what values should be in Edit Manager Permissions
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @PostMapping("{userID}/store/{storeID}/edit_manager_permissions/{managerID}")
    public Response EditManagerPermissions(@PathVariable int userID, @PathVariable int storeID, @PathVariable int managerID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj)  {
        List<PermissionEnum.Permission> Permissions=new LinkedList<>();
        try {
            if((boolean) obj.get("AddProduct"))
                Permissions.add(PermissionEnum.Permission.AddProduct);
            if((boolean) obj.get("ReduceProduct"))
                Permissions.add(PermissionEnum.Permission.ReduceProduct);
            if((boolean) obj.get("DeleteProduct"))
                Permissions.add(PermissionEnum.Permission.DeleteProduct);
            if((boolean) obj.get("EditProduct"))
                Permissions.add(PermissionEnum.Permission.EditProduct);
            if((boolean) obj.get("AppointmentOwner"))
                Permissions.add(PermissionEnum.Permission.AppointmentOwner);
            if((boolean) obj.get("AppointmentManager"))
                Permissions.add(PermissionEnum.Permission.AppointmentManager);
            if((boolean) obj.get("EditManagerPermission"))
                Permissions.add(PermissionEnum.Permission.EditManagerPermission);
            if((boolean) obj.get("RemoveManager"))
                Permissions.add(PermissionEnum.Permission.RemoveManager);
            if((boolean) obj.get("GetInfoOfficials"))
                Permissions.add(PermissionEnum.Permission.GetInfoOfficials);
            if((boolean) obj.get("GetInfoRequests"))
                Permissions.add(PermissionEnum.Permission.GetInfoRequests);
            if((boolean) obj.get("ResponseRequests"))
                Permissions.add(PermissionEnum.Permission.ResponseRequests);
            if((boolean) obj.get("GetStoreHistory"))
                Permissions.add(PermissionEnum.Permission.GetStoreHistory);
            if((boolean) obj.get("GetDailyIncomeForStore"))
                Permissions.add(PermissionEnum.Permission.GetDailyIncomeForStore);
            if((boolean) obj.get("RequestBidding"))
                Permissions.add(PermissionEnum.Permission.RequestBidding);
            if((boolean) obj.get("EditDiscountPolicy"))
                Permissions.add(PermissionEnum.Permission.EditDiscountPolicy);
            if((boolean) obj.get("EditBuyingPolicy"))
                Permissions.add(PermissionEnum.Permission.EditBuyingPolicy);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Response res = tradingSystem.EditManagerPermissions(userID, connID, storeID, managerID, Permissions);
        res.AddConnID(connID);
        WriteToLogger(res);
        return res;
    }

    /**
     * @param userID
     * @param connID
     * @returnResponse{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     *  "permissions":List[
     *  permissions:String]
     * }
    */
    @GetMapping("{userID}/store/{storeID}/get_permissions_to_manager")
    public Response GetPossiblePermissionsToManager(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID)  {
        Response res = tradingSystem.GetPossiblePermissionsToManager(userID, connID, storeID);
        res.AddConnID(connID);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.7
     *
     * @param userID : int (Path)
     * @param storeID: int (Path)
     * @param managerID: int (Path)
     * @param connID: String (Header)
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @GetMapping("{userID}/store/{storeID}/remove_manager/{managerID}")
    public Response RemoveManager(@PathVariable int userID, @PathVariable int storeID, @PathVariable int managerID, @RequestHeader("connID") String connID)  {
        Response res=tradingSystem.RemoveManager(userID, connID, storeID,managerID);
        res.AddConnID(connID);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.9
     *
     * @param userID : int (Path)
     * @param storeID: int (Path)
     * @param connID: String (Header)
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     *  "workers": List [{
     *      "userID": int
     *      "name": String
     *      "permissions": Permissions
     *      TODO: Think what values should be of the worker
     *  }]
     * }
     */
    @GetMapping("{userID}/store/{storeID}/workers")
    public Response ShowStoreWorkers(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID)  {
        Response res = tradingSystem.ShowStoreWorkers(userID, connID, storeID);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.11
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param connID: String (Header)
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "history": List [{
     *      "userID": int
     *      "storeID": int
     *      "products": List [{
     *          "storeID": int
     *          "storeName": String
     *          "productID": int
     *          "productName": String
     *          "price": double
     *          "category": String
     *          "quantity": int
     *      }]
     *  }]
     * }
     */
    @GetMapping("{userID}/store/{storeID}/store_history_owner")
    public Response OwnerStoreHistory(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID){
        Response res = tradingSystem.StoreHistoryOwner(userID,storeID,connID);
        res.AddConnID(connID);
        WriteToLogger(res);
        return res;
    }

     /**
     * @requirement 4.12
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param connID: String (Header)
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "DailyIncome": {[Double]}
     *  }
     * }
     */
    @GetMapping("{userID}/store/{storeID}/owner_daily_income_for_store")
    public Response OwnerDailyIncomeForStore(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID){
        Response res = tradingSystem.getDailyIncomeForStore(userID,storeID,connID);
        res.AddConnID(connID);
        WriteToLogger(res);
        return res;
    }



    
    /**
     * @requirement 8.3.2
     *
     * @param userID: int (Path)
     * @param connID: String (Header)
     * @param obj:{
     *  "userWhoOffer" : Integer
     *  "storeID": Integer
     *  "productID": Integer
     *   "quantity" : Integer
     *  "productPrice": Integer
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     *
     */
    @PostMapping("{userID}/response_for_submission_bidding")
    public Response ResponseForSubmissionBidding(@PathVariable int userID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        int mode,storeID,productID,userWhoOffer,quantity;
        Integer productPrice;
        try {
            userWhoOffer = (int) obj.get("userWhoOffer");
            storeID = (int) obj.get("storeID");
            productID = (int) obj.get("productID");
            quantity = (int) obj.get("quantity");
            productPrice = (Integer) obj.get("productPrice");
            mode=(Integer) obj.get("mode");
        }
        catch (Exception e){
            System.out.println(e);
            Response res = new Response(true, "Error in parse body : ResponseForSubmissionBidding");
            System.out.println(res);
            WriteToLogger(res);
            return res;
        }
        Response res = tradingSystem.ResponseForSubmissionBidding(userID,connID,storeID,productID,productPrice,userWhoOffer,quantity,mode);
        res.AddConnID(connID);
        WriteToLogger(res);
        return res;
    }
    /**
     * @requirement none
     *
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "Bids": [{
     *       "userID" :Integer
     *       "productID" : Integer
     *       "price " :  Double
     *  }]
     * }
     */

    @GetMapping("{userID}/store/{storeID}/show_bids")
    public Response ShowBids(@PathVariable int userID,@PathVariable int storeID, @RequestHeader("connID") String connID) {
        Response res = this.tradingSystem.ShowBids(userID, connID,storeID);
        res.AddConnID(connID);
        WriteToLogger(res);
        return res;
    }



    /**
     * @requirement none
     *
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "stores": [{
     *      "storeID": int
     *      "storeName": String
     *  }]
     * }
     */
    @GetMapping("{userID}/stores_owner")
    public Response ShowOwnerStores(@PathVariable int userID, @RequestHeader("connID") String connID) {
        Response res = this.tradingSystem.ShowOwnerStores(userID, connID);
        WriteToLogger(res);
        return res;
    }

    @GetMapping("{userID}/stores_manager")
    public Response ShowManagerStores(@PathVariable int userID, @RequestHeader("connID") String connID) {
        Response res = this.tradingSystem.ShowManagerStores(userID, connID);
        WriteToLogger(res);
        return res;
    }

    @GetMapping("{userID}/store/{storeID}/product/{productID}/comments")
    public Response ShowProductComments(@PathVariable int userID,@RequestHeader("connID") String connID,@PathVariable int storeID,@PathVariable int productID){
        Response res = this.tradingSystem.ShowProductComments(connID,userID,storeID,productID);
        WriteToLogger(res);
        return res;
    }

    private void WriteToLogger(Response res){
        if(res.getIsErr()) {
            loggerController.WriteErrorMsg("Guest Error: " + res.getMessage());
        }
        else{
            loggerController.WriteLogMsg("Guest: " + res.getMessage());
        }
    }
}
