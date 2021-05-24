package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.ServiceLayer.DummyObject.DummyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<DummyUser,Integer> {
}
