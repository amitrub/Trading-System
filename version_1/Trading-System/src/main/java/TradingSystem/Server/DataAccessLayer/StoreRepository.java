package TradingSystem.Server.DataAccessLayer;

import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface StoreRepository extends JpaRepository<DummyStore, Integer> {

    DummyStore findDummyStoreById(int Id);
}
