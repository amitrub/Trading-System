package TradingSystem.Server.DataLayer.Services;


import TradingSystem.Server.DataLayer.Data_Modules.Bid.DataBid;
import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DBExpression;
import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DataBuyingPolicy;
import TradingSystem.Server.DataLayer.Data_Modules.Sales.DBSale;
import TradingSystem.Server.DataLayer.Data_Modules.Sales.DataDiscountPolicy;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory.DataShoppingHistory;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.UserComponent.ManagerPermission;
import TradingSystem.Server.DomainLayer.UserComponent.OwnerPermission;
import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class Data_Controller {
    @Autowired
    private BuyingService buyingService;
    @Autowired
    private DiscountPolicyService discountPolicyService;
    @Autowired
    private SubcriberService subscriberService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private ShoppingHistoryService shoppingHistoryService;
    @Autowired
    private PermissionsService permissionsService;
    @Autowired
    private BidService bidService;


    public Data_Controller(){

    }

    public Response test(){
        try {
            System.out.println("=========================test=======================");
            storeService.test();
            System.out.println("=========================test=======================");
            return new Response();
        }
        catch (Exception e){
            System.out.println("=========================Exception test=======================");
            return new Response(true, "bad bad bad");
        }
    }

    //Req 1.2 Register
    public Response AddSubscriber(String userName, String password){
        try {
            if(userName== null || password==null){
                return new Response(true,"User name or password can't be empty");
            }
            else{
                return subscriberService.AddSubscriber(userName, password);
            }
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }

    }

    public Response GetSubscriber(String userName, String password){
        try {
            return subscriberService.GetSubscriber(userName, password);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    //Req 2.1 add new store
    public Response AddStore(String storeName, int userID){
        try {
            return storeService.AddStore(storeName, userID);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    //Req 4.1 storeOwner can add products and edit them
    public Response AddProductToStore(int storeID, String productName,
                                  String category, Double price, int quantity){
        try {
            return productService.AddProductToStore(storeID, productName, category, price, quantity);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response RemoveProduct(int productId) {
        try {
            return productService.RemoveProduct(productId);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response setQuantity(Integer productID, int newQuantity){
        try {
            return productService.setQuantity(productID, newQuantity);
        } catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }
    //
    public Response editProductDetails(Integer productID, String productName, Double price, String category, Integer quantity) {
        try {
            return productService.editProductDetails(productID, productName, price, category, quantity);
        } catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response addCommentToProduct(Integer productID, Integer userID, String comment) {
        try {
            return productService.addCommentToProduct(productID, userID, comment);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response addProductToBag(int userID, Integer storeID, Integer productID, Integer quantity){
        try {
            return shoppingCartService.addProductToBag(userID, storeID, productID, quantity);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response deleteSubscriberBag(Integer userID, Integer storeID){
        try {
            return shoppingCartService.deleteSubscriberBag(userID, storeID);
        } catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response setBagProductQuantityAndFinalPrice(int userID, Integer storeID, int productID, Integer quantity, Double finalPrice) {
        try {
            return shoppingCartService.setBagProductQuantityAndFinalPrice(userID, storeID, productID, quantity, finalPrice);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }

    }

    public Response setBagFinalPrice(int userID, Integer storeID, Double finalPrice) {
        try {
            return shoppingCartService.setBagFinalPrice(userID, storeID, finalPrice);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response setBagProductQuantity(int userID, Integer storeID, int productID, Integer quantity) {
        try {
            return shoppingCartService.setBagProductQuantity(userID, storeID, productID, quantity);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response subscriberPurchase(int userID, Map<Integer, Integer> productID_quantity, List<ShoppingHistory> historyList){
        try {
            return shoppingCartService.subscriberPurchase(userID, productID_quantity, historyList);
        } catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }



    public Response addSpacialProductToBag(int userID, Integer storeID, Integer productID, Integer quantity, int price) {
        try {
            shoppingCartService.addSpacialProductToBag(userID, storeID, productID, quantity, price);
            return new Response("good");
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response setBagSpacialFinalPrice(int userID, Integer storeID, int finalPrice) {
        try {
            shoppingCartService.setBagSpacialFinalPrice(userID, storeID, finalPrice);
            return new Response("good");
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public void RemoveBagSpacialProduct(int userID, Integer storeID, int productID) {
        shoppingCartService.RemoveBagSpacialProduct(userID, storeID, productID);
    }

    public Response RemoveBagProduct(int userID, Integer storeID, int productID) {
        try {
            return shoppingCartService.RemoveBagProduct(userID, storeID, productID);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    //ADD pair Owner_permissions to get list of the permissions
    public Response getOwnerPermissions(int userID, int storeID){
        try {
            return permissionsService.getOwnerPermissions(userID, storeID);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response addHistoryToStoreAndUser(ShoppingHistory shoppingHistory){
        try {
            return shoppingHistoryService.addHistoryToStoreAndUser(shoppingHistory);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }
    public Response findAllByCategoryAndProductNameAndPriceBetween(String name, String category, int min,int max){
        try {
            return productService.findAllByCategoryAndProductNameAndPriceBetween(name,category,min,max);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }
    public Response AddNewOwner(int storeID, int newOwnerID, OwnerPermission OP) {
        try {
            return storeService.AddNewOwner(storeID, newOwnerID, OP);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response AddNewManager(int storeID, int newManagerID, ManagerPermission MP) {
        try {
            return storeService.AddNewManager(storeID, newManagerID, MP);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response EditManagerPermissions(int storeID, int managerID, List<PermissionEnum.Permission> permissions) {
        try {
            permissionsService.EditManagerPermissions(storeID, managerID, permissions);
            return new Response("");
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response RemoveOwner(int storeID, int ownerID){
        try {
            permissionsService.RemoveOwner(storeID, ownerID);
            return new Response(false, "");
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response RemoveManager(int storeID, int managerID){
        try {
            permissionsService.RemoveManager(storeID, managerID);
            return new Response(false, "");
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }



    public Response AddBidForProduct(int productID, int userID, Integer productPrice,Integer quantity, ConcurrentHashMap<Integer,Boolean> managerList) {
        try {
            return bidService.AddBidForProduct(productID, userID, productPrice, quantity, managerList);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response RemoveBid(int productID, int userID) {
        try {
            return bidService.RemoveBid(productID, userID);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response approveBid(int productID, int userID, int managerID) {
        try {
            return bidService.approveBid(productID, userID, managerID);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response UpdateBidOwnerList(int productID, int userID, ConcurrentHashMap<Integer, Boolean> managerList) {
        try {
            return bidService.UpdateBidOwnerList(productID, userID, managerList);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response initialAprrovment(int productID, int userID) {
        try {
            return bidService.initialAprrovment(productID, userID);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response setBidPrice(int productID, int userID, Integer price) {
        try {
            return bidService.setBidPrice(productID, userID, price);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response setBidQuantity(int productID, int userID, Integer quantity) {
        try {
            return bidService.setBidQuantity(productID, userID, quantity);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public List<DataBid> getAllBids(){
        return bidService.getAllBids();
    }

    public Response getAllSubscribers(){
        try {
            return subscriberService.getAllSubscribers();
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }


    //Req 1.2 get information on store
    public Response getAllStores(){
        try {
            return storeService.getAllStores();
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }


    public Response findStorebyId(int storeid){
        try {
            return storeService.findStorebyId(storeid);
        }
        catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public void deleteAll(){
        productService.deleteAll();
        subscriberService.deleteAll();
        shoppingHistoryService.deleteAll();
        storeService.deleteAll();
        shoppingCartService.deleteAll();
    }

    public Response AddBuyingPolicy(Integer storeId, Expression expression){
        try {
            DBExpression parent=new DBExpression(expression,null);
            Response response=findStorebyId(storeId);
            if(response.getIsErr()){
                return new Response(true,"Could not found storeid");
            }
            DataBuyingPolicy buyingPolicy=new DataBuyingPolicy(response.returnDataStore().getStoreID(),parent);
            return buyingService.AddBuyingPolicy(buyingPolicy);
        } catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response AddDiscountPolicy(Integer storeId, Sale sale){
        try {
            DBSale parent=new DBSale(sale,null);
            Response response= findStorebyId(storeId);
            if(response.getIsErr()){
                return new Response(true,"Could not found storeid");
            }
            DataDiscountPolicy dataDiscountPolicy= new DataDiscountPolicy(response.returnDataStore().getStoreID(),parent);
            return discountPolicyService.AddDiscountPolicy(dataDiscountPolicy);
        } catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response getBuyingByStoreId(Integer storeid){
        try {
            return buyingService.getBuyingByStore(storeid);
        } catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public Response getdiscountByStoreId(Integer storeid){
        try {
            return discountPolicyService.getDiscountByStore(storeid);
        } catch (Exception e){
            return new Response(true, "Error In DB!");
        }
    }

    public HashMap<Date,Integer> getAllSubscribersWeek(){
        return subscriberService.getAllSubscribersWeek();
    }

    public HashMap<Date,Integer> getAllStoresWeek(){
        return storeService.getAllStoresWeek();
    }
    public HashMap<Date,Integer> getAllShoppingHistoriesWeek(){
        return shoppingHistoryService.getAllShoppingHistoriesWeek();
    }
    public HashMap<Date,Integer> getAllMoneyWeek(){
        return shoppingHistoryService.getAllMoneyWeek();
    }


}
