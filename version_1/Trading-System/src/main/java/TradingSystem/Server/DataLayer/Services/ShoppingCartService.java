package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagKey;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagProduct;
import TradingSystem.Server.DataLayer.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
@Service
@Transactional
public class ShoppingCartService {

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

    public void addProductToBag(int userID, Integer storeID, Integer productID, Integer quantity) {
        DataSubscriber user = subscriberRepository.getOne(userID);
        DataStore store = storeRepository.getOne(storeID);
        DataProduct product = productRepository.getOne(productID);
        DataShoppingBagCart bag = user.FindBag(storeID);
        if(bag==null){
            bag = new DataShoppingBagCart(user, store);
            shoppingCartRepository.saveAndFlush(bag);
        }
        DataShoppingBagProduct dataProduct = new DataShoppingBagProduct(bag, product, quantity);
        shoppingBagProductRepository.saveAndFlush(dataProduct);
    }

    public void setBagFinalPrice(int userID, Integer storeID, Double finalPrice) {
        DataShoppingBagCart bag = shoppingCartRepository.getOne(new DataShoppingBagKey(userID, storeID));
        bag.setFinalPrice(finalPrice);
        shoppingCartRepository.saveAndFlush(bag);
    }

//    public DataShoppingCart findDummyShoppingCartByUserID(int UserId){
//        return shoppingCartRepository.findDummyShoppingCartByUserID(UserId);
//    }
//
//    public void addShoppingCart(DataShoppingCart shoppingCart){
//        shoppingCartRepository.saveAndFlush(shoppingCart);
//    }
}
