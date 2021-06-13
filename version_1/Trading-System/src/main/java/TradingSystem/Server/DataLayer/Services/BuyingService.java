package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DataBuyingPolicy;
import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DBExpression;
import TradingSystem.Server.DataLayer.Repositories.BuyingRepository;
import TradingSystem.Server.DataLayer.Repositories.DBExpRepository;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class BuyingService {

    @Autowired
    BuyingRepository buyingRepository;
    @Autowired
    DBExpRepository dataExpCompositeRepository;

    @Transactional(rollbackFor = { Exception.class }, timeout = 20)
    public Response getBuyingByStore(Integer storeid)
    {
        try {
            Optional<DataBuyingPolicy> buyingPolicy=buyingRepository.findById(storeid);
            if(!buyingPolicy.isPresent()){
                return new Response(true,"Could not found buying policy for store");
            }
            Response response=new Response(false," ");
            response.AddDBBuyingPolicy(buyingPolicy.get());
            return response;
        }
        catch (Exception e){
            return new Response(true,"Could not add discount Policy");
        }
    }

    @Transactional(rollbackFor = { Exception.class }, timeout = 20)
    public Response AddBuyingPolicy(DataBuyingPolicy buyingPolicy){
        try {
            DBExpression dataExpression= buyingPolicy.getExpression();
            dataExpCompositeRepository.saveAndFlush(dataExpression);
            buyingRepository.saveAndFlush(buyingPolicy);
            return new Response(false,"buying policy was added successfully");
        }
        catch (Exception e){
            return new Response(true,"could not add buying policy for store");
        }
    }
}
