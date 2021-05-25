package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Repositories.ProductRepository;
import TradingSystem.Server.DataLayer.Repositories.ShoppingHistoryRepository;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public ProductService() {

    }

    public DummyProduct AddProduct(DummyProduct product) {
        return productRepository.saveAndFlush(product);
    }

    public List<DummyProduct> findDummyProductByStoreID(int storeId){
        return productRepository.findDummyProductByStoreID(storeId);
    }
    public List<DummyProduct> findDummyProductByName(String storeName, double minprice, double maxprice){
        if(maxprice!=-1 && minprice!=-1){
            return productRepository.findDummyProductByCategoryAndPriceBetween(storeName,minprice,minprice);
        }
        else{
            return productRepository.findDummyProductByProductName(storeName);
        }
    }

    public List<DummyProduct> findDummyProductByCategory(String category, int minprice, int maxprice){
        if(maxprice!=-1 && minprice!=-1){
            return productRepository.findDummyProductByCategoryAndPriceBetween(category,minprice,minprice);
        }
        else{
            return productRepository.findDummyProductByCategory(category);
        }
    }
}
