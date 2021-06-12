package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataComment;
import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Repositories.CommentRepository;
import TradingSystem.Server.DataLayer.Repositories.ProductRepository;
import TradingSystem.Server.DataLayer.Repositories.StoreRepository;
import TradingSystem.Server.DataLayer.Repositories.SubscriberRepository;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;

import java.util.List;
import java.util.Optional;

//import com.journaldev.hibernate.util.HibernateUtil;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    SubscriberRepository subscriberRepository;
    @Autowired
    CommentRepository commentRepository;


    public ProductService() {

    }
    @org.springframework.transaction.annotation.Transactional(timeout = 20)
    public Response AddProductToStore(int storeID, String productName,
                                         String category, Double price, int quantity) {
        try {
            DataProduct product = new DataProduct(productName, category, price, quantity);
            DataStore store = storeRepository.getOne(storeID);
            product.setStore(store);
            DataProduct dataProduct =  productRepository.saveAndFlush(product);
            Response response=new Response(false, "Product was added successfully");
            response.AddProductID(dataProduct.getProductID());
            return response;
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Could not add the store on the limit time");
        }
    }
    @org.springframework.transaction.annotation.Transactional(timeout = 20)
    public Response RemoveProduct(int productId) {
        try {
            Optional<DataProduct> product=productRepository.findById(productId);
            if(!product.isPresent()){
                return new Response(true,"Product id doesn't exist");
            }
            productRepository.removeDataProductByProductID(productId);
            return new Response("Removed product successfully");
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Could not add the store on the limit time");
        }
    }

    public Response findDummyProductByStore(Integer storeID){
        Optional<DataStore> store = storeRepository.findById(storeID);
        if(!store.isPresent()){
           return new Response(true,"Could not found store id");
        }
        List<DataProduct> products= productRepository.findDummyProductByStore(store.get());
        Response response=new Response(false," ");
        response.AddDBProductsList(products);
        return response;
    }

    @org.springframework.transaction.annotation.Transactional(timeout = 20)
    public Response setQuantity(Integer productID, int newQuantity){
        try {
            Optional<DataProduct> product = productRepository.findById(productID);
            if(!product.isPresent()){
                return new Response(true,"Could not find product");
            }
            product.get().setQuantity(newQuantity);
            productRepository.saveAndFlush(product.get());
            return new Response(false," ");
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Could not add the store on the limit time");
        }
    }


    public void deleteAll(){
        productRepository.deleteAll();
    }

    @org.springframework.transaction.annotation.Transactional(timeout = 20)
    public Response editProductDetails(Integer productID, String productName, Double price, String category, Integer quantity) {
        try {
            Optional<DataProduct> product_opt = productRepository.findById(productID);
            if(!product_opt.isPresent()){
                return new Response(true,"Could not find product");
            }
            DataProduct product=product_opt.get();
            product.setProductName(productName);
            product.setPrice(price);
            product.setCategory(category);
            product.setQuantity(quantity);
            productRepository.saveAndFlush(product);
            return new Response(false," ");
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Could not add the store on the limit time");
        }
    }
    @org.springframework.transaction.annotation.Transactional(timeout = 20)
    public Response addCommentToProduct(Integer productID, Integer userID, String comment) {
        try {
            Optional<DataProduct> product = productRepository.findById(productID);
            Optional<DataSubscriber> subscriber = subscriberRepository.findById(userID);
            if(!product.isPresent() || !subscriber.isPresent()){
                return new Response(true, "Could not find product or user");
            }
            DataComment comment1 = new DataComment(subscriber.get(), product.get(), comment);
            commentRepository.saveAndFlush(comment1);
            return new Response(false," ");
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Could not add the store on the limit time");
        }
    }


    @org.springframework.transaction.annotation.Transactional(timeout = 20)
    public Response findAllByCategoryAndProductNameAndPriceBetween(String name,String category, int min, int max){
        try {
            List<DataProduct> products=productRepository.findAllByCategoryAndProductNameAndPriceBetween(name,category,min,max);
            Response response=new Response(false, " ");
            response.AddDBProductsList(products);
            return response;
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Time limit is over, upload to db failed");
        }
    }
}
