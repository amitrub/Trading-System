package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Repositories.ProductRepository;
import TradingSystem.Server.DataLayer.Repositories.ShoppingCartRepository;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.List;
@Service
@Transactional
public class ShoppingCartService {

    @Autowired
    ShoppingCartRepository shoppingCartRepository;
    public List<DummyShoppingCart> findDummyShoppingCartByUserID(int UserId){
        return shoppingCartRepository.findDummyShoppingCartByUserID(UserId);
    }
}
