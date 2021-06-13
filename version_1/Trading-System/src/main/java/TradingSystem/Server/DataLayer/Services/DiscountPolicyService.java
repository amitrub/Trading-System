package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.Sales.DBSale;
import TradingSystem.Server.DataLayer.Data_Modules.Sales.DBSaleExpression;
import TradingSystem.Server.DataLayer.Data_Modules.Sales.DataDiscountPolicy;
import TradingSystem.Server.DataLayer.Repositories.DBSaleExpRepository;
import TradingSystem.Server.DataLayer.Repositories.DiscountRepository;
import TradingSystem.Server.DataLayer.Repositories.SaleRepository;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public Response AddDiscountPolicy(DataDiscountPolicy dataDiscountPolicy){
        try {
            DBSale sale= dataDiscountPolicy.getSale();
            DBSaleExpression expression=sale.getExpression();
            saleExpRepository.saveAndFlush(expression);
            saleRepository.saveAndFlush(sale);
            discountPolicyService.saveAndFlush(dataDiscountPolicy);
            return new Response(false," ");
        }
        catch (Exception e){
            return new Response(true,"Could not add discount Policy");
        }
    }
    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public Response getDiscountByStore(Integer storeid) throws DataAccessException {
        try {
            Optional<DataDiscountPolicy> dataDiscountPolicy= discountPolicyService.findById(storeid);
            if(!dataDiscountPolicy.isPresent()){
                return new Response(true,"could not find discount policy for store");
            }
            Response response=new Response(false, " ");
            response.AddDBDiscountPolicy(dataDiscountPolicy.get());
            return response;
        }
        catch (Exception e){
            return new Response(true,"Could not add discount Policy");
        }
    }
}
