package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.Keys.DataShoppingBagProductKey;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagProduct;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagSpacialProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingBagSpacialProductRepository extends JpaRepository<DataShoppingBagSpacialProduct, DataShoppingBagProductKey> {
    List<DataShoppingBagSpacialProduct> findAllByShoppingBag(DataShoppingBagCart shoppingBagCart);


}
