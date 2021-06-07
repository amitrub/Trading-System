package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Repositories.ProductRepository;
import TradingSystem.Server.DataLayer.Repositories.StoreRepository;
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
  //  SessionFactory sessionFactory = HibernateUtil.getSessionFactory();


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

//    //TODO make it transaction
//    public void editProduct(DataProduct product){
////        Session session = null;
////        Transaction tx = null;
////
////        try {
////            session = sessionFactory.openSession();
////            tx = session.beginTransaction();
//            productRepository.deleteById(product.getProductID());
//            productRepository.saveAndFlush(product);
////            tx.commit();
////
////        }catch (Exception ex) {
////            ex.printStackTrace();
////            tx.rollback();
////        }
////        finally {session.close();}
//    }
//
//    public List<DataProduct> findDummyProductByStoreID(int storeId){
//        return productRepository.findDummyProductByStoreID(storeId);
//    }
//    public List<DataProduct> findDummyProductByName(String storeName, double minprice, double maxprice){
//        if(maxprice!=-1 && minprice!=-1){
//            return productRepository.findDummyProductByCategoryAndPriceBetween(storeName,minprice,minprice);
//        }
//        else{
//            return productRepository.findDummyProductByProductName(storeName);
//        }
//    }
//
//    public List<DataProduct> findDummyProductByCategory(String category, int minprice, int maxprice){
//        if(maxprice!=-1 && minprice!=-1){
//            return productRepository.findDummyProductByCategoryAndPriceBetween(category,minprice,minprice);
//        }
//        else{
//            return productRepository.findDummyProductByCategory(category);
//        }
//    }

    public List<DataProduct> findAllByCategoryAndProductNameAndPriceBetween(String name,String category, int min, int max){
        return productRepository.findAllByCategoryAndProductNameAndPriceBetween(name,category,min,max);
    }
}
