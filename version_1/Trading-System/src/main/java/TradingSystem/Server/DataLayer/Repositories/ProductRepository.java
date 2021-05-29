package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<DataProduct,Integer> {


    List<DataProduct> findDummyProductByStore(DataStore store);

//    List<DataProduct> findDummyProductByProductName(String productName);
//
//    List<DataProduct> findDummyProductByCategory(String category);
//
//    List<DataProduct> findDummyProductByCategoryAndPriceBetween(String category, double minprice, double maxprice);

    List<DataProduct> findAllByCategoryAndProductNameAndPriceBetween(String name, String category, double minprice, double maxprice);

    void removeDataProductByProductID(int id);

}

