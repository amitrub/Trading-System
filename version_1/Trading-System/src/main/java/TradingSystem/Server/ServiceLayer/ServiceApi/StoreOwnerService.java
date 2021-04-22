package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/owner")
public class StoreOwnerService {
    private final TradingSystem tradingSystem = TradingSystem.getInstance();

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
    //TODO: not check yet
    @PostMapping("{userID}/store/{storeID}/change_quantity_product/{productID}")
    public Response ChangeQuantityProduct(@PathVariable int userID, @PathVariable int storeID, @PathVariable int productID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        int quantity  = (int) obj.get("quantity");
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
    //TODO: not check yet
    @PostMapping("{userID}/store/{storeID}/edit_product/{productID}")
    public Response EditProduct(@PathVariable int userID, @PathVariable int storeID, @PathVariable int productID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
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
    //TODO: not check yet
    @GetMapping("{userID}/store/{storeID}/remove_product/{productID}")
    public Response RemoveProduct(@PathVariable int userID, @PathVariable int storeID, @PathVariable int productID, @RequestHeader("connID") String connID){
        Response res = this.tradingSystem.RemoveProduct(userID,storeID,productID,connID);
        System.out.println(res);
        tradingSystem.printProducts();
        return res;
    }

    /**
     * @requirement 4.2
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
    //TODO: not implemented version 2
    @PostMapping("{userID}/store/{storeID}/add_buying_policy}")
    public Response AddBuyingPolicy(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
//        Response res = this.tradingSystem.AddBuyingPolicy(userID,storeID,connID);
        Response res = new Response(true, "not implemented");
        return res;
    }

    /**
     * @requirement 4.2
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
    //TODO: not implemented version 2
    @PostMapping("{userID}/store/{storeID}/add_discount_policy}")
    public Response AddDiscountPolicy(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
//        Response res = this.tradingSystem.AddDiscountPolicy(userID,storeID,connID);
        Response res = new Response(true, "not implemented");
        return res;
    }

    /**
     * @requirement 4.2
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param buyingPolicyID : int (Path)
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
    //TODO: not implemented version 2
    @PostMapping("{userID}/store/{storeID}/edit_buying_policy/{buyingPolicyID}")
    public Response EditBuyingPolicy(@PathVariable int userID, @PathVariable int storeID, @PathVariable int buyingPolicyID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
//        Response res = this.tradingSystem.EditBuyingPolicy(userID,storeID,connID);
        Response res = new Response(true, "not implemented");
        return res;
    }

    /**
     * @requirement 4.2
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param discountPolicyID : int (Path)
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
    //TODO: not implemented version 2
    @PostMapping("{userID}/store/{storeID}/edit_discount_policy/{discountPolicyID}}")
    public Response EditDiscountPolicy(@PathVariable int userID, @PathVariable int storeID, @PathVariable int discountPolicyID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
//        Response res = this.tradingSystem.EditDiscountPolicy(userID,storeID,connID);
        Response res = new Response(true, "not implemented");
        return res;
    }

    /**
     * @requirement 4.2
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param buyingPolicyID : int (Path)
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
    //TODO: not implemented version 2
    @DeleteMapping("{userID}/store/{storeID}/remove_buying_policy/{buyingPolicyID}")
    public Response RemoveBuyingPolicy(@PathVariable int userID, @PathVariable int storeID, @PathVariable int buyingPolicyID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
//        Response res = this.tradingSystem.RemoveBuyingPolicy(userID,storeID,connID);
        Response res = new Response(true, "not implemented");
        return res;
    }

    /**
     * @requirement 4.2
     *
     * @param userID: int (Path)
     * @param storeID: int (Path)
     * @param discountPolicyID : int (Path)
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
    //TODO: not implemented version 2
    @DeleteMapping("{userID}/store/{storeID}/remove_discount_policy/{discountPolicyID}}")
    public Response RemoveDiscountPolicy(@PathVariable int userID, @PathVariable int storeID, @PathVariable int discountPolicyID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
//        Response res = this.tradingSystem.RemoveDiscountPolicy(userID,storeID,connID);
        Response res = new Response(true, "not implemented");
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
    //TODO: not implemented version 2
    @DeleteMapping("{userID}/store/{storeID}/add_new_owner/{OwnerID}")
    public Response RemoveOwner(@PathVariable int userID, @PathVariable int storeID, @PathVariable int OwnerID, @RequestHeader("connID") String connID)  {
//        Response res = tradingSystem.RemoveOwner(userID, connID, storeID, newOwnerID);
        Response res = new Response(true, "not implemented");
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
    //TODO: not check yet
    @PostMapping("{userID}/store/{storeID}/add_new_manager/{managerID}")
    public Response EditManagerPermissions(@PathVariable int userID, @PathVariable int storeID, @PathVariable int managerID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj)  {
//        Response res = tradingSystem.EditManagerPermissions(userID, connID, storeID, newOwnerID);
        Response res = new Response(true, "not implemented");
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
    //TODO: not implemented
    @GetMapping("{userID}/store/{storeID}/workers")
    public Response ShowStoreWorkers(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID)  {
//        Response res = tradingSystem.ShowStoreWorkers(userID, connID, storeID);
        Response res = new Response(true, "not implemented");
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
    @GetMapping("{userID}/store_history_owner/{storeID}")
    public Response OwnerStoreHistory(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID){
        Response res = tradingSystem.StoreHistoryOwner(userID,storeID,connID);
        res.AddConnID(connID);
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
        return res;
    }

    @GetMapping("{userID}/stores_manager")
    public Response ShowManagerStores(@PathVariable int userID, @RequestHeader("connID") String connID) {
        Response res = this.tradingSystem.ShowManagerStores(userID, connID);
        return res;
    }
}
