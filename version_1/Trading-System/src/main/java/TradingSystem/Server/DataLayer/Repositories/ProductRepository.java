package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<DummyProduct,Integer> {
}
