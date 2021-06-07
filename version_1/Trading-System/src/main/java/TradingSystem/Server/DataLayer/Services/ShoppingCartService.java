package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.DataShoppingBagProductKey;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreKey;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagProduct;
import TradingSystem.Server.DataLayer.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
    ShoppingBagProductRepository shoppingBagProductRepository;

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
        DataShoppingBagCart bag = shoppingCartRepository.getOne(new UserStoreKey(userID, storeID));
        bag.setFinalPrice(finalPrice);
        shoppingCartRepository.saveAndFlush(bag);
    }

    public void setBagProductQuantity(int userID, Integer storeID, int productID, Integer quantity) {
        DataShoppingBagProduct product = shoppingBagProductRepository.getOne(new DataShoppingBagProductKey(new UserStoreKey(userID, storeID), productID));
        product.setQuantity(quantity);
        shoppingBagProductRepository.saveAndFlush(product);
    }

    public void RemoveBagProduct(int userID, Integer storeID, int productID) {
        DataShoppingBagCart bag = shoppingCartRepository.getOne(new UserStoreKey(userID, storeID));
        DataShoppingBagProduct product = shoppingBagProductRepository.getOne(new DataShoppingBagProductKey(new UserStoreKey(userID, storeID), productID));
        bag.removeProduct(product);
        shoppingCartRepository.saveAndFlush(bag);
    }

    public void deleteAll(){
        shoppingCartRepository.deleteAll();
    }

    public void deleteSubscriberBag(Integer userID, Integer storeID){
        shoppingCartRepository.deleteById(new UserStoreKey(userID, storeID));
    }

    public List<DataShoppingBagCart> getSubscriberShoppingCart(int userID){
        DataSubscriber subscriber = subscriberRepository.getOne(userID);
        List<DataShoppingBagCart> list= shoppingCartRepository.findAllBySubscriber(subscriber);
//        for(DataShoppingBagCart shoppingBagCart:list){
//            shoppingBagCart.setProducts(shoppingBagProductRepository.findAllByShoppingBag(shoppingBagCart));
//        }
        return list;
    }
    public List<DataShoppingBagCart> getAllcardsBystore(int storeid){
        DataStore store=storeRepository.getOne(storeid);
        return shoppingCartRepository.findAllByStore(store);
    }
//    public DataShoppingCart findDummyShoppingCartByUserID(int UserId){
//        return shoppingCartRepository.findDummyShoppingCartByUserID(UserId);
//    }
//
//    public void addShoppingCart(DataShoppingCart shoppingCart){
//        shoppingCartRepository.saveAndFlush(shoppingCart);
//    }
}
