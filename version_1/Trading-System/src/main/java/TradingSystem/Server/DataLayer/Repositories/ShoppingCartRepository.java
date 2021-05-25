package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.DataShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<DataShoppingCart,Integer> {

    List<DataShoppingCart> findDummyShoppingCartByUserID(int UserId);
}
