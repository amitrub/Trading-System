package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.Bid.DataBid;
import TradingSystem.Server.DataLayer.Data_Modules.Bid.DataBidManagerApproves;
import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.BidManagerKey;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserProductKey;
import TradingSystem.Server.DataLayer.Repositories.*;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public Response AddBidForProduct(int productID, int userID, Integer productPrice, Integer quantity, ConcurrentHashMap<Integer,Boolean> managerList) {
        Optional<DataSubscriber> subscriber = subscriberRepository.findById(userID);
        if(!subscriber.isPresent()){
            return new Response(true,"Could not find subscriber");
        }
        Optional<DataProduct> product = productRepository.findById(productID);
        if(!product.isPresent()){
            return new Response(true,"Could not find product");
        }
        DataBid bid = new DataBid(subscriber.get(), product.get(), productPrice, quantity);
        bidRepository.saveAndFlush(bid);
        for (Integer managerID: managerList.keySet()){
            boolean approves = managerList.get(managerID);
            Optional<DataSubscriber> manager = subscriberRepository.findById(managerID);
            if(!manager.isPresent()){
                return new Response(true,"Could not find manager");
            }
            DataBidManagerApproves bidManagerApproves = new DataBidManagerApproves(bid, manager.get(), approves);
            bidManagerApprovesRepository.saveAndFlush(bidManagerApproves);
        }
        return new Response("Bid was added successfully");
    }

    @Transactional(rollbackFor = { Exception.class }, timeout = 20)
    public Response RemoveBid(int productID, int userID) {
        Optional<DataBid> bid = bidRepository.findById(new UserProductKey(userID, productID));
        if(!bid.isPresent()){
            return new Response(true,"Could not find bid");
        }
        for (DataBidManagerApproves managerApproves: bid.get().getOwnerAndManagerApprovals()){
            bidManagerApprovesRepository.delete(managerApproves);
        }
        bidRepository.delete(bid.get());
        return new Response("Bid was deleted successfully");
    }

    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public Response approveBid(int productID, int userID, int managerID) {
        Optional<DataBid> bid = bidRepository.findById(new UserProductKey(userID, productID));
        if(!bid.isPresent()){
            return new Response(true,"Could not find bid");
        }
        Optional<DataSubscriber> manager = subscriberRepository.findById(managerID);
        if(!manager.isPresent()){
            return new Response(true,"Could not find manager");
        }
        DataBidManagerApproves managerApproves = bidManagerApprovesRepository.getOne(new BidManagerKey(bid.get().getKey(), managerID));
        if(managerApproves == null){
            managerApproves = new DataBidManagerApproves(bid.get(), manager.get(), true);
        }
        else {
            managerApproves.setApproves(true);
        }
        bidManagerApprovesRepository.saveAndFlush(managerApproves);
        return new Response("Bid was approve successfully");
    }

    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public Response UpdateBidOwnerList(int productID, int userID, ConcurrentHashMap<Integer, Boolean> managerList) {
        Optional<DataBid> bid = bidRepository.findById(new UserProductKey(userID, productID));
        if(!bid.isPresent()){
            return new Response(true,"Could not find bid");
        }
        for (Integer managerID: managerList.keySet()) {
            boolean approves = managerList.get(managerID);
            Optional<DataSubscriber> manager = subscriberRepository.findById(managerID);
            if(!manager.isPresent()){
                return new Response(true,"Could not find manager");
            }
            DataBidManagerApproves managerApproves = bidManagerApprovesRepository.getOne(new BidManagerKey(bid.get().getKey(), managerID));
            if(managerApproves == null){
                managerApproves = new DataBidManagerApproves(bid.get(), manager.get(), approves);
                bidManagerApprovesRepository.saveAndFlush(managerApproves);
            }
        }
        return new Response("Update Owner List successfully");
    }

    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public Response initialAprrovment(int productID, int userID) {
        Optional<DataBid> bid = bidRepository.findById(new UserProductKey(userID, productID));
        if(!bid.isPresent()){
            return new Response(true,"Could not find bid");
        }
        for (DataBidManagerApproves managerApproves: bid.get().getOwnerAndManagerApprovals()){
            managerApproves.setApproves(false);
            bidManagerApprovesRepository.saveAndFlush(managerApproves);
        }
        return new Response("initial Aprrovment successfully");
    }

    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public Response setBidPrice(int productID, int userID, Integer price) {
        Optional<DataBid> bid = bidRepository.findById(new UserProductKey(userID, productID));
        if(!bid.isPresent()){
            return new Response(true,"Could not find bid");
        }
        bid.get().setPrice(price);
        bidRepository.saveAndFlush(bid.get());
        return new Response("set Bid Price successfully");
    }

    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public Response setBidQuantity(int productID, int userID, Integer quantity) {
        Optional<DataBid> bid = bidRepository.findById(new UserProductKey(userID, productID));
        if(!bid.isPresent()){
            return new Response(true,"Could not find bid");
        }
        bid.get().setQuantity(quantity);
        bidRepository.saveAndFlush(bid.get());
        return new Response("set Bid Quantity successfully");
    }
    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public List<DataBid> getAllBids(){
        return bidRepository.findAll();
    }
}
