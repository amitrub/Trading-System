package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.DataShoppingBagProductKey;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreKey;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagProduct;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagSpacialProduct;
import TradingSystem.Server.DataLayer.Repositories.*;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;

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
    @Autowired
    ShoppingBagSpacialProductRepository shoppingBagSpacialProductRepository;

    @org.springframework.transaction.annotation.Transactional(timeout = 20)
    public Response addProductToBag(int userID, Integer storeID, Integer productID, Integer quantity) {
        try {
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
        catch (UnexpectedRollbackException e){
            return new Response(true," Time limit is over, upload to db failed");
        }

    }
    @org.springframework.transaction.annotation.Transactional(timeout = 20)
    public Response setBagFinalPrice(int userID, Integer storeID, Double finalPrice) {
        try {
            DataShoppingBagCart bag = shoppingCartRepository.getOne(new UserStoreKey(userID, storeID));
            bag.setFinalPrice(finalPrice);
            shoppingCartRepository.saveAndFlush(bag);
            return new Response(false,"Set final price was done successfully");
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Time limit is over, upload to db failed");
        }
    }

    @org.springframework.transaction.annotation.Transactional(timeout = 20)
    public Response setBagProductQuantity(int userID, Integer storeID, int productID, Integer quantity) {
        try {
            Optional<DataShoppingBagProduct> product = shoppingBagProductRepository.findById(new DataShoppingBagProductKey(new UserStoreKey(userID, storeID), productID));
            if(!product.isPresent()){
                return new Response(true,"Couldn't set Product Quantity");
            }
            product.get().setQuantity(quantity);
            shoppingBagProductRepository.saveAndFlush(product.get());
            return new Response(false,"Set Product Quantity successfully");
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Time limit is over, upload to db failed");
        }
    }
    @org.springframework.transaction.annotation.Transactional(timeout = 20)
    public Response RemoveBagProduct(int userID, Integer storeID, int productID) {
        try {
            Optional<DataShoppingBagCart> bag = shoppingCartRepository.findById(new UserStoreKey(userID, storeID));
            Optional<DataShoppingBagProduct> product = shoppingBagProductRepository.findById(new DataShoppingBagProductKey(new UserStoreKey(userID, storeID), productID));
            if(!bag.isPresent() || !product.isPresent()){
                return new Response(true,"Couldn't remove Product");
            }
            bag.get().removeProduct(product.get());
            shoppingCartRepository.saveAndFlush(bag.get());
            return new Response(true,"Successfully removed product");
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Time limit is over, upload to db failed");
        }
    }

    public void addSpacialProductToBag(int userID, Integer storeID, Integer productID, Integer quantity, int price) {
        DataSubscriber user = subscriberRepository.getOne(userID);
        DataStore store = storeRepository.getOne(storeID);
        DataProduct product = productRepository.getOne(productID);
        DataShoppingBagCart bag = user.FindBag(storeID);
        if(bag==null){
            bag = new DataShoppingBagCart(user, store);
            shoppingCartRepository.saveAndFlush(bag);
        }
        DataShoppingBagSpacialProduct spacialProduct = new DataShoppingBagSpacialProduct(bag, product, quantity, price);
        shoppingBagSpacialProductRepository.saveAndFlush(spacialProduct);
    }

    public void setBagSpacialFinalPrice(int userID, Integer storeID, int finalPrice) {
        DataShoppingBagCart bag = shoppingCartRepository.getOne(new UserStoreKey(userID, storeID));
        bag.setFinalPriceSpacial(finalPrice);
        shoppingCartRepository.saveAndFlush(bag);
    }



    public void RemoveBagSpacialProduct(int userID, Integer storeID, int productID) {
        DataShoppingBagCart bag = shoppingCartRepository.getOne(new UserStoreKey(userID, storeID));
        DataShoppingBagSpacialProduct product = shoppingBagSpacialProductRepository.getOne(new DataShoppingBagProductKey(new UserStoreKey(userID, storeID), productID));
        bag.removeSpacialProduct(product);
        shoppingCartRepository.saveAndFlush(bag);
    }



    public void deleteAll(){
        shoppingCartRepository.deleteAll();
    }

    @org.springframework.transaction.annotation.Transactional(timeout = 20)
    public Response deleteSubscriberBag(Integer userID, Integer storeID){
        try {
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
            List<DataShoppingBagSpacialProduct> spacialProductList = shoppingBagSpacialProductRepository.findAllByShoppingBag(bag);
            for (DataShoppingBagSpacialProduct product: spacialProductList){
                bag.removeSpacialProduct(product);
            }
            bag = shoppingCartRepository.saveAndFlush(bag);
            setBagFinalPrice(userID,storeID, 0.0);
            setBagSpacialFinalPrice(userID,storeID, 0);


            DataSubscriber subscriber = subscriberRepository.getOne(userID);
            subscriber.removeShoppingBag(bag);
            subscriberRepository.saveAndFlush(subscriber);
            return new Response(false," ");
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Time limit is over, upload to db failed");
        }
    }

    @org.springframework.transaction.annotation.Transactional(timeout = 20)
    public Response getSubscriberShoppingCart(int userID){
        try {
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
        catch (UnexpectedRollbackException e){
            return new Response(true," Time limit is over, upload to db failed");
        }
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
