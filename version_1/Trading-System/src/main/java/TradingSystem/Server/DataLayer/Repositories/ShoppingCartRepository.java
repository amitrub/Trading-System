package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<DummyShoppingCart,Integer> {

    List<DummyShoppingCart> findDummyShoppingCartByUserID(int UserId);
}
