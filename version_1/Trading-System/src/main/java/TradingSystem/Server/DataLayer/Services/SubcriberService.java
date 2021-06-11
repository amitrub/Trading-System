package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Repositories.StoreRepository;
import TradingSystem.Server.DataLayer.Repositories.SubscriberRepository;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.Query;
import javax.transaction.Transactional;
import javax.websocket.Session;
import java.util.*;

@Service
@Transactional
public class SubcriberService {

    @Autowired
    SubscriberRepository subscriberRepository;

    @Autowired
    StoreRepository storeRepository;


    public int AddSubscriber(String userName, String password){
        DataSubscriber subscriber = new DataSubscriber(userName,password);
        DataSubscriber ret = subscriberRepository.saveAndFlush(subscriber);
        return ret.getUserID();
    }

    public DataSubscriber GetSubscriber(String userName, String password) {
//        return null;
        DataSubscriber subscriber = subscriberRepository.findByName(userName);
        return subscriber;
    }

    public List<DataSubscriber> getAllSubscribers(){
        List<DataSubscriber> subscribers = subscriberRepository.findAll();
        return subscribers;
    }

    public Optional<DataSubscriber> findSubscriberById(int subscriberid){
        return subscriberRepository.findById(subscriberid);
    }
    public void deleteAll(){
        subscriberRepository.deleteAll();
    }

    public List<DataSubscriber> findAllByStoresManagerContains(int storeid){
        DataStore store=storeRepository.getOne(storeid);
        return subscriberRepository.findAllByStoresManagerContains(store);
    }
    public List<DataSubscriber> findAllByStoresOwnedContains(int storeid){
        DataStore store=storeRepository.getOne(storeid);
        return subscriberRepository.findAllByStoresOwnerContains(store);
    }

    public HashMap<Date,Integer> getAllSubscribersWeek(){
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
            List<DataSubscriber> subscribers=subscriberRepository.findAllByDateBetween(start,end);
            hashMap.put(start,subscribers.size());
        }
        return hashMap;
    }
}
