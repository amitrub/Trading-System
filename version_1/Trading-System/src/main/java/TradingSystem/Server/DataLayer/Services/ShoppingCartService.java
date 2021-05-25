package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataShoppingCart;
import TradingSystem.Server.DataLayer.Repositories.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
@Service
@Transactional
public class ShoppingCartService {

    @Autowired
    ShoppingCartRepository shoppingCartRepository;
    public List<DataShoppingCart> findDummyShoppingCartByUserID(int UserId){
        return shoppingCartRepository.findDummyShoppingCartByUserID(UserId);
    }
}
