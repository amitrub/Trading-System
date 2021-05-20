package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingHistoryRepository extends JpaRepository<DummyShoppingHistory,Integer> {
}

