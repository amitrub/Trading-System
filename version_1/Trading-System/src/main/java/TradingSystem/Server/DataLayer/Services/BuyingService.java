package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataBuyingPolicy;
import TradingSystem.Server.DataLayer.Repositories.BuyingRepository;
import TradingSystem.Server.DataLayer.Repositories.ExpressionRepository;
import TradingSystem.Server.DataLayer.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BuyingService {

    @Autowired
    BuyingRepository buyingRepository;
    @Autowired
    ExpressionRepository expressionRepository;

    //TODO make to transction
    public void AddBuyingPolicy(DataBuyingPolicy buyingPolicy){
        expressionRepository.saveAndFlush(buyingPolicy.getExpression());
        buyingRepository.saveAndFlush(buyingPolicy);
    }
}
