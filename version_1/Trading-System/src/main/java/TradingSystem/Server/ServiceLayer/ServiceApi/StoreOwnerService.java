package TradingSystem.Server.ServiceLayer.ServiceApi;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.NewResponse;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public NewResponse AddProductToStore(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj) {
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
        NewResponse res = tradingSystem.AddProductToStore(userID, connID, storeID, productName, category, price, quantity);
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
    public NewResponse ChangeQuantityProduct(@PathVariable int userID, @PathVariable int storeID, @PathVariable int productID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
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
    public NewResponse EditProduct(@PathVariable int userID, @PathVariable int storeID, @PathVariable int productID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
        System.out.println("----------------------> EditProduct");
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
    @DeleteMapping("{userID}/store/{storeID}/remove_product/{productID}")
    public NewResponse RemoveProduct(@PathVariable int userID, @PathVariable int storeID, @PathVariable int productID, @RequestHeader("connID") String connID){
        NewResponse res = this.tradingSystem.RemoveProduct(userID,storeID,productID,connID);    
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
    public NewResponse AddBuyingPolicy(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
//        Response res = this.tradingSystem.AddBuyingPolicy(userID,storeID,connID);
        NewResponse res = new NewResponse(true, "not implemented");
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
    public NewResponse AddDiscountPolicy(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
//        Response res = this.tradingSystem.AddDiscountPolicy(userID,storeID,connID);
        NewResponse res = new NewResponse(true, "not implemented");
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
    public NewResponse EditBuyingPolicy(@PathVariable int userID, @PathVariable int storeID, @PathVariable int buyingPolicyID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
//        Response res = this.tradingSystem.EditBuyingPolicy(userID,storeID,connID);
        NewResponse res = new NewResponse(true, "not implemented");
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
    public NewResponse EditDiscountPolicy(@PathVariable int userID, @PathVariable int storeID, @PathVariable int discountPolicyID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
//        Response res = this.tradingSystem.EditDiscountPolicy(userID,storeID,connID);
        NewResponse res = new NewResponse(true, "not implemented");
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
    public NewResponse RemoveBuyingPolicy(@PathVariable int userID, @PathVariable int storeID, @PathVariable int buyingPolicyID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
//        Response res = this.tradingSystem.RemoveBuyingPolicy(userID,storeID,connID);
        NewResponse res = new NewResponse(true, "not implemented");
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
    public NewResponse RemoveDiscountPolicy(@PathVariable int userID, @PathVariable int storeID, @PathVariable int discountPolicyID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj){
//        Response res = this.tradingSystem.RemoveDiscountPolicy(userID,storeID,connID);
        NewResponse res = new NewResponse(true, "not implemented");
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
    //TODO: not check yet
    @PostMapping("{userID}/store/{storeID}/add_new_owner/{newOwnerID}")
    public NewResponse AddNewOwner(@PathVariable int userID, @PathVariable int storeID, @PathVariable int newOwnerID, @RequestHeader("connID") String connID)  {
        NewResponse res = tradingSystem.AddNewOwner(userID, connID, storeID, newOwnerID);
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
    public NewResponse RemoveOwner(@PathVariable int userID, @PathVariable int storeID, @PathVariable int OwnerID, @RequestHeader("connID") String connID)  {
//        Response res = tradingSystem.RemoveOwner(userID, connID, storeID, newOwnerID);
        NewResponse res = new NewResponse(true, "not implemented");
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
    //TODO: not check yet
    @PostMapping("{userID}/store/{storeID}/add_new_manager/{newManagerID}")
    public NewResponse AddNewManager(@PathVariable int userID, @PathVariable int storeID, @PathVariable int newManagerID, @RequestHeader("connID") String connID)  {
        return tradingSystem.AddNewManager(userID, connID, storeID,newManagerID);
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
    public NewResponse EditManagerPermissions(@PathVariable int userID, @PathVariable int storeID, @PathVariable int managerID, @RequestHeader("connID") String connID, @RequestBody Map<String, Object> obj)  {
//        Response res = tradingSystem.EditManagerPermissions(userID, connID, storeID, newOwnerID);
        NewResponse res = new NewResponse(true, "not implemented");
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
    //TODO: not check yet
    @GetMapping("{userID}/store/{storeID}/remove_manager/{managerID}")
    public NewResponse RemoveManager(@PathVariable int userID, @PathVariable int storeID, @PathVariable int managerID, @RequestHeader("connID") String connID)  {
        return tradingSystem.RemoveManager(userID, connID, storeID,managerID);
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
    public NewResponse ShowStoreWorkers(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID)  {
//        Response res = tradingSystem.ShowStoreWorkers(userID, connID, storeID);
        NewResponse res = new NewResponse(true, "not implemented");
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
    //TODO: not check yet
    @GetMapping("{userID}/store_history/{storeID}")
    public NewResponse ShowStoreHistory(@PathVariable int userID, @PathVariable int storeID, @RequestHeader("connID") String connID){
        return tradingSystem.StoreHistory(userID,storeID,connID);
    }
    
}
