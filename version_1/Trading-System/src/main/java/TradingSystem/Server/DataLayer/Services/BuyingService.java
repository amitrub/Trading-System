package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DataBuyingPolicy;
import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DBExpression;
import TradingSystem.Server.DataLayer.Repositories.BuyingRepository;
import TradingSystem.Server.DataLayer.Repositories.CompositeExp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BuyingService {

    @Autowired
    BuyingRepository buyingRepository;
    @Autowired
    CompositeExp dataExpCompositeRepository;
    //TODO make to transction
    public void AddBuyingPolicy(DataBuyingPolicy buyingPolicy){
        DBExpression dataExpression= buyingPolicy.getExpression();
        dataExpCompositeRepository.saveAndFlush(dataExpression);
        buyingRepository.saveAndFlush(buyingPolicy);
    }
}
