package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.Sales.DataDiscountPolicy;
import TradingSystem.Server.DataLayer.Repositories.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class DiscountPolicyService {

    @Autowired
    DiscountRepository discountPolicyService;

    public void AddDiscountPolicy(DataDiscountPolicy dataDiscountPolicy){
        discountPolicyService.saveAndFlush(dataDiscountPolicy);
    }
}
