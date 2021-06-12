package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory.DataHistoryProduct;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory.DataShoppingHistory;
import TradingSystem.Server.DataLayer.Repositories.*;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;

import javax.transaction.Transactional;
import java.util.*;

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
    ShoppingBagProductRepository shoppingBagProductRepository;

    @org.springframework.transaction.annotation.Transactional(timeout = 20)
    public Response addHistoryToStoreAndUser(ShoppingHistory shoppingHistory){
        try {
            Integer userID = shoppingHistory.getUserID();
            Integer storeID = shoppingHistory.getStoreID();
            Date date = shoppingHistory.getDate();
            Double finalPrice = shoppingHistory.getFinalPrice();
            DataSubscriber user = null;
            if (userID>=1){
                user = subscriberRepository.getOne(userID);
            }
            Optional<DataStore> store = storeRepository.findById(storeID);
            if(!store.isPresent()){
                return new Response(true,"Could not add history");
            }
            DataShoppingHistory dataShoppingHistory = new DataShoppingHistory(user, store.get(), date, finalPrice);

            List<Product> products = shoppingHistory.getProducts();
            for (Product p : products){
                DataHistoryProduct historyProduct = new DataHistoryProduct(p);
                dataShoppingHistory.addProduct(historyProduct);
            }
            shoppingHistoryRepository.save(dataShoppingHistory);
            return new Response(true,"History wad added successfully");
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Time limit is over, upload to db failed");
        }
    }

    public void deleteAll(){
        shoppingHistoryRepository.deleteAll();
    }

    public List<DataShoppingHistory> findAllBySubscriber(int userid){
        DataSubscriber subscriber=subscriberRepository.getOne(userid);
        return shoppingHistoryRepository.findAllBySubscriber(subscriber);
    }

    public List<DataShoppingHistory> findAllByStore(int storeid){
        DataStore store=storeRepository.getOne(storeid);
        return shoppingHistoryRepository.findAllByStore(store);
    }
    public HashMap<Date,Integer> getAllShoppingHistoriesWeek(){
        HashMap<Date,Integer> hashMap=new HashMap<>();
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i);
        //   Date start = c.getTime();
        for(int j=0;j<=6;j++){
            Date start = c.getTime();
            c.add(Calendar.DATE, 1);
            Date end = c.getTime();
            List<DataShoppingHistory> shoppingHistories=shoppingHistoryRepository.findAllByDateBetween(start,end);
            hashMap.put(start,shoppingHistories.size());
        }
        return hashMap;
    }
    public HashMap<Date,Integer> getAllMoneyWeek(){
        HashMap<Date,Integer> hashMap=new HashMap<>();
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i);
        //   Date start = c.getTime();
        for(int j=0;j<=6;j++){
            Date start = c.getTime();
            c.add(Calendar.DATE, 1);
            Date end = c.getTime();
            List<DataShoppingHistory> shoppingHistories=shoppingHistoryRepository.findAllByDateBetween(start,end);
            int sum=0;
            for(DataShoppingHistory dataShoppingHistory: shoppingHistories){
                sum+=dataShoppingHistory.getprice();
            }
            hashMap.put(start,sum);
        }
        return hashMap;
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
