package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class Data_Controller {

    @Autowired
    private SubcriberService subscriberService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShoppingCartService shoppingCartService;
//    @Autowired
//    private ShoppingHistoryService shoppingHistoryService;


    public Data_Controller(){

    }

    //Req 1.2 Register
    public int AddSubscriber(String userName, String password){
        return subscriberService.AddSubscriber(userName, password);
    }

    //Req 2.1 add new store
    public int AddStore(String storeName, int userID){
        return storeService.AddStore(storeName, userID);
    }

    //Req 4.1 storeOwner can add products and edit them
    public int AddProductToStore(int storeID, String productName,
                                  String category, Double price, int quantity){
        return productService.AddProductToStore(storeID, productName, category, price, quantity);
    }

    public void addProductToBag(int userID, Integer storeID, Integer productID, Integer quantity){
        shoppingCartService.addProductToBag(userID, storeID, productID, quantity);
    }

    public void setBagFinalPrice(int userID, Integer storeID, Double finalPrice) {
        shoppingCartService.setBagFinalPrice(userID, storeID, finalPrice);
    }

    public List<DataProduct> findAllByCategoryAndProductNameAndPriceBetween(String name, String category, int min,int max){
        return productService.findAllByCategoryAndProductNameAndPriceBetween(name,category,min,max);
    }

    public void RemoveProduct(int productId) {
        productService.RemoveProduct(productId);
    }

    public List<DataSubscriber> getAllSubscribers(){
        return subscriberService.getAllSubscribers();
    }


    //Req 1.2 get information on store
    public List<DataStore> getAllStores(){
        return storeService.getAllStores();
    }
//
//    //Req 1.2 get information on the products of the store
//    public List<DataProduct> getAllProductsByStoreId(int storeid){
//        return productService.findDummyProductByStoreID(storeid);
//    }

    public Optional<DataStore> findStorebyId(int storeid){
        return storeService.findStorebyId(storeid);
    }

    public Optional<DataSubscriber> findSubscriberById(int subscriberId){
        return subscriberService.findSubscriberById(subscriberId);
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
