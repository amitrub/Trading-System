package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class Data_Controller {

    @Autowired
    private StoreService storeService;
    @Autowired
    private SubcriberService subscriberService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private ShoppingHistoryService shoppingHistoryService;
    public Data_Controller(){

    }

    //Req 1.2 Register
    public int AddSubscriber(DataSubscriber dataSubscriber){
        return subscriberService.AddSubscriber(dataSubscriber);
    }

    //Req 1.2 get information on store
    public List<DataStore> getAllStores(){
        return storeService.getAllStores();
    }

    //Req 1.2 get information on the products of the store
    public List<DataProduct> getAllProductsByStoreId(int storeid){
        return productService.findDummyProductByStoreID(storeid);
    }

    //Req 1.3 search Product By Name
    public List<DataProduct> serachByName(String productName, int minprice, int maxprice){
        return productService.findDummyProductByName(productName, minprice, maxprice);
    }

    //Req 1.3 search Product By Category
    public List<DataProduct> serachByCategory(String category, int minprice, int maxprice){
        return productService.findDummyProductByCategory(category, minprice, maxprice);
    }

    //Req 1.8 find shoppingcart by id
    public DataShoppingCart findShoppingCartByUserId(int userId){
        return shoppingCartService.findDummyShoppingCartByUserID(userId);
    }

    //TODO implement 1.9 purchase by discount

    //Req 2.1 add new store
    public int AddStore(DataStore store){
        return storeService.Addstore(store);
    }

    //TODO implement 2.2 write comment

    //Req 3.7 get information about personal history
    public DataShoppingHistory findShoppinghistoryByid(int userId){
        return shoppingHistoryService.findByuserId(userId);
    }

    //Req 4.1 storeOwner can add products and edit them
    public void AddProduct(DataProduct product){
        productService.AddProduct(product);
    }

    public void editProduct(DataProduct product){
        productService.editProduct(product);
    }

    //TODO implement 4.2 policies

    //Req 4.11 get all the history of the store
    public List<DataShoppingHistory> getHistoriesByStoreId(int storeid){
        return shoppingHistoryService.getShoppingHistoryByStoreId(storeid);
    }

}
