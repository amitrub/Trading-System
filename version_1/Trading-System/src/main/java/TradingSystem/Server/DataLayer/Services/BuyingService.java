package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DataBuyingPolicy;
import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DBExpression;
import TradingSystem.Server.DataLayer.Repositories.BuyingRepository;
import TradingSystem.Server.DataLayer.Repositories.CompositeExp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BuyingService {

    @Autowired
    BuyingRepository buyingRepository;
    @Autowired
    CompositeExp dataExpCompositeRepository;

    public Optional<DataBuyingPolicy> getBuyingByStore(Integer storeid) throws EntityNotFoundException
    {
        return buyingRepository.findById(storeid);
    }

    //TODO make to transction
    public void AddBuyingPolicy(DataBuyingPolicy buyingPolicy){
        DBExpression dataExpression= buyingPolicy.getExpression();
        dataExpCompositeRepository.saveAndFlush(dataExpression);
        buyingRepository.saveAndFlush(buyingPolicy);
    }
}
