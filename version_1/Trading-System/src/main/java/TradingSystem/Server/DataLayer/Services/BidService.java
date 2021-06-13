package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.Bid.DataBid;
import TradingSystem.Server.DataLayer.Data_Modules.Bid.DataBidManagerApproves;
import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.BidManagerKey;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserProductKey;
import TradingSystem.Server.DataLayer.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class BidService {

    @Autowired
    SubscriberRepository subscriberRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    BidRepository bidRepository;
    @Autowired
    BidManagerApprovesRepository bidManagerApprovesRepository;


    public void AddBidForProduct(int productID, int userID, Integer productPrice,Integer quantity, ConcurrentHashMap<Integer,Boolean> managerList) {
        DataSubscriber subscriber = subscriberRepository.getOne(userID);
        DataProduct product = productRepository.getOne(productID);
        DataBid bid = new DataBid(subscriber, product, productPrice, quantity);
        bidRepository.saveAndFlush(bid);
        for (Integer managerID: managerList.keySet()){
            boolean approves = managerList.get(managerID);
            DataSubscriber manager = subscriberRepository.getOne(managerID);
            DataBidManagerApproves bidManagerApproves = new DataBidManagerApproves(bid, manager, approves);
            bidManagerApprovesRepository.saveAndFlush(bidManagerApproves);
        }
    }

    public void RemoveBid(int productID, int userID) {
        DataBid bid = bidRepository.getOne(new UserProductKey(userID, productID));
        for (DataBidManagerApproves managerApproves: bid.getOwnerAndManagerApprovals()){
            bidManagerApprovesRepository.delete(managerApproves);
        }
        bidRepository.delete(bid);
    }

    public void approveBid(int productID, int userID, int managerID) {
        DataBid bid = bidRepository.getOne(new UserProductKey(userID, productID));
        DataSubscriber manager = subscriberRepository.getOne(managerID);
        DataBidManagerApproves managerApproves = bidManagerApprovesRepository.getOne(new BidManagerKey(bid.getKey(), managerID));
        if(managerApproves == null){
            managerApproves = new DataBidManagerApproves(bid, manager, true);
        }
        else {
            managerApproves.setApproves(true);
        }
        bidManagerApprovesRepository.saveAndFlush(managerApproves);
    }

    public void UpdateOwnerList(int productID, int userID, ConcurrentHashMap<Integer, Boolean> managerList) {
        DataBid bid = bidRepository.getOne(new UserProductKey(userID, productID));
        for (Integer managerID: managerList.keySet()) {
            boolean approves = managerList.get(managerID);
            DataSubscriber manager = subscriberRepository.getOne(managerID);
            DataBidManagerApproves managerApproves = bidManagerApprovesRepository.getOne(new BidManagerKey(bid.getKey(), managerID));
            if(managerApproves == null){
                managerApproves = new DataBidManagerApproves(bid, manager, approves);
                bidManagerApprovesRepository.saveAndFlush(managerApproves);
            }
        }
    }

    public void initialAprrovment(int productID, int userID) {
        DataBid bid = bidRepository.getOne(new UserProductKey(userID, productID));
        for (DataBidManagerApproves managerApproves: bid.getOwnerAndManagerApprovals()){
            managerApproves.setApproves(false);
            bidManagerApprovesRepository.saveAndFlush(managerApproves);
        }
    }

    public void setBidPrice(int productID, int userID, Integer price) {
        DataBid bid = bidRepository.getOne(new UserProductKey(userID, productID));
        bid.setPrice(price);
        bidRepository.saveAndFlush(bid);
    }

    public void setBidQuantity(int productID, int userID, Integer quantity) {
        DataBid bid = bidRepository.getOne(new UserProductKey(userID, productID));
        bid.setQuantity(quantity);
        bidRepository.saveAndFlush(bid);
    }

    public List<DataBid> getAllBids(){
        return bidRepository.findAll();
    }
}
