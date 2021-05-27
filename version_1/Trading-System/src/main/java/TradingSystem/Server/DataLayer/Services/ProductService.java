package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//import com.journaldev.hibernate.util.HibernateUtil;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
  //  SessionFactory sessionFactory = HibernateUtil.getSessionFactory();


    public ProductService() {

    }

    public DataProduct AddProduct(DataProduct product) {
        return productRepository.saveAndFlush(product);
    }

    //TODO make it transaction
    public void editProduct(DataProduct product){
//        Session session = null;
//        Transaction tx = null;
//
//        try {
//            session = sessionFactory.openSession();
//            tx = session.beginTransaction();
            productRepository.deleteById(product.getProductID());
            productRepository.saveAndFlush(product);
//            tx.commit();
//
//        }catch (Exception ex) {
//            ex.printStackTrace();
//            tx.rollback();
//        }
//        finally {session.close();}
    }

    public List<DataProduct> findDummyProductByStoreID(int storeId){
        return productRepository.findDummyProductByStoreID(storeId);
    }
    public List<DataProduct> findDummyProductByName(String storeName, double minprice, double maxprice){
        if(maxprice!=-1 && minprice!=-1){
            return productRepository.findDummyProductByCategoryAndPriceBetween(storeName,minprice,minprice);
        }
        else{
            return productRepository.findDummyProductByProductName(storeName);
        }
    }

    public List<DataProduct> findDummyProductByCategory(String category, int minprice, int maxprice){
        if(maxprice!=-1 && minprice!=-1){
            return productRepository.findDummyProductByCategoryAndPriceBetween(category,minprice,minprice);
        }
        else{
            return productRepository.findDummyProductByCategory(category);
        }
    }
}
