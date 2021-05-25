package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<DummyProduct,Integer> {

    List<DummyProduct> findDummyProductByStoreID(int storeid);

    List<DummyProduct> findDummyProductByProductName(String productName);

    List<DummyProduct> findDummyProductByCategory(String category);

    List<DummyProduct> findDummyProductByCategoryAndPriceBetween(String category, double minprice, double maxprice);

    List<DummyProduct> findDummyProductByProductNameAndPriceBetween(String name, double minprice, double maxprice);
}
