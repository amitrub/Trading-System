package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<DataProduct,Integer> {


    List<DataProduct> findDummyProductByStoreID(int storeid);

    List<DataProduct> findDummyProductByProductName(String productName);

    List<DataProduct> findDummyProductByCategory(String category);

    List<DataProduct> findDummyProductByCategoryAndPriceBetween(String category, double minprice, double maxprice);

    List<DataProduct> findDummyProductByProductNameAndPriceBetween(String name, double minprice, double maxprice);
}

