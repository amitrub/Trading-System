package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DBExpression;
import TradingSystem.Server.DataLayer.Data_Modules.Sales.DBSale;
import TradingSystem.Server.DataLayer.Data_Modules.Sales.DBSaleExpression;
import TradingSystem.Server.DataLayer.Data_Modules.Sales.DataDiscountPolicy;
import TradingSystem.Server.DataLayer.Repositories.CompositeExp;
import TradingSystem.Server.DataLayer.Repositories.DBSaleExpRepository;
import TradingSystem.Server.DataLayer.Repositories.DiscountRepository;
import TradingSystem.Server.DataLayer.Repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class DiscountPolicyService {

    @Autowired
    DiscountRepository discountPolicyService;
    @Autowired
    SaleRepository saleRepository;
    @Autowired
    DBSaleExpRepository saleExpRepository;

    //TODO- make transaction
    public void AddDiscountPolicy(DataDiscountPolicy dataDiscountPolicy){
        DBSale sale= dataDiscountPolicy.getSale();
        DBSaleExpression expression=sale.getExpression();
        saleExpRepository.saveAndFlush(expression);
        saleRepository.saveAndFlush(sale);
        discountPolicyService.saveAndFlush(dataDiscountPolicy);
    }

    public Optional<DataDiscountPolicy> getDiscountByStore(Integer storeid) throws DataAccessException {
        return discountPolicyService.findById(storeid);
    }
}
