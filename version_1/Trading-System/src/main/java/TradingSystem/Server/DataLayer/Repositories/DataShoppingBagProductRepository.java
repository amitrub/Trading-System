package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagProduct;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.DataShoppingBagProductKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataShoppingBagProductRepository extends JpaRepository<DataShoppingBagProduct, DataShoppingBagProductKey> {
    List<DataShoppingBagProduct> findAllByShoppingBag(DataShoppingBagCart shoppingBagCart);
}
