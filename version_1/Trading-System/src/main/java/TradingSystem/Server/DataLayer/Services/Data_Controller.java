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

    //Req 1.2 Register
    public Response AddSubscriber(String userName, String password){
        if(userName== null || password==null){
            return new Response(true,"User name or password can't be empty");
        }
        else{
            return subscriberService.AddSubscriber(userName, password);
        }
    }

    public Response GetSubscriber(String userName, String password){
        return subscriberService.GetSubscriber(userName, password);
    }

    //Req 2.1 add new store
    public Response AddStore(String storeName, int userID){
        return storeService.AddStore(storeName, userID);
    }

    //Req 4.1 storeOwner can add products and edit them
    public Response AddProductToStore(int storeID, String productName,
                                  String category, Double price, int quantity){
        return productService.AddProductToStore(storeID, productName, category, price, quantity);
    }

    public Response addProductToBag(int userID, Integer storeID, Integer productID, Integer quantity){
        return shoppingCartService.addProductToBag(userID, storeID, productID, quantity);
    }

    public Response setBagFinalPrice(int userID, Integer storeID, Double finalPrice) {
        return shoppingCartService.setBagFinalPrice(userID, storeID, finalPrice);
    }

    public Response setBagProductQuantity(int userID, Integer storeID, int productID, Integer quantity) {
        return shoppingCartService.setBagProductQuantity(userID, storeID, productID, quantity);
    }



    public void addSpacialProductToBag(int userID, Integer storeID, Integer productID, Integer quantity, int price) {
        shoppingCartService.addSpacialProductToBag(userID, storeID, productID, quantity, price);
    }

    public void setBagSpacialFinalPrice(int userID, Integer storeID, int finalPrice) {
        shoppingCartService.setBagSpacialFinalPrice(userID, storeID, finalPrice);
    }

    public void RemoveBagSpacialProduct(int userID, Integer storeID, int productID) {
        shoppingCartService.RemoveBagSpacialProduct(userID, storeID, productID);
    }

    public Response RemoveBagProduct(int userID, Integer storeID, int productID) {
        return shoppingCartService.RemoveBagProduct(userID, storeID, productID);
    }

    //ADD pair Owner_permissions to get list of the permissions
    public Response getOwnerPermissions(int userID, int storeID){
        return permissionsService.getOwnerPermissions(userID, storeID);
    }

    public Response addHistoryToStoreAndUser(ShoppingHistory shoppingHistory){
        return shoppingHistoryService.addHistoryToStoreAndUser(shoppingHistory);
    }
    public Response findAllByCategoryAndProductNameAndPriceBetween(String name, String category, int min,int max){
        return productService.findAllByCategoryAndProductNameAndPriceBetween(name,category,min,max);
    }
    public Response AddNewOwner(int storeID, int newOwnerID, OwnerPermission OP) {
        return storeService.AddNewOwner(storeID, newOwnerID, OP);
    }

    public Response AddNewManager(int storeID, int newManagerID, ManagerPermission MP) {
        return storeService.AddNewManager(storeID, newManagerID, MP);
    }

    public Response EditManagerPermissions(int storeID, int managerID, List<PermissionEnum.Permission> permissions) {
        permissionsService.EditManagerPermissions(storeID, managerID, permissions);
        return new Response("");
    }

    public Response addCommentToProduct(Integer productID, Integer userID, String comment) {
        return productService.addCommentToProduct(productID, userID, comment);
    }

    public void AddBidForProduct(int productID, int userID, Integer productPrice,Integer quantity, ConcurrentHashMap<Integer,Boolean> managerList) {
        bidService.AddBidForProduct(productID, userID, productPrice, quantity, managerList);
    }

    public void RemoveBid(int productID, int userID) {
        bidService.RemoveBid(productID, userID);
    }

    public void approveBid(int productID, int userID, int managerID) {
        bidService.approveBid(productID, userID, managerID);
    }

    public void UpdateOwnerList(int productID, int userID, ConcurrentHashMap<Integer, Boolean> managerList) {
        bidService.UpdateOwnerList(productID, userID, managerList);
    }

    public void initialAprrovment(int productID, int userID) {
        bidService.initialAprrovment(productID, userID);
    }

    public void setBidPrice(int productID, int userID, Integer price) {
        bidService.setBidPrice(productID, userID, price);
    }

    public void setBidQuantity(int productID, int userID, Integer quantity) {
        bidService.setBidQuantity(productID, userID, quantity);
    }

    public List<DataBid> getAllBids(){
        return bidService.getAllBids();
    }

    public Response RemoveProduct(int productId) {
        return productService.RemoveProduct(productId);
    }

    public Response getAllSubscribers(){
        return subscriberService.getAllSubscribers();
    }


    //Req 1.2 get information on store
    public Response getAllStores(){
        return storeService.getAllStores();
    }
