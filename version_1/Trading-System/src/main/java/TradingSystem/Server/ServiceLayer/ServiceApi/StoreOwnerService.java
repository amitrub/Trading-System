package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.DomainLayer.UserComponent.Permission;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import TradingSystem.Server.ServiceLayer.LoggerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import static TradingSystem.Server.ServiceLayer.Configuration.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@RestController
@MessageMapping("api/owner")
@CrossOrigin("*") public class StoreOwnerService {
    @Autowired
    SimpMessagingTemplate template;

    private final TradingSystem tradingSystem = TradingSystemImpl.getInstance();
    private static final LoggerController loggerController=LoggerController.getInstance();


    /**
     * @requirement 4.1.1
     * 
     * @param userID: int (Path) 
     * @param storeID: int (Path)
     * @param obj:{
     *  "connID": String
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
    @MessageMapping("{userID}/store/{storeID}/add_new_product")
    public Response AddProductToStore(@DestinationVariable int userID, @DestinationVariable int storeID, @Payload Map<String, Object> obj) {
        String connID = (String) obj.get("connID");
        String productName = (String) obj.get("productName");
        String category = (String) obj.get("category");
        int quantity  = (int) obj.get("quantity");
        int price_int;
        Double price;
        try {
            price = (Double) obj.get("price");
        } catch (Exception e) {
            price_int = (int) obj.get("price");
            price = new Double(price_int);
        }
        Response res = tradingSystem.AddProductToStore(userID, connID, storeID, productName, category, price, quantity);
        res.AddTag("AddProductToStore");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.1.2
     *
     * @param userID : int (Path)
     * @param storeID: int (Path)
     * @param productID: int (Path)
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @MessageMapping("{userID}/store/{storeID}/remove_product/{productID}")
    public Response RemoveProduct(@DestinationVariable int userID, @DestinationVariable int storeID, @DestinationVariable int productID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        Response res = this.tradingSystem.RemoveProduct(userID,storeID,productID,connID);
        System.out.println(res);
        tradingSystem.printProducts();
        res.AddTag("RemoveProduct");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.1.3
     *
     * @param userID : int (Path)
     * @param storeID: int (Path)
     * @param productID: int (Path)
     * @param obj:{
     *  "connID": String
     *  "quantity": int
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @MessageMapping("{userID}/store/{storeID}/change_quantity_product/{productID}")
    public Response ChangeQuantityProduct(@DestinationVariable int userID, @DestinationVariable int storeID, @DestinationVariable int productID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        int quantity  = (int) obj.get("quantity");
        Response res = tradingSystem.ChangeQuantityProduct(userID,connID,storeID,productID,quantity);
        res.AddTag("ChangeQuantityProduct");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.1.3
     *
     * @param userID : int (Path)
     * @param storeID: int (Path)
     * @param productID: int (Path)
     * @param obj:{
     *  "connID": String
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
    @MessageMapping("{userID}/store/{storeID}/edit_product/{productID}")
    public Response EditProduct(@DestinationVariable int userID, @DestinationVariable int storeID, @DestinationVariable int productID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        String productName = (String) obj.get("productName");
        String category = (String) obj.get("category");
        int price_int;
        Double price;
        try {
            price = (Double) obj.get("price");
        } catch (Exception e) {
            price_int = (int) obj.get("price");
            price = new Double(price_int);
        }
        int quantity  = (int) obj.get("quantity");
        Response res = tradingSystem.EditProduct(userID, connID, storeID,productID, productName, category, price,quantity);
        res.AddTag("EditProduct");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.2.1
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param obj:{
     *  "connID": String
     *  TODO: Think what values should be in Buying Policy
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @MessageMapping("{userID}/store/{storeID}/add_buying_policy")
    public Response AddBuyingPolicy(@DestinationVariable int userID, @DestinationVariable int storeID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
//        Response res = this.tradingSystem.AddBuyingPolicy(userID,storeID,connID);
        Response res = new Response(true, "not implemented");
        res.AddTag("AddBuyingPolicy");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.2.2
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param obj:{
     *  "connID": String
     *  TODO: Think what values should be in Discount Policy
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @MessageMapping("{userID}/store/{storeID}/add_discount_policy")
    public Response AddDiscountPolicy(@DestinationVariable int userID, @DestinationVariable int storeID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
//        Response res = this.tradingSystem.AddDiscountPolicy(userID,storeID,connID);
        Response res = new Response(true, "not implemented");
        res.AddTag("AddDiscountPolicy");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.2.3
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param buyingPolicyID : int (Path)
     * @param obj:{
     *  "connID": String
     *  TODO: Think what values should be in Buying Policy
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @MessageMapping("{userID}/store/{storeID}/edit_buying_policy/{buyingPolicyID}")
    public Response EditBuyingPolicy(@DestinationVariable int userID, @DestinationVariable int storeID, @DestinationVariable int buyingPolicyID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
//        Response res = this.tradingSystem.EditBuyingPolicy(userID,storeID,connID);
        Response res = new Response(true, "not implemented");
        res.AddTag("EditBuyingPolicy");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.2.4
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param discountPolicyID : int (Path)
     * @param obj:{
     *  "connID": String
     *  TODO: Think what values should be in Discount Policy
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @MessageMapping("{userID}/store/{storeID}/edit_discount_policy/{discountPolicyID}")
    public Response EditDiscountPolicy(@DestinationVariable int userID, @DestinationVariable int storeID, @DestinationVariable int discountPolicyID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
//        Response res = this.tradingSystem.EditDiscountPolicy(userID,storeID,connID);
        Response res = new Response(true, "not implemented");
        res.AddTag("EditDiscountPolicy");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.2.5
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param buyingPolicyID : int (Path)
     * @param obj:{
     *  "connID": String
     *  TODO: Think what values should be in Buying Policy
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @MessageMapping("{userID}/store/{storeID}/remove_buying_policy/{buyingPolicyID}")
    public Response RemoveBuyingPolicy(@DestinationVariable int userID, @DestinationVariable int storeID, @DestinationVariable int buyingPolicyID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
//        Response res = this.tradingSystem.RemoveBuyingPolicy(userID,storeID,connID);
        Response res = new Response(true, "not implemented");
        res.AddTag("RemoveBuyingPolicy");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.2.6
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param discountPolicyID : int (Path)
     * @param obj:{
     *  "connID": String
     *  TODO: Think what values should be in Discount Policy
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @MessageMapping("{userID}/store/{storeID}/remove_discount_policy/{discountPolicyID}")
    public Response RemoveDiscountPolicy(@DestinationVariable int userID, @DestinationVariable int storeID, @DestinationVariable int discountPolicyID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
//        Response res = this.tradingSystem.RemoveDiscountPolicy(userID,storeID,connID);
        Response res = new Response(true, "not implemented");
        res.AddTag("RemoveDiscountPolicy");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.3
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param newOwnerID: int (Path)
     * @param obj:{
     *  "connID": String
     * }
     * @return Response{
     *      *  "isErr: boolean
     *      *  "message": String
     *      *  "connID": String
     *      * }
     */
    @MessageMapping("{userID}/store/{storeID}/add_new_owner/{newOwnerID}")
    public Response AddNewOwner(@DestinationVariable int userID, @DestinationVariable int storeID, @DestinationVariable int newOwnerID, @Payload Map<String, Object> obj)  {
        String connID = (String) obj.get("connID");
        Response res = tradingSystem.AddNewOwner(userID, connID, storeID, newOwnerID);
        res.AddConnID(connID);
        res.AddTag("AddNewOwner");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.4
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param OwnerID: int (Path)
     * @param obj:{
     *  "connID": String
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @MessageMapping("{userID}/store/{storeID}/remove_owner/{OwnerID}")
    public Response RemoveOwner(@DestinationVariable int userID, @DestinationVariable int storeID, @DestinationVariable int OwnerID, @Payload Map<String, Object> obj)  {
        String connID = (String) obj.get("connID");
        Response res = tradingSystem.RemoveOwnerByOwner(userID, connID, storeID, OwnerID);
        res.AddTag("RemoveOwner");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.5
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param newManagerID: int (Path)
     * @param obj:{
     *  "connID": String
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @MessageMapping("{userID}/store/{storeID}/add_new_manager/{newManagerID}")
    public Response AddNewManager(@DestinationVariable int userID, @DestinationVariable int storeID, @DestinationVariable int newManagerID, @Payload Map<String, Object> obj)  {
        String connID = (String) obj.get("connID");
        Response res=tradingSystem.AddNewManager(userID, connID, storeID,newManagerID);
        res.AddConnID(connID);
        res.AddTag("AddNewManager");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.6
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param managerID: int (Path)
     * @param obj:{
     *  "connID": String
     *  TODO: Think what values should be in Edit Manager Permissions
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @MessageMapping("{userID}/store/{storeID}/add_new_manager/{managerID}")
    public Response EditManagerPermissions(@DestinationVariable int userID, @DestinationVariable int storeID, @DestinationVariable int managerID, @Payload Map<String, Object> obj)  {
        String connID = (String) obj.get("connID");
        List<User.Permission> Permissions=new LinkedList<>();
        try {
            if((boolean) obj.get("AddProduct"))
                Permissions.add(User.Permission.AddProduct);
            if((boolean) obj.get("ReduceProduct"))
                Permissions.add(User.Permission.ReduceProduct);
            if((boolean) obj.get("DeleteProduct"))
                Permissions.add(User.Permission.DeleteProduct);
            if((boolean) obj.get("EditProduct"))
                Permissions.add(User.Permission.EditProduct);
            if((boolean) obj.get("AppointmentOwner"))
                Permissions.add(User.Permission.AppointmentOwner);
            if((boolean) obj.get("AppointmentManager"))
                Permissions.add(User.Permission.AppointmentManager);
            if((boolean) obj.get("EditManagerPermission"))
                Permissions.add(User.Permission.EditManagerPermission);
            if((boolean) obj.get("RemoveManager"))
                Permissions.add(User.Permission.RemoveManager);
            if((boolean) obj.get("GetInfoOfficials"))
                Permissions.add(User.Permission.GetInfoOfficials);
            if((boolean) obj.get("GetInfoRequests"))
                Permissions.add(User.Permission.GetInfoRequests);
            if((boolean) obj.get("ResponseRequests"))
                Permissions.add(User.Permission.ResponseRequests);
            if((boolean) obj.get("GetStoreHistory"))
                Permissions.add(User.Permission.GetStoreHistory);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Response res = tradingSystem.EditManagerPermissions(userID, connID, storeID, managerID, Permissions);
        res.AddConnID(connID);
        res.AddTag("EditManagerPermissions");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @param userID
     * @param obj:{
     *  "connID": String
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     *  "permissions":List[
     *  permissions:String]
     * }
    */
    @MessageMapping("{userID}/store/get_possible_permissions_to_manager")
    public Response GetPossiblePermissionsToManager(@DestinationVariable int userID, @Payload Map<String, Object> obj)  {
        String connID = (String) obj.get("connID");
        Response res = tradingSystem.GetPossiblePermissionsToManager(userID, connID);
        res.AddConnID(connID);
        res.AddTag("GetPossiblePermissionsToManager");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.7
     *
     * @param userID : int (Path)
     * @param storeID: int (Path)
     * @param managerID: int (Path)
     * @param obj:{
     *  "connID": String
     * }
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    @MessageMapping("{userID}/store/{storeID}/remove_manager/{managerID}")
    public Response RemoveManager(@DestinationVariable int userID, @DestinationVariable int storeID, @DestinationVariable int managerID, @Payload Map<String, Object> obj)  {
        String connID = (String) obj.get("connID");
        Response res=tradingSystem.RemoveManager(userID, connID, storeID,managerID);
        res.AddConnID(connID);
        res.AddTag("RemoveManager");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.9
     *
     * @param userID : int (Path)
     * @param storeID: int (Path)
     * @param obj:{
     *  "connID": String
     * }
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
    @MessageMapping("{userID}/store/{storeID}/workers")
    public Response ShowStoreWorkers(@DestinationVariable int userID, @DestinationVariable int storeID, @Payload Map<String, Object> obj)  {
        String connID = (String) obj.get("connID");
        Response res = tradingSystem.ShowStoreWorkers(userID, connID, storeID);
        res.AddTag("ShowStoreWorkers");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement 4.11
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param obj:{
     *  "connID": String
     * }
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
    @MessageMapping("{userID}/store_history_owner/{storeID}")
    public Response OwnerStoreHistory(@DestinationVariable int userID, @DestinationVariable int storeID, @Payload Map<String, Object> obj){
        String connID = (String) obj.get("connID");
        Response res = tradingSystem.StoreHistoryOwner(userID,storeID,connID);
        res.AddConnID(connID);
        res.AddTag("OwnerStoreHistory");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        WriteToLogger(res);
        return res;
    }

    /**
     * @requirement none
     *
     * @param obj:{
     *  "connID": String
     * }
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
    @MessageMapping("{userID}/stores_owner")
    public Response ShowOwnerStores(@DestinationVariable int userID, @Payload Map<String, Object> obj) {
        String connID = (String) obj.get("connID");
        Response res = this.tradingSystem.ShowOwnerStores(userID, connID);
        res.AddTag("ShowOwnerStores");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        return res;
    }

    @MessageMapping("{userID}/stores_manager")
    public Response ShowManagerStores(@DestinationVariable int userID, @Payload Map<String, Object> obj) {
        String connID = (String) obj.get("connID");
        Response res = this.tradingSystem.ShowManagerStores(userID, connID);
        res.AddTag("ShowManagerStores");
        template.convertAndSend(String.format("/topic/%s", connID), res);
        return res;
    }

    private void WriteToLogger(Response res){
        if(res.getIsErr()) {
            loggerController.WriteErrorMsg("Store Owner Error: " + res.getMessage());
        }
        else{
            loggerController.WriteLogMsg("Store Owner: " + res.getMessage());
        }
    }
}
