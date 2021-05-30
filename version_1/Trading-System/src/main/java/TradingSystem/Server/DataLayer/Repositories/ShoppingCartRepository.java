package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreProductKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<DataShoppingBagCart, UserStoreProductKey> {

    @Override
    void deleteById(UserStoreProductKey userStoreProductKey);

    List<DataShoppingBagCart> findAllBySubscriber(DataSubscriber subscriber);

    List<DataShoppingBagCart> findAllByStore(DataStore store);

}
