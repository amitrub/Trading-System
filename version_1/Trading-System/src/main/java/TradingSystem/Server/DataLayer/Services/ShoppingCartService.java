package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.DataShoppingBagProductKey;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreKey;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagProduct;
import TradingSystem.Server.DataLayer.Repositories.*;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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

    public Response addProductToBag(int userID, Integer storeID, Integer productID, Integer quantity) {
        Optional<DataSubscriber> user = subscriberRepository.findById(userID);
        Optional<DataStore> store = storeRepository.findById(storeID);
        Optional<DataProduct> product = productRepository.findById(productID);
        if(!user.isPresent() || !store.isPresent() || !product.isPresent()){
            return new Response(true,"could not add product to Bag");
        }
        DataShoppingBagCart bag = user.get().FindBag(storeID);
        if(bag==null){
            bag = new DataShoppingBagCart(user.get(), store.get());
            shoppingCartRepository.saveAndFlush(bag);
        }
        DataShoppingBagProduct dataProduct = new DataShoppingBagProduct(bag, product.get(), quantity);
        shoppingBagProductRepository.saveAndFlush(dataProduct);
        return new Response(false,"Product was added successfully");
    }

    public Response setBagFinalPrice(int userID, Integer storeID, Double finalPrice) {
        DataShoppingBagCart bag = shoppingCartRepository.getOne(new UserStoreKey(userID, storeID));
        bag.setFinalPrice(finalPrice);
        shoppingCartRepository.saveAndFlush(bag);
        return new Response(false,"Set final price was done successfully");
    }

    public Response setBagProductQuantity(int userID, Integer storeID, int productID, Integer quantity) {
        Optional<DataShoppingBagProduct> product = shoppingBagProductRepository.findById(new DataShoppingBagProductKey(new UserStoreKey(userID, storeID), productID));
        if(!product.isPresent()){
            return new Response(true,"Couldn't set Product Quantity");
        }
        product.get().setQuantity(quantity);
        shoppingBagProductRepository.saveAndFlush(product.get());
        return new Response(false,"Set Product Quantity successfully");

    }

    public Response RemoveBagProduct(int userID, Integer storeID, int productID) {
        Optional<DataShoppingBagCart> bag = shoppingCartRepository.findById(new UserStoreKey(userID, storeID));
        Optional<DataShoppingBagProduct> product = shoppingBagProductRepository.findById(new DataShoppingBagProductKey(new UserStoreKey(userID, storeID), productID));
        if(!bag.isPresent() || !product.isPresent()){
            return new Response(true,"Couldn't remove Product");
        }
        bag.get().removeProduct(product.get());
        shoppingCartRepository.saveAndFlush(bag.get());
        return new Response(true,"Successfully removed product");
    }

    public void deleteAll(){
        shoppingCartRepository.deleteAll();
    }

    public Response deleteSubscriberBag(Integer userID, Integer storeID){
        Optional<DataShoppingBagCart> bag_opt = shoppingCartRepository.findById(new UserStoreKey(userID, storeID));
        Optional<DataSubscriber> subscriber_opt = subscriberRepository.findById(userID);
        if(!bag_opt.isPresent() || !subscriber_opt.isPresent()){
            return new Response("Wrong details in user id or store id");
        }
        DataShoppingBagCart bag=bag_opt.get();
        List<DataShoppingBagProduct> productList = shoppingBagProductRepository.findAllByShoppingBag(bag);
        for (DataShoppingBagProduct product: productList){
            bag.removeProduct(product);
        }
        bag = shoppingCartRepository.saveAndFlush(bag);
        setBagFinalPrice(userID,storeID, 0.0);

        DataSubscriber subscriber = subscriberRepository.getOne(userID);
        subscriber.removeShoppingBag(bag);
        subscriberRepository.saveAndFlush(subscriber);
        return new Response(false," ");
    }

    public Response getSubscriberShoppingCart(int userID){
        Optional<DataSubscriber> subscriber = subscriberRepository.findById(userID);
        if(!subscriber.isPresent()){
            return new Response(true,"could not find subscriber");
        }
        List<DataShoppingBagCart> list= shoppingCartRepository.findAllBySubscriber(subscriber.get());
//        for(DataShoppingBagCart shoppingBagCart:list){
//            shoppingBagCart.setProducts(shoppingBagProductRepository.findAllByShoppingBag(shoppingBagCart));
//        }
        Response response=new Response(false," ");
        response.AddDBShoppingCart(list);
        return response;
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
