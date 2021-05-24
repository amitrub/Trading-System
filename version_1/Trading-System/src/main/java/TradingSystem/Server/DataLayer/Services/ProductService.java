package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Repositories.ProductRepository;
import TradingSystem.Server.DataLayer.Repositories.ShoppingHistoryRepository;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public ProductService() {

    }

    public DummyProduct ShoppingHistory(DummyProduct product) {
        return productRepository.saveAndFlush(product);
    }

    public List<DummyProduct> findDummyProductByStoreID(int storeId){
        return productRepository.findDummyProductByStoreID(storeId);
    }
}
