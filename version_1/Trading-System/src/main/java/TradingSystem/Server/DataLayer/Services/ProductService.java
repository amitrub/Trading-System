package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public ProductService() {

    }

    public DataProduct AddProduct(DataProduct product) {
        return productRepository.saveAndFlush(product);
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
