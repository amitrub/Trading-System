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

import java.util.List;

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

    public int AddProductToStore(int storeID, String productName,
                                         String category, Double price, int quantity) {
        DataProduct product = new DataProduct(productName, category, price, quantity);
        DataStore store = storeRepository.getOne(storeID);
        product.setStore(store);
        DataProduct dataProduct =  productRepository.saveAndFlush(product);
        return dataProduct.getProductID();
    }

    public void RemoveProduct(int productId) {
        productRepository.removeDataProductByProductID(productId);
    }

    public List<DataProduct> findDummyProductByStore(Integer storeID){
        DataStore store = storeRepository.getOne(storeID);
        return productRepository.findDummyProductByStore(store);
    }

    public void setQuantity(Integer productID, int newQuantity){
        DataProduct product = productRepository.getOne(productID);
        product.setQuantity(newQuantity);
        productRepository.saveAndFlush(product);
    }


    public void deleteAll(){
        productRepository.deleteAll();
    }

    public void editProductDetails(Integer productID, String productName, Double price, String category, Integer quantity) {
        DataProduct product = productRepository.getOne(productID);
        product.setProductName(productName);
        product.setPrice(price);
        product.setCategory(category);
        product.setQuantity(quantity);
        productRepository.saveAndFlush(product);
    }

    public void addCommentToProduct(Integer productID, Integer userID, String comment) {
        DataProduct product = productRepository.getOne(productID);
        DataSubscriber subscriber = subscriberRepository.getOne(userID);
        DataComment comment1 = new DataComment(subscriber, product, comment);
        commentRepository.saveAndFlush(comment1);
    }



    public List<DataProduct> findAllByCategoryAndProductNameAndPriceBetween(String name,String category, int min, int max){
        return productRepository.findAllByCategoryAndProductNameAndPriceBetween(name,category,min,max);
    }
}