//
//    //Req 1.2 get information on the products of the store
//    public List<DataProduct> getAllProductsByStoreId(int storeid){
//        return productService.findDummyProductByStoreID(storeid);
//    }

    public Response findStorebyId(int storeid){
        return storeService.findStorebyId(storeid);
    }

    public Response findSubscriberById(int subscriberId){
        return subscriberService.findSubscriberById(subscriberId);
    }

    public Response findDummyProductByStore(Integer storeID){
        return productService.findDummyProductByStore(storeID);
    }

    public void deleteAll(){
        productService.deleteAll();
        subscriberService.deleteAll();
        shoppingHistoryService.deleteAll();
        storeService.deleteAll();
        shoppingCartService.deleteAll();
    }

    public Response getAllFoundedStores(int userid){
        return storeService.getAllStoresofFounder(userid);
    }
    public Response getAllOwnedStores(int userid){
        return storeService.getAllStoresOfOwner(userid);
    }
    public Response getAllManagerStores(int userid){
        return storeService.getAllStoresofManager(userid);
    }

    public List<DataShoppingHistory> getAllHistoryOfSubscriber(int userid){
        return shoppingHistoryService.findAllBySubscriber(userid);
    }

    public Response RemoveOwner(int storeID, int ownerID){
        permissionsService.RemoveOwner(storeID, ownerID);
        return new Response(false, "");
    }

    public void RemoveManager(int storeID, int managerID){
        permissionsService.RemoveManager(storeID, managerID);
    }


//    public List<DataSubscriber> findAllStoresManagerContains(int storeid){
//        return subscriberService.findAllByStoresManagerContains(storeid);
//    }
//    public List<DataSubscriber> findAllByStoresOwnedContains(int storeid){
//        return subscriberService.findAllByStoresOwnedContains(storeid);
//    }
//
//    public List<DataShoppingHistory> findAllByStore(int storeid){
//        return shoppingHistoryService.findAllByStore(storeid);
//    }

    public Response getSubscriberShoppingCart(int userID){
        return shoppingCartService.getSubscriberShoppingCart(userID);
    }

    public Response setQuantity(Integer productID, int newQuantity){
        return productService.setQuantity(productID, newQuantity);
    }
//
    public Response editProductDetails(Integer productID, String productName, Double price, String category, Integer quantity) {
        return productService.editProductDetails(productID, productName, price, category, quantity);
    }

    public Response deleteSubscriberBag(Integer userID, Integer storeID){
        return shoppingCartService.deleteSubscriberBag(userID, storeID);
    }

    public Response AddBuyingPolicy(Integer storeId, Expression expression){
        DBExpression parent=new DBExpression(expression,null);
        Response response=findStorebyId(storeId);
        if(response.getIsErr()){
            return new Response(true,"Could not found storeid");
        }
        DataBuyingPolicy buyingPolicy=new DataBuyingPolicy(response.returnDataStore().getStoreID(),parent);
        return buyingService.AddBuyingPolicy(buyingPolicy);
    }

    public Response AddDiscountPolicy(Integer storeId, Sale sale){
        DBSale parent=new DBSale(sale,null);
        Response response= findStorebyId(storeId);
        if(response.getIsErr()){
            return new Response(true,"Could not found storeid");
        }
        DataDiscountPolicy dataDiscountPolicy= new DataDiscountPolicy(response.returnDataStore().getStoreID(),parent);
        return discountPolicyService.AddDiscountPolicy(dataDiscountPolicy);
    }
//    public Response AddDiscountPolicy(DataDiscountPolicy service){
//        return discountPolicyService.AddDiscountPolicy(service);
//    }

    public Response getBuyingByStoreId(Integer storeid){
        return buyingService.getBuyingByStore(storeid);
    }

    public Response getdiscountByStoreId(Integer storeid){
        return discountPolicyService.getDiscountByStore(storeid);
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


//
//    //Req 1.3 search Product By Name
//    public List<DataProduct> serachByName(String productName, int minprice, int maxprice){
//        return productService.findDummyProductByName(productName, minprice, maxprice);
//    }
//
//    //Req 1.3 search Product By Category
//    public List<DataProduct> serachByCategory(String category, int minprice, int maxprice){
//        return productService.findDummyProductByCategory(category, minprice, maxprice);
//    }
//
//    //Req 1.8 find shoppingcart by id
//    public DataShoppingCart findShoppingCartByUserId(int userId){
//        return shoppingCartService.findDummyShoppingCartByUserID(userId);
//    }
//
//    //TODO implement 1.9 purchase by discount
//
//
//
//    //TODO implement 2.2 write comment
//
//    //Req 3.7 get information about personal history
//    public DataShoppingHistory findShoppinghistoryByid(int userId){
//        return shoppingHistoryService.findByuserId(userId);
//    }
//
//
//
//    public void editProduct(DataProduct product){
//        productService.editProduct(product);
//    }
//
//    //TODO implement 4.2 policies
//
//    //Req 4.11 get all the history of the store
//    public List<DataShoppingHistory> getHistoriesByStoreId(int storeid){
//        return shoppingHistoryService.getShoppingHistoryByStoreId(storeid);
//    }

}
