package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagProduct;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory.DataHistoryProduct;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory.DataShoppingHistory;
import TradingSystem.Server.DataLayer.Repositories.*;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ShoppingHistoryService {

    @Autowired
    ShoppingHistoryRepository shoppingHistoryRepository;
    @Autowired
    SubscriberRepository subscriberRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ShoppingCartRepository shoppingCartRepository;
    @Autowired
    DataShoppingBagProductRepository shoppingBagProductRepository;


    public void addHistoryToStoreAndUser(ShoppingHistory shoppingHistory){
        Integer userID = shoppingHistory.getUserID();
        Integer storeID = shoppingHistory.getStoreID();
        Date date = shoppingHistory.getDate();
        Double finalPrice = shoppingHistory.getFinalPrice();
        DataSubscriber user = null;
        if (userID>=1){
            user = subscriberRepository.getOne(userID);
        }
        DataStore store = storeRepository.getOne(storeID);
        DataShoppingHistory dataShoppingHistory = new DataShoppingHistory(user, store, date, finalPrice);

        List<Product> products = shoppingHistory.getProducts();
        for (Product p : products){
            DataHistoryProduct historyProduct = new DataHistoryProduct(p);
            dataShoppingHistory.addProduct(historyProduct);
        }
        shoppingHistoryRepository.save(dataShoppingHistory);
    }
    public void deleteAll(){
        shoppingHistoryRepository.deleteAll();
    }
    public List<DataShoppingHistory> findAllBySubscriber(int userid){
        DataSubscriber subscriber=subscriberRepository.getOne(userid);
        return shoppingHistoryRepository.findAllBySubscriber(subscriber);
    }


//    public void addProductToBag(int userID, Integer storeID, Integer productID, Integer quantity) {
//        DataSubscriber user = subscriberRepository.getOne(userID);
//        DataStore store = storeRepository.getOne(storeID);
//        DataProduct product = productRepository.getOne(productID);
//        DataShoppingBagCart bag = user.FindBag(storeID);
//        if(bag==null){
//            bag = new DataShoppingBagCart(user, store);
//            shoppingCartRepository.saveAndFlush(bag);
//        }
//        DataShoppingBagProduct dataProduct = new DataShoppingBagProduct(bag, product, quantity);
//        shoppingBagProductRepository.saveAndFlush(dataProduct);
//    }
}
